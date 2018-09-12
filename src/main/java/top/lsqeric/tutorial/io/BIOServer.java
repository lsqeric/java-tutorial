package top.lsqeric.tutorial.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class BIOServer implements Runnable {

    private ServerSocket serverSocket;

    BIOServer(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        System.out.println("Start bio server");
        try {
            Socket socket = serverSocket.accept();
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            byte[] buffer = new byte[32];
            int count = in.read(buffer);
            while(count!=-1){
                byte[] content = new byte[count];
                System.arraycopy(buffer,0, content,0,count);
                System.out.println(Arrays.toString(content));
                count = in.read(buffer);
            }
            in.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Stop  bio server");
    }
}

