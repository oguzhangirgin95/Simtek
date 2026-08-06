package models.units.response;

public class UnitListItem {
    public String id;

    public String name;

    public String cityId;

    public String cityName;

    public UnitListItem() {
    }

    public UnitListItem(String id, String name, String cityId, String cityName) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.cityName = cityName;
    }
}
