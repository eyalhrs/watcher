package main.watcher.db;

import main.watcher.handler.DirectoryWatcherHandler;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * In memory db to keep track of the directory being watched
 */
public class DirectoryWatchedDB {

    private static DirectoryWatchedDB ourInstance = new DirectoryWatchedDB();
    private Map<Path, DirectoryWatcherHandler> dirWatchedDb;
    public static DirectoryWatchedDB getInstance() {
        return ourInstance;
    }

    private DirectoryWatchedDB() {
        this.dirWatchedDb = new HashMap<>();
    }

    public synchronized void setDirWatcher(Path registeredDir, DirectoryWatcherHandler dirHandler) {
        dirWatchedDb.put(registeredDir,dirHandler);
    }

    public synchronized void removeDirWatcher(Path registeredDir) {
        dirWatchedDb.remove(registeredDir);
    }

    public synchronized DirectoryWatcherHandler getDirWatcher(Path registeredDir) {
        return dirWatchedDb.get(registeredDir);
    }

}
