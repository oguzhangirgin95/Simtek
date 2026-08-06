package com.example.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import models.analytics.request.TaskTrendRequest;
import models.firstlevel.request.LoginRequest;
import models.general.request.ResourceRequest;
import models.monitoring.request.DashboardRequest;
import models.operations.request.TaskAssignRequest;
import models.operations.request.TaskDeleteRequest;
import models.operations.request.TaskListRequest;
import models.operations.request.TaskTypeListRequest;
import models.personnel.request.PoliceDeleteRequest;
import models.personnel.request.PoliceDetailRequest;
import models.personnel.request.PoliceListRequest;
import models.personnel.request.PoliceSaveRequest;
import models.regions.request.RegionDeleteRequest;
import models.regions.request.RegionListRequest;
import models.regions.request.RegionSaveRequest;
import models.reports.request.ReportEntryRequest;
import models.reports.request.ReportListRequest;
import models.settings.request.SettingGetRequest;
import models.settings.request.SettingSaveRequest;
import models.units.request.UnitDeleteRequest;
import models.units.request.UnitListRequest;
import models.units.request.UnitSaveRequest;
import models.vehicles.request.VehicleDetailRequest;
import models.vehicles.request.VehicleListRequest;
import models.vehicles.request.VehicleTypeListRequest;
import modules.analytics.business.AnalyticsBusiness;
import modules.firstlevel.business.LoginBusiness;
import modules.general.business.ResourceBusiness;
import modules.monitoring.business.DashboardBusiness;
import modules.operations.business.TaskBusiness;
import modules.personnel.business.PoliceBusiness;
import modules.regions.business.RegionBusiness;
import modules.reports.business.ReportEntryBusiness;
import modules.settings.business.SettingBusiness;
import modules.units.business.UnitBusiness;
import modules.vehicles.business.VehicleBusiness;

