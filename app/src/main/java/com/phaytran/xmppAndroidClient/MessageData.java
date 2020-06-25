package com.phaytran.xmppAndroidClient;

public class MessageData {
    private String heading,message;
    public MessageData(String heading,String message){
        this.heading = heading;
        this.message = message;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
