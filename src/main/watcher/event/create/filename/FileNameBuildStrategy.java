package main.watcher.event.create.filename;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

/**
 * Controls the strategy in which filename is being built
 */

public class FileNameBuildStrategy implements IFileNameBuildStrategy {

    //String that will be attached to the end of files which are not of type text
    private String stringToAttach;

    public FileNameBuildStrategy(String stringToAttach) {
        this.stringToAttach = stringToAttach;
    }

    public FileNameBuildStrategy() {
        Long timeStamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.stringToAttach = timeStamp.toString();
    }


    @Override
    public Path build(Path file) {
        Path returnedFile = file.getFileName();
        //if not .txt file - attach given String to the end of file's name
        if (!file.getFileName().toString().endsWith(".txt")) {
            returnedFile = attachString(file);
        }
        return returnedFile;
    }

    private Path attachString(Path file) {
        Path returnedFile;
        String fileNameString = file.getFileName().toString();
        int lastDot = fileNameString.lastIndexOf('.');
        if (lastDot > -1) {
            fileNameString = fileNameString.substring(0,lastDot) + "_" + stringToAttach + fileNameString.substring(lastDot);
            returnedFile = Paths.get(fileNameString);
        } else {
            returnedFile = Paths.get(fileNameString + "_" + stringToAttach);
        }
        return returnedFile;
    }

}
