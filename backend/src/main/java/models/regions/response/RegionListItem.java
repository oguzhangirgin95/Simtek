package models.regions.response;

public class RegionListItem {
    public String id;

    public String name;

    public String plateCode;

    public Double x;

    public Double y;

    public RegionListItem() {
    }

    public RegionListItem(String id, String name, String plateCode, Double x, Double y) {
        this.id = id;
        this.name = name;
        this.plateCode = plateCode;
        this.x = x;
        this.y = y;
    }
}
