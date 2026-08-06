package modules.monitoring.business;

import java.util.ArrayList;
import java.util.List;

import models.monitoring.request.DashboardRequest;
import models.monitoring.response.CityStatistic;
import models.monitoring.response.MapStatisticsResponse;
import models.monitoring.response.StatusCount;
import models.monitoring.response.SummaryResponse;
import models.monitoring.response.UnitWorkloadItem;
import models.monitoring.response.UnitWorkloadResponse;
import models.personnel.entity.Police;
import models.regions.entity.City;
import models.units.entity.Unit;
import modules.personnel.business.PoliceBusiness;
import modules.regions.business.RegionBusiness;
import modules.units.business.UnitBusiness;

public class DashboardBusiness {

    /** Harita icin sehir bazli polis / aktif memur istatistikleri */
    public MapStatisticsResponse MapStatistics(DashboardRequest dashboardRequest) {

        String unitId = dashboardRequest == null || dashboardRequest.unitId == null ? "" : dashboardRequest.unitId;
        String status = dashboardRequest == null || dashboardRequest.status == null ? "" : dashboardRequest.status;

        MapStatisticsResponse mapStatisticsResponse = new MapStatisticsResponse();

        for (City city : RegionBusiness.GetCities()) {

            CityStatistic cityStatistic = new CityStatistic();
            cityStatistic.cityId = city.id;
            cityStatistic.cityName = city.name;
            cityStatistic.plateCode = city.plateCode;
            cityStatistic.x = city.x;
            cityStatistic.y = city.y;
            cityStatistic.totalPolice = 0;
            cityStatistic.activePolice = 0;
            cityStatistic.unitCount = UnitBusiness.GetUnitsByCity(city.id).size();

            for (Police police : PoliceBusiness.GetPoliceList()) {

                if (!police.cityId.equals(city.id)) {
                    continue;
                }
                if (!unitId.isEmpty() && !police.unitId.equals(unitId)) {
                    continue;
                }
                if (!status.isEmpty() && !police.status.equals(status)) {
                    continue;
                }

                cityStatistic.totalPolice++;
                if ("SAHADA".equals(police.status)) {
                    cityStatistic.activePolice++;
                }
            }

            cityStatistic.activePercent = cityStatistic.totalPolice == 0
                    ? 0
                    : (cityStatistic.activePolice * 100) / cityStatistic.totalPolice;

            mapStatisticsResponse.cities.add(cityStatistic);
            mapStatisticsResponse.totalPolice += cityStatistic.totalPolice;
            mapStatisticsResponse.activePolice += cityStatistic.activePolice;

            if (cityStatistic.activePolice > mapStatisticsResponse.busiestCityActivePolice) {
                mapStatisticsResponse.busiestCityActivePolice = cityStatistic.activePolice;
                mapStatisticsResponse.busiestCityName = cityStatistic.cityName;
            }
        }

        return mapStatisticsResponse;
    }

