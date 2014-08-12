package com.ludo.jar2pom;

import org.junit.Test;

public class CmdLineTest {

    @Test
    public void testName() throws Exception {
        final String[] args = new String[] { "-i", "E:/Documents and Settings/Sauron/.m2/repository/args4j/args4j/2.0.29/args4j-2.0.29.jar", "-r" };
        // final String[] args = new String[] { "-i", "E:/Documents and Settings/Sauron/.m2/repository/args4j/args4j", "-o", "c:/toto.txt", "-r" };
        // final String[] args = new String[] { "-i", "E:/Documents and Settings/Sauron/.m2/repository/args4j/args4j/2.0.29", "-o", "c:/toto.txt", "-r" };
        // final String[] args = new String[] { "-r", "-h" };
        CmdLine.main(args);
    }

}
