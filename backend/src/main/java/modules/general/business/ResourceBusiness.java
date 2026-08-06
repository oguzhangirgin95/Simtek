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
                // tum ekranlarda kullanilan filtre yazilari
                resourceResponse.resources.add(new Resource("FILTER_CITY", "Sehir"));
                resourceResponse.resources.add(new Resource("FILTER_UNIT", "Birim"));
                resourceResponse.resources.add(new Resource("FILTER_STATUS", "Durum"));
                resourceResponse.resources.add(new Resource("FILTER_ALL", "Tumu"));
                resourceResponse.resources.add(new Resource("FILTER_TYPE", "Arac tipi"));
                resourceResponse.resources.add(new Resource("FILTER_TASKTYPE", "Gorev tipi"));
                resourceResponse.resources.add(new Resource("FILTER_LIMIT", "Limit durumu"));
                resourceResponse.resources.add(new Resource("BUTTON_CLEAR", "Temizle"));
                // grid basliklari tum listelerde ortak
                resourceResponse.resources.add(new Resource("GRID_BADGE", "Sicil"));
                resourceResponse.resources.add(new Resource("GRID_FULLNAME", "Ad Soyad"));
                resourceResponse.resources.add(new Resource("GRID_RANK", "Rutbe"));
                resourceResponse.resources.add(new Resource("GRID_CITY", "Sehir"));
                resourceResponse.resources.add(new Resource("GRID_UNIT", "Birim"));
                resourceResponse.resources.add(new Resource("GRID_STATUS", "Durum"));
                resourceResponse.resources.add(new Resource("GRID_TASKTYPE", "Gorev"));
                resourceResponse.resources.add(new Resource("GRID_SCORE", "Puan"));
                resourceResponse.resources.add(new Resource("GRID_LOCATION", "Konum"));
                resourceResponse.resources.add(new Resource("GRID_STARTTIME", "Baslangic"));
                resourceResponse.resources.add(new Resource("GRID_ENDTIME", "Bitis"));
                resourceResponse.resources.add(new Resource("GRID_TOTALPOLICE", "Personel"));
                resourceResponse.resources.add(new Resource("GRID_ACTIVEPOLICE", "Sahada"));
                resourceResponse.resources.add(new Resource("GRID_PATROL", "Devriye"));
                resourceResponse.resources.add(new Resource("GRID_RADAR", "Radar"));
                resourceResponse.resources.add(new Resource("GRID_MOTORCYCLE", "Motosiklet"));
                resourceResponse.resources.add(new Resource("GRID_TASKLOAD", "Gorev"));
                // detay panelleri
                resourceResponse.resources.add(new Resource("DETAIL_AGE", "Yas"));
                resourceResponse.resources.add(new Resource("DETAIL_PHONE", "Telefon"));
                resourceResponse.resources.add(new Resource("DETAIL_STARTDATE", "Goreve baslama"));
                resourceResponse.resources.add(new Resource("DETAIL_DAILYTASK", "Gunluk gorev"));
                resourceResponse.resources.add(new Resource("DETAIL_PLATE", "Plaka"));
                resourceResponse.resources.add(new Resource("DETAIL_TYPE", "Tip"));
                resourceResponse.resources.add(new Resource("DETAIL_BRAND", "Marka"));
                resourceResponse.resources.add(new Resource("DETAIL_MODEL", "Model"));
                resourceResponse.resources.add(new Resource("DETAIL_KM", "Kilometre"));
                resourceResponse.resources.add(new Resource("DETAIL_MAINTENANCE", "Son bakim"));
                resourceResponse.resources.add(new Resource("BUTTON_SAVE", "Kaydet"));
                resourceResponse.resources.add(new Resource("BUTTON_LOGOUT", "Cikis"));
                resourceResponse.resources.add(new Resource("GRID_TASKCOUNT", "Gorev"));
                break;

            case "taskassign":
                resourceResponse.resources.add(new Resource("TASKASSIGN_TITLE", "Gorev Atama"));
                resourceResponse.resources.add(new Resource("TASKASSIGN_FORM", "Gorev bilgileri"));
                resourceResponse.resources.add(new Resource("TASKASSIGN_HINT", "Devam ile onay ekranina gecilir."));
                resourceResponse.resources.add(new Resource("TASKASSIGN_POLICE", "Personel"));
                resourceResponse.resources.add(new Resource("TASKASSIGN_LOCATION", "Gorev yeri"));
                break;

            case "reportentry":
                resourceResponse.resources.add(new Resource("REPORTENTRY_TITLE", "Rapor Girisi"));
                resourceResponse.resources.add(new Resource("REPORTENTRY_FORM", "Rapor bilgileri"));
                resourceResponse.resources.add(new Resource("REPORTENTRY_HINT", "Devam ile kapsam ozetine gecilir."));
                resourceResponse.resources.add(new Resource("REPORTENTRY_NAME", "Rapor adi"));
                resourceResponse.resources.add(new Resource("REPORTENTRY_TYPE", "Rapor tipi"));
                resourceResponse.resources.add(new Resource("REPORTENTRY_STARTDATE", "Baslangic tarihi"));
                resourceResponse.resources.add(new Resource("REPORTENTRY_ENDDATE", "Bitis tarihi"));
                break;

            case "reportlist":
                resourceResponse.resources.add(new Resource("REPORTLIST_TITLE", "Olusturulan Raporlar"));
                resourceResponse.resources.add(new Resource("REPORTLIST_LIST", "Raporlar"));
                resourceResponse.resources.add(new Resource("REPORTLIST_TOTAL", "Rapor sayisi"));
                resourceResponse.resources.add(new Resource("REPORTLIST_EMPTY", "Henuz rapor olusturulmadi"));
                resourceResponse.resources.add(new Resource("REPORTLIST_NEW", "Yeni rapor"));
                resourceResponse.resources.add(new Resource("REPORTENTRY_TYPE", "Rapor tipi"));
                resourceResponse.resources.add(new Resource("GRID_REPORTNO", "Rapor No"));
                resourceResponse.resources.add(new Resource("GRID_REPORTNAME", "Rapor adi"));
                resourceResponse.resources.add(new Resource("GRID_PERIOD", "Donem"));
                resourceResponse.resources.add(new Resource("GRID_CREATEDDATE", "Tarih"));
                break;

            case "preferences":
                resourceResponse.resources.add(new Resource("PREFERENCES_TITLE", "Ayarlar"));
                resourceResponse.resources.add(new Resource("PREFERENCES_FORM", "Kullanici tercihleri"));
                resourceResponse.resources.add(new Resource("PREFERENCES_LANGUAGE", "Dil"));
                resourceResponse.resources.add(new Resource("PREFERENCES_PAGESIZE", "Sayfa basina kayit"));
                resourceResponse.resources.add(new Resource("PREFERENCES_DEFAULTCITY", "Varsayilan sehir"));
                resourceResponse.resources.add(new Resource("PREFERENCES_REFRESH", "Otomatik yenileme (sn)"));
                resourceResponse.resources.add(new Resource("LANGUAGE_TR", "Turkce"));
                resourceResponse.resources.add(new Resource("LANGUAGE_EN", "Ingilizce"));
                break;

            case "policelist":
                resourceResponse.resources.add(new Resource("POLICELIST_TITLE", "Personel Listesi"));
                resourceResponse.resources.add(new Resource("POLICELIST_SEARCH", "Ad veya sicil"));
                resourceResponse.resources.add(new Resource("POLICELIST_LIST", "Personel"));
                resourceResponse.resources.add(new Resource("POLICELIST_HINT", "Detay icin satira tiklayin"));
                resourceResponse.resources.add(new Resource("POLICELIST_DETAIL", "Detay"));
                resourceResponse.resources.add(new Resource("POLICELIST_SELECT", "Listeden bir personel secin."));
                resourceResponse.resources
                        .add(new Resource("POLICELIST_OVERLIMIT", "Bu personel gunluk gorev limitini asmis."));
                resourceResponse.resources.add(new Resource("POLICELIST_NOVEHICLE", "Bu personele tanimli arac yok."));
                resourceResponse.resources.add(new Resource("POLICELIST_EMPTY", "Kayit bulunamadi"));
                resourceResponse.resources.add(new Resource("POLICELIST_EMPTYTASK", "Gorev kaydi yok"));
                resourceResponse.resources.add(new Resource("TAB_PERSON", "Kisi"));
                resourceResponse.resources.add(new Resource("TAB_VEHICLE", "Arac"));
                resourceResponse.resources.add(new Resource("TAB_TASK", "Gorevler"));
                break;

            case "vehiclelist":
                resourceResponse.resources.add(new Resource("VEHICLELIST_TITLE", "Arac Envanteri"));
                resourceResponse.resources.add(new Resource("VEHICLELIST_SEARCH", "Plaka veya marka"));
                resourceResponse.resources.add(new Resource("VEHICLELIST_LIST", "Araclar"));
                resourceResponse.resources.add(new Resource("VEHICLELIST_HINT", "Detay icin satira tiklayin"));
                resourceResponse.resources.add(new Resource("VEHICLELIST_DETAIL", "Arac detayi"));
                resourceResponse.resources.add(new Resource("VEHICLELIST_SELECT", "Listeden bir arac secin."));
                resourceResponse.resources.add(new Resource("VEHICLELIST_TYPECHART", "Tip dagilimi"));
                resourceResponse.resources.add(new Resource("VEHICLELIST_TOTAL", "Listelenen arac"));
                resourceResponse.resources.add(new Resource("VEHICLELIST_EMPTY", "Arac bulunamadi"));
                resourceResponse.resources.add(new Resource("GRID_PLATE", "Plaka"));
                resourceResponse.resources.add(new Resource("GRID_TYPE", "Tip"));
                resourceResponse.resources.add(new Resource("GRID_BRAND", "Marka"));
                resourceResponse.resources.add(new Resource("GRID_MODEL", "Model"));
                resourceResponse.resources.add(new Resource("GRID_MODELYEAR", "Yil"));
                resourceResponse.resources.add(new Resource("GRID_KM", "Km"));
                resourceResponse.resources.add(new Resource("GRID_ASSIGNED", "Zimmetli"));
                break;

            case "tasklist":
                resourceResponse.resources.add(new Resource("TASKLIST_TITLE", "Gorev Takibi"));
                resourceResponse.resources.add(new Resource("TASKLIST_TOTAL", "Listelenen gorev"));
                resourceResponse.resources.add(new Resource("TASKLIST_LIST", "Gorevler"));
                resourceResponse.resources.add(new Resource("TASKLIST_TYPECHART", "Gorev tipi dagilimi"));
                resourceResponse.resources.add(new Resource("TASKLIST_EMPTY", "Gorev bulunamadi"));
                resourceResponse.resources.add(new Resource("TASKLIST_ONLYOVERLIMIT", "Limiti asanlar"));
                resourceResponse.resources.add(new Resource("TASKSTATUS_DONE", "Tamamlandi"));
                resourceResponse.resources.add(new Resource("TASKSTATUS_ACTIVE", "Devam ediyor"));
                resourceResponse.resources.add(new Resource("TASKSTATUS_PLANNED", "Planlandi"));
                break;

            case "unitlist":
                resourceResponse.resources.add(new Resource("UNITLIST_TITLE", "Birim Yogunlugu"));
                resourceResponse.resources.add(new Resource("UNITLIST_LIST", "Birimler"));
                resourceResponse.resources.add(new Resource("UNITLIST_TOTALTASK", "Toplam gorev"));
                resourceResponse.resources.add(new Resource("UNITLIST_UNITCOUNT", "Birim sayisi"));
                resourceResponse.resources.add(new Resource("UNIT_WORKLOAD_TITLE", "birim gorev yogunlugu"));
                resourceResponse.resources.add(new Resource("EMPTY_UNIT", "Birim bulunamadi"));
                resourceResponse.resources.add(new Resource("GRID_SCHOOL", "Okul Gecidi"));
                resourceResponse.resources.add(new Resource("GRID_ACCIDENT", "Kaza"));
                resourceResponse.resources.add(new Resource("GRID_LOADPERCENT", "Yogunluk %"));
                break;

            case "regionlist":
                resourceResponse.resources.add(new Resource("REGIONLIST_TITLE", "Sehir Bazli Dagilim"));
                resourceResponse.resources.add(new Resource("REGIONLIST_LIST", "Sehirler"));
                resourceResponse.resources.add(new Resource("REGIONLIST_HINT", "Haritada secmek icin satira tiklayin"));
                resourceResponse.resources.add(new Resource("REGIONLIST_BUSIEST", "En yogun sehir"));
                resourceResponse.resources.add(new Resource("REGIONLIST_EMPTY", "Sehir bulunamadi"));
                resourceResponse.resources.add(new Resource("MAP_TITLE", "Sehir bazli aktif memur"));
                resourceResponse.resources.add(new Resource("STAT_TOTAL", "Toplam personel"));
                resourceResponse.resources.add(new Resource("STAT_ONDUTY", "Sahada"));
                resourceResponse.resources.add(new Resource("GRID_PLATECODE", "Plaka"));
                resourceResponse.resources.add(new Resource("GRID_ACTIVEPERCENT", "Aktiflik %"));
                resourceResponse.resources.add(new Resource("GRID_UNITCOUNT", "Birim"));
                break;

            case "tasktrend":
                resourceResponse.resources.add(new Resource("TASKTREND_TITLE", "Gorev Analizi"));
                resourceResponse.resources.add(new Resource("TASKTREND_CHART", "Gunluk gorev trendi"));
                resourceResponse.resources.add(new Resource("TASKTREND_SCOPE", "Kapsam"));
                resourceResponse.resources.add(new Resource("TASKTREND_TOTAL", "Toplam gorev"));
                resourceResponse.resources.add(new Resource("TASKTREND_AVERAGE", "Gunluk ortalama"));
                resourceResponse.resources.add(new Resource("TASKLIST_TYPECHART", "Gorev tipi dagilimi"));
                resourceResponse.resources.add(new Resource("GRID_DAY", "Gun"));
                resourceResponse.resources.add(new Resource("GRID_TASKCOUNT", "Gorev"));
                break;

            case "dashboard":
                resourceResponse.resources.add(new Resource("DASHBOARD_TITLE", "Trafik Polisi Takip Panosu"));
                resourceResponse.resources.add(new Resource("STAT_TOTAL", "Toplam personel"));
                resourceResponse.resources.add(new Resource("STAT_ONDUTY", "Sahada"));
                resourceResponse.resources.add(new Resource("STAT_ATSTATION", "Merkezde"));
                resourceResponse.resources.add(new Resource("STAT_ONLEAVE", "Izinde"));
                resourceResponse.resources.add(new Resource("STAT_ONREPORT", "Raporlu"));
                resourceResponse.resources.add(new Resource("STAT_OVERLIMIT", "Limiti asan"));
                resourceResponse.resources.add(new Resource("MAP_TITLE", "Sehir bazli aktif memur"));
                resourceResponse.resources.add(new Resource("STATUS_CHART_TITLE", "Durum dagilimi"));
                resourceResponse.resources.add(new Resource("UNIT_WORKLOAD_TITLE", "birim gorev yogunlugu"));
                resourceResponse.resources.add(new Resource("EMPTY_UNIT", "Birim bulunamadi"));
                resourceResponse.resources.add(new Resource("GRID_UNIT", "Birim"));
                resourceResponse.resources.add(new Resource("GRID_TOTALPOLICE", "Personel"));
                resourceResponse.resources.add(new Resource("GRID_ACTIVEPOLICE", "Sahada"));
                resourceResponse.resources.add(new Resource("GRID_PATROL", "Devriye"));
                resourceResponse.resources.add(new Resource("GRID_RADAR", "Radar"));
                resourceResponse.resources.add(new Resource("GRID_MOTORCYCLE", "Motosiklet"));
                resourceResponse.resources.add(new Resource("GRID_TASKLOAD", "Gorev"));
                break;

            case "login":
                resourceResponse.resources.add(new Resource("LOGIN_TITLE", "Giris"));
                resourceResponse.resources.add(new Resource("LOGIN_USERNAME", "Kullanici adi"));
                resourceResponse.resources.add(new Resource("LOGIN_PASSWORD", "Sifre"));
                resourceResponse.resources.add(new Resource("LOGIN_BUTTON", "Giris yap"));
                resourceResponse.resources.add(new Resource("LOGIN_ERROR", "Kullanici adi veya sifre hatali."));
                break;


            default:
                break;
        }

        return resourceResponse;
    }
}