    /** First-sight bilgiler: sahada, merkezde, izinde, raporlu, limit asan */
    public SummaryResponse Summary(DashboardRequest dashboardRequest) {

        String cityId = dashboardRequest == null || dashboardRequest.cityId == null ? "" : dashboardRequest.cityId;
        String unitId = dashboardRequest == null || dashboardRequest.unitId == null ? "" : dashboardRequest.unitId;
        String status = dashboardRequest == null || dashboardRequest.status == null ? "" : dashboardRequest.status;

        SummaryResponse summaryResponse = new SummaryResponse();

        for (Police police : PoliceBusiness.GetPoliceList()) {

            if (!cityId.isEmpty() && !police.cityId.equals(cityId)) {
                continue;
            }
            if (!unitId.isEmpty() && !police.unitId.equals(unitId)) {
                continue;
            }
            if (!status.isEmpty() && !police.status.equals(status)) {
                continue;
            }

            summaryResponse.totalPolice++;

            switch (police.status) {
                case "SAHADA":
                    summaryResponse.onDuty++;
                    break;
                case "MERKEZDE":
                    summaryResponse.atStation++;
                    break;
                case "IZINDE":
                    summaryResponse.onLeave++;
                    break;
                case "RAPORLU":
                    summaryResponse.onReport++;
                    break;
                default:
                    break;
            }

            if (PoliceBusiness.IsOverDailyLimit(police)) {
                summaryResponse.overDailyLimit++;
            }
        }

        summaryResponse.unitCount = cityId.isEmpty()
                ? UnitBusiness.GetUnits().size()
                : UnitBusiness.GetUnitsByCity(cityId).size();

        summaryResponse.statusDistribution.add(new StatusCount("SAHADA", "Sahada", summaryResponse.onDuty));
        summaryResponse.statusDistribution.add(new StatusCount("MERKEZDE", "Merkezde", summaryResponse.atStation));
        summaryResponse.statusDistribution.add(new StatusCount("IZINDE", "Izinde", summaryResponse.onLeave));
        summaryResponse.statusDistribution.add(new StatusCount("RAPORLU", "Raporlu", summaryResponse.onReport));

        return summaryResponse;
    }

    /** Grafik icin birim bazli gorev yogunlugu, cityId verilmezse Ankara doner */
    public UnitWorkloadResponse UnitWorkload(DashboardRequest dashboardRequest) {

        String cityId = dashboardRequest == null || dashboardRequest.cityId == null || dashboardRequest.cityId.isEmpty()
                ? "06"
                : dashboardRequest.cityId;

        String status = dashboardRequest == null || dashboardRequest.status == null ? "" : dashboardRequest.status;

        UnitWorkloadResponse unitWorkloadResponse = new UnitWorkloadResponse();
        unitWorkloadResponse.cityId = cityId;

        City city = RegionBusiness.GetCity(cityId);
        unitWorkloadResponse.cityName = city == null ? "" : city.name;

        List<UnitWorkloadItem> items = new ArrayList<>();
        int enYuksekYogunluk = 0;

        for (Unit unit : UnitBusiness.GetUnitsByCity(cityId)) {

            UnitWorkloadItem item = new UnitWorkloadItem();
            item.unitId = unit.id;
            item.unitName = unit.name;
            item.totalPolice = 0;
            item.activePolice = 0;
            item.taskLoad = 0;
            item.patrol = 0;
            item.radar = 0;
            item.motorcycle = 0;
            item.schoolCrossing = 0;
            item.accidentInvestigation = 0;

            for (Police police : PoliceBusiness.GetPoliceList()) {

                if (!police.unitId.equals(unit.id)) {
                    continue;
                }
                if (!status.isEmpty() && !police.status.equals(status)) {
                    continue;
                }

                item.totalPolice++;
                item.taskLoad += police.dailyTaskCount;

                if ("SAHADA".equals(police.status)) {
                    item.activePolice++;
                }

                switch (police.taskType) {
                    case "DEVRIYE":
                        item.patrol++;
                        break;
                    case "RADAR":
                        item.radar++;
                        break;
                    case "MOTOSIKLET":
                        item.motorcycle++;
                        break;
                    case "OKUL_GECIDI":
                        item.schoolCrossing++;
                        break;
                    case "KAZA_INCELEME":
                        item.accidentInvestigation++;
                        break;
                    default:
                        break;
                }
            }

            if (item.taskLoad > enYuksekYogunluk) {
                enYuksekYogunluk = item.taskLoad;
            }

            unitWorkloadResponse.totalTaskLoad += item.taskLoad;
            items.add(item);
        }

        for (UnitWorkloadItem item : items) {
            item.loadPercent = enYuksekYogunluk == 0 ? 0 : (item.taskLoad * 100) / enYuksekYogunluk;
        }

        unitWorkloadResponse.units = items;

        return unitWorkloadResponse;
    }
}
