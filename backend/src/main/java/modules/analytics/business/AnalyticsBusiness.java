package modules.analytics.business;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import models.analytics.request.TaskTrendRequest;
import models.analytics.response.TaskTrendResponse;
import models.analytics.response.TrendPoint;
import models.personnel.entity.Police;
import models.regions.entity.City;
import modules.operations.repositories.TaskRepository;
import modules.personnel.repositories.PoliceRepository;
import modules.regions.repositories.CityRepository;

@Service
public class AnalyticsBusiness {

    private static final String[] DAY_LABELS = { "Pzt", "Sal", "Car", "Per", "Cum", "Cmt", "Paz" };

    /** gunlere gore dalgalanma orani (%), bugun son gun kabul edilir */
    private static final int[] DAY_FACTORS = { 92, 96, 100, 98, 104, 78, 70 };

    private final TaskRepository taskRepository;
    private final PoliceRepository policeRepository;
    private final CityRepository cityRepository;

    public AnalyticsBusiness(TaskRepository taskRepository, PoliceRepository policeRepository,
            CityRepository cityRepository) {
        this.taskRepository = taskRepository;
        this.policeRepository = policeRepository;
        this.cityRepository = cityRepository;
    }

    /**
     * Gunluk gorev trendi. Veritabaninda tarihli gorev kaydi tutulmadigi icin
     * bugunun gorev sayisi gun katsayilariyla gecmise dogru olceklenir.
     */
    @Transactional(readOnly = true)
    public TaskTrendResponse TaskTrend(TaskTrendRequest taskTrendRequest) {

        String cityId = Filter(taskTrendRequest == null ? null : taskTrendRequest.cityId);
        String unitId = Filter(taskTrendRequest == null ? null : taskTrendRequest.unitId);

        int dayCount = taskTrendRequest == null || taskTrendRequest.dayCount == null || taskTrendRequest.dayCount < 1
                ? DAY_LABELS.length
                : Math.min(taskTrendRequest.dayCount, DAY_LABELS.length);

        TaskTrendResponse response = new TaskTrendResponse();

        City city = cityId == null ? null : cityRepository.findById(cityId).orElse(null);
        response.cityName = city == null ? "Ulke geneli" : city.name;

        int todayTaskCount = (int) taskRepository.countByFilter(cityId, unitId);
        int activePolice = (int) policeRepository.count(BuildActiveSpecification(cityId, unitId));

        for (int i = dayCount - 1; i >= 0; i--) {

            int factorIndex = (DAY_LABELS.length - 1 - i) % DAY_FACTORS.length;
            int factor = DAY_FACTORS[factorIndex];

            TrendPoint point = new TrendPoint(
                    DAY_LABELS[factorIndex],
                    (todayTaskCount * factor) / 100,
                    (activePolice * factor) / 100);

            response.points.add(point);
            response.totalTaskCount += point.taskCount;
        }

        response.averageTaskCount = response.points.isEmpty()
                ? 0
                : response.totalTaskCount / response.points.size();

        return response;
    }

    private Specification<Police> BuildActiveSpecification(String cityId, String unitId) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("status"), "SAHADA"));
            if (cityId != null) {
                predicates.add(builder.equal(root.get("cityId"), cityId));
            }
            if (unitId != null) {
                predicates.add(builder.equal(root.get("unitId"), unitId));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String Filter(String value) {
        return value == null || value.trim().isEmpty() ? null : value;
    }
}
