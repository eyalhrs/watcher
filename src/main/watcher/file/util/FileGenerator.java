package main.watcher.file.util;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileGenerator {
    private static final String LINE_OF_TEXT =  "Let's make America great again! " +
                                               "Let's make America great again! " +
                                               "Let's make America great again! " +
                                               "Let's make America great again! " +
                                               "Let's make America great again! " +
                                               "Let's make America great again! " +
                                               "Let's make America great again! ";

    private FileGenerator(){}
    
    
    public static void generate(Path path, int lines) throws Exception {
        try(PrintWriter printWriter = new PrintWriter(Files.newBufferedWriter(path, Charset.defaultCharset()))){
            for(int i = 0; i< lines; i++){
                printWriter.println(LINE_OF_TEXT);
            }
        }
    }
}
