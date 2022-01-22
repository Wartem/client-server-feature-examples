public interface Protocol {

    int port = 6779;
    String ip = "localhost";
    int List = 0;
    int Get = 1;
    int Put = 2;
    int Del = 3;
    int Rename = 4;
    String Dir = "Directory//";
    int Chat = 5;
    int Connect = 11;
    String Quit = "Quit";
    String Close = "Close1000";

    enum StatusEnum {
        SET_USERNAME,
        CHAT,
        CONNECT,
        DISCONNECT
    }
}
