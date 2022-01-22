import org.json.simple.parser.ParseException;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerReceiver implements Protocol {
    DataInputStream dataInputStream;
    ClientConnectionHandler clientConnectionHandler;

    String clientUserName = "";

    public ServerReceiver(DataInputStream dataInputStream, ClientConnectionHandler clientConnectionHandler) {
        this.dataInputStream = dataInputStream;
        this.clientConnectionHandler = clientConnectionHandler;
    }

    public void receiveMessage() throws IOException, ParseException {
        String message;

        while (true) {
            message = dataInputStream.readUTF();
            System.out.println("Message received: " + message);
            MessageData messageData = new MessageData(message);

            messageHandle(messageData);
        }
    }

    private void disconnectClient() throws IOException {
        String broadcastMessage = "Client " + clientUserName + " has disconnected.";
        clientConnectionHandler.sendServerMessageBroadcast(broadcastMessage);
        printlnClientStatus("disconnected.");
        clientConnectionHandler.close();
    }

    private void printlnClientStatus(String status) {
        System.out.println(Thread.currentThread().getName() + " " + clientUserName + " " + status);
    }

    private void welcomeClient(MessageData messageData) throws IOException {
        clientUserName = messageData.getUserName();
        clientConnectionHandler.sendServerMessageBroadcast(clientUserName + " has connected.");
        printlnClientStatus("connected.");
    }

    private void messageHandle(MessageData messageData) throws IOException {

        switch (messageData.getStatus()) {

            case CONNECT:
                welcomeClient(messageData);
                break;
            case DISCONNECT:
                disconnectClient();
                break;
            case CHAT:
                clientConnectionHandler.sendClientMessageForBroadcasting(clientUserName + ": " + messageData.getChatMessage());
                break;

            default:
        }
    }

    public void operate() throws IOException, ParseException {
        System.out.println("Server is ready for receiving");
        receiveMessage();
    }
}
