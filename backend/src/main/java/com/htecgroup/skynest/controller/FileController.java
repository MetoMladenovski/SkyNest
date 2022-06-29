package com.htecgroup.skynest.controller;

import com.htecgroup.skynest.model.response.FileResponse;
import com.htecgroup.skynest.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
@Log4j2
@Tag(name = "File API", description = "File operations")
public class FileController {

  private final FileService fileService;

  @PostMapping("/bucket/{uuid}")
  public ResponseEntity<FileResponse> uploadFileToBucket(
      @PathVariable(name = "uuid") UUID bucketId, @RequestParam("file") MultipartFile file) {

    FileResponse fileResponse = fileService.uploadFile(file, bucketId);

    ResponseEntity<FileResponse> responseEntity = new ResponseEntity<>(fileResponse, HttpStatus.OK);
    return responseEntity;
  }

  //  @GetMapping("/{uuid}")
  //  public ResponseEntity<Resource> download(@PathVariable(name = "uuid") UUID fileId){
  //
  //    return new InputStreamResource();
  //  }

  @GetMapping("/{uuid}/metadata")
  public ResponseEntity<FileResponse> getFileDetails(@PathVariable(name = "uuid") UUID fileId) {

    FileResponse fileResponse = new FileResponse();
    ResponseEntity<FileResponse> responseEntity = new ResponseEntity<>(fileResponse, HttpStatus.OK);

    return responseEntity;
  }
}
