package com.ludo.jar2pom;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;

import static org.assertj.core.api.Assertions.assertThat;

public class CmdLineMainTest {

    public static final PathMatcher POM_MATCHER = FileSystems.getDefault().getPathMatcher("glob:**/pom*.xml");

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void should_display_help_message_when_add_argument_h() throws Exception {
        final String[] args = new String[]{"-h"};
        CmdLine.main(args);
    }

    @Test
    public void should_create_a_pom_when_scanned_folder_contains_jar_files() throws Exception {
        final File root = this.temporaryFolder.getRoot();
        this.temporaryFolder.newFile("foobar.jar");
        this.temporaryFolder.newFile("foobar.txt");
        this.temporaryFolder.newFile("foobar.xml");

        final File output = this.temporaryFolder.newFolder("output");

        final String[] args = new String[]{"-i", root.toString(), "-o", output.toString(), "-r"};

        CmdLine.main(args);

        final FileFilter filter = path -> POM_MATCHER.matches(path.toPath());

        final File[] files = output.listFiles(filter);

        assertThat(files)
                .isNotNull()
                .hasSize(1)
                .extracting(File::getName)
                .allMatch(name -> name.matches("pom_[\\d]{4}-[\\d]{2}-[\\d]{2}_[\\d]{2}-[\\d]{2}-[\\d]{2}.xml"));
    }

}
