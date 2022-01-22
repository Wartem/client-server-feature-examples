package Network;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageData {

    Protocol.StatusEnum status = Protocol.StatusEnum.CHAT;
    String chatMessage = "";
    String userName = "";

    public MessageData() {
    }

    public MessageData(String json) throws ParseException {
        if (!json.isEmpty()) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;

            this.setStatus(getEnumFromString((String) jsonObject.get("status")));
            this.setChatMessage((String) jsonObject.get("chat"));
        }
    }

    public static MessageData getSimpleMessageDataChatObject(String chatMessage) {
        MessageData messageData = new MessageData();
        messageData.setStatus(Protocol.StatusEnum.CHAT);
        messageData.setChatMessage(chatMessage);
        return messageData;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Protocol.StatusEnum getEnumFromString(String enumString) {
        return Protocol.StatusEnum.valueOf(enumString);
    }

    public String getStringFromEnum(Protocol.StatusEnum enumVar) {
        return enumVar.toString();
    }

    public String createJsonFromMessageData() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("status", getStringFromEnum(this.getStatus()));
        jsonObj.put("userName", getUserName());
        jsonObj.put("chat", this.getChatMessage());
        return jsonObj.toString();
    }

    public Protocol.StatusEnum getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = getEnumFromString(status);
    }

    public void setStatus(Protocol.StatusEnum status) {
        this.status = status;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }
}
