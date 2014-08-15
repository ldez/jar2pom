package com.ludo.jar2pom.service.converter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ludo.jar2pom.core.model.Descriptor;
import com.ludo.jar2pom.core.remote.DescriptorStrategy;
import com.ludo.jar2pom.model.Arguments;
import com.ludo.jar2pom.service.converter.JarConverter;
import com.ludo.jar2pom.service.output.OutputWriter;

@RunWith(MockitoJUnitRunner.class)
public class JarConverterProcessTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Mock
    private DescriptorStrategy descriptorStrategy;

    @Mock
    private OutputWriter outputWriter;

    @InjectMocks
    private JarConverter converter;

    @Mock
    private Arguments arguments;

    @Test
    public void processNoData() throws Exception {

        final Path input = this.temporaryFolder.getRoot().toPath();
        when(this.arguments.getInput()).thenReturn(input);

        this.converter.process(this.arguments);

        verify(this.descriptorStrategy, never()).search(any(Path.class), anyString());
        verify(this.outputWriter).writeOutputFile((List<Descriptor>) anyCollectionOf(Descriptor.class), any(Path.class));
    }

    @Test
    public void processWithData() throws Exception {

        this.temporaryFolder.newFile("foobar.jar");
        this.temporaryFolder.newFile("foobar.txt");
        this.temporaryFolder.newFile("foobar.xml");

        final Path input = this.temporaryFolder.getRoot().toPath();
        when(this.arguments.getInput()).thenReturn(input);

        this.converter.process(this.arguments);

        verify(this.descriptorStrategy).search(any(Path.class), anyString());
        verify(this.outputWriter).writeOutputFile((List<Descriptor>) anyCollectionOf(Descriptor.class), any(Path.class));
    }

}
