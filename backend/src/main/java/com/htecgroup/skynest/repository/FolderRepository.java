package com.htecgroup.skynest.repository;

import com.htecgroup.skynest.model.entity.FolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FolderRepository extends JpaRepository<FolderEntity, UUID> {
  Optional<FolderEntity> findFolderByName(String name);
}
