package it.units.advancedprogramming.project.core;

import it.units.advancedprogramming.project.util.LoggerUtils;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorCommandProcessingServer {
    private static final String QUIT_COMMAND = "BYE";
    private static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
    private final int port;
    private final CommandProcessor commandProcessor;
    private final ExecutorService executorService;

    public ExecutorCommandProcessingServer(int port, CommandProcessor commandProcessor) {
        this.port = port;
        this.commandProcessor = commandProcessor;
        executorService = Executors.newFixedThreadPool(AVAILABLE_PROCESSORS);
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    final Socket socket = serverSocket.accept();
                    LoggerUtils.LOGGER.info("Connection from " + socket.getRemoteSocketAddress());

                    executorService.submit(() -> {
                        try (socket) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                            while (true) {
                                String request = bufferedReader.readLine();
                                if (request == null) {
                                    LoggerUtils.LOGGER.warning("Client " + socket.getRemoteSocketAddress() + " abruptly connection");
                                    break;
                                }
                                if (request.equals(QUIT_COMMAND)) {
                                    LoggerUtils.LOGGER.info("Disconnection from " + socket.getRemoteSocketAddress());
                                    break;
                                }
                                bufferedWriter.write(commandProcessor.process(request) + System.lineSeparator());
                                bufferedWriter.flush();
                            }
                        } catch (IOException e) {
                            LoggerUtils.LOGGER.warning(String.format("IO error: %s", e.getMessage()));
                        }
                    });
                } catch (IOException e) {
                    LoggerUtils.LOGGER.warning(String.format("Cannot accept connection due to %s", e.getMessage()));
                }
            }
        } catch (BindException e) {
            LoggerUtils.LOGGER.warning(String.format("Port %d already used", port));
        } finally {
            executorService.shutdown();
        }
    }

}
