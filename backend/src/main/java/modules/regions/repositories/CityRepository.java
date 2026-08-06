package modules.regions.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import models.regions.entity.City;

public interface CityRepository extends JpaRepository<City, String> {

    List<City> findAllByOrderBySortOrderAsc();

    List<City> findByNameContainingIgnoreCaseOrderBySortOrderAsc(String name);
}
