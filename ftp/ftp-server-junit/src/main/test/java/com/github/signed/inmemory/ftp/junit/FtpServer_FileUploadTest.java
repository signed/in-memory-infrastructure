package com.github.signed.inmemory.ftp.junit;

import com.github.signed.inmemory.ftp.FtpServerConfigurationBuilder;
import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FtpServer_FileUploadTest {

    @Rule
    public FtpServer ftpServer = new FtpServer(new FtpServerConfigurationBuilder().listeningOnPort(10021).registerAccountFor("bob", "secret"));

    @Test
    public void provideAccessToUploadedFile() throws Exception {
        bobUploads("file.txt", "Hello World!");

        File uploadedFile = ftpServer.fileUploadedBy("bob");
        assertThat(uploadedFile.getName(), is("file.txt"));
        assertThat(contentOf(uploadedFile), is("Hello World!"));
    }

    @Test
    public void tellCallerIfUserDoesNotExist() throws Exception {
        try {
            ftpServer.fileUploadedBy("some unknown user");
            Assert.fail();
        }catch(RuntimeException ex){
            assertThat(ex.getMessage(), is("I'm sorry, but I do not know <some unknown user>."));
        }
    }

    @Test
    public void tellCallerIfNoFileWasUploadedByBob() throws Exception {
        try {
            ftpServer.fileUploadedBy("bob");
            Assert.fail();
        }catch(RuntimeException ex){
            assertThat(ex.getMessage(), is("I'm sorry, but <bob> did not upload any files."));
        }
    }

    @Test
    public void tellCallerIfMoreThanOneFileWasUploadedByBob() throws Exception {
        bobUploads("first.file", "do not care");
        bobUploads("second.file", "do not care");

        try {
            ftpServer.fileUploadedBy("bob");
            Assert.fail();
        } catch (RuntimeException ex) {
            assertThat(ex.getMessage(), is("Actually <bob> uploaded <2> files. I'm sorry, but I do not know which one you want."));
        }
    }

    private void bobUploads(String remotePath, String content1) {
        FtpUploadToLocalHost uploader = new FtpUploadToLocalHost("bob", "secret", ftpServer.port);
        uploader.upload(remotePath, content1);
    }

    private String contentOf(File uploadedFile) throws IOException {
        return Files.toString(uploadedFile, Charset.forName("UTF-8"));
    }
}