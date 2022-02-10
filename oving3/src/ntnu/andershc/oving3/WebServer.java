package ntnu.andershc.oving3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class WebServer {
    public static void main(String[] args) throws IOException {
        int port = 80;
        ServerSocket serverSocket = new ServerSocket(port);

        Socket socket = serverSocket.accept();
        System.out.println("Connection created");
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String string;
        ArrayList<String> listOfStrings = new ArrayList<>();

        writer.println("HTTP/1.0 200 OK");
        writer.println("Content-Type: text/html; charset=utf-8");
        writer.println("");
        writer.println("<!DOCTYPE html>");
        writer.println("<html>");
        writer.println("body");
        writer.println("<h1> Hilsen. Du har koblet deg opp til min enkle web-tjener </h1>");
        writer.println("<h3>The returned headers</h3>");
        writer.println("<ul>");

        while(!(string=reader.readLine()).equals("")){
            listOfStrings.add(string);
        }
        for(String s: listOfStrings){
            writer.println("<li>" + s + "</li>");
        }
        writer.println("</ul>");
        writer.println("</body></html>");

        reader.close();
        writer.close();
        socket.close();
        System.out.println("Connection is closing");
    }
}
