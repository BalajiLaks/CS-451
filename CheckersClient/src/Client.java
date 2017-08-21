import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Time;
import java.util.Scanner;

public class Client {

    private static View view;
    private static ObjectOutputStream out = null;
    private static ObjectInputStream in = null;
    private static Board board = new Board();


    public static void main(String[] args)
    {
        Board testBoard = new Board();
        view = new View();
    }

	static Thread runGame = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				char color = 'b';
				view.removeConnectButton();
				Socket socket = new Socket("localhost", 1234);
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());

				boolean hasQuit = false;

				//update board first time

				while (!hasQuit) {
					Object obj = (Object) in.readObject();
					if (obj.getClass() == String.class) {
						String s = (String) obj;
						if (s.equals("waitscreen")) {
							color = 'r';
							view.changeStatus("Waiting for other client to connect...");
						//	out.writeObject("still waiting");
						}
						else
							System.out.println(s);
					}
					else if (obj.getClass() == Board.class) {
						view.updateView(board);
						view.changeStatus(String.copyValueOf(new char[]{color}));
						//out.writeObject("assss");
						//if it is my turn, show buttons
					}
					System.out.println(color);

					//sleep a lol
					Thread.sleep(1000);
				}

			}
			catch (Exception e)
			{
				System.out.println(e);
				view.changeStatus("Cannot connect to game at this time.");
			}

		}
	});

    public static void connectButtonClicked()
    {
		runGame.start();
    }

    public static void startGame()
    {
		try {
			//view.removeConnectButton();
			Socket socket = new Socket("localhost", 1234);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			boolean hasQuit = false;

			//update board first time

			while (!hasQuit) {
				Object obj = (Object) in.readObject();
				if (obj.getClass() == String.class) {
					String s = (String) obj;
					if (s == "waitscreen") {
						//color = 'r';
						view.changeStatus("Waiting for other client to connect...");
						out.writeObject("still here");
					}
					else
						System.out.println(s);
				}
				else if (obj.getClass() == Board.class) {
					view.updateView(board);
					out.writeObject("fuck you");
					//if it is my turn, show buttons
				}
				//sleep a lol
			}

		}
		catch (Exception e)
		{
			System.out.println(e);
			view.changeStatus("Cannot connect to game at this time.");
		}
    }
}
