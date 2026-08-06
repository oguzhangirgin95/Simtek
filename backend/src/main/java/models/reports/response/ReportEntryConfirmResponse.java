package models.reports.response;

public class ReportEntryConfirmResponse {

    public String reportName;

    public String period;

    public String message;

    public ReportEntryConfirmResponse() {
    }

    public ReportEntryConfirmResponse(String reportName, String period, String message) {
        this.reportName = reportName;
        this.period = period;
        this.message = message;
    }
}
