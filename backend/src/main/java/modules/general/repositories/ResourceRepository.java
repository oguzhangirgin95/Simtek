package modules.general.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import models.general.entity.ResourceItem;

public interface ResourceRepository extends JpaRepository<ResourceItem, Long> {

    List<ResourceItem> findByTransactionNameOrderByKeyAsc(String transactionName);
}
