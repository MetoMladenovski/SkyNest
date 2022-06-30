package com.htecgroup.skynest.repository;

import com.htecgroup.skynest.model.entity.BucketEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface BucketRepository extends CrudRepository<BucketEntity, UUID> {
  Optional<BucketEntity> findBucketByName(String name);
}
