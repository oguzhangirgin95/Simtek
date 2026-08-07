package modules.personnel.repositories;

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
