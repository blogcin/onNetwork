package xyz.miffle.onnetwork.broadcast;

import com.google.gson.annotations.SerializedName;


public class BroadCastData {

    @SerializedName("message")
    private String message;

    @SerializedName("nickname")
    private String nickName;

    @SerializedName("ipAddress")
    private String ipAddress;

    @SerializedName("type")
    private String type;

    public BroadCastData(String message, String nickname, String ipAddress, BroadCastDataType type) {
        this.message = message;
        this.nickName = nickname;
        this.ipAddress = ipAddress;
        this.type = type.toString();
    }

    public String getMessage() {
        return message;
    }

    public String getNickName() {
        return nickName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getType() {
        return type;
    }
}
