package Project_Part3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        if(args.length != 2){
            System.out.println("Usage: ClientTCP <host> <port>");
            return;
        }
        int serverPort = Integer.parseInt(args[1]);
        String serverIP = args[0];
        System.out.println("Enter a Character");
        char c = scanner.nextLine().charAt(0);
        if(c == 'D'){
            System.out.println("Enter the filename");
            String s = scanner.nextLine();
        }
        byte[] b = new byte[1];
        System.out.println();
        b[0] = (byte) c;
        ByteBuffer buffer = ByteBuffer.wrap(b);

        SocketChannel listenChannel = SocketChannel.open();
        listenChannel.connect(new InetSocketAddress(serverIP,serverPort));
        listenChannel.write(buffer);
        buffer.clear();
        listenChannel.read(buffer);
        buffer.flip();
        System.out.println("Message Received : " + (char)buffer.get(0));
    }
}
