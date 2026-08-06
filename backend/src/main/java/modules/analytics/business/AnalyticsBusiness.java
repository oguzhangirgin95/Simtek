package modules.analytics.business;

import models.analytics.request.TaskTrendRequest;
import models.analytics.response.TaskTrendResponse;
import models.analytics.response.TrendPoint;
import models.operations.entity.Task;
import models.personnel.entity.Police;
import models.regions.entity.City;
import modules.operations.business.TaskBusiness;
import modules.personnel.business.PoliceBusiness;
import modules.regions.business.RegionBusiness;

public class AnalyticsBusiness {

    private static final String[] DAY_LABELS = { "Pzt", "Sal", "Car", "Per", "Cum", "Cmt", "Paz" };

    /** gunlere gore dalgalanma orani (%), bugun son gun kabul edilir */
    private static final int[] DAY_FACTORS = { 92, 96, 100, 98, 104, 78, 70 };

    /**
     * Gunluk gorev trendi. Sistemde tarihli gorev kaydi tutulmadigi icin
     * bugunun gorev sayisi gun katsayilariyla gecmise dogru olceklenir.
     */
    public TaskTrendResponse TaskTrend(TaskTrendRequest taskTrendRequest) {

        String cityId = taskTrendRequest == null || taskTrendRequest.cityId == null ? "" : taskTrendRequest.cityId;
        String unitId = taskTrendRequest == null || taskTrendRequest.unitId == null ? "" : taskTrendRequest.unitId;

        int dayCount = taskTrendRequest == null || taskTrendRequest.dayCount == null || taskTrendRequest.dayCount < 1
                ? 7
                : Math.min(taskTrendRequest.dayCount, DAY_LABELS.length);

        TaskTrendResponse taskTrendResponse = new TaskTrendResponse();

        City city = cityId.isEmpty() ? null : RegionBusiness.GetCity(cityId);
        taskTrendResponse.cityName = city == null ? "Ulke geneli" : city.name;

        int todayTaskCount = 0;
        for (Task task : TaskBusiness.GetTasks()) {
            if (!cityId.isEmpty() && !task.cityId.equals(cityId)) {
                continue;
            }
            if (!unitId.isEmpty() && !task.unitId.equals(unitId)) {
                continue;
            }
            todayTaskCount++;
        }

        int activePolice = 0;
        for (Police police : PoliceBusiness.GetPoliceList()) {
            if (!cityId.isEmpty() && !police.cityId.equals(cityId)) {
                continue;
            }
            if (!unitId.isEmpty() && !police.unitId.equals(unitId)) {
                continue;
            }
            if ("SAHADA".equals(police.status)) {
                activePolice++;
            }
        }

        for (int i = dayCount - 1; i >= 0; i--) {

            int factorIndex = (DAY_LABELS.length - 1 - i) % DAY_FACTORS.length;
            int factor = DAY_FACTORS[factorIndex];

            TrendPoint point = new TrendPoint(
                    DAY_LABELS[factorIndex],
                    (todayTaskCount * factor) / 100,
                    (activePolice * factor) / 100);

            taskTrendResponse.points.add(point);
            taskTrendResponse.totalTaskCount += point.taskCount;
        }

        taskTrendResponse.averageTaskCount = taskTrendResponse.points.isEmpty()
                ? 0
                : taskTrendResponse.totalTaskCount / taskTrendResponse.points.size();

        return taskTrendResponse;
    }
}
