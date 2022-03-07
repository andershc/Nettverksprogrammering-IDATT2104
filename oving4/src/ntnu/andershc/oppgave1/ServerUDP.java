package ntnu.andershc.oppgave1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

public class ServerUDP {

    private DatagramSocket datagramSocket;
    private byte[] buffer;

    public ServerUDP(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    public void receiveThenSend() {
        while(true) {
            try {
                buffer = new byte[256];
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);

                String dataReceived = new String(buffer, 0, buffer.length);
                dataReceived = dataReceived.trim();
                System.out.println("Received: " + dataReceived);

                if(dataReceived.equalsIgnoreCase("exit")){
                    break;
                }

                InetAddress inetAddress = datagramPacket.getAddress();
                int port = datagramPacket.getPort();

                StringTokenizer tokenizer = new StringTokenizer(dataReceived);
                double firstNumber = Integer.parseInt(tokenizer.nextToken());
                String operation = tokenizer.nextToken();
                double secondNumber = Integer.parseInt(tokenizer.nextToken());
                double answer = 0;

                switch (operation){
                    case "+" :
                        answer= firstNumber + secondNumber;
                        break;
                    case "-":
                        answer = firstNumber - secondNumber;
                        break;
                    case "*":
                        answer = firstNumber * secondNumber;
                        break;
                    case "/":
                        answer = firstNumber / secondNumber;
                        break;
                    default:
                        break;
                };

                System.out.println("Sending " + answer);
                buffer = Double.toString(answer).getBytes(StandardCharsets.UTF_8);
                datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                datagramSocket.send(datagramPacket);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        System.out.println("UDP Server closing.");
        datagramSocket.close();
    }

    public static void main(String[] args) throws SocketException {
        DatagramSocket datagramSocket = new DatagramSocket(1234);
        ServerUDP server = new ServerUDP(datagramSocket);
        server.receiveThenSend();
    }
}
