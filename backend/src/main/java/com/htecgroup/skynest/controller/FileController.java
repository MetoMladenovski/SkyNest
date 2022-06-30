package com.htecgroup.skynest.controller;

import com.htecgroup.skynest.model.response.ErrorMessage;
import com.htecgroup.skynest.model.response.FileDownloadResponse;
import com.htecgroup.skynest.model.response.FileResponse;
import com.htecgroup.skynest.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

  @Operation(summary = "Upload a new file to a bucket")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully uploaded a new file",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = FileResponse.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"id\": \"1d132ffe-51c0-43fb-aaed-290d4501b8dd\",\n"
                                + "    \"createdOn\": null,\n"
                                + "    \"modifiedOn\": null,\n"
                                + "    \"deletedOn\": null,\n"
                                + "    \"name\": \"change user role.sql\",\n"
                                + "    \"createdById\": \"67898b3b-4d5f-4a51-95e2-3808b4dfc903\",\n"
                                + "    \"parentFolderId\": null,\n"
                                + "    \"bucketId\": \"1ebdec68-f6d7-11ec-8822-0242ac160002\",\n"
                                + "    \"type\": \"application/x-sql\",\n"
                                + "    \"size\": \"499\"\n"
                                + "}")
                  })
            }),
        @ApiResponse(
            responseCode = "400",
            description = "File IO error/Failed to write file contents",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"Failed to write file contents\"],"
                                + " \"status\": \"400\","
                                + " \"timestamp\": \"2022-06-07 16:18:12\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "401",
            description = "Invalid session token",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"Invalid session token\"],"
                                + " \"status\": \"401\","
                                + " \"timestamp\": \"2022-06-07 16:18:12\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "403",
            description = "Access denied",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"User does not have access to bucket\"],"
                                + " \"status\": \"403\","
                                + " \"timestamp\": \"2022-06-07 16:18:12\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Bucket/User not found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        name = "bucket",
                        value =
                            "{\"messages\":[\"Bucket not found\"],"
                                + " \"status\": \"404\","
                                + " \"timestamp\": \"2022-06-03 16:18:12\"}"),
                    @ExampleObject(
                        name = "user",
                        value =
                            "{\"messages\":[\"User not found\"],"
                                + " \"status\": \"404\","
                                + " \"timestamp\": \"2022-06-03 16:18:12\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "409",
            description = "File name is not unique in folder",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"File with the same name already exists in folder\"],"
                                + " \"status\": \"409\","
                                + " \"timestamp\": \"2022-06-03 16:18:12\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = String.class),
                  examples = {@ExampleObject(value = "Internal Server Error")})
            })
      })
  @PostMapping(path = "/bucket/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<FileResponse> uploadFileToBucket(
      @PathVariable(name = "uuid") UUID bucketId, @RequestPart("file") MultipartFile file) {

    FileResponse fileResponse = fileService.uploadFile(file, bucketId);

    ResponseEntity<FileResponse> responseEntity = new ResponseEntity<>(fileResponse, HttpStatus.OK);
    return responseEntity;
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<Resource> downloadFile(@PathVariable(name = "uuid") UUID fileId) {

    FileDownloadResponse fileDownloadResponse = fileService.downloadFile(fileId);

    ResponseEntity<Resource> responseEntity =
        ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(fileDownloadResponse.getType()))
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileDownloadResponse.getName() + "\"")
            .body(fileDownloadResponse.getFileContent());

    return responseEntity;
  }

  @GetMapping("/{uuid}/metadata")
  public ResponseEntity<FileResponse> getFileDetails(@PathVariable(name = "uuid") UUID fileId) {

    FileResponse fileResponse = fileService.getFileMetadata(fileId);
    ResponseEntity<FileResponse> responseEntity = new ResponseEntity<>(fileResponse, HttpStatus.OK);

    return responseEntity;
  }
}
