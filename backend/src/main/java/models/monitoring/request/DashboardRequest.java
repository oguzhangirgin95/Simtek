package models.monitoring.request;

public class DashboardRequest {
    /** Sehir filtresi, bos ise ulke geneli */
    public String cityId;

    /** Birim filtresi, bos ise tum birimler */
    public String unitId;

    /** Durum filtresi: SAHADA, MERKEZDE, IZINDE, RAPORLU. Bos ise tumu */
    public String status;

    public DashboardRequest() {
    }
}
