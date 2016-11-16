package main.watcher;


import org.junit.runner.JUnitCore;

public class RunTests {

    public static void  main(String[] args) throws Exception {
        JUnitCore.main(
                "main.watcher.event.EventCreateHandlerTest","main.watcher.handler.DirectoryWatcherHandlerTest");
    }
}
