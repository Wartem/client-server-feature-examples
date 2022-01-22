package Network;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs all the time, getting messages from the server's broadcast
 */
public class ClientReceiver implements Runnable {

    private final DataInputStream dataInputStream;
    private final Client client;
    private boolean run = true;

    public ClientReceiver(DataInputStream dataInputStream, Client client) throws IOException {
        this.dataInputStream = dataInputStream;
        this.client = client;
    }

    public void receiveMessage() {
        String message = "";
        while (run) {
            try {
                message = dataInputStream.readUTF();
            } catch (IOException ex) {
                Logger.getLogger(ClientReceiver.class.getName()).log(Level.SEVERE, null, ex);
                run = false;
            }
            System.out.println(message);
        }
        client.disconnect();
    }

    public void run() {
        receiveMessage();
    }
}
