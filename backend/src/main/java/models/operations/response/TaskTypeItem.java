package models.operations.response;

public class TaskTypeItem {
    public String key;

    public String name;

    public Integer count;

    public TaskTypeItem() {
    }

    public TaskTypeItem(String key, String name, Integer count) {
        this.key = key;
        this.name = name;
        this.count = count;
    }
}
