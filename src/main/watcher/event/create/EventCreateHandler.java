package main.watcher.event.create;
import main.watcher.event.IEventHandler;
import main.watcher.event.create.filename.IFileNameBuildStrategy;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Handles EVENT_CREATE
 */
public class EventCreateHandler implements IEventHandler {

    //controls the strategy in which filename is being renamed
    private IFileNameBuildStrategy fileNamebuildStrategy;

    public EventCreateHandler(IFileNameBuildStrategy fileNamebuildStrategy) {
        this.fileNamebuildStrategy = fileNamebuildStrategy;
    }

    @Override
    public void handleEvent(Path sourceDir,Path targetDir) throws IOException {
        //if regular file, build it accordingly and move to target dir
        if (!Files.isDirectory(sourceDir)) {
            Path targetFileName =  fileNamebuildStrategy.build(sourceDir);
            Files.move(sourceDir, targetDir.resolve(targetFileName));
        } else {
            //Traverse over files in src dir tree and rename filenames accordingly
            Files.walkFileTree(sourceDir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetfileNme = fileNamebuildStrategy.build(file);
                    Files.move(file, file.resolveSibling(targetfileNme));
                    return FileVisitResult.CONTINUE;
                }
            });
            //move files to target dir
            Files.move(sourceDir,targetDir.resolve(sourceDir.getFileName()));
        }
    }

}
