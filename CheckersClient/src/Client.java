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
	private static LinkedList<Color> replaceColors = new LinkedList<Color>();; //colors to replace for pieces

    public static void main(String[] args)
    {
        view = new View();
        view.hideMoveButtons();
    }

	static Thread runGame() {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					view.hideConnectButton();
					Socket socket = new Socket("localhost", 1234);
					out = new ObjectOutputStream(socket.getOutputStream());
					in = new ObjectInputStream(socket.getInputStream());


					boolean hasQuit = false;
					while (!hasQuit) {
						Object obj = (Object) in.readObject();
						if (obj.getClass() == String.class) {
							String s = (String) obj;
							if (s.equals("waitscreen")) {
								color = 'r';
								view.changeStatus("Waiting for other client to connect...");
							}
							else if (s.equals(("forfeit"))) {
								board.reset();
								view.updateView(board);
								view.showMessage("Other player forfeited");
								view.hideMoveButtons();
								view.changeStatus("");
								view.showConnectButton();
								break;
							}
							else if (s.equals("ping")) {
								out.writeObject("ping");
							}
						}
						else if (obj.getClass() == Board.class) {
							board = (Board) obj;

							if (board.isWon()) {
								view.showMessage(board.getWinner() + " won");
								view.hideMoveButtons();
								view.changeStatus("");
								view.showConnectButton();
								board.reset();
								break;
							}
							view.updateView(board);
							setStatus(color, board.getTurn());
							if (color == board.getTurn()) {
								while (true) {
									if (!view.submitButtonIsVisible()) {
										view.showMoveButtons();
									}
									if (moves.size() != 0) {
										MoveSequence moveSeq = new MoveSequence(getMoves());
										if (!board.isValidMoveSequence(moveSeq)) {
											view.showMessage("Invalid move");
											clearMoveSequence();
										}
										else {
											out.writeObject(moveSeq);
											clearMoveSequence();
											break;
										}
									}
								}
							}
							else {
								if (view.submitButtonIsVisible()) {
									view.hideMoveButtons();
								}
								out.writeObject("ping");
							}
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
					view.changeStatus("Cannot connect to game at this time.");
				}

			}
		});
	}

    public static void setStatus(char color, char turn) {
    	String playerColor = color == 'b' ? "black" : "red";
		String turnColor = color == 'b' ? "black" : "red";
		view.changeStatus(String.format("You color: %s     Current turn: %s \n", playerColor, turnColor));
	}

    public static void connectButtonClicked()
    {
		runGame().start();
    }

	public static void submitButtonClicked() {
		//create list of Moves using the list of points
		//send the list of Moves to server
		if (points.size() < 2)
		{
			view.showMessage("Invalid Move");
			clearMoveSequence();
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

	private static void clearMoveSequence() {
		for (int i = 0; i < replaceColors.size(); i++) {
			Point p = points.get(i);
			view.setPositionBGColor(p, replaceColors.get(i));
		}
		replaceColors.clear();
		moves.clear();
		points.clear();
	}

	public static void clearButtonClicked() {
		clearMoveSequence();
	}

	public static void gameButtonClicked(Point point)
	{
		if (board.getTurn() == color) {
			points.add(point);
			replaceColors.add(view.getPositionBGColor(point));
			view.setPositionBGColor(point, Color.GREEN);
		}
	}

	public static LinkedList<Move> getMoves() {
		LinkedList<Move> moveList = new LinkedList<Move>();
    	for (Move m : moves) {
			moveList.add(m);
		}
		return moveList;
	}

	public static void sendForfeit() {
		try {
			if (out != null)
				out.writeObject("forfeit");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
