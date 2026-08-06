package models.monitoring.response;

public class StatusCount {
    public String key;

    public String name;

    public Integer count;

    public StatusCount() {
    }

    public StatusCount(String key, String name, Integer count) {
        this.key = key;
        this.name = name;
        this.count = count;
    }
}
