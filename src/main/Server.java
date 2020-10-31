package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8080, 0, InetAddress.getLocalHost());
        server.accept();
    }
}
