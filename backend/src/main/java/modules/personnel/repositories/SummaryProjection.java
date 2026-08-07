package modules.personnel.repositories;

public interface SummaryProjection {

    Long getTotalPolice();

    Long getOnDuty();

    Long getAtStation();

    Long getOnLeave();

    Long getOnReport();

    Long getOverDailyLimit();
}
