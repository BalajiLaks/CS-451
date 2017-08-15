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
            while (true)
            {
                //try{
                    testBoard = (Board) is.readObject();
                //}
                //catch(SocketException e)
                //{
                //    System.out.println("Message not sent to me");
                //    break;
                //}
                if (testBoard.getStatus().equals("Player1"))
                {
                    System.out.println(testBoard.getStatus());
                    testBoard.setStatus("Player2");
                    os.writeObject(testBoard);
                }
                else if (testBoard.getStatus().equals("Player2"))
                {
                    System.out.println(testBoard.getStatus());
                    testBoard.setStatus("Player1");
                }
                break;
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

}
