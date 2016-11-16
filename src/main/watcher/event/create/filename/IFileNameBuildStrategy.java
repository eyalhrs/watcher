package main.watcher.event.create.filename;

import java.nio.file.Path;

public interface IFileNameBuildStrategy {

    public Path build(Path file);

}
