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
        view.hideHelpButton();
    }

	static Thread runGame() {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					view.hideAboutButton();
					view.hideConnectButton();
					view.showHelpButton();
					Socket socket = new Socket("18.220.201.61", 1234);
					out = new ObjectOutputStream(socket.getOutputStream());
					in = new ObjectInputStream(socket.getInputStream());


					boolean hasQuit = false;
					while (!hasQuit) {
						Object obj = (Object) in.readObject();
						if (obj.getClass() == String.class) {
							view.hideHelpButton();
							String s = (String) obj;
							if (s.equals("waitscreen")) {
								color = 'r';
								view.changeStatus("Waiting for other client to connect...");
							}
							else if (s.equals(("forfeit"))) {
								board.reset();
								view.updateView(board);
								view.showMessage("Other player forfeited", "Game Over");
								view.hideMoveButtons();
								view.changeStatus("");
								view.showConnectButton();
								view.showAboutButton();
								break;
							}
							else if (s.equals("ping")) {
								out.writeObject("ping");
							}
						}
						else if (obj.getClass() == Board.class) {
							board = (Board) obj;
							view.showHelpButton();
							if (board.isWon()) {
								view.showMessage(board.getWinner() + " won", "Game Over");
								view.hideMoveButtons();
								view.changeStatus("");
								view.showConnectButton();
								view.showAboutButton();
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
											view.showMessage("Invalid move", "Move Error");
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
					view.changeStatus("Cannot connect to game at this time");
					view.showConnectButton();
				}

			}
		});
	}

    public static void setStatus(char color, char turn) {
    	String playerColor = color == 'b' ? "black" : "red";
		String turnColor = turn == 'b' ? "black" : "red";
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
			view.showMessage("Invalid Move", "Move Error");
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
		for (int i = replaceColors.size()-1; i >= 0; i--) {
			Point p = points.get(i);
			view.setPositionBGColor(p, replaceColors.get(i));
		}
		replaceColors.clear();
		moves.clear();
		points.clear();
	}

	public static void helpButtonClicked() {
		view.showMessage("To make a move, click the piece you want to move followed\n"
						+ "by all the positions you want to move to. These positions\n"
						+ "will be highlighted on the board in green. When you are \n"
						+ "done making your move press the submit button. If it is a \n "
						+ "valid move the board will update and the turn will change. \n"
						+ "If it is an invalid move, you will be notified that the move\n"
						+ "is invalid and allowed to submit another turn. To clear your \n"
						+ "move after highlighting positions press the clear buttton\n", "Help");

	}

	public static void aboutButtonClicked() {
		view.showMessage("v1.5 Final Release\n"
						+ "\nWhat's New:\n"
						+ " -   fixed: missing king piece graphic\n"
						+ " -   fixed: highlighting not getting removed when board is double clicked\n"
						+ " -   fixed: help menu getting removed after first turn\n",
				"About");
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
