package modules.personnel.repositories;

/** /dashboard/unitworkload icin birim bazli sayilar */
public interface UnitWorkloadProjection {

    String getUnitId();

    String getUnitName();

    Long getTotalPolice();

    Long getActivePolice();

    Long getTaskLoad();

    Long getPatrol();

    Long getRadar();

    Long getMotorcycle();

    Long getSchoolCrossing();

    Long getAccidentInvestigation();
}
