package Network;

import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Protocol {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String userName;
    private final Scanner scanner = new Scanner(System.in);
    private ClientReceiver clientReceiver;
    private boolean shouldRun = true;

    public void connect() {
        try {
            System.out.println("Connecting from client. IP: " + ip + " Port: " + port);
            socket = new Socket(ip, port);
        } catch (IOException ioe) {
            System.out.println("Error");
            ioe.printStackTrace();
        }
    }

    public void setup() throws IOException, InterruptedException, ParseException {
        connect();
        startSession();
        System.out.println("Type your username and press Enter");
        this.userName = ClientHandler.askForName("Username");
        MessageData messageData = new MessageData();
        messageData.setStatus(StatusEnum.CONNECT);
        messageData.setUserName(this.userName);
        dataOutputStream.writeUTF(messageData.createJsonFromMessageData());
    }

    public void startSession() throws IOException, InterruptedException {
        dataInputStream = new DataInputStream(socket.getInputStream());
        clientReceiver = new ClientReceiver(dataInputStream, this);
        new Thread(clientReceiver).start();
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void disconnect() {
        System.out.println("Disconnect");
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
                dataInputStream = null;
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
                dataOutputStream = null;
            }

            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void sendUserMessage(String message) throws IOException {
        if (message.equals(Quit)) {
            dataOutputStream.writeUTF(Close);
            System.out.println("Breaking");
            shouldRun = false;
        } else {
            dataOutputStream.writeUTF(message);
        }
    }
}
