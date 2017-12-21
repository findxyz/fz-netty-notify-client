package xyz.fz.netty.notify.model;

import java.util.Map;

public class NotifyMessage {

    public static final String PING = "ping";

    public static final String PONG = "pong";

    public static final String CONNECT = "connect";

    public static final String CLOSE = "close";

    public static final String MESSAGE = "message";

    public NotifyMessage() {}

    private String fromId;

    private String type;

    private Map<String, Object> data;

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    NotifyMessage(Builder builder) {
        this.fromId = builder.fromId;
        this.type = builder.type;
        this.data = builder.data;
    }

    public static final class Builder {

        String fromId;

        String type;

        Map<String, Object> data;

        public Builder() {
            this.fromId = "";
            this.type = "";
            this.data = null;
        }

        public Builder pingMessage() {
            this.type = PING;
            return this;
        }

        public Builder pongMessage() {
            this.type = PONG;
            return this;
        }

        public Builder connectMessage() {
            this.type = CONNECT;
            return this;
        }

        public Builder closeMessage() {
            this.type = CLOSE;
            return this;
        }

        public Builder message() {
            this.type = MESSAGE;
            return this;
        }

        public Builder from(String id) {
            this.fromId = id;
            return this;
        }

        public Builder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        public NotifyMessage build() {
            return new NotifyMessage(this);
        }
    }
}
