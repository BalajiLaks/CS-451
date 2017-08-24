import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Server {

    public static void main(String args[]) throws Exception {
        ServerSocket ssocket = new ServerSocket(1234);
        System.out.println("Listening...");

        LinkedList<Thread> clients  = new LinkedList<Thread>();
		LinkedList<Socket> socks = new LinkedList<Socket>();

        Socket sock = null;
        while (true) {
            sock = ssocket.accept();
			if (clients.size() == 0) {
				System.out.println("Connected: " + sock.getInetAddress());
				clients.add(new Thread(new ServerThread(sock)));
				clients.get(0).start();

			}
			else if (clients.size() == 1) {
            	if (clients.get(0).isAlive()) {
					System.out.println("Connected: " + sock.getInetAddress());
					clients.add(new Thread(new ServerThread(sock)));
					clients.get(1).start();

					for (Thread client : clients) {
						client.join();
						System.out.println("done");
					}
					for (Socket s : socks) {
						sock.close();
					}
					clients.clear();
				}
				else {
					for (Socket s : socks) {
						sock.close();
					}
					clients.clear();
					clients.add(new Thread(new ServerThread(sock)));
					clients.get(0).start();
				}

			}
			else {
				System.out.println("Rejected: " + sock.getInetAddress());
				sock.close();
			}
        }
    }

}
