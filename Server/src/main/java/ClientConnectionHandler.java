import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientConnectionHandler implements Protocol, Runnable {

    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final Server server;
    private ServerReceiver serverReceiver;

    public ClientConnectionHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        System.out.println(Thread.currentThread().getName() + " Server: Client + " +
                socket.getInetAddress() + "has connected.");
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        server.addOutputToList(dataOutputStream);
        dataInputStream = new DataInputStream(socket.getInputStream());
    }

    public void sendClientMessageForBroadcasting(String message) throws IOException {
        server.broadcastMessage(message);
    }

    public void sendServerMessageBroadcast(String message) throws IOException {
        server.broadcastMessage("SERVER: " + message);
    }

    public void close() throws IOException {
        server.removeOutputFromList(dataOutputStream);
        dataInputStream.close();
        dataOutputStream.close();
        socket.close();
    }

    public void run() {
        serverReceiver = new ServerReceiver(dataInputStream, this);
        try {
            serverReceiver.operate();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
