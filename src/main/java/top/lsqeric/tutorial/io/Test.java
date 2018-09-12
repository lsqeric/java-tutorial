package top.lsqeric.tutorial.io;

public class Test {

    public static void main(String[] args) {
        int port = 3333;
        //bio
        Thread bioServerThread = new Thread(new BIOServer(port));
        bioServerThread.start();

        Thread clientThread1 = new Thread(new Client(port));
        clientThread1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //nio
        Thread nioServerThread = new Thread(new NIOServer(port));
        nioServerThread.start();

        Thread clientThread2 = new Thread(new Client(port));
        clientThread2.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //aio
        Thread aioServerThread = new Thread(new AIOServer(port));
        aioServerThread.start();

        Thread clientThread3 = new Thread(new Client(port));
        clientThread3.start();


    }

}


