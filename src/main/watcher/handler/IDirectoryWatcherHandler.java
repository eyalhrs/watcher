package main.watcher.handler;


import java.io.IOException;

public interface IDirectoryWatcherHandler {

    public void register() throws IOException;
    public void unregister() throws IOException;
    public void startWatching();

}