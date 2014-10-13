package com.github.signed.inmemory.sftp.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.github.signed.inmemory.sftp.SftpServerConfigurationBuilder;
import com.github.signed.inmemory.shared.file.UploadedFiles;

import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.xfer.InMemorySourceFile;
import net.schmizz.sshj.xfer.LocalSourceFile;

public class FileUpload_Test {

    private final SftpServerConfigurationBuilder configurationBuilder = SftpServerConfigurationBuilder.sftpServer()
            .registerAccountFor("user", "password");

    @Rule
    public final SftpClientBuilder clientBuilder = new SftpClientBuilder();
    @Rule
    public SftpServer sftpServer = new SftpServer(configurationBuilder);

    @Test
    @Ignore
    public void uploadABasicFile() throws Exception {
        clientBuilder.connectTo(sftpServer).loginAs("user", "password");

        final String content = "hello sftp upload";

        LocalSourceFile source = new InMemorySourceFile() {
            @Override
            public String getName() {
                return "sample.txt";
            }

            @Override
            public long getLength() {
                return content.getBytes().length;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(content.getBytes());
            }
        };
        SFTPClient client = clientBuilder.client();
        client.mkdirs("upload");
        client.put(source, "upload");

        UploadedFiles uploadedFiles = sftpServer.filesUploadedBy("user");

        assertThat(uploadedFiles.singleFile().getName(), is("sample.txt") );
    }
}
