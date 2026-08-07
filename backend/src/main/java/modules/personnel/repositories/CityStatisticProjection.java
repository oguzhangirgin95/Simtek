package modules.personnel.repositories;

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
