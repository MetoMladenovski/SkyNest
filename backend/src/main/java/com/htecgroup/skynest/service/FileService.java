package com.htecgroup.skynest.service;

import com.htecgroup.skynest.model.response.FileDownloadResponse;
import com.htecgroup.skynest.model.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService {

  FileResponse uploadFile(MultipartFile multipartFile, UUID bucketId);

  FileResponse getFileMetadata(UUID fileId);

  FileDownloadResponse downloadFile(UUID fileId);

  List<FileResponse> getAllRootFiles(UUID bucketId);

  List<FileResponse> getAllFilesWithParent(UUID parentFolderId);

  FileResponse updateFileContent(MultipartFile multipartFile, UUID fileId);
}
