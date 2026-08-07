package models.reports.response;

public class ReportEntryConfirmResponse {

    public Boolean valid = false;

    public String reportName;

    public String reportTypeName;

    public String cityName;

    public String unitName;

    public String period;

    public Integer policeCount;

    public Integer taskCount;

    public String message;

    public ReportEntryConfirmResponse() {
    }
}
