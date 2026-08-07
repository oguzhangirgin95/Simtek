package modules.menu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import models.menu.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByActiveTrueOrderBySortOrderAsc();
}
