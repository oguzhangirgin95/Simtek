package models.reports.request;

public class ReportEntryRequest {
    public String reportName;

    public String startDate;

    public String endDate;

    public ReportEntryRequest() {
    }

    public ReportEntryRequest(String reportName, String startDate, String endDate) {
        this.reportName = reportName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
