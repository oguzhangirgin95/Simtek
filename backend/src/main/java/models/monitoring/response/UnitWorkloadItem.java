package models.monitoring.response;

public class UnitWorkloadItem {
    public String unitId;

    public String unitName;

    /** birimdeki toplam memur */
    public Integer totalPolice;

    /** sahadaki memur */
    public Integer activePolice;

    /** gunluk gorev sayilarinin toplami */
    public Integer taskLoad;

    /** gorev tiplerine gore kirilim */
    public Integer patrol;

    public Integer radar;

    public Integer motorcycle;

    public Integer schoolCrossing;

    public Integer accidentInvestigation;

    /** en yogun birime gore doluluk orani (%) */
    public Integer loadPercent;

    public UnitWorkloadItem() {
    }
}
