package com.github.signed.inmemory.sftp;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.sshd.common.Session;
import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.file.FileSystemView;
import org.apache.sshd.common.file.nativefs.NativeFileSystemView;

public class SubDirectoryForEachUserSystemFactory implements FileSystemFactory {

    private final Path sharedRootDirectory;

    public SubDirectoryForEachUserSystemFactory(Path sharedRootDirectory) {
        this.sharedRootDirectory = sharedRootDirectory;
    }

    @Override
    public FileSystemView createFileSystemView(Session session) {
        Map<String, String> roots = new HashMap<String, String>();
        roots.put("/", computeRootDir(session.getUsername()));
        return new NativeFileSystemView(session.getUsername(), roots, "/");
    }

    private String computeRootDir(String userName) {
        return sharedRootDirectory.resolve(userName).normalize().toString();
    }
}