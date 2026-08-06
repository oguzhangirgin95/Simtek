package models.analytics.response;

public class TrendPoint {
    /** gun etiketi, ornek: 'Pzt' */
    public String label;

    public Integer taskCount;

    public Integer activePolice;

    public TrendPoint() {
    }

    public TrendPoint(String label, Integer taskCount, Integer activePolice) {
        this.label = label;
        this.taskCount = taskCount;
        this.activePolice = activePolice;
    }
}
