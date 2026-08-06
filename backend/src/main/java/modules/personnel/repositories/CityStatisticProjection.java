package modules.personnel.repositories;

/** /dashboard/mapstatistics icin sehir bazli sayilar */
public interface CityStatisticProjection {

    String getCityId();

    String getCityName();

    String getPlateCode();

    Double getX();

    Double getY();

    Long getTotalPolice();

    Long getActivePolice();

    Long getUnitCount();
}
