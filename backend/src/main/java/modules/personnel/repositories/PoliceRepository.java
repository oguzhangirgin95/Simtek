package modules.personnel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import models.personnel.entity.Police;

public interface PoliceRepository extends JpaRepository<Police, String>, JpaSpecificationExecutor<Police> {

    long countByUnitId(String unitId);

    long countByCityId(String cityId);

    @Query(value = """
            SELECT count(*)                                                          AS "totalPolice",
                   count(*) FILTER (WHERE p.status = 'SAHADA')                       AS "onDuty",
                   count(*) FILTER (WHERE p.status = 'MERKEZDE')                     AS "atStation",
                   count(*) FILTER (WHERE p.status = 'IZINDE')                       AS "onLeave",
                   count(*) FILTER (WHERE p.status = 'RAPORLU')                      AS "onReport",
                   count(*) FILTER (WHERE p.daily_task_count > p.daily_task_limit)   AS "overDailyLimit"
            FROM police p
            WHERE (CAST(:cityId AS VARCHAR) IS NULL OR p.city_id = :cityId)
              AND (CAST(:unitId AS VARCHAR) IS NULL OR p.unit_id = :unitId)
              AND (CAST(:status AS VARCHAR) IS NULL OR p.status  = :status)
            """, nativeQuery = true)
    SummaryProjection summary(@Param("cityId") String cityId,
                              @Param("unitId") String unitId,
                              @Param("status") String status);

    @Query(value = """
            SELECT c.id                                              AS "cityId",
                   c.name                                            AS "cityName",
                   c.plate_code                                      AS "plateCode",
                   c.map_x                                           AS "x",
                   c.map_y                                           AS "y",
                   count(p.id)                                       AS "totalPolice",
                   count(p.id) FILTER (WHERE p.status = 'SAHADA')    AS "activePolice",
                   (SELECT count(*) FROM unit u WHERE u.city_id = c.id) AS "unitCount"
            FROM city c
            LEFT JOIN police p
                   ON p.city_id = c.id
                  AND (CAST(:unitId AS VARCHAR) IS NULL OR p.unit_id = :unitId)
                  AND (CAST(:status AS VARCHAR) IS NULL OR p.status  = :status)
            GROUP BY c.id, c.name, c.plate_code, c.map_x, c.map_y, c.sort_order
            ORDER BY c.sort_order
            """, nativeQuery = true)
    List<CityStatisticProjection> cityStatistics(@Param("unitId") String unitId,
                                                 @Param("status") String status);

    @Query(value = """
            SELECT u.id                                                        AS "unitId",
                   u.name                                                      AS "unitName",
                   count(p.id)                                                 AS "totalPolice",
                   count(p.id) FILTER (WHERE p.status = 'SAHADA')              AS "activePolice",
                   coalesce(sum(p.daily_task_count), 0)                        AS "taskLoad",
                   count(p.id) FILTER (WHERE p.task_type = 'DEVRIYE')          AS "patrol",
                   count(p.id) FILTER (WHERE p.task_type = 'RADAR')            AS "radar",
                   count(p.id) FILTER (WHERE p.task_type = 'MOTOSIKLET')       AS "motorcycle",
                   count(p.id) FILTER (WHERE p.task_type = 'OKUL_GECIDI')      AS "schoolCrossing",
                   count(p.id) FILTER (WHERE p.task_type = 'KAZA_INCELEME')    AS "accidentInvestigation"
            FROM unit u
            LEFT JOIN police p
                   ON p.unit_id = u.id
                  AND (CAST(:status AS VARCHAR) IS NULL OR p.status = :status)
            WHERE u.city_id = :cityId
            GROUP BY u.id, u.name, u.seq
            ORDER BY u.seq
            """, nativeQuery = true)
    List<UnitWorkloadProjection> unitWorkload(@Param("cityId") String cityId,
                                              @Param("status") String status);
}
