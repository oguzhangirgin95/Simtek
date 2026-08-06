package models.personnel.request;

public class PoliceListRequest {
    /** Sehir filtresi, bos ise tum sehirler */
    public String cityId;

    /** Birim filtresi, bos ise tum birimler */
    public String unitId;

    /** Durum filtresi: SAHADA, MERKEZDE, IZINDE, RAPORLU. Bos ise tumu */
    public String status;

    /** ad soyad veya sicil numarasinda arama */
    public String searchText;

    /** siralama alani: fullName, score, badgeNumber, cityName, unitName */
    public String sortField;

    /** ASC veya DESC, bos ise ASC */
    public String sortDirection;

    /** 1'den baslar, bos veya 0 ise sayfalama yapilmaz */
    public Integer pageNumber;

    /** sayfa basina kayit, bos veya 0 ise tum kayitlar doner */
    public Integer pageSize;

    public PoliceListRequest() {
    }
}
