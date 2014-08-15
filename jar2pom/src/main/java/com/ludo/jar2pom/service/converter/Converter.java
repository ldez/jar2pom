package com.ludo.jar2pom.service.converter;

import java.io.IOException;

import com.ludo.jar2pom.model.Arguments;

public interface Converter {

    void process(Arguments arguments) throws IOException;
}
