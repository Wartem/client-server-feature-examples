package Structure;

import Structure.Protocol.MessageStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static Structure.Protocol.MessageOrigin.*;
import static Structure.Protocol.MessageStatus.*;

public class MessageData {

    MessageStatus status = CLIENT_CHAT;
    private String userName = "Undefined";
    private String payload = "Undefined";
    private Protocol.MessageOrigin source = CLIENT;

    public MessageData() {
    }

    public MessageData(MessageStatus messageStatus){
        this.status = messageStatus;
    }

    public MessageData(String json) throws ParseException {
        if (!json.isEmpty()) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;
            this.setStatus(getMessageStatusFromString((String) jsonObject.get("status")));
            this.setSource(getMessageSourceFromString((String) jsonObject.get("source")));
            this.setPayload((String) jsonObject.get("payload"));
            this.setUserName((String) jsonObject.get("username"));
        }
    }

    public static MessageStatus getMessageStatusFromString(String enumString) {
        return MessageStatus.valueOf(enumString);
    }

    public static Protocol.MessageOrigin getMessageSourceFromString(String enumString) {
        return Protocol.MessageOrigin.valueOf(enumString);
    }

    public static String getStringFromEnum(Enum enumVar) {
        return enumVar.toString();
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {

        this.userName = userName;
    }

    public MessageStatus getStatus() {

        return status;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setStatus(MessageStatus status) {

        this.status = status;
    }

    public Protocol.MessageOrigin getSource() {
        return source;
    }

    public void setSource(Protocol.MessageOrigin source) {
        this.source = source;
    }

    @Override
    public String toString(){
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("status", getStringFromEnum(this.getStatus()));
        jsonObj.put("username", getUserName());
        jsonObj.put("payload", this.getPayload());
        jsonObj.put("source", getStringFromEnum(this.getSource()));
        return jsonObj.toString();
    }
}
