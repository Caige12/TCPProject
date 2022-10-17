package udp_project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class ClientUDP {
    private final DatagramSocket socket;

// ip and port # "3000"
    public ClientUDP() throws SocketException{
        socket = new DatagramSocket();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Syntax: ClientUPD <serverIP> <serverPort>");
            return;
        } try {
            InetAddress serverIP = InetAddress.getByName(args[0]);
            int serverPort = Integer.parseInt(args[1]);
            ClientUDP client = new ClientUDP();
            client.service(serverIP,serverPort);
        } catch (SocketException ex) {
            System.out.println("Socket error: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    private void service(InetAddress serverIP, int serverPort) throws IOException {
        while(true){
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Input a character");
            char c = keyboard.nextLine().charAt(0);
            byte[] buffer = new byte[1];
            buffer[0] = (byte) c;
            DatagramPacket request = new DatagramPacket(buffer, 1,serverIP,serverPort);
            socket.send(request);
            // receive the response packet from the server and display content
            DatagramPacket response = new DatagramPacket(new byte[1],1);

            socket.receive(response);
            byte[] data = response.getData();

            System.out.println(new String(data));

            //retrieve the character sent by the client

        }
    }
}
