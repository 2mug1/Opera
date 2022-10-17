package me.hyperbone.opera.server;

public enum ServerStatus {

    ONLINE("オンライン"),
    OFFLINE("オフライン"),
    WHITELISTED("ホワイトリスト"),
    BOOTING("起動中");

    private final String format;

    ServerStatus(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

}
