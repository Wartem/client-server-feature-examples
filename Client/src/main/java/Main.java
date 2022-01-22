import Network.ClientHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        ClientHandler clientHandler = new ClientHandler();
        clientHandler.operate();
    }
}
