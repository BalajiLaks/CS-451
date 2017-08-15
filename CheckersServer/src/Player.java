import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Player {

    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;
    private PrintStream stringOutput;
    private Scanner stringInput;
    private Socket mySocket;

    public ObjectInputStream getObjectInput() {
        return objectInput;
    }

    public void setObjectInput(ObjectInputStream objectInput) {
        this.objectInput = objectInput;
    }

    public ObjectOutputStream getObjectOutput() {
        return objectOutput;
    }

    public void setObjectOutput(ObjectOutputStream objectOutput) {
        this.objectOutput = objectOutput;
    }

    public PrintStream getStringOutput() {
        return stringOutput;
    }

    public void setStringOutput(PrintStream stringOutput) {
        this.stringOutput = stringOutput;
    }

    public Scanner getStringInput() {
        return stringInput;
    }

    public void setStringInput(Scanner stringInput) {
        this.stringInput = stringInput;
    }

    public Socket getMySocket() {
        return mySocket;
    }

    public void setMySocket(Socket mySocket) {
        this.mySocket = mySocket;
    }

    Player(Socket clientSocket)
    {
        this.setMySocket(clientSocket);
        try
        {
            this.setObjectInput(new ObjectInputStream(clientSocket.getInputStream()));
            this.setObjectOutput(new ObjectOutputStream((clientSocket.getOutputStream())));
            this.setStringInput(new Scanner(clientSocket.getInputStream()));
            this.setStringOutput(new PrintStream((clientSocket.getOutputStream())));
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
}
