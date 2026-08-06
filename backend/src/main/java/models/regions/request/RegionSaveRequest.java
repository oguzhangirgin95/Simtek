package models.regions.request;

public class RegionSaveRequest {
    /** dolu ise guncelleme, bos ise yeni kayit */
    public String id;

    public String name;

    public String plateCode;

    public Double x;

    public Double y;

    public Integer sortOrder;

    public RegionSaveRequest() {
    }
}
