package top.lsqeric.tutorial.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{

    private Socket socket;

    Client(int port){
        try {
            socket = new Socket("127.0.0.1",port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
            byte[] buffer = {1,1,1,2,2,2,3,3,3};
            out.write(buffer);
            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
