package models.vehicles.response;

public class VehicleTypeItem {
    public String key;

    public String name;

    public Integer count;

    public VehicleTypeItem() {
    }

    public VehicleTypeItem(String key, String name, Integer count) {
        this.key = key;
        this.name = name;
        this.count = count;
    }
}
