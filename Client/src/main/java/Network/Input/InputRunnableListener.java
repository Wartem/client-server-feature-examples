package Network.Input;

import Network.Client;
import Structure.MessageData;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Structure.Protocol.error;

/**
 * Runs all the time, getting messages from the server.
 */
public class InputRunnableListener implements Runnable {

    private final Client client;
    private boolean run = true;

    public InputRunnableListener(Client client) {
        this.client = client;
    }

    private void messageHandle(String message) throws ParseException {
        MessageData messageData = new MessageData(message);

        switch (messageData.getStatus()) {
            case USERNAME_OK -> {
                System.out.println(messageData.getSource() + ": " + messageData.getPayload());
                client.setLoggedIn(true);
                client.setUsername(messageData.getUserName());
            }
            case USERNAME_ERROR -> System.out.println(messageData.getSource() + ": The username is not valid");
            case DISCONNECT -> {
                System.out.println(messageData.getSource() + ": Disconnected.");
                client.disconnect();
                run = false;
            }
            case SERVER_INFO -> System.out.println(messageData.getSource() + ": " + messageData.getPayload());
            case CLIENT_CHAT -> System.out.println(messageData.getUserName() + ": " + messageData.getPayload());
            default -> System.out.println("Message from server was in wrong format.");
        }
    }

    public String readString() throws InterruptedException {
        try {
            return client.getDataInputStream().readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection to the server was lost. System exits.");
        }
        System.exit(error);
        return "";
    }

    public void run()  {
        String rawMessageString = "";
        while (run) {
            try {
                rawMessageString = readString();
            } catch (InterruptedException ex) {
                Logger.getLogger(InputRunnableListener.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                //System.out.println(rawMessageString);
                messageHandle(rawMessageString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
