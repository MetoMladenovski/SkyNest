package com.htecgroup.skynest.service.impl;

import com.htecgroup.skynest.annotation.ParentFolderIsInTheSameBucket;
import com.htecgroup.skynest.exception.buckets.BucketNotFoundException;
import com.htecgroup.skynest.exception.folder.FolderNotFoundException;
import com.htecgroup.skynest.model.dto.LoggedUserDto;
import com.htecgroup.skynest.model.entity.BucketEntity;
import com.htecgroup.skynest.model.entity.FolderEntity;
import com.htecgroup.skynest.model.entity.UserEntity;
import com.htecgroup.skynest.model.request.FolderCreateRequest;
import com.htecgroup.skynest.model.response.FileResponse;
import com.htecgroup.skynest.model.response.FolderResponse;
import com.htecgroup.skynest.model.response.StorageContentResponse;
import com.htecgroup.skynest.repository.BucketRepository;
import com.htecgroup.skynest.repository.FolderRepository;
import com.htecgroup.skynest.repository.UserRepository;
import com.htecgroup.skynest.service.CurrentUserService;
import com.htecgroup.skynest.service.FileService;
import com.htecgroup.skynest.service.FolderService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Validated
public class FolderServiceImpl implements FolderService {

  private ModelMapper modelMapper;
  private FolderRepository folderRepository;
  private CurrentUserService currentUserService;
  private FileService fileService;

  private UserRepository userRepository;

  private BucketRepository bucketRepository;

  @Override
  public FolderResponse createFolder(
      @Valid @ParentFolderIsInTheSameBucket FolderCreateRequest folderCreateRequest) {
    FolderEntity folderEntity = modelMapper.map(folderCreateRequest, FolderEntity.class);

    LoggedUserDto loggedUserDto = currentUserService.getLoggedUser();
    folderEntity =
        createFolder(
            folderEntity,
            loggedUserDto.getUuid(),
            folderCreateRequest.getBucketId(),
            folderCreateRequest.getParentFolderId());
    folderEntity = folderRepository.save(folderEntity);
    return modelMapper.map(folderEntity, FolderResponse.class);
  }

  private FolderEntity createFolder(
      FolderEntity folderEntity,
      UUID currentUserId,
      UUID bucketEntityId,
      UUID parentFolderEntityId) {
    UserEntity currentUser = userRepository.getById(currentUserId);
    BucketEntity bucketEntity =
        bucketRepository.findById(bucketEntityId).orElseThrow(BucketNotFoundException::new);
    bucketEntity = modelMapper.map(bucketEntity, BucketEntity.class);
    FolderEntity parentFolderEntity = folderRepository.findFolderById(parentFolderEntityId);

    folderEntity.setParentFolder(parentFolderEntity);
    folderEntity.setBucket(bucketEntity);
    folderEntity.setCreatedBy(currentUser);

    return folderEntity;
  }

  @Override
  public FolderResponse getFolderDetails(UUID uuid) {
    FolderEntity folderEntity =
        folderRepository.findById(uuid).orElseThrow(FolderNotFoundException::new);
    FolderResponse folderResponse = modelMapper.map(folderEntity, FolderResponse.class);
    return folderResponse;
  }

  @Override
  public List<FolderResponse> getAllRootFolders(UUID bucketId) {
    List<FolderEntity> allFolders =
        folderRepository.findAllByBucketIdAndParentFolderIsNull(bucketId);
    return asFolderResponseList(allFolders);
  }

  @Override
  public List<FolderResponse> getAllFoldersWithParent(UUID parentFolderId) {
    List<FolderEntity> allFolders = folderRepository.findAllByParentFolderId(parentFolderId);
    return asFolderResponseList(allFolders);
  }

  @Override
  public StorageContentResponse getFolderContent(UUID folderId) {
    List<FolderResponse> allFoldersResponse = getAllFoldersWithParent(folderId);
    List<FileResponse> allFilesResponse = fileService.getAllFilesWithParent(folderId);
    StorageContentResponse storageContentResponse =
        new StorageContentResponse(allFoldersResponse, allFilesResponse);
    return storageContentResponse;
  }

  private List<FolderResponse> asFolderResponseList(List<FolderEntity> allFolders) {
    return allFolders.stream()
        .map(folder -> modelMapper.map(folder, FolderResponse.class))
        .collect(Collectors.toList());
  }
}
