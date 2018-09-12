package top.lsqeric.tutorial.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

public class NIOServer implements Runnable {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public NIOServer(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            InetSocketAddress address = new InetSocketAddress(port);
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false);
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("Start nio server");
        while (true) {
            try {
                selector.select();
            } catch (IOException ex) {
                ex.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = server.accept();

                        socketChannel.configureBlocking(false);
                        SelectionKey clientKey = socketChannel.register(selector,
                                SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                        ByteBuffer buffer = ByteBuffer.allocate(200);
                        clientKey.attach(buffer);
                    }
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        int count = socketChannel.read(output);
                        while(count!=-1) {
                            byte[] content = new byte[count];
                            System.arraycopy(output.array(), 0, content, 0, count);
                            System.out.println(Arrays.toString(content));
                            count = socketChannel.read(output);
                        }
                        key.channel().close();
                        serverSocketChannel.close();
                        selector.close();
                        System.out.println("Stop nio server");
                        return;
                    }
                    if (key.isWritable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer output = (ByteBuffer) key.attachment();
                        output.flip();
                        socketChannel.write(output);
                        output.compact();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }
                }
            }
        }
    }
}
