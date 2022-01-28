package Network;

import Network.Input.InputRunnableListener;
import Structure.MessageData;
import Structure.Protocol;

import java.io.*;
import java.net.Socket;

import static Structure.Protocol.*;
import static Structure.Protocol.MessageStatus.CLIENT_CHAT;

public class Client {

    private static Socket socket;
    private static DataInputStream dataInputStream;
    private static DataOutputStream dataOutputStream;

    public String username = "Undefined";
    boolean run = true;
    boolean loggedIn = false;

    public void startSession() throws IOException{
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        InputRunnableListener inputRunnableListener = new InputRunnableListener(this);
        new Thread(inputRunnableListener).start();
    }


    public static synchronized void sendMessage(DataOutputStream dataOutputStream, String message) throws IOException {
        dataOutputStream.writeUTF(message);
    }

    private static void sendAsJson(MessageData messageData) throws IOException {
        sendMessage(dataOutputStream, messageData.toString());
    }

    public static String askForName(String nameType) {
        System.out.println("Please type your " + nameType + " and press Enter.");
        return readAndTrimInput();
    }

    public synchronized void setLoggedIn(boolean loggedIn){
        this.loggedIn = loggedIn;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void chat() throws IOException {
        String rawUserInputString = readAndTrimInput();

        MessageData messageData = new MessageData();
        messageData.setStatus(CLIENT_CHAT);
        messageData.setPayload(rawUserInputString);
        sendAsJson(messageData);
    }

    public void login() throws IOException {
        String userName = askForName("Username");
        MessageData messageData = new MessageData();
        messageData.setStatus(Protocol.MessageStatus.USERNAME_SET);
        messageData.setPayload(userName);
        sendAsJson(messageData);
    }

    public void setup() throws IOException, InterruptedException {
        connect();
        startSession();
        int time = 1000;
        while(!loggedIn){
            login();
            System.out.println("Waiting for server response ...");
            Thread.sleep(time);
            if(time > 5000){
                break;
            }
            time += 1000;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void operate() throws IOException, InterruptedException {
        System.out.println("Welcome " + getUsername() + " to the chat client.");
        setup();
        while (run) {
            chat();
        }
        disconnect();
    }

    public void sendUserMessage(String message) throws IOException {
            dataOutputStream.writeUTF(message);
    }

    public void connect() throws InterruptedException {

        try {
            System.out.println("Connecting from client. IP: " + ip + " Port: " + port);
            socket = new Socket(ip, port);
        } catch (IOException ioe) {
            System.out.println("Connection Error");
            //ioe.printStackTrace();
            Thread.sleep(5000);
            connect();
        }
    }

    public static String readAndTrimInput() {

        String input = "";
        try {
            InputStreamReader streamReader = new InputStreamReader(System.in);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            input = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if ("0".equals(input)) {
            System.exit(0);
        }
        return input;
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
}
