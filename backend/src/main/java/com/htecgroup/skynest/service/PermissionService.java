package com.htecgroup.skynest.service;

import com.htecgroup.skynest.model.entity.AccessType;
import com.htecgroup.skynest.model.entity.FileMetadataEntity;
import com.htecgroup.skynest.model.entity.FolderEntity;
import com.htecgroup.skynest.model.entity.ObjectEntity;
import com.htecgroup.skynest.model.request.PermissionEditRequest;
import com.htecgroup.skynest.model.request.PermissionGrantRequest;
import com.htecgroup.skynest.model.response.PermissionResponse;

import java.util.List;
import java.util.UUID;

public interface PermissionService {

  PermissionResponse grantPermissionForBucket(PermissionGrantRequest permissionGrantRequest);

  void grantOwnerForObject(ObjectEntity object);

  void currentUserHasPermissionForBucket(UUID bucketId, AccessType minimumAccessType);

  void currentUserHasPermissionForFolder(FolderEntity folder, AccessType minimumAccessType);

  void currentUserHasPermissionForFile(FileMetadataEntity file, AccessType minimumAccessType);

  List<PermissionResponse> getAllBucketPermission(UUID bucketId);

  PermissionResponse editPermissionForBucket(
      PermissionEditRequest permissionEditRequest, UUID bucketId);

  void revokePermissionForBucket(UUID bucketId, String userEmail);

  void revokeFilePermission(UUID fileId, String email);

  List<PermissionResponse> getAllFilePermissions(UUID fileId);

  List<PermissionResponse> getAllFolderPermission(UUID folderId);

  PermissionResponse grantPermissionForFile(PermissionGrantRequest permissionGrantRequest);

  PermissionResponse editPermissionForFile(
      PermissionEditRequest permissionEditRequest, UUID fileId);
}