@SpringBootTest
@Import(EmbeddedPostgresConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseIntegrationTests {

    @Autowired
    private RegionBusiness regionBusiness;
    @Autowired
    private UnitBusiness unitBusiness;
    @Autowired
    private PoliceBusiness policeBusiness;
    @Autowired
    private VehicleBusiness vehicleBusiness;
    @Autowired
    private TaskBusiness taskBusiness;
    @Autowired
    private DashboardBusiness dashboardBusiness;
    @Autowired
    private AnalyticsBusiness analyticsBusiness;
    @Autowired
    private ReportEntryBusiness reportEntryBusiness;
    @Autowired
    private SettingBusiness settingBusiness;
    @Autowired
    private LoginBusiness loginBusiness;
    @Autowired
    private ResourceBusiness resourceBusiness;

    /* ---------------- migration / seed ---------------- */

    @Test
    @Order(1)
    void scriptler_calisti_ve_veri_yuklendi() {
        assertThat(regionBusiness.RegionList(new RegionListRequest()).totalCount).isEqualTo(14);
        assertThat(unitBusiness.UnitList(new UnitListRequest()).totalCount).isEqualTo(45);
        assertThat(unitBusiness.UnitList(new UnitListRequest("06")).totalCount).isEqualTo(6);
        assertThat(policeBusiness.PoliceList(new PoliceListRequest()).totalCount).isEqualTo(276);
    }

    @Test
    @Order(2)
    void resourcelar_veritabanindan_geliyor() {
        assertThat(resourceBusiness.Get(new ResourceRequest("general")).resources).isNotEmpty();
        assertThat(resourceBusiness.Get(new ResourceRequest("dashboard")).resources).isNotEmpty();
    }

    @Test
    @Order(3)
    void kullanici_veritabanindan_dogrulaniyor() {
        LoginRequest ok = new LoginRequest("oguz", "1234");
        assertThat(loginBusiness.Login(ok).success).isTrue();
        assertThat(loginBusiness.Login(ok).token).isEqualTo("TOKEN-OGUZ");

        assertThat(loginBusiness.Login(new LoginRequest("oguz", "yanlis")).success).isFalse();
        assertThat(loginBusiness.Login(new LoginRequest("yok", "1234")).success).isFalse();
    }

    /* ---------------- pano sorgulari ---------------- */

    @Test
    @Order(4)
    void pano_sayilari_dogru() {
        var summary = dashboardBusiness.Summary(new DashboardRequest());
        assertThat(summary.totalPolice).isEqualTo(276);
        assertThat(summary.onDuty + summary.atStation + summary.onLeave + summary.onReport).isEqualTo(276);
        assertThat(summary.overDailyLimit).isGreaterThan(0);

        DashboardRequest ankara = new DashboardRequest();
        ankara.cityId = "06";
        assertThat(dashboardBusiness.Summary(ankara).totalPolice).isEqualTo(36);
        assertThat(dashboardBusiness.Summary(ankara).unitCount).isEqualTo(6);

        var map = dashboardBusiness.MapStatistics(new DashboardRequest());
        assertThat(map.cities).hasSize(14);
        assertThat(map.totalPolice).isEqualTo(276);
        assertThat(map.busiestCityName).isEqualTo("Istanbul");

        var workload = dashboardBusiness.UnitWorkload(new DashboardRequest());
        assertThat(workload.cityName).isEqualTo("Ankara");
        assertThat(workload.units).hasSize(6);
        // her birimde sahada personel olmali
        assertThat(workload.units).allMatch(unit -> unit.activePolice > 0);
        assertThat(workload.totalTaskLoad).isEqualTo(226);
    }

    @Test
    @Order(5)
    void gorev_sayilari_pano_ile_tutarli() {
        TaskTypeListRequest request = new TaskTypeListRequest();
        request.cityId = "06";
        assertThat(taskBusiness.TaskTypeList(request).totalTaskCount).isEqualTo(226);
    }

    /* ---------------- listeler ---------------- */

    @Test
    @Order(6)
    void filtre_siralama_sayfalama_veritabaninda() {
        PoliceListRequest request = new PoliceListRequest();
        request.cityId = "06";
        request.unitId = "06-B1";
        request.status = "SAHADA";
        assertThat(policeBusiness.PoliceList(request).totalCount).isEqualTo(3);

        PoliceListRequest search = new PoliceListRequest();
        search.cityId = "06";
        search.searchText = "zeynep";
        assertThat(policeBusiness.PoliceList(search).totalCount).isEqualTo(2);

        PoliceListRequest paged = new PoliceListRequest();
        paged.cityId = "34";
        paged.pageNumber = 2;
        paged.pageSize = 5;
        var pageResult = policeBusiness.PoliceList(paged);
        assertThat(pageResult.totalCount).isEqualTo(48);
        assertThat(pageResult.policeList).hasSize(5);

        PoliceListRequest sorted = new PoliceListRequest();
        sorted.cityId = "06";
        sorted.sortField = "score";
        sorted.sortDirection = "DESC";
        var sortedResult = policeBusiness.PoliceList(sorted);
        assertThat(sortedResult.policeList.get(0).score)
                .isGreaterThanOrEqualTo(sortedResult.policeList.get(1).score);
    }

    @Test
    @Order(7)
    void detay_ve_arac_eslesiyor() {
        var detail = policeBusiness.PoliceDetail(new PoliceDetailRequest("06-1001"));
        assertThat(detail.found).isTrue();
        assertThat(detail.cityName).isEqualTo("Ankara");
        assertThat(detail.unitName).isEqualTo("Cankaya Trafik Denetleme");

        VehicleDetailRequest vehicleRequest = new VehicleDetailRequest();
        vehicleRequest.policeId = "06-1001";
        var vehicle = vehicleBusiness.VehicleDetail(vehicleRequest);
        assertThat(vehicle.found).isTrue();
        assertThat(vehicle.plate).isEqualTo(detail.vehiclePlate);

        assertThat(policeBusiness.PoliceDetail(new PoliceDetailRequest("YOK")).found).isFalse();
    }

    @Test
    @Order(8)
    void arac_envanteri_filtreleniyor() {
        assertThat(vehicleBusiness.VehicleList(new VehicleListRequest()).totalCount).isEqualTo(276);

        VehicleListRequest moto = new VehicleListRequest();
        moto.type = "Motosiklet";
        assertThat(vehicleBusiness.VehicleList(moto).totalCount).isEqualTo(55);

        VehicleListRequest ankaraMoto = new VehicleListRequest();
        ankaraMoto.cityId = "06";
        ankaraMoto.type = "Motosiklet";
        assertThat(vehicleBusiness.VehicleList(ankaraMoto).totalCount).isEqualTo(7);

        assertThat(vehicleBusiness.VehicleTypeList(new VehicleTypeListRequest()).types).hasSize(2);
    }

    @Test
    @Order(9)
    void gorev_listesi_ve_limit_filtresi() {
        TaskListRequest single = new TaskListRequest();
        single.policeId = "06-1005";
        assertThat(taskBusiness.TaskList(single).totalCount).isEqualTo(7);

        TaskListRequest overLimit = new TaskListRequest();
        overLimit.cityId = "06";
        overLimit.onlyOverLimit = true;
        var overLimitResult = taskBusiness.TaskList(overLimit);
        assertThat(overLimitResult.totalCount).isEqualTo(76);
        // limiti asan 8 personelin gorevleri listelenir
        assertThat(overLimitResult.tasks).extracting(task -> task.policeId).doesNotContainNull();
        assertThat(overLimitResult.tasks.stream().map(task -> task.policeId).distinct().count()).isEqualTo(8);
    }

    @Test
    @Order(10)
    void trend_hesaplaniyor() {
        TaskTrendRequest request = new TaskTrendRequest();
        request.cityId = "06";
        var trend = analyticsBusiness.TaskTrend(request);
        assertThat(trend.cityName).isEqualTo("Ankara");
        assertThat(trend.points).hasSize(7);
        assertThat(trend.averageTaskCount).isGreaterThan(0);
    }

    /* ---------------- yazma islemleri ---------------- */

    @Test
    @Order(11)
    void gorev_atama_veritabanina_yaziliyor() {
        TaskAssignRequest request = new TaskAssignRequest();
        request.policeId = "06-1001";
        request.type = "RADAR";
        request.location = "Merkez Kavsagi";
        request.startTime = "16:00";
        request.endTime = "17:00";

        var confirm = taskBusiness.TaskAssignConfirm(request);
        assertThat(confirm.valid).isTrue();
        assertThat(confirm.typeName).isEqualTo("Radar");
        int before = confirm.currentTaskCount;

        var execute = taskBusiness.TaskAssignExecute(request);
        assertThat(execute.success).isTrue();
        assertThat(execute.newTaskCount).isEqualTo(before + 1);

        // sayac personel kaydina da yansidi mi
        assertThat(policeBusiness.PoliceDetail(new PoliceDetailRequest("06-1001")).dailyTaskCount)
                .isEqualTo(before + 1);

        // silince geri donuyor mu
        TaskDeleteRequest delete = new TaskDeleteRequest();
        delete.taskId = execute.taskId;
        assertThat(taskBusiness.TaskDelete(delete).success).isTrue();
        assertThat(policeBusiness.PoliceDetail(new PoliceDetailRequest("06-1001")).dailyTaskCount).isEqualTo(before);
    }

    @Test
    @Order(12)
    void personel_ekleme_guncelleme_silme() {
        PoliceSaveRequest save = new PoliceSaveRequest();
        save.badgeNumber = "069999";
        save.fullName = "Test Personel";
        save.age = 30;
        save.rank = "Komiser";
        save.score = 88;
        save.cityId = "06";
        save.unitId = "06-B1";
        save.status = "SAHADA";
        save.taskType = "RADAR";

        var saved = policeBusiness.PoliceSave(save);
        assertThat(saved.success).isTrue();

        var detail = policeBusiness.PoliceDetail(new PoliceDetailRequest(saved.id));
        assertThat(detail.found).isTrue();
        assertThat(detail.fullName).isEqualTo("Test Personel");

        // guncelleme
        save.id = saved.id;
        save.fullName = "Test Personel Guncel";
        assertThat(policeBusiness.PoliceSave(save).success).isTrue();
        assertThat(policeBusiness.PoliceDetail(new PoliceDetailRequest(saved.id)).fullName)
                .isEqualTo("Test Personel Guncel");

        // hatali birim reddedilmeli
        PoliceSaveRequest invalid = new PoliceSaveRequest();
        invalid.fullName = "Hatali";
        invalid.badgeNumber = "060001";
        invalid.cityId = "06";
        invalid.unitId = "34-B1";
        assertThat(policeBusiness.PoliceSave(invalid).success).isFalse();

        // silme
        PoliceDeleteRequest delete = new PoliceDeleteRequest();
        delete.policeId = saved.id;
        assertThat(policeBusiness.PoliceDelete(delete).success).isTrue();
        assertThat(policeBusiness.PoliceDetail(new PoliceDetailRequest(saved.id)).found).isFalse();
    }

    @Test
    @Order(13)
    void sehir_ve_birim_crud_kisitlari() {
        RegionSaveRequest city = new RegionSaveRequest();
        city.id = "99";
        city.name = "Test Sehir";
        city.plateCode = "99";
        city.x = 50.0;
        city.y = 30.0;
        assertThat(regionBusiness.RegionSave(city).success).isTrue();

        UnitSaveRequest unit = new UnitSaveRequest();
        unit.name = "Test Birim";
        unit.cityId = "99";
        var savedUnit = unitBusiness.UnitSave(unit);
        assertThat(savedUnit.success).isTrue();

        // birimi olan sehir silinemez
        RegionDeleteRequest deleteCity = new RegionDeleteRequest();
        deleteCity.id = "99";
        assertThat(regionBusiness.RegionDelete(deleteCity).success).isFalse();

        // personeli olan birim silinemez
        UnitDeleteRequest deleteUnitWithPolice = new UnitDeleteRequest();
        deleteUnitWithPolice.id = "06-B1";
        assertThat(unitBusiness.UnitDelete(deleteUnitWithPolice).success).isFalse();

        // once birim, sonra sehir silinir
        UnitDeleteRequest deleteUnit = new UnitDeleteRequest();
        deleteUnit.id = savedUnit.id;
        assertThat(unitBusiness.UnitDelete(deleteUnit).success).isTrue();
        assertThat(regionBusiness.RegionDelete(deleteCity).success).isTrue();
    }

    @Test
    @Order(14)
    void rapor_olusturuluyor_ve_listeleniyor() {
        ReportEntryRequest request = new ReportEntryRequest();
        request.reportName = "Test Raporu";
        request.reportType = "RADAR";
        request.cityId = "06";
        request.startDate = "2026-08-01";
        request.endDate = "2026-08-31";

        var confirm = reportEntryBusiness.Confirm(request);
        assertThat(confirm.valid).isTrue();
        assertThat(confirm.reportTypeName).isEqualTo("Radar Raporu");
        assertThat(confirm.policeCount).isEqualTo(36);

        var execute = reportEntryBusiness.Execute(request);
        assertThat(execute.success).isTrue();

        var list = reportEntryBusiness.ReportList(new ReportListRequest());
        assertThat(list.totalCount).isEqualTo(1);
        assertThat(list.reports.get(0).reportNo).isEqualTo(execute.reportNo);
    }

    @Test
    @Order(15)
    void ayarlar_veritabaninda_saklaniyor() {
        SettingSaveRequest save = new SettingSaveRequest();
        save.token = "TOKEN-TEST";
        save.language = "en";
        save.pageSize = 50;
        save.defaultCityId = "06";
        save.refreshSeconds = 30;

        assertThat(settingBusiness.SettingSave(save).success).isTrue();

        var loaded = settingBusiness.SettingGet(new SettingGetRequest("TOKEN-TEST"));
        assertThat(loaded.language).isEqualTo("en");
        assertThat(loaded.pageSize).isEqualTo(50);

        // kaydi olmayan token varsayilanlari alir
        assertThat(settingBusiness.SettingGet(new SettingGetRequest("TOKEN-YOK")).language).isEqualTo("tr");
    }
}
