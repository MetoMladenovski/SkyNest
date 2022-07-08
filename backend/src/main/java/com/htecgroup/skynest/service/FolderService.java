package com.htecgroup.skynest.service;

import com.htecgroup.skynest.annotation.ParentFolderIsInTheSameBucket;
import com.htecgroup.skynest.model.request.FolderCreateRequest;
import com.htecgroup.skynest.model.response.FolderResponse;
import com.htecgroup.skynest.model.response.StorageContentResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface FolderService {
  FolderResponse createFolder(
      @Valid @ParentFolderIsInTheSameBucket FolderCreateRequest folderCreateRequest);

  FolderResponse getFolderDetails(UUID uuid);

  List<FolderResponse> getAllRootFolders(UUID bucketId);

  List<FolderResponse> getAllFoldersWithParent(UUID parentFolderId);

  StorageContentResponse getFolderContent(UUID folderId);
}
