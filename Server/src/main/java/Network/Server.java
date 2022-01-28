package Network;

import Network.Input.InputRunnableListener;
import Structure.MessageData;

import static Structure.Protocol.MessageOrigin.CLIENT;
import static Structure.Protocol.MessageOrigin.SERVER;
import static Structure.Protocol.port;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server{

    ServerSocket serverSocket;
    ArrayList<InputRunnableListener> inputRunnableListenerArrayList;
    boolean run = true;

    public Server() {
        inputRunnableListenerArrayList = new ArrayList<>();
    }


    public synchronized void broadcastMessageFromClient(MessageData messageData) throws IOException {
        for (InputRunnableListener inputRunnableListener : inputRunnableListenerArrayList) {
            // System.out.println(dataOutputStreamList.size() + " dataoutputstreams found in list");
            messageData.setSource(CLIENT);
            writeTheUTF(inputRunnableListener.getDataOutputStream(), messageData.toString());
        }
    }

    public synchronized void broadcastServerMessage(MessageData messageData) throws IOException {
        for (InputRunnableListener inputRunnableListener : inputRunnableListenerArrayList) {
            // System.out.println(dataOutputStreamList.size() + " dataoutputstreams found in list");
            messageData.setSource(SERVER);
            if(inputRunnableListener.getDataOutputStream() != null) {
                writeTheUTF(inputRunnableListener.getDataOutputStream(), messageData.toString());
            }else{
                inputRunnableListenerArrayList.remove(inputRunnableListener);
            }
        }
    }

    public void writeTheUTF(DataOutputStream dataOutputStream, String string) throws IOException {
        try {
            dataOutputStream.writeUTF(string);
        } catch (IOException e) {
            dataOutputStream.close();
            e.printStackTrace();
        }
    }

    public void operate() throws IOException {
        System.out.println("Server started.");
        serverSocket = new ServerSocket(port);
        while (run) {
            try {
                Socket socket = serverSocket.accept();
                InputRunnableListener inputRunnableListener;
                inputRunnableListener = new InputRunnableListener(socket, this);

                new Thread(inputRunnableListener).start();
            } catch (IOException e) {
                e.printStackTrace();
                serverSocket.close();
            }
        }
    }

    public boolean isClientNameAvailable(String name){
        for(InputRunnableListener inputRunnableListener : inputRunnableListenerArrayList){
            if(inputRunnableListener.getClientUserName().equals(name)){
                return false;
            }
        }
        return true;
    }

    public void addInputRunnableListenerToList(InputRunnableListener inputRunnableListener){
        inputRunnableListenerArrayList.add(inputRunnableListener);
    }

    public void removeInputRunnableListenerFromList(InputRunnableListener inputListener){
        for(InputRunnableListener inputRunnableListener : inputRunnableListenerArrayList){
            if(inputRunnableListener.getDataOutputStream() == inputListener.getDataOutputStream()){
                inputRunnableListenerArrayList.remove(inputListener);
            }
        }
    }
}
