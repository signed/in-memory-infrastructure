package com.github.signed.inmemory.sftp.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.github.signed.inmemory.sftp.SftpServerConfigurationBuilder;
import com.github.signed.inmemory.sftp.junit.sshj.InMemoryFile;
import com.github.signed.inmemory.sftp.junit.sshj.SftpClientBuilder;

import net.schmizz.sshj.sftp.SFTPClient;

public class FileUpload_Test {

    private final SftpServerConfigurationBuilder configurationBuilder = SftpServerConfigurationBuilder.sftpServer()
            .registerAccountFor("user", "password");

    @Rule
    public final SftpClientBuilder clientBuilder = new SftpClientBuilder();
    @Rule
    public SftpServer sftpServer = new SftpServer(configurationBuilder);

    @Test
    public void uploadABasicFile() throws Exception {
        clientBuilder.connectTo(sftpServer).loginAs("user", "password");

        SFTPClient client = clientBuilder.client();
        client.mkdirs("./upload");
        client.put(new InMemoryFile("sample.txt", "hello sftp upload"), "./upload");

        assertThat(sftpServer.filesUploadedBy("user").singleFile().getName(), is("sample.txt") );
    }
}
