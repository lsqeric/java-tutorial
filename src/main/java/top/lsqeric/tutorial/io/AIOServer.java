package top.lsqeric.tutorial.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AIOServer implements Runnable {

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AIOServer(int port) {
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, 4 * 1024);
            asynchronousServerSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Start aio server");
        final ExecutorService taskExecutor = Executors.newCachedThreadPool(Executors.defaultThreadFactory());
        Future<AsynchronousSocketChannel> asynchronousSocketChannelFuture = asynchronousServerSocketChannel.accept();
        try {
            final AsynchronousSocketChannel asynchronousSocketChannel = asynchronousSocketChannelFuture.get();
            final ByteBuffer buffer = ByteBuffer.allocate(200);

            asynchronousSocketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if(result!=-1){
                        byte[] content = new byte[result];
                        System.arraycopy(buffer.array(), 0, content, 0, result);
                        System.out.println(Arrays.toString(content));
                        asynchronousSocketChannel.read(buffer,buffer,this);
                    }
                }
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });
            System.out.println("Stop aio server");
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }
}
