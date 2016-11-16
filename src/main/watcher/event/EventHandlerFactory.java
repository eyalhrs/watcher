package main.watcher.event;

import main.watcher.event.create.EventCreateHandler;
import main.watcher.event.create.filename.FileNameBuildStrategy;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * Factory that constructs the matching event handler
 */
public class EventHandlerFactory {

    public static IEventHandler  getEventHandler(WatchEvent.Kind<Path> eventKind) {
        IEventHandler eventHandler = null;
        if (eventKind == ENTRY_CREATE) {
            eventHandler =  new EventCreateHandler(new FileNameBuildStrategy());
        }
        return eventHandler;
    }

}
