package models.monitoring.response;

public class CityStatistic {
    public String cityId;

    public String cityName;

    public String plateCode;

    /** harita uzerindeki konum */
    public Double x;

    public Double y;

    public Integer totalPolice;

    /** sahada gorevde olan memur sayisi */
    public Integer activePolice;

    /** aktif memurun toplama orani (%) */
    public Integer activePercent;

    public Integer unitCount;

    public CityStatistic() {
    }
}
