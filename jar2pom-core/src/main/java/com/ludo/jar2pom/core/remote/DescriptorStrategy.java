package com.ludo.jar2pom.core.remote;

import java.io.IOException;
import java.nio.file.Path;

import com.ludo.jar2pom.core.model.Descriptor;

public interface DescriptorStrategy {

    Descriptor search(Path file, String customHost) throws IOException;

    Descriptor search(Path file) throws IOException;

}
