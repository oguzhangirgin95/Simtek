package models.analytics.request;

public class TaskTrendRequest {
    public String cityId;

    public String unitId;

    /** kac gunluk trend istendigi, bos ise 7 */
    public Integer dayCount;

    public TaskTrendRequest() {
    }
}
