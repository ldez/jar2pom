package com.ludo.jar2pom.core.remote;

import java.io.IOException;
import java.nio.file.Path;

import com.ludo.jar2pom.core.model.Descriptor;

/**
 * The Interface DescriptorStrategy.
 */
public interface DescriptorStrategy {

    /**
     * Search.
     *
     * @param file the file to analyze
     * @param customHost the custom Nexus host
     * @return the descriptor
     * @throws IOException Signals that an I/O exception has occurred.
     */
    Descriptor search(Path file, String customHost) throws IOException;

    /**
     * Search.
     *
     * @param file the file to analyze
     * @return the descriptor
     * @throws IOException Signals that an I/O exception has occurred.
     */
    Descriptor search(Path file) throws IOException;

}
