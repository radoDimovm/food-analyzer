package bg.sofia.uni.fmi.mjt.analyzer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FoodAnalyzerClient {
    private SocketChannel socketChannel;
    private InputStream inputStream;
    private final CommandValidator commandValidator;
    private final ByteBuffer buffer;

    public FoodAnalyzerClient(InputStream inputStream, SocketChannel socketChannel, CommandValidator commandValidator) {
        this.inputStream = inputStream;
        this.socketChannel = socketChannel;
        this.commandValidator = commandValidator;
        buffer = ByteBuffer.allocateDirect(4096);
    }

    public void start() {

        try (Scanner consoleInput = new Scanner(inputStream)) {
            String command;
            while (!(command = consoleInput.nextLine()).trim().equals("quit")) {

                if (commandValidator.isCommandValid(command)) {
                    command = processCommand(command);

                    sendCommandToServer(command);

                    getResponse();

                } else {
                    System.out.println("Invalid command!");
                }

                // connect 127.0.0.1 4444
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getResponse() throws IOException {
        socketChannel.configureBlocking(true);

        buffer.clear();
        int r = socketChannel.read(buffer);

        socketChannel.configureBlocking(false);

        while (r > 0) {
            buffer.flip();

            System.out.print(StandardCharsets.UTF_8
                    .decode(buffer)
                    .toString()
                    .trim());

            buffer.clear();
            r = socketChannel.read(buffer);
        }

        System.out.println();
    }

    private String processCommand(String command) {
        String[] commandTokens = command.strip().split("\\s+");

        if (commandTokens.length == 2 && commandTokens[1].length() >= 6 && commandTokens[1].startsWith("--img=")) {
            //tested and working for barcodes of type and format EAN-13.png
            String UPC = new ImageHandler().receiveUPCCode(commandTokens[1].substring(6));

            command = commandTokens[0] + " --upc=" + UPC;
        }

        return command;
    }

    private void sendCommandToServer(String command) throws IOException {
        buffer.clear();
        buffer.put(command.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
    }

    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 4444));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FoodAnalyzerClient foodAnalyzerClient = new FoodAnalyzerClient(System.in, socketChannel, new CommandValidator());

        foodAnalyzerClient.start();
    }
}
