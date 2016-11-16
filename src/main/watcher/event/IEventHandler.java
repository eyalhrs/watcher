package main.watcher.event;


import java.io.IOException;
import java.nio.file.Path;

public interface IEventHandler {

    public void handleEvent(Path sourceDir,Path targetDir) throws IOException;

}
