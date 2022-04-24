package com.jpa.app;

import com.jpa.core.sprank.SprankApplication;

/**
 * Generic Application Implementation.
 */
public class Application {

    private static final int PORT = 7070;

    private static final boolean DEBUG = true;

    public static void main(String[] args) {
        new Bootstrap().bootstrapData();
        new SprankApplication(PORT, DEBUG).run();
    }

}
