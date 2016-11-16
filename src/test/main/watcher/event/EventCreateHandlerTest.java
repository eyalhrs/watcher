package main.watcher.event;

import main.watcher.event.create.EventCreateHandler;
import main.watcher.event.create.filename.FileNameBuildStrategy;
import main.watcher.file.BaseFileTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EventCreateHandlerTest extends BaseFileTest {

    @Before
    public void setUp() throws Exception {
        createPaths();
        cleanUp();
        createDirectories();
    }

    @Test
    public void testHandleEvent() throws Exception {
        Path file = Paths.get(srcDir, "PresidentElect.java");
        generateFile(file, 333);

        Long curremtTime = new Timestamp(System.currentTimeMillis()).getTime();
        EventCreateHandler eventCreateHandler = new EventCreateHandler(new FileNameBuildStrategy(curremtTime.toString()));
        eventCreateHandler.handleEvent(file,targetPath);

        Map<String,Boolean> fileNames = new HashMap<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(targetPath)) {
            for (Path path : directoryStream) {
                fileNames.put(path.toString(),true);
            }
        }


        assertThat(fileNames.size(),is(1));
        assertThat(fileNames.get(Paths.get(targetDir, "PresidentElect_" +curremtTime.toString()+ ".java").toString()),is(true));

    }

    @After
    public void tearDown() throws Exception {
        cleanUp();
    }

}