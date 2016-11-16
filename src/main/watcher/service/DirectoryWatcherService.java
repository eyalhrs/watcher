package main.watcher.service;

import main.watcher.db.DirectoryWatchedDB;
import main.watcher.handler.DirectoryWatcherHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Exposes two APIs:
 * startListening - start watching over srcDir and sync files to targetDir
 * stopListening - stop watching over srcDir
 */
public class DirectoryWatcherService implements IDirectoryWatcherService {

    public void startListening(Path srcDir,Path targetDir) throws Exception {
        //validates that directory is not already being watced
        DirectoryWatcherHandler dirWatcher = DirectoryWatchedDB.getInstance().getDirWatcher(srcDir);
        if (dirWatcher != null) {
            throw new Exception("Source directory already being watched " + srcDir.toString());
        }
        //registers srcDir and all of his sub directories
        dirWatcher = new DirectoryWatcherHandler(srcDir,targetDir);
        dirWatcher.register();
        //start watching for events
        dirWatcher.startWatching();
        //attach watcher to source dir
        DirectoryWatchedDB.getInstance().setDirWatcher(srcDir,dirWatcher);
    }

    public void stopListening(Path srcDir) throws Exception {
        //get handler that matches to srcDir
        DirectoryWatcherHandler dirWatcher = DirectoryWatchedDB.getInstance().getDirWatcher(srcDir);
        if (dirWatcher != null) {
            //unregister watcher from directory
            dirWatcher.unregister();
            DirectoryWatchedDB.getInstance().removeDirWatcher(srcDir);
        } else {
            throw new Exception("Source directory is not currently watched " + srcDir.toString());
        }
    }


}
