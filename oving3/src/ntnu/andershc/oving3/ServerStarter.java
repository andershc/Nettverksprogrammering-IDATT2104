package ntnu.andershc.oving3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerStarter {
    public static void main(String[] args) throws IOException {
        final int PORTNR = 1250;
        int port = PORTNR;
        int numberOfConnections = 4;
        ArrayList<ServerThreads> serverThreads = new ArrayList<>();

        for (int i = 0; i < numberOfConnections; i++) {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server created on port " + port);
            port++;
            Socket connection = serverSocket.accept();
            serverThreads.add(new ServerThreads(connection, port));
            serverThreads.get(i).start();
        }
    }
}
