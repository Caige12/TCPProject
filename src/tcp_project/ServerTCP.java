package tcp_project;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerTCP {

    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            System.out.println("Usage: ServerTCP <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        ServerSocketChannel listenChannel = ServerSocketChannel.open();

        listenChannel.bind(new InetSocketAddress(port));
        while(true){
            //accept() is a blocking call
            //it will return only when it receives a new connection request from a client
            //accepts() performs the three-way handshake
            // with the client before it returns
            SocketChannel serveChannel = listenChannel.accept();
            ByteBuffer buffer = ByteBuffer.allocate(1);
            serveChannel.read(buffer);
            buffer.flip();
            System.out.println("Message from Client: "+ (char)buffer.get());
            buffer.rewind();
            serveChannel.write(buffer);
            serveChannel.close();



        }
    }
}
