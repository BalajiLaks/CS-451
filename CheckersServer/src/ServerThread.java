import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Joseph Flynn jaf384@drexel.edu
 */
public class ServerThread implements Runnable{
	private Socket socket;
	private static volatile Board board = new Board();;
	private static AtomicInteger numConnected = new AtomicInteger(0);
	private static boolean shouldDie = false;

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

				if (numConnected.get() == 1) {
					out.writeObject("waitscreen");
				}
				else if (numConnected.get() == 2) {
					//String s = (String )in.readObject();
					//System.out.println(s);
						out.writeObject(board);
						//read in objin.readObject();
						//if read a move and valid
						//send back updateboard
						//if read invalid move
						//send back message saying invalid move
						//if read question what to do
						//send back board client will know its still not turing, or tha other player dcEd
						//otherwise send back thing telling player to



				}
				//make thread sleep a lil
				Thread.sleep(1000);
			} while (numConnected.get() > 0);


		}
		catch (IOException e) {
			System.out.println("Connection to client lost");
			numConnected.getAndAdd(-1);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}


	}
}
