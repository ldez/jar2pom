package com.ludo.jar2pom.model;

import java.nio.file.Path;

/**
 * The Interface Arguments.
 */
public interface Arguments {

    /**
     * Gets the input.
     *
     * @return the input
     */
    Path getInput();

    /**
     * Checks if is recursive.
     *
     * @return true, if is recursive
     */
    boolean isRecursive();

    /**
     * Gets the output.
     *
     * @return the output
     */
    Path getOutput();

    /**
     * Gets the custom host.
     *
     * @return the custom host
     */
    String getCustomHost();

    /**
     * Checks if is system proxy.
     *
     * @return true, if is system proxy
     */
    boolean isSystemProxy();

    /**
     * Checks if is help.
     *
     * @return true, if is help
     */
    boolean isHelp();

}
