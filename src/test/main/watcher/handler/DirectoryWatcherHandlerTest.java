package main.watcher.handler;

import main.watcher.file.BaseFileTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DirectoryWatcherHandlerTest extends BaseFileTest {

    @Before
    public void setUp() throws Exception {
        createPaths();
        cleanUp();
        createDirectories();
    }

    @Test
    public void testSimpleTextFileTransfter() throws Exception {

        System.out.println("Starting testSimpleTextFileTransfter...");

        //registers srcDir and all of his sub directories
        DirectoryWatcherHandler dirWatcher = new DirectoryWatcherHandler(srcPath,targetPath);
        dirWatcher.register();
        //start watching for events
        dirWatcher.startWatching();
        generateFile(Paths.get(srcDir, "eyal.txt"), 500);

        Thread.sleep(3000);

        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(targetPath)) {
            for (Path path : directoryStream) {
                fileNames.add(path.toString());
            }
        }

        dirWatcher.unregister();
        assertThat("Assert that eyal.txt file was moved to target dir",fileNames.size(),is(1));
        assertThat("Assert that eyal.txt file was renamed correctly",fileNames.get(0),is(Paths.get(targetDir, "eyal.txt").toString()));
    }

    @Test
    public void testMultipleTextFileTransfter() throws Exception {

        System.out.println("Starting testMultipleTextFileTransfter...");

        //registers srcDir and all of his sub directories
        DirectoryWatcherHandler dirWatcher = new DirectoryWatcherHandler(srcPath,targetPath);
        dirWatcher.register();
        //start watching for events
        dirWatcher.startWatching();
        for (int i = 0 ; i < 100 ; i++) {
            generateFile(Paths.get(srcDir, "eyal"+i+".txt"), 500);
        }

        Thread.sleep(5000);

        Map<String,Boolean> fileNames = new HashMap<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(targetPath)) {
            for (Path path : directoryStream) {
                fileNames.put(path.toString(),true);
            }
        }

        dirWatcher.unregister();

        assertThat(fileNames.size(),is(100));
        for (int i = 0 ; i < 100 ; i++) {
            assertThat("Assert that "+"eyal"+i+".txt"+" file was moved to target dir",fileNames.get(Paths.get(targetDir, "eyal"+i+".txt").toString()),is(true));
        }
    }

    @Test
    public void testSubDirTransfter() throws Exception {

        System.out.println("Starting testSubDirTransfter...");

        //registers srcDir and all of his sub directories
        DirectoryWatcherHandler dirWatcher = new DirectoryWatcherHandler(srcPath,targetPath);
        dirWatcher.register();
        //start watching for events
        dirWatcher.startWatching();

        Path subDirCreated1 = createSubDirectoryInPath(srcPath,"sub1");
        generateFile(Paths.get(subDirCreated1.toString(), "eyal1.txt"), 500);
        Path subDirCreated2 = createSubDirectoryInPath(subDirCreated1,"sub2");
        generateFile(Paths.get(subDirCreated2.toString(), "eyal2.txt"), 500);

        Thread.sleep(3000);

        dirWatcher.unregister();

        Map<String,Boolean> fileNames = new HashMap<>();
        Files.walkFileTree(targetPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                fileNames.put(file.toString(),true);
                return FileVisitResult.CONTINUE;
            }
        });

        assertThat("Assert that two sub directories were moved to target",fileNames.size(),is(2));
        assertThat("Assert that eyal.txt file resides in the right place in target dir sub1/eyal1.txt",fileNames.get(Paths.get(targetDir, "sub1/eyal1.txt").toString()),is(true));
        assertThat("Assert that eyal.txt file resides in the right place in target dir sub2/eyal2.txt",fileNames.get(Paths.get(targetDir, "sub1/sub2/eyal2.txt").toString()),is(true));

    }

    @After
    public void tearDown() throws Exception {
        cleanUp();
    }

}