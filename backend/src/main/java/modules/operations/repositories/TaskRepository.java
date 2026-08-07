package modules.operations.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import models.operations.entity.Task;

public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task> {

    long countByPoliceId(String policeId);

    void deleteByPoliceId(String policeId);

    @Query(value = """
            SELECT t.task_type AS "taskType", count(*) AS "typeCount"
            FROM task t
            WHERE (CAST(:cityId AS VARCHAR) IS NULL OR t.city_id = :cityId)
            GROUP BY t.task_type
            ORDER BY t.task_type
            """, nativeQuery = true)
    List<TaskTypeCountProjection> typeCounts(@Param("cityId") String cityId);

    @Query(value = """
            SELECT count(*)
            FROM task t
            WHERE (CAST(:cityId AS VARCHAR) IS NULL OR t.city_id = :cityId)
              AND (CAST(:unitId AS VARCHAR) IS NULL OR t.unit_id = :unitId)
            """, nativeQuery = true)
    long countByFilter(@Param("cityId") String cityId, @Param("unitId") String unitId);
}
