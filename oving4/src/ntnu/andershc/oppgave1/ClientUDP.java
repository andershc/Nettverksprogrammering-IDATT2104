package ntnu.andershc.oppgave1;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientUDP {

    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;
    private byte[] buffer;

    public ClientUDP(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }

    public void sendThenReceive() {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Write a simple equation which includes: number operator number!   (You can type exit" +
                        "to exit the application at any time.");
                String input = scanner.nextLine();
                buffer = input.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, inetAddress, 1234);
                datagramSocket.send(sendPacket);

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                buffer = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(receivePacket);

                String answer = new String(buffer, 0 , buffer.length);
                System.out.println("Your answer is: " + answer.trim());
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("Connection closing.");
        datagramSocket.close();
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        ClientUDP clientUDP = new ClientUDP(datagramSocket, inetAddress);
        System.out.println("Connection successfully created.");
        clientUDP.sendThenReceive();
    }
}
