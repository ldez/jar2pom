package com.ludo.jar2pom.service.output;

import com.ludo.jar2pom.core.model.Descriptor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * The Interface OutputWriter.
 */
public interface OutputWriter {

    /**
     * Write output file.
     *
     * @param descriptors the descriptors
     * @param file        the output directory path
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void writeOutputFile(List<Descriptor> descriptors, Path file) throws IOException;

}
