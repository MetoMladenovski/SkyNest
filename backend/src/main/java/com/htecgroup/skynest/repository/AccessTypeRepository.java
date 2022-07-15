package com.htecgroup.skynest.repository;

import com.htecgroup.skynest.model.entity.AccessTypeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccessTypeRepository extends CrudRepository<AccessTypeEntity, UUID> {
  Optional<AccessTypeEntity> findByName(String name);
}
