package com.htecgroup.skynest.repository;

import com.htecgroup.skynest.model.entity.BucketEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BucketRepository extends CrudRepository<BucketEntity, UUID> {
  Optional<BucketEntity> findBucketByName(String name);

  @Query("SELECT SUM(b.size) FROM bucket b WHERE b.company.id = ?1")
  Long sumSizeByCompanyId(UUID companyId);

  @Query("SELECT SUM(b.size) FROM bucket b WHERE b.company IS NULL AND b.createdBy.id = ?1")
  Long sumSizeByUserId(UUID userId);

  List<BucketEntity> findAllByDeletedOnIsNotNull();
}
