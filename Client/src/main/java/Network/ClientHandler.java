package Network;

import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientHandler {

    Client client;

    public ClientHandler() {
        client = new Client();
    }

    public static boolean isNameValid(String name) {
        Pattern pattern = Pattern.compile("^[A-Za-z_.]+$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        boolean matchedAtoZ = matcher.matches();
        return matchedAtoZ && !name.isBlank() && name.length() >= 2;
    }

    public static String askForName(String nameType) throws FileNotFoundException {
        System.out.println("Please type your " + nameType + " and press Enter.");
        String name = readAndTrimInput();
        if (!isNameValid(name)) {
            System.out.println(name + " is invalid as " + nameType + " name.");
            askForName(nameType);
        }
        return name;
    }

    public static String readAndTrimInput() throws FileNotFoundException {

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

    public void operate() throws IOException, InterruptedException, ParseException {
        System.out.println("Welcome to the chat client.");
        client.setup();
        while (true) {
            String rawUserInputString = readAndTrimInput();
            client.sendUserMessage(MessageData.getSimpleMessageDataChatObject
                    (rawUserInputString).createJsonFromMessageData());
        }
    }
}
