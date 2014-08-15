package com.ludo.jar2pom.model;

import java.nio.file.Path;

public interface Arguments {

    Path getInput();

    boolean isRecursive();

    Path getOutput();

    String getCustomHost();

    boolean isSystemProxy();

    boolean isHelp();

}
