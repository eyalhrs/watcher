package main.watcher.file;

import main.watcher.file.util.DirUtils;
import main.watcher.file.util.FileGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class BaseFileTest {

    protected String baseDir = "test-files";
    protected String srcDir = "test-files/source";
    protected String targetDir = "test-files/target";
    protected String dir1 = "dir1";
    protected String dir2 = "dir2";
    protected Path srcPath;
    protected Path targetPath;
    protected Path dir1Path;
    protected Path dir2Path;


    public void createPaths() {
        srcPath = Paths.get(srcDir);
        targetPath = Paths.get(targetDir);
    }

    public Path createSubDirectoryInPath(Path path,String subDir) throws IOException {
        Path subDirPath = path.resolve(subDir);
        Files.createDirectories(subDirPath);
        return subDirPath;
    }

    protected void cleanUp() throws Exception {
        DirUtils.deleteIfExists(srcPath);
        DirUtils.deleteIfExists(targetPath);
    }

    protected void createDirectories() throws Exception {
        Files.createDirectories(srcPath);
        Files.createDirectories(targetPath);
    }

    protected void generateFile(Path path, int numberLines) throws Exception {
        FileGenerator.generate(path, numberLines);
    }

}
