package main.watcher.controller;
import main.watcher.service.DirectoryWatcherService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static spark.Spark.*;

/**
 * Controller
 */
public class DirectoryWatcherController {

    public DirectoryWatcherController() {
        get("/startListen", (request, response) -> {
            try {
                String srcDir = request.queryParams("srcDir");
                String targetDir = request.queryParams("targetDir");
                //validates input
                validateDir(srcDir,"source");
                validateDir(targetDir,"target");
                DirectoryWatcherService service = new DirectoryWatcherService();
                service.startListening(Paths.get(srcDir),Paths.get(targetDir));
            } catch (Exception e) {
                response.status(500);
                return "An error occurred: " + e.getMessage();
            }
            response.status(200);
            return "Started listening to dir "+request.queryParams("srcDir") + ", syncing to dir " + request.queryParams("targetDir");
        });

        get("/stopListen", (request, response) -> {
            try {
                String srcDir = request.queryParams("srcDir");
                //validates input
                validateDir(srcDir,"source");
                DirectoryWatcherService service = new DirectoryWatcherService();
                service.stopListening(Paths.get(srcDir));
            } catch (Exception e) {
                response.status(500);
                return "An error occurred: " + e.getMessage();
            }
            response.status(200);
            return "Stopped listening to "+request.queryParams("srcDir");
        });

    }

    private void validateDir(String dir,String type) throws IOException {
        if (dir == null) {
            throw new IOException("Directory of type "+type+" does not exist");
        }
        Path dirPath = Paths.get(dir);
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            throw new IOException(dir + "directory of type " + type + " does not exist or is not a directory");
        }
    }

}
