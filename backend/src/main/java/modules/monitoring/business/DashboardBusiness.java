package modules.monitoring.business;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import models.monitoring.request.DashboardRequest;
import models.monitoring.response.CityStatistic;
import models.monitoring.response.MapStatisticsResponse;
import models.monitoring.response.StatusCount;
import models.monitoring.response.SummaryResponse;
import models.monitoring.response.UnitWorkloadItem;
import models.monitoring.response.UnitWorkloadResponse;
import models.regions.entity.City;
import modules.personnel.repositories.CityStatisticProjection;
import modules.personnel.repositories.PoliceRepository;
import modules.personnel.repositories.SummaryProjection;
import modules.personnel.repositories.UnitWorkloadProjection;
import modules.regions.repositories.CityRepository;
import modules.units.repositories.UnitRepository;

@Service
public class DashboardBusiness {

    private static final String DEFAULT_CITY_ID = "06";

    private final PoliceRepository policeRepository;
    private final CityRepository cityRepository;
    private final UnitRepository unitRepository;

    public DashboardBusiness(PoliceRepository policeRepository, CityRepository cityRepository,
            UnitRepository unitRepository) {
        this.policeRepository = policeRepository;
        this.cityRepository = cityRepository;
        this.unitRepository = unitRepository;
    }

    @Transactional(readOnly = true)
    public MapStatisticsResponse MapStatistics(DashboardRequest dashboardRequest) {

        String unitId = Filter(dashboardRequest == null ? null : dashboardRequest.unitId);
        String status = Filter(dashboardRequest == null ? null : dashboardRequest.status);

        MapStatisticsResponse response = new MapStatisticsResponse();

        for (CityStatisticProjection projection : policeRepository.cityStatistics(unitId, status)) {

            CityStatistic cityStatistic = new CityStatistic();
            cityStatistic.cityId = projection.getCityId();
            cityStatistic.cityName = projection.getCityName();
            cityStatistic.plateCode = projection.getPlateCode();
            cityStatistic.x = projection.getX();
            cityStatistic.y = projection.getY();
            cityStatistic.totalPolice = projection.getTotalPolice().intValue();
            cityStatistic.activePolice = projection.getActivePolice().intValue();
            cityStatistic.overLimitPolice = projection.getOverLimitPolice().intValue();
            cityStatistic.unitCount = projection.getUnitCount().intValue();
            cityStatistic.activePercent = cityStatistic.totalPolice == 0
                    ? 0
                    : (cityStatistic.activePolice * 100) / cityStatistic.totalPolice;

            response.cities.add(cityStatistic);
            response.totalPolice += cityStatistic.totalPolice;
            response.activePolice += cityStatistic.activePolice;

            if (cityStatistic.activePolice > response.busiestCityActivePolice) {
                response.busiestCityActivePolice = cityStatistic.activePolice;
                response.busiestCityName = cityStatistic.cityName;
            }
        }

        return response;
    }

    @Transactional(readOnly = true)
    public SummaryResponse Summary(DashboardRequest dashboardRequest) {

        String cityId = Filter(dashboardRequest == null ? null : dashboardRequest.cityId);
        String unitId = Filter(dashboardRequest == null ? null : dashboardRequest.unitId);
        String status = Filter(dashboardRequest == null ? null : dashboardRequest.status);

        SummaryProjection projection = policeRepository.summary(cityId, unitId, status);

        SummaryResponse response = new SummaryResponse();
        response.totalPolice = projection.getTotalPolice().intValue();
        response.onDuty = projection.getOnDuty().intValue();
        response.atStation = projection.getAtStation().intValue();
        response.onLeave = projection.getOnLeave().intValue();
        response.onReport = projection.getOnReport().intValue();
        response.overDailyLimit = projection.getOverDailyLimit().intValue();
        response.unitCount = cityId == null
                ? (int) unitRepository.count()
                : (int) unitRepository.countByCityId(cityId);

        response.statusDistribution.add(new StatusCount("SAHADA", "Sahada", response.onDuty));
        response.statusDistribution.add(new StatusCount("MERKEZDE", "Merkezde", response.atStation));
        response.statusDistribution.add(new StatusCount("IZINDE", "Izinde", response.onLeave));
        response.statusDistribution.add(new StatusCount("RAPORLU", "Raporlu", response.onReport));

        return response;
    }

    @Transactional(readOnly = true)
    public UnitWorkloadResponse UnitWorkload(DashboardRequest dashboardRequest) {

        String cityId = Filter(dashboardRequest == null ? null : dashboardRequest.cityId);
        if (cityId == null) {
            cityId = DEFAULT_CITY_ID;
        }
        String status = Filter(dashboardRequest == null ? null : dashboardRequest.status);

        UnitWorkloadResponse response = new UnitWorkloadResponse();
        response.cityId = cityId;

        City city = cityRepository.findById(cityId).orElse(null);
        response.cityName = city == null ? "" : city.name;

        List<UnitWorkloadProjection> projections = policeRepository.unitWorkload(cityId, status);

        int highestLoad = 0;
        for (UnitWorkloadProjection projection : projections) {
            highestLoad = Math.max(highestLoad, projection.getTaskLoad().intValue());
        }

        for (UnitWorkloadProjection projection : projections) {

            UnitWorkloadItem item = new UnitWorkloadItem();
            item.unitId = projection.getUnitId();
            item.unitName = projection.getUnitName();
            item.totalPolice = projection.getTotalPolice().intValue();
            item.activePolice = projection.getActivePolice().intValue();
            item.taskLoad = projection.getTaskLoad().intValue();
            item.patrol = projection.getPatrol().intValue();
            item.radar = projection.getRadar().intValue();
            item.motorcycle = projection.getMotorcycle().intValue();
            item.schoolCrossing = projection.getSchoolCrossing().intValue();
            item.accidentInvestigation = projection.getAccidentInvestigation().intValue();
            item.loadPercent = highestLoad == 0 ? 0 : (item.taskLoad * 100) / highestLoad;

            response.units.add(item);
            response.totalTaskLoad += item.taskLoad;
        }

        return response;
    }

    private static String Filter(String value) {
        return value == null || value.trim().isEmpty() ? null : value;
    }
}
