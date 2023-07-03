package Serverside;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadHandler implements Runnable{
    private final Socket socket;
    private static int connectionCount = 0;
    private final int connectionNumber;

    public ThreadHandler(Socket socket) throws IOException {
        this.socket = socket;
        connectionCount++;
        connectionNumber = connectionCount;
        threadSays("Connection " + connectionNumber + " established.");
    }

    private void threadSays(String s) {
        System.out.println("ClientHandlerCW" + connectionNumber + ": " + s);

    }

    @Override
    public void run() {
        try{
            threadSays("Waiting for data from client...");
            System.out.println("Server: Waiting for data from client...");

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream((socket.getInputStream()));


        } catch (Exception e) {
            Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                threadSays("We have lost connection to client " + connectionNumber + ".");
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }

    }

