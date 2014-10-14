package com.github.signed.inmemory.sftp.junit.sshj;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.schmizz.sshj.xfer.InMemorySourceFile;

public class InMemoryFile extends InMemorySourceFile {
    private final String name;
    private final String content;

    public InMemoryFile(String name, String content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getLength() {
        return content.getBytes().length;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content.getBytes());
    }
}
