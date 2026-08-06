package models.units.request;

public class UnitListRequest {
    /** bos ise tum sehirlerin birimleri doner */
    public String cityId;

    public UnitListRequest() {
    }

    public UnitListRequest(String cityId) {
        this.cityId = cityId;
    }
}
