package com.ludo.jar2pom;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class CmdLineMainTest {

    public static final PathMatcher POM_MATCHER = FileSystems.getDefault().getPathMatcher("glob:**/pom*.xml");

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void mainHelp() throws Exception {
        final String[] args = new String[] { "-h" };
        CmdLine.main(args);
    }

    @Test
    public void mainFullProcess() throws Exception {
        final File root = this.temporaryFolder.getRoot();
        this.temporaryFolder.newFile("foobar.jar");
        this.temporaryFolder.newFile("foobar.txt");
        this.temporaryFolder.newFile("foobar.xml");

        final File output = this.temporaryFolder.newFolder("output");

        final String[] args = new String[] { "-i", root.toString(), "-o", output.toString(), "-r" };

        CmdLine.main(args);

        final FileFilter filter = path -> POM_MATCHER.matches(path.toPath());

        final File[] list = output.listFiles(filter);
        assertNotNull(list);

        assertThat(Arrays.asList(list), hasSize(1));
    }

}
