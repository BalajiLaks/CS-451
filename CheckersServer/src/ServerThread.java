import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joseph Flynn jaf384@drexel.edu
 */
public class ServerThread implements Runnable{
	private Socket socket;
	private static volatile Board board = new Board();
	private static AtomicInteger numConnected = new AtomicInteger(0);
	private static AtomicBoolean hasForfeited =  new AtomicBoolean(false);
	private static AtomicBoolean isWon =  new AtomicBoolean(false);
	private static AtomicBoolean isTurnOver =  new AtomicBoolean(false);

	 public ServerThread(Socket socket) {
		this.socket = socket;
	 }

	public void run()  {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			numConnected.getAndAdd(1);
			do {

				if (isWon.get()) {
					numConnected.getAndAdd(-1);
					if (numConnected.get() == 0)
						isWon.set(false);
				}
				else if (hasForfeited.get()) {
					System.out.println(numConnected.getAndAdd(-1));
					if (numConnected.get() == 2) {
						numConnected.getAndAdd(-1);
						break;
					}
					else {
						System.out.printf("Closing other connection");
						out.writeObject("forfeit");
						if (!isWon.get())
							board.reset();
						hasForfeited.set(false);
					}
				}
				else if (numConnected.get() == 1) {
					out.writeObject("waitscreen");
					Thread.sleep(1000);
					if (!isWon.get())
						board.reset();
				}
				else if (numConnected.get() == 2) {
					out.writeObject(board);
					while (numConnected.get() == 2) {
						Object obj = (Object) in.readObject();
						if (obj.getClass() == String.class) {
							String s = (String) obj;
							if(s.equals("ping")) {
								if(!isTurnOver.get() && !hasForfeited.get()) {
									out.writeObject("ping");
								}
								else if (isTurnOver.get()){
									out.reset();
									out.writeObject(board);
									isTurnOver.set(false);
									if (isWon.get()) {
										break;
									}
								}
								else if (hasForfeited.get()) {
									System.out.printf("Closing other connection");
									out.writeObject("forfeit");
									numConnected.getAndAdd(-1);
									hasForfeited.set(false);
								}
							}
						}
						else if (obj.getClass() == MoveSequence.class) {
							if (hasForfeited.get()) {
								System.out.printf("Closing other connection");
								out.writeObject("forfeit");
								numConnected.getAndAdd(-1);
								hasForfeited.set(false);
								break;
							}
							else {
								MoveSequence moves = (MoveSequence) obj;
								if (board.isValidMoveSequence(moves)) {
									board.doTurn(moves);
									out.reset();
									out.writeObject(board);
									isTurnOver.set(true);
									if (board.isWon()) {
										isWon.set(true);
										break;
									}
								}
							}
						}
					}
				}
			} while (numConnected.get() > 0);
		}
		catch (IOException e) {
			System.out.println("Connection to client lost");
			if (numConnected.get() == 2)
				hasForfeited.set(true);
			if (numConnected.get() > 0)
				numConnected.getAndAdd(-1);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			System.out.println("Unknown object read");
			e.printStackTrace();
		}
		catch (Board.MoveException e) {
			System.out.println("Move Validation must be broken :(");
			e.printStackTrace();
		}
	}
}
