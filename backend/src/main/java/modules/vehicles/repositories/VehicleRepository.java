package modules.vehicles.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import models.vehicles.entity.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, String>, JpaSpecificationExecutor<Vehicle> {

    Optional<Vehicle> findByPoliceId(String policeId);

    void deleteByPoliceId(String policeId);

    @Query(value = """
            SELECT v.vehicle_type AS "vehicleType", count(*) AS "typeCount"
            FROM vehicle v
            GROUP BY v.vehicle_type
            ORDER BY v.vehicle_type
            """, nativeQuery = true)
    List<VehicleTypeCountProjection> typeCounts();
}
