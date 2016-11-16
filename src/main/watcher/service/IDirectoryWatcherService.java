package main.watcher.service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by miao on 14/11/2016.
 */
public interface IDirectoryWatcherService {

    public void startListening(Path srcDir, Path targetDir) throws Exception;

    public void stopListening(Path targetDir) throws Exception;

}
