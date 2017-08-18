import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args)
    {
        Board testBoard = new Board();
        try
        {
            InetAddress address = InetAddress.getByName("localhost");
            Socket socket = new Socket(address, 2017);
            Scanner input  = new Scanner(socket.getInputStream());
            PrintStream out = new PrintStream(socket.getOutputStream());
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            View view = new View();
            boolean GameOn = true;
            while (GameOn)
            {
                testBoard = (Board) is.readObject();
                view.updateView(testBoard);
                Move move;
                if (testBoard.getTurn() == 'r') {
                    move = new Move(5, 0, 4, 1);
                }
                else
                {
                    move = new Move(2, 1, 3, 2);
                }
                testBoard.makeMove(move);
                testBoard.switchTurn();
                os.writeObject(testBoard);
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
