package com.ludo.jar2pom;

import com.ludo.jar2pom.model.Arguments;
import com.ludo.jar2pom.service.converter.Converter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CmdLineTest {

    @Mock
    private Converter converter;

    @Test
    public void processHelp() throws Exception {
        final CmdLine cmdLine = new CmdLine(this.converter);

        final String[] args = new String[]{"-h"};
        cmdLine.doMain(args);

        verify(this.converter, never()).process(any(Arguments.class));
    }

    @Test
    public void processWithIOException() throws Exception {
        final CmdLine cmdLine = new CmdLine(this.converter);

        doThrow(IOException.class).when(this.converter).process(any(Arguments.class));

        final String[] args = new String[]{};
        cmdLine.doMain(args);

        verify(this.converter).process(any(Arguments.class));
    }

    @Test
    public void processWithProxy() throws Exception {
        final CmdLine cmdLine = new CmdLine(this.converter);

        final String[] args = new String[]{"-p"};
        cmdLine.doMain(args);

        verify(this.converter).process(any(Arguments.class));
    }

}
