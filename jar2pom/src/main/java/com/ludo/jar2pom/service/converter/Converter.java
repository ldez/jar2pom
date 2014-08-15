package com.ludo.jar2pom.service.converter;

import java.io.IOException;

import com.ludo.jar2pom.model.Arguments;

/**
 * The Interface Converter.
 */
public interface Converter {

    /**
     * Process.
     *
     * @param arguments the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void process(Arguments arguments) throws IOException;
}
