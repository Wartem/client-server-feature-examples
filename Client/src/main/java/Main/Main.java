package Main;

import Network.Client;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        Client client = new Client();
        client.operate();
    }
}
