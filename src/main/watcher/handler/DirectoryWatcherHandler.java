package main.watcher.handler;
import com.sun.nio.file.SensitivityWatchEventModifier;
import main.watcher.event.EventHandlerFactory;
import main.watcher.event.IEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Handlers the registration to srcDir
 */
public class DirectoryWatcherHandler implements IDirectoryWatcherHandler {

    private WatchService watcher;
    private final Path srcDir;
    private final Path targetDir;
    //Keeps track of the dir being wacthed
    private Map<WatchKey,Path> keys;
    //Signals the thread which processing incoming events to keep/stop watching
    private boolean keepWatching = true;
    private static final Logger logger = LoggerFactory.getLogger(DirectoryWatcherHandler.class);

    public DirectoryWatcherHandler(Path srcDir, Path targetDir) {
        this.srcDir = srcDir;
        this.targetDir = targetDir;
    }

    /**
     * Creates a WatchService and registers the srcDir directory
     * @throws IOException
     */
    public void register() throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        keys = new HashMap<>();

        logger.debug("Scanning %s ...\n", srcDir);
        registerAll(srcDir);
        logger.debug("Done.");
    }

    /**
     * Unregisters the WatchService from the srcDir directory
     * @throws IOException
     */
    public void unregister() throws IOException {
        keepWatching = false;
        watcher.close();
    }

    /**
     * Initiates a thread to start processing the events occurring in srcDir
     */
    public void startWatching() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (keepWatching) {
                    if (!processEvents()) {
                        keepWatching = false;
                    }
                }
            }
        }).start();
    }

    /**
     * Register the given directory with the WatchService
     */
    private void registerSpecificDir(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_CREATE}, SensitivityWatchEventModifier.HIGH);
        keys.put(key, dir);
    }

    /**
     * Registers the given directory, and all its sub-directories, with the WatchService
     * @throws IOException
     */
    private void registerAll(final Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                registerSpecificDir(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Processing events of srcDir
     * @return true/false whether the thread needs to continue processing events
     */
    private boolean processEvents() {
        // wait for key to be signalled
        WatchKey key;
        try {
            key = watcher.take();
        } catch (InterruptedException ex) {
            logger.error("Interrupted while watching " + srcDir.toString(),ex);
            return false;
        } catch (ClosedWatchServiceException ex) {
            logger.debug("WacthService was closed " + srcDir.toString(),ex);
            return false;
        }

        Path dir = keys.get(key);
        if (dir == null) {
            logger.error("WatchKey was not recognized");
            return true;
        }

        for (WatchEvent<?> event: key.pollEvents()) {
            WatchEvent.Kind kind = event.kind();
            if (kind == OVERFLOW) {
                return true;
            }
            // Context for directory entry main.watcher.event is the file name of entry
            WatchEvent<Path> ev = (WatchEvent<Path>)(event);
            Path name = ev.context();
            Path child = dir.resolve(name);

            //log the event that occurred
            logger.debug(event.kind().name() + "::" + child);

            IEventHandler handler = EventHandlerFactory.getEventHandler(kind);
            if (handler != null) {
                try {
                    handler.handleEvent(child,targetDir);
                } catch (IOException ex) {
                    logger.error("An error occred while handling an event " + srcDir.toString(),ex);
                }
            }

        }

        // reset key and remove from set if directory no longer accessible
        boolean valid = key.reset();
        if (!valid) {
            keys.remove(key);

            // all directories are inaccessible
            if (keys.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
