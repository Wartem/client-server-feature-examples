package Main;

import Network.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new Server().operate();
        System.exit(0);
    }
}
