package modules.units.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import models.units.entity.Unit;

public interface UnitRepository extends JpaRepository<Unit, String> {

    List<Unit> findByCityIdOrderBySeqAsc(String cityId);

    List<Unit> findAllByOrderByCityIdAscSeqAsc();

    long countByCityId(String cityId);
}
