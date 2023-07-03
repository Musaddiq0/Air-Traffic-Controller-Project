package Serverside;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnection {
    /**
     * Wait until a client connects to the server on a port, then establish the
     * connection via a socket object and create a thread to handle requests.
     */
    private void connectToClients() {
        System.out.println("Server: Server starting.");

        try (ServerSocket serverSocket = new ServerSocket(2000)) {

            while (true) {
                System.out.println("Server: Waiting for connecting client...");

                try {
                    Socket socket = serverSocket.accept();

                    ThreadHandler clientHandlerThread = new ThreadHandler(socket);
                    Thread connectionThread = new Thread(clientHandlerThread);
                    connectionThread.start();
//                    clientHandlerThread.start();
                } catch (IOException ex) {
                    System.out.println("Server: Could not start connection to a client.");
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Server: Closed down");
        }
    }
    public static void main(String[] args){
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.connectToClients();
    }

}
