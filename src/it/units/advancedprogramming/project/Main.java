package it.units.advancedprogramming.project;

import it.units.advancedprogramming.project.core.ExecutorCommandProcessingServer;
import it.units.advancedprogramming.project.core.MathematicalServerProcessor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Error! Wrong number of arguments");
            return;
        }

        int port;

        if (!args[0].matches("[0-9]+")) {
            System.err.println("Error! Invalid port number");
            return;
        } else {
            port = Integer.parseInt(args[0]);
        }

        ExecutorCommandProcessingServer executorCommandProcessingServer = new ExecutorCommandProcessingServer(port, new MathematicalServerProcessor());

        executorCommandProcessingServer.start();
    }

}
