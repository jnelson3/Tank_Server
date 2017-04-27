package com.nelson;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class Main {
    private static Selector selector;

    static class SelectorHandler implements Runnable {

        @Override
        public void run() {





            while (true) {
                try {
                    //System.out.println("About to select ...");
                    int readyChannels = selector.select(500);
                    if (readyChannels == 0) {
                        //System.out.println("No tasks available");
                    } else {
                        Set<SelectionKey> keys = selector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = keys.iterator();

                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();

                            if (key.isAcceptable()) {
                                // Connection accepted
                            } else if (key.isConnectable()) {
                                // Connection established

                            } else if (key.isReadable()) {
                                SocketChannel socketChannel = (SocketChannel) key.channel();
                                ByteBuffer buffer = ByteBuffer.allocate(64);
                                StringBuilder inputString = new StringBuilder();
                                int bytesRead = socketChannel.read(buffer);
                                while (bytesRead > 0) {
                                    buffer.flip();
                                    while (buffer.hasRemaining()) {
                                        inputString.append((char) buffer.get());
                                    }
                                    bytesRead = socketChannel.read(buffer);
                                }

                                JsonReader jsonReader = Json.createReader(new StringReader(inputString.toString()));
                                JsonObject inputObject = jsonReader.readObject();
                                jsonReader.close();

                                System.out.println(inputObject.getString("action"));

                            } else if (key.isWritable()) {
                                String message = "Date: "
                                        + new Date(System.currentTimeMillis());

                                ByteBuffer buffer = ByteBuffer.allocate(64);
                                buffer.put(message.getBytes());
                                buffer.flip();
                                SocketChannel socketChannel = null;
                                while (buffer.hasRemaining()) {
                                    socketChannel = (SocketChannel) key.channel();
                                    socketChannel.write(buffer);
                                }
                                System.out.println("Sent: "
                                        + message + " to: " + socketChannel);
                            }
                            Thread.sleep(1000);
                            keyIterator.remove();
                        }
                    }
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Time Server started");
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(5000));
            selector = Selector.open();
            new Thread(new SelectorHandler()).start();
            while (true) {
//                System.out.println("Waiting for request ...");
                SocketChannel socketChannel
                        = serverSocketChannel.accept();
                System.out.println("Socket channel accepted - " + socketChannel);

                if (socketChannel != null) {
                    socketChannel.configureBlocking(false);
                    selector.wakeup();
                    socketChannel.register(selector,
                            SelectionKey.OP_WRITE | SelectionKey.OP_READ,
                            null);
                }
            }
        } catch (ClosedChannelException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
