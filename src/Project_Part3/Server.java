package Project_Part3;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {

    private static final int MAX_Client_MESSAGE_LENGTH = 1024;

    public static void main(String[] args) throws IOException {
        if(args.length != 1){
            System.out.println("Usage: ServerTCP <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);

        ServerSocketChannel listenChannel =
                ServerSocketChannel.open();

        listenChannel.bind(new InetSocketAddress(port));

        while (true) {
            //accept() is a blocking call
            //it will return only when it receives a new
            //connection request from a client
            //accept() performs the three-way handshake
            //with the client before it returns
            SocketChannel serveChannel = listenChannel.accept();

            ByteBuffer buffer = ByteBuffer.allocate(MAX_Client_MESSAGE_LENGTH);
            //ensures that we read the whole message
            serveChannel.read((buffer));
            //while(serveChannel.read((buffer)) >= 0);
            buffer.flip();
            //get the first character from the client message
            char command = (char) buffer.get();
            System.out.println("Command from client: " + command);

            switch (command) {
                case 'D':
                    //"Get": client wants to get the file
                    byte[] a = new byte[buffer.remaining()];
                    // copy the rest of the client message (i.e., the file name)
                    // to the byte array
                    buffer.get(a);
                    String fileName = new String(a);
                    File file = new File(fileName);
                    if (!file.exists() || file.isDirectory()) {
                        sendReplyCode(serveChannel, 'N');
                    } else {
                        sendReplyCode(serveChannel, 'Y');
                        //read contents of file
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;
                        while ((line = br.readLine()) != null) {
                            //write contents of file to client
                            line = line + "\n";
                            serveChannel.write(ByteBuffer.wrap(line.getBytes()));
                        }
                    }
                    serveChannel.close();
                    break;
                case 'U':
                    //"Upload": client wants to Upload the fil
                    // copy the rest of the client message (i.e., the file name)
                    // to the byte array
                    byte[] b = new byte[buffer.remaining()];
                    // copy the rest of the client message (i.e., the file name)
                    // to the byte array
                    buffer.get(b);
                    serveChannel.read(buffer.get(b));
                    fileName = buffer.toString();
                    File newFile = new File(fileName);
                    serveChannel.read(buffer);

                    //read contents of file
                    BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
                    ByteBuffer data = ByteBuffer.allocate(1024);
                    while (serveChannel.read(data) != -1) {
                        //write contents of file to client
                        // line = line + "\n";
                        data.flip();
                        data.clear();
                    }
                    bw.close();
                    break;
                case 'R':
                    byte[] c = new byte[buffer.remaining()];
                    buffer.get(c);
                    fileName = new String(c);
                    file = new File("src\\Resources\\"+ fileName);
                    if (!file.exists() || file.isDirectory()) {
                        sendReplyCode(serveChannel, 'F');
                    } else {
                        sendReplyCode(serveChannel, 'S');
                        file.delete();
                    }
                    break;
                case 'L':
                    File f = new File("src\\Resources");
                    System.out.println(f.getAbsolutePath());
                    File[] fList = f.listFiles();
                    for (int i = 0; i < fList.length; i++) {
                        serveChannel.write(ByteBuffer.wrap((fList[i].getName()).getBytes()));
                    }
                    break;
                case 'M':
                    byte[] byte2Buffer = new byte[buffer.remaining()];
                    buffer.get(byte2Buffer);
                    String fileNames = new String(byte2Buffer);
                    String[] oldNewName = fileNames.split(":");
                    file = new File("src\\Resources\\"+ oldNewName[0]);
                    if (!file.exists() || file.isDirectory()) {
                        sendReplyCode(serveChannel, 'F');
                    } else {
                        sendReplyCode(serveChannel, 'S');
                        file.renameTo(new File("src\\Resources\\" + oldNewName[1]));
                    }
                    break;
            }
        }
    }
        private static void sendReplyCode (SocketChannel channel,char code) throws IOException {
            byte[] a = new byte[1];
            a[0] = (byte) code;
            ByteBuffer data = ByteBuffer.wrap(a);
            channel.write(data);
        }
}