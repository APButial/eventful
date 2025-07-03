package com.btp.appfx.service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class DirectoryUtils {
    public static void deleteDirectory(Path path) throws IOException {
        // Check if the path is a directory
        if (Files.isDirectory(path)) {
            // Walk the file tree and delete all files and directories
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file); // Delete the file
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir); // Delete the directory after its contents have been deleted
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            // If it's not a directory, just delete the file
            Files.delete(path);
        }
    }
}
