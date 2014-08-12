package com.ludo.jar2pom.support;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.Messages;
import org.kohsuke.args4j.spi.OneArgumentOptionHandler;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Setter;

/**
 * {@link File} {@link OptionHandler}.
 */
public class PathOptionHandler extends OneArgumentOptionHandler<Path> {

    public PathOptionHandler(final CmdLineParser parser, final OptionDef option, final Setter<? super Path> setter) {
        super(parser, option, setter);
    }

    @Override
    protected final Path parse(final String argument) throws CmdLineException {
        return Paths.get(argument);
    }

    @Override
    public final String getDefaultMetaVariable() {
        return Messages.DEFAULT_META_FILE_OPTION_HANDLER.format();
    }
}
