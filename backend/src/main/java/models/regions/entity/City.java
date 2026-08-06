package models.regions.entity;

public class City {
    public String id;

    public String name;

    public String plateCode;

    /** harita uzerindeki yatay konum (0-100) */
    public Double x;

    /** harita uzerindeki dikey konum (0-60) */
    public Double y;

    public City() {
    }

    public City(String id, String name, String plateCode, Double x, Double y) {
        this.id = id;
        this.name = name;
        this.plateCode = plateCode;
        this.x = x;
        this.y = y;
    }
}
