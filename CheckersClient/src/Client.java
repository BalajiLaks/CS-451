import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Time;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Client {

    private static View view;
    private static ObjectOutputStream out = null;
    private static ObjectInputStream in = null;
    private static Board board = null;
	private static char color = 'b';
	private static ConcurrentLinkedQueue<Move> moves = new ConcurrentLinkedQueue<Move>();
	private static LinkedList<Point> points = new LinkedList<>();

    public static void main(String[] args)
    {
        view = new View();
    }

	static Thread runGame = new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				view.removeConnectButton();
				Socket socket = new Socket("localhost", 1234);
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());


				boolean hasQuit = false;
				while (!hasQuit) {
					System.out.println("waiting");
					Object obj = (Object) in.readObject();
					if (obj.getClass() == String.class) {
						String s = (String) obj;
						System.out.println(s);
						if (s.equals("waitscreen")) {
							color = 'r';
							view.changeStatus("Waiting for other client to connect...");
						}
					}
					else if (obj.getClass() == Board.class) {
						board = (Board) obj;
						view.updateView(board);
						setStatus(color, board.getTurn());
						if (color == board.getTurn()) {
							while (true) {
								if (!view.submitButtonIsVisible()) {
									view.showSubmitMoveButton();
								}
								if (moves.size() != 0 ) {
									MoveSequence moveSeq = new MoveSequence(getMoves());
									if (!board.isValidMoveSequence(moveSeq)) {
										view.showErrorMessage("Invalid move");
										moves.clear();
										points.clear();
									}
									else {
										out.writeObject(moveSeq);
										moves.clear();
										points.clear();
										break;
									}
								}
							}
						}
						else {
							if (view.submitButtonIsVisible()) {
								view.hideSubmitMoveButton();
							}
							out.writeObject("ping");
						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e);
				view.changeStatus("Cannot connect to game at this time.");
			}

		}
	});

    public static void setStatus(char color, char turn) {
		view.changeStatus(String.format("You color: %s     Current turn: %s \n",
				Character.toString(color), Character.toString(turn)));
	}

    public static void connectButtonClicked()
    {
		runGame.start();
    }

	public static void submitButtonClicked() {
		//create list of Moves using the list of points
		//send the list of Moves to server
		System.out.println(points.size());
		if (points.size() < 2)
		{
			view.showErrorMessage("Invalid Move");
			moves.clear();
			points.clear();
		}
		else
		{
			//convert points to moves
			for (int i = 0; i < points.size() - 1; i++) {
				int x1 = points.get(i).x;
				int y1 = points.get(i).y;
				int x2 = points.get(i+1).x;
				int y2 = points.get(i+1).y;
				Move currentMove = new Move(x1,y1,x2,y2);
				moves.add(currentMove);
			}
		}
	}

	public static void gameButtonClicked(Point point)
	{
		if (board.getTurn() == color)
			points.add(point);
	}

	public static LinkedList<Move> getMoves() {
		LinkedList<Move> moveList = new LinkedList<Move>();
    	for (Move m : moves) {
			moveList.add(m);
		}
		return moveList;
	}
}
