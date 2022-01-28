package Network.Input;

import Network.Server;
import Structure.MessageData;

import static Structure.Protocol.MessageOrigin.*;
import static Structure.Protocol.MessageStatus.*;

import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.Socket;

public class InputRunnableListener implements Runnable {

    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final Server server;
    private boolean run = true;

    String clientUserName = "Undefined";

    public InputRunnableListener(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        server.addInputRunnableListenerToList(this);
        dataInputStream = new DataInputStream(socket.getInputStream());

        System.out.println(Thread.currentThread().getName() + " Server: Client + " +
                socket.getInetAddress() + " has connected.");
    }

    private void messageHandle(MessageData messageData) throws IOException {
        System.out.println(clientUserName + ": String received: " + messageData);

        switch (messageData.getStatus()) {
            case USERNAME_SET -> handleUserNameRequest(messageData);
            case CLIENT_CHAT -> {
                messageData.setUserName(clientUserName);
                server.broadcastMessageFromClient(messageData);
            }
            case DISCONNECT -> disconnectClient();
            default -> System.out.println("Message from " + clientUserName + " was in wrong format.");
        }
    }

    public void sendServerMessage(MessageData messageData) throws IOException {
        messageData.setSource(SERVER);
        server.writeTheUTF(dataOutputStream, messageData.toString());
    }

    public void receiveMessage() throws IOException {
        while (run) {
            try {
                messageHandle(new MessageData(dataInputStream.readUTF()));
            } catch (IOException | ParseException e) {
                dataInputStream.close();
            }
        }
    }

    public void run() {
        try {
            System.out.println("Server is ready for receiving");
            receiveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptUserNameRequest(MessageData handleMessageData) throws IOException {
        clientUserName = handleMessageData.getPayload();
        System.out.println(clientUserName + " Connected.");
        MessageData messageData = new MessageData(USERNAME_OK);
        messageData.setUserName(clientUserName);
        messageData.setPayload("Username accepted.");
        sendServerMessage(messageData);
    }

    private void broadCastAboutNewClientConnection() throws IOException {
        MessageData messageData = new MessageData(SERVER_INFO);
        messageData.setPayload(clientUserName + " connected.");
        messageData.setUserName(clientUserName);
        server.broadcastServerMessage(messageData);
    }

    private void handleUserNameRequest(MessageData handleMessageData) throws IOException {

        if(MessageData.isNameValid(handleMessageData.getPayload()) &&
        server.isClientNameAvailable(handleMessageData.getPayload())
        ){
            acceptUserNameRequest(handleMessageData);
            broadCastAboutNewClientConnection();
        }else
        {
            sendServerMessage(new MessageData(USERNAME_ERROR));
        }
    }

    private void printlnClientStatus(String status) {
        System.out.println(Thread.currentThread().getName() + " " + clientUserName + " " + status);
    }

    public void close() throws IOException {
        server.removeInputRunnableListenerFromList(this);
        dataInputStream.close();
        dataOutputStream.close();
        socket.close();
        run = false;
    }

    private void disconnectClient() throws IOException {
        MessageData messageData = new MessageData();
        messageData.setStatus(DISCONNECT);
        messageData.setPayload("Client " + clientUserName + " has disconnected.");
        server.broadcastServerMessage(messageData);
        printlnClientStatus("disconnected.");
        close();
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public String getClientUserName() {
        return clientUserName;
    }
}
