import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Protocol {

    ServerSocket serverSocket;
    Socket socket;
    ArrayList<DataOutputStream> dataOutputStreamList;

    public Server() {
        dataOutputStreamList = new ArrayList<DataOutputStream>();
    }

    public void operate() {
        System.out.println("Server started.");
        while (true) {

            try {
                serverSocket = new ServerSocket(port);
                socket = serverSocket.accept();
                new Thread(new ClientConnectionHandler(socket, this)).start();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addOutputToList(DataOutputStream dataOutputStream) {
        dataOutputStreamList.add(dataOutputStream);
    }

    public void removeOutputFromList(DataOutputStream dataOutputStream) throws IOException {
        for (DataOutputStream dataOut : dataOutputStreamList) {
            if (dataOutputStream == dataOut) {
                dataOut.close();
                dataOutputStreamList.remove(dataOut);
            }
        }
    }

    public synchronized void broadcastMessage(String message) throws IOException {
        for (DataOutputStream dataOutputStream : dataOutputStreamList) {
            // System.out.println(dataOutputStreamList.size() + " dataoutputstreams found in list");
            dataOutputStream.writeUTF(message);
        }
    }
}
