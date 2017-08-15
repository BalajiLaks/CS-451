import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Server {

    public static void main(String[] args)
    {
        int port = 2017;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server running");
            Socket clientSocket1 = serverSocket.accept();
            System.out.println("Client  1 connected");
            Player player1 = new Player(clientSocket1);
            Socket clientSocket2 = serverSocket.accept();
            System.out.println("Client  2 connected");
            Player player2 = new Player(clientSocket2);
            Scanner receiveString1 = player1.getStringInput();
            Scanner receiveString2= player2.getStringInput();
            PrintStream sendString1 = player1.getStringOutput();
            PrintStream sendString2 = player2.getStringOutput();
            ObjectOutputStream sendObject1 = player1.getObjectOutput();
            ObjectOutputStream sendObject2 = player2.getObjectOutput();
            ObjectInputStream receiveObject1 = player1.getObjectInput();
            ObjectInputStream receiveObject2 = player2.getObjectInput();

            Board gameBoard = new Board();
            gameBoard.setStatus("Player1");
            sendObject1.writeObject(gameBoard);
            gameBoard = (Board)receiveObject1.readObject();

            TimeUnit.SECONDS.sleep(10);
            //sendObject2.writeObject(gameBoard);



            //Game game = new Game(player1, player2);
            //game.startGame();

            serverSocket.setSoTimeout(1000);
            serverSocket.close();
            System.out.println("Server closing");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
