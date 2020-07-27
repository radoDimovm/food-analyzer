package bg.sofia.uni.fmi.mjt.analyzer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodAnalyzerServer implements AutoCloseable {
    private static final int SERVER_PORT = 4444;

    //selector is playing a role of switching between the channels. All socketChannels should be registered in it
    //so server can handle all channels with a single thread.
    private Selector selector;
    private ByteBuffer commandBuffer;
    private ExecutorService executorService;

    private final String apiKey;

    public FoodAnalyzerServer() {
        try {
            commandBuffer = ByteBuffer.allocateDirect(2048);
            selector = Selector.open();
            initServerSocket();
            executorService = Executors.newFixedThreadPool(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        apiKey = "73DmSA3VvgrYteYNZtB3wnakxV2ZnrTgfVGmLI0c";
    }

    private void initServerSocket() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();//Трябва ли да се затвори?
        ssc.socket().bind(new InetSocketAddress(SERVER_PORT));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws IOException, InterruptedException {

        while (true) {
            int readyChannels = selector.select();
            if (readyChannels <= 0) {
                continue;
            }

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectedKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isReadable()) {
                    read(key);
                } else if (key.isAcceptable()) {
                    accept(key);
                }
            }
        }
    }

    private void read(SelectionKey key) throws InterruptedException {
        SocketChannel sc = (SocketChannel) key.channel();

        int r = 0;
        commandBuffer.clear();
        try {
            r = sc.read(commandBuffer);
        } catch (IOException e) {
            return;
        }

        if (r == -1) {
            return;
        }

        commandBuffer.flip();
        String[] commands = StandardCharsets.UTF_8.decode(commandBuffer).toString().trim().split("\\r?\\n");
        for (String command : commands) {
            executorService.execute(new CommandResponser(command, sc, apiKey));
        }
    }

    //add new socketChannel to selector
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel sc = ssc.accept();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
    }

    @Override
    public void close() throws Exception {
        selector.close();
        executorService.shutdown();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        FoodAnalyzerServer server = new FoodAnalyzerServer();
        server.start();
    }
}
