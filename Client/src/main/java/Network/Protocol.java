package Network;

public interface Protocol {

    int port = 6779;
    String ip = "localhost";
    int List = 0;
    int Get = 1;
    int Put = 2;
    int Del = 3;
    int Rename = 4;
    String Dir = "Directory//";
    String Quit = "Quit";
    String Close = "Close1000";

    enum StatusEnum {
        SET_USERNAME,
        CHAT,
        CONNECT,
        DISCONNECT
    }
}
