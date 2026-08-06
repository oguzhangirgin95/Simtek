package modules.reports.business;

import models.reports.request.ReportEntryRequest;
import models.reports.response.ReportEntryConfirmResponse;
import models.reports.response.ReportEntryExecuteResponse;

public class ReportEntryBusiness {

    // confirm adiminda olusturulacak raporun ozetini doner
    public ReportEntryConfirmResponse Confirm(ReportEntryRequest reportEntryRequest) {

        ReportEntryConfirmResponse reportEntryConfirmResponse = new ReportEntryConfirmResponse();

        if(reportEntryRequest != null && reportEntryRequest.reportName != null) {

            reportEntryConfirmResponse.reportName = reportEntryRequest.reportName;
            reportEntryConfirmResponse.period = reportEntryRequest.startDate + " - " + reportEntryRequest.endDate;
            reportEntryConfirmResponse.message = "Rapor olusturulmaya hazir.";
        }
        else {
            reportEntryConfirmResponse.message = "Rapor adi bulunamadi.";
        }

        return reportEntryConfirmResponse;
    }

    // execute adiminda raporu olusturur
    public ReportEntryExecuteResponse Execute(ReportEntryRequest reportEntryRequest) {

        ReportEntryExecuteResponse reportEntryExecuteResponse = new ReportEntryExecuteResponse(false, null, "Rapor olusturulamadi.");

        if(reportEntryRequest != null && reportEntryRequest.reportName != null) {

            reportEntryExecuteResponse.success = true;
            reportEntryExecuteResponse.reportNo = "RPR-" + Math.abs(reportEntryRequest.reportName.hashCode());
            reportEntryExecuteResponse.message = "Rapor olusturuldu.";
        }

        return reportEntryExecuteResponse;
    }
}
