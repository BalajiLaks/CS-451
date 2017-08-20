import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Server {

    private static Game game;
    private static Player client1 = null;
    private static Player client2 = null;

    public static void main(String[] args)
    {
        int port = 2017;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server running");

            while (client1 == null || client2 == null) {
                Socket clientSocket = serverSocket.accept();
                if (client1 == null)
                {
                    client1 = new Player(clientSocket);
                    System.out.println("Client 1 connected");
                }
                else if (client2 == null)
                {
                    client2 = new Player(clientSocket);
                    System.out.println("Client 2 connected");
                }
                if (client1 != null && client2 != null)
                {
                    if (!client1.isConnected())
                    {
                        client1 = null;
                    }
                    if (!client2.isConnected())
                    {
                        client2 = null;
                    }
                }
            }

            if (client1.sendConnectionSuccess() && client2.sendConnectionSuccess())
            {
                game = new Game(client1, client2, new Board());
                game.startGame();
            }

            TimeUnit.SECONDS.sleep(10);

            serverSocket.setSoTimeout(1000);
            serverSocket.close();
            System.out.println("Server closing");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
