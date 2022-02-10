package ntnu.andershc.oving3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        int PORTNR;

        /* Bruker en scanner til å lese fra kommandovinduet */
        Scanner readFromTerminal = new Scanner(System.in);
        System.out.println("Please provide the name of the machine where the server is running: ");
        String server = readFromTerminal.nextLine();

        System.out.println("Please provide a port you want to connect to: ");
        PORTNR = Integer.parseInt(readFromTerminal.nextLine());

        /* Setter opp forbindelsen til serverprogrammet */
        Socket connection = new Socket(server, PORTNR);
        if(connection.isConnected()){
            System.out.println("The connection is created.\n");
        }


        /* Åpner en forbindelse for kommunikasjon med serverprogrammet */
        InputStreamReader readConnection = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(readConnection);
        PrintWriter writer = new PrintWriter(connection.getOutputStream(), true);

        /* Reads startup lines from server and writes them to the terminal window */
        String start1 = reader.readLine();
        String start2 = reader.readLine();
        System.out.println(start1 + "\n" + start2 + "\n");

        String input1;
        String input2;
        String input3;
        while(connection.isConnected()){
            System.out.println(reader.readLine());
            input1 = readFromTerminal.nextLine();
            writer.println(input1);
            if(input1.equalsIgnoreCase("close")){
                break;
            }
            System.out.println(reader.readLine());

            input2 = readFromTerminal.nextLine();
            writer.println(input2);
            if(input2.equalsIgnoreCase("close")){
                break;
            }
            System.out.println(reader.readLine());

            input3 = readFromTerminal.nextLine();
            writer.println(input3);
            if(input3.equalsIgnoreCase("close")){
                break;
            }
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());
        }
        System.out.println("Connection is closing...");
        reader.close();
        writer.close();
        connection.close();
    }
}
