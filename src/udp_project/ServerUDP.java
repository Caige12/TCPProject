package udp_project;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.InetAddress;

public class ServerUDP {
    private final DatagramSocket socket;

    public ServerUDP(int port) throws SocketException{
        socket = new DatagramSocket(port);
    }

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Syntax: ServerUPD <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        try {
            ServerUDP server = new ServerUDP(port);
            server.service();
        } catch (SocketException ex) {
            System.out.println("Socket error: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    private void service() throws IOException {
        while(true){
            DatagramPacket request = new DatagramPacket(new byte[1],1);
            // socket.receive is a blocking call!
            socket.receive(request);
            //retrieve the character sent by the client
            byte[] buffer = request.getData();

            //note: Do NOT do buffer.toString() as it returns
            // the string representation of the memory address
            System.out.println(new String(buffer));
            InetAddress clientIP = request.getAddress();
            int clientPort = request.getPort();

            DatagramPacket response = new DatagramPacket(buffer, 1,clientIP,clientPort);
            socket.send(response);
        }
    }
}
