package bg.sofia.uni.fmi.mjt.analyzer;

import bg.sofia.uni.fmi.mjt.analyzer.command.Command;
import bg.sofia.uni.fmi.mjt.analyzer.command.GetFoodByBarcodeCommand;
import bg.sofia.uni.fmi.mjt.analyzer.command.GetFoodByNameCommand;
import bg.sofia.uni.fmi.mjt.analyzer.command.GetFoodByNdbnoCommand;
import bg.sofia.uni.fmi.mjt.analyzer.dto.reports.ReportResponse;
import bg.sofia.uni.fmi.mjt.analyzer.dto.search.ListResponse;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandResponser implements Runnable {
    private String command;
    private final SocketChannel socketChannel;
    private String apiKey;
    private static final Map<String, ListResponse> cacheNameSearch = new ConcurrentHashMap<>();
    private static final Map<String, ReportResponse> cacheNdbnoSearch = new ConcurrentHashMap<>();
    private Map<String, Command> commands;

    public CommandResponser(String command, SocketChannel socketChannel, String apiKey) {
        this.command = command;
        this.socketChannel = socketChannel;
        this.apiKey = apiKey;
        initCommands();
    }

    private void initCommands() {
        commands = new HashMap<>();
        commands.put("get-food", new GetFoodByNameCommand(cacheNameSearch, apiKey));
        commands.put("get-food-report", new GetFoodByNdbnoCommand(cacheNdbnoSearch, apiKey));
        commands.put("get-food-by-barcode", new GetFoodByBarcodeCommand(cacheNameSearch));
    }

    @Override
    public void run() {
        command = command.strip();

        String commandWord = command.substring(0, command.indexOf(' '));

        //get arguments after the first word, which is the command.
        String[] arguments = command.substring(command.indexOf(" ")).strip().split("\\s+");
        String responseMessage = commands.get(commandWord).execute(arguments);

        sendBackResponse(responseMessage);
    }

    private void sendBackResponse(String responseMessage) {
        ByteBuffer buffer = ByteBuffer.allocate(responseMessage.getBytes().length);

        buffer.put(responseMessage.getBytes());
        buffer.flip();
        try {
            synchronized (socketChannel) {
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
