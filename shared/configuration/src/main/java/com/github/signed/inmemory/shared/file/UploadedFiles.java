package com.github.signed.inmemory.shared.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class UploadedFiles {

    private final File userHome;
    private final String username;

    public UploadedFiles(File userHome, String username) {
        this.userHome = userHome;
        this.username = username;
    }

    public File singleFile() {
        if (!userHome.isDirectory()) {
            throw new RuntimeException(String.format("I'm sorry, but I do not know <%s>.", username));
        }
        List<File> foundFiles = allUploadedFiles(userHome);
        if (foundFiles.isEmpty()) {
            throw new RuntimeException(String.format("I'm sorry, but <%s> did not upload any files.", username));
        }
        if (foundFiles.size() > 1) {
            throw new RuntimeException(String.format("Actually <%s> uploaded <%d> files. I'm sorry, but I do not know which one you want.", username, foundFiles.size()));
        }
        return foundFiles.get(0);
    }

    private List<File> allUploadedFiles(File file) {
        final ArrayList<File> paths = new ArrayList<File>();

        try {
            Files.walkFileTree(file.toPath(), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    paths.add(file.toFile());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    throw new RuntimeException(exc);
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return paths;
    }
}
