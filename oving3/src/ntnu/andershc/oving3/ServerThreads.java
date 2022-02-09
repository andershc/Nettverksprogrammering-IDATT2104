package ntnu.andershc.oving3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThreads extends Thread {
    Socket connection;
    int port;

    public ServerThreads(Socket socket, int port){
        connection = socket;
        this.port = port;
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Log for server. Now we wait...");
            Socket connection = serverSocket.accept(); // venter til noen tar kontakt

        /*
        Åpner strømmer for kommunikasjon med klientprogrammet
         */
            InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(readConnection);
            PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

            writer.println("Hi, you have contacted the server!");
            writer.println("This is a very simple calculator.");

            String firstNumberRead;
            String secondNumberRead;
            String operator;

            while (true) {
                int firstNumber;
                int secondNumber;


                writer.println("Write the first number: ");
                firstNumberRead = reader.readLine();
                if (firstNumberRead.equalsIgnoreCase("close")) {
                    break;
                }
                writer.println("Write either + or -, for the operator you want to use: ");
                operator = reader.readLine();
                if (operator.equalsIgnoreCase("close")) {
                    break;
                }
                writer.println("Write the second number: ");
                secondNumberRead = reader.readLine();
                if (secondNumberRead.equalsIgnoreCase("close")) {
                    break;
                }
                try {
                    firstNumber = Integer.parseInt(firstNumberRead);
                    secondNumber = Integer.parseInt(secondNumberRead);
                    int answer;


                    switch (operator) {
                        case "+" -> {
                            answer = firstNumber + secondNumber;
                            writer.println("Your answer is: " + answer + "\n");
                        }
                        case "-" -> {
                            answer = firstNumber - secondNumber;
                            writer.println("Your answer is: " + answer + "\n");
                        }
                        default -> writer.println("Your operator is not valid (+/-).\n");
                    }
                } catch (NumberFormatException e) {
                    writer.println("Please use integers\n");
                }
                writer.println("We go again! Type 'close' at any time to quit the calculator\n");
            }

            /* Close connection */
            reader.close();
            writer.close();
            connection.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
