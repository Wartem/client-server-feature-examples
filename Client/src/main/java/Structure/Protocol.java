package Structure;

public final class Protocol {

    public static int port = 6779;
    public static String ip = "localhost";
    public static int error = 3;

    public enum MessageStatus {
        CONNECT,
        OK,
        USERNAME_SET,
        USERNAME_ERROR,
        USERNAME_OK,
        CLIENT_CHAT,
        SERVER_INFO,
        ERROR,
        QUIT,
        DISCONNECT
    }

    public enum MessageOrigin {
        SERVER,
        CLIENT
    }
}
