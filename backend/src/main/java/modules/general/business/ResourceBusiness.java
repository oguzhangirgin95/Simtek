package modules.general.business;

import models.general.request.ResourceRequest;
import models.general.response.Resource;
import models.general.response.ResourceResponse;

public class ResourceBusiness {

    // transactionName'e ait resource listesini doner, 'general' tum ekranlarda ortak olanlardir
    public ResourceResponse Get(ResourceRequest resourceRequest) {

        String transactionName = resourceRequest == null || resourceRequest.transactionName == null
                ? ""
                : resourceRequest.transactionName;

        ResourceResponse resourceResponse = new ResourceResponse(transactionName);

        switch (transactionName) {
            case "general":
                resourceResponse.resources.add(new Resource("APP_TITLE", "Simtek"));
                resourceResponse.resources.add(new Resource("BUTTON_CONTINUE", "Devam"));
                resourceResponse.resources.add(new Resource("BUTTON_BACK", "Geri"));
                resourceResponse.resources.add(new Resource("LOADING", "Yukleniyor..."));
                resourceResponse.resources.add(new Resource("CONFIRM_TITLE", "Onay"));
                resourceResponse.resources.add(new Resource("EXECUTE_TITLE", "Sonuc"));
                break;

            case "login":
                resourceResponse.resources.add(new Resource("LOGIN_TITLE", "Giris"));
                resourceResponse.resources.add(new Resource("LOGIN_USERNAME", "Kullanici adi"));
                resourceResponse.resources.add(new Resource("LOGIN_PASSWORD", "Sifre"));
                resourceResponse.resources.add(new Resource("LOGIN_BUTTON", "Giris yap"));
                resourceResponse.resources.add(new Resource("LOGIN_ERROR", "Kullanici adi veya sifre hatali."));
                break;

            case "reportentry":
                resourceResponse.resources.add(new Resource("REPORT_TITLE", "Rapor Girisi"));
                resourceResponse.resources.add(new Resource("REPORT_NAME", "Rapor adi"));
                resourceResponse.resources.add(new Resource("REPORT_STARTDATE", "Baslangic tarihi"));
                resourceResponse.resources.add(new Resource("REPORT_ENDDATE", "Bitis tarihi"));
                break;

            default:
                break;
        }

        return resourceResponse;
    }
}
