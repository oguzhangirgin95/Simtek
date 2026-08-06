package models.reports.response;

public class ReportEntryExecuteResponse {

    public Boolean success;

    public String reportNo;

    public String message;

    public ReportEntryExecuteResponse() {
    }

    public ReportEntryExecuteResponse(Boolean success, String reportNo, String message) {
        this.success = success;
        this.reportNo = reportNo;
        this.message = message;
    }
}
