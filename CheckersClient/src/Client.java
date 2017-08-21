import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {

    private static View view;
    private static Scanner stringInput = null;
    private static PrintStream stringOutput = null;
    private static ObjectOutputStream objectOutput = null;
    private static ObjectInputStream objectInput = null;
    private static Board testBoard = new Board();
    public static char myPieceColor;

    public static void main(String[] args)
    {
        Board testBoard = new Board();
        view = new View();
        view.updateView(testBoard);
        //wait for connect button to be clicked
    }

    public static void connectButtonClicked()
    {
        try {
            view.removeConnectButton();         //connect only once
            InetAddress address = InetAddress.getByName("localhost");
            Socket socket = new Socket(address, 2017);
            stringInput = new Scanner(socket.getInputStream());
            stringOutput = new PrintStream(socket.getOutputStream());
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectInput = new ObjectInputStream(socket.getInputStream());
            boolean connection = false;
            while (!connection)
            {
                String testConnection  = (String) objectInput.readObject();
                String temp = testConnection;
                if (testConnection.equals("Connected"))
                {
                    connection = true;
                }
            }
            startGame();
        }
        catch (Exception e)
        {
            System.out.println(e);
            view.changeStatus("Cannot connect to game at this time.");
        }
    }

    public static void startGame()
    {
        try {
            boolean GameOn = true;
            while (GameOn) {
                testBoard = (Board) objectInput.readObject();
                view.updateView(testBoard);
                Move move;
                myPieceColor = testBoard.getTurn();
                if (myPieceColor == 'r') {
                    move = new Move(5, 0, 4, 1);
                } else {
                    move = new Move(2, 1, 3, 2);
                }
                testBoard.makeMove(move);
                testBoard.switchTurn();
                objectOutput.writeObject(testBoard);
                view.updateView(testBoard);
                testBoard.printBoard();
                GameOn = false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
