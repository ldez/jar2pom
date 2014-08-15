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
 * The Class PathOptionHandler. {@link File} {@link OptionHandler}.
 */
public class PathOptionHandler extends OneArgumentOptionHandler<Path> {

    /**
     * Instantiates a new path option handler.
     *
     * @param parser the parser
     * @param option the option
     * @param setter the setter
     */
    public PathOptionHandler(final CmdLineParser parser, final OptionDef option, final Setter<? super Path> setter) {
        super(parser, option, setter);
    }

    /*
     * (non-Javadoc)
     * @see org.kohsuke.args4j.spi.OneArgumentOptionHandler#parse(java.lang.String)
     */
    @Override
    protected final Path parse(final String argument) throws CmdLineException {
        return Paths.get(argument);
    }

    /*
     * (non-Javadoc)
     * @see org.kohsuke.args4j.spi.OneArgumentOptionHandler#getDefaultMetaVariable()
     */
    @Override
    public final String getDefaultMetaVariable() {
        return Messages.DEFAULT_META_FILE_OPTION_HANDLER.format();
    }
}
