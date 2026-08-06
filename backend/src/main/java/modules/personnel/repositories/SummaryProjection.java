package modules.personnel.repositories;

/** /dashboard/summary icin tek sorguda hesaplanan sayilar */
public interface SummaryProjection {

    Long getTotalPolice();

    Long getOnDuty();

    Long getAtStation();

    Long getOnLeave();

    Long getOnReport();

    Long getOverDailyLimit();
}
