package models.units.entity;

public class Unit {
    public String id;

    public String name;

    public String cityId;

    public String cityName;

    public Unit() {
    }

    public Unit(String id, String name, String cityId, String cityName) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.cityName = cityName;
    }
}
