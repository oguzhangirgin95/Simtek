package models.vehicles.request;

public class VehicleListRequest {
    /** Sehir filtresi, bos ise tum sehirler */
    public String cityId;

    /** Birim filtresi, bos ise tum birimler */
    public String unitId;

    /** Arac tipi: Otomobil, Motosiklet. Bos ise tumu */
    public String type;

    /** plaka veya marka/model icinde arama */
    public String searchText;

    public Integer pageNumber;

    public Integer pageSize;

    public VehicleListRequest() {
    }
}
