package com.ludo.jar2pom.service.converter;

import com.ludo.jar2pom.model.Arguments;

import java.io.IOException;

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
