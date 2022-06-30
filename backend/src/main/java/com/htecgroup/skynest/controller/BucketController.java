package com.htecgroup.skynest.controller;

import com.htecgroup.skynest.model.request.BucketCreateRequest;
import com.htecgroup.skynest.model.response.BucketResponse;
import com.htecgroup.skynest.model.response.ErrorMessage;
import com.htecgroup.skynest.service.BucketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/buckets")
@AllArgsConstructor
@Log4j2
@Tag(name = "Bucket API", description = "Bucket-related operations")
public class BucketController {

  private BucketService bucketService;

  @Operation(summary = "Create new bucket")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bucket successfully created",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = BucketResponse.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"createdById\": \"a6fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + "  \"name\": \"Name\","
                                + "  \"companyId\": \"a6fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + "  \"description\": \"Description\","
                                + "  \"size\": \"1000\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"name cannot be null or empty\","
                                + " \"description cannot be null or empty\"],"
                                + " \"status\": \"400\","
                                + " \"timestamp\": \"2022-06-07 16:18:12\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized request",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"Access denied\"],"
                                + " \"status\": \"401\","
                                + " \"timestamp\": \"2022-06-07 16:18:12\"}")
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
  @PostMapping
  public ResponseEntity<BucketResponse> createBucket(
      @Valid @RequestBody BucketCreateRequest bucketCreateRequest) {

    ResponseEntity<BucketResponse> bucketResponseEntity =
        new ResponseEntity<>(bucketService.createBucket(bucketCreateRequest), HttpStatus.OK);

    log.info("Bucket {} was successfully created", bucketCreateRequest.getName());
    return bucketResponseEntity;
  }

  @Operation(summary = "Get bucket with given uuid")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bucket returned",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = BucketResponse.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"createdById\": \"a6fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + "  \"name\": \"Name\","
                                + "  \"companyId\": \"a6fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + "  \"description\": \"Description\","
                                + "  \"size\": \"1000\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized request",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"Access denied\"],"
                                + " \"status\": \"401\","
                                + " \"timestamp\": \"2022-06-07 16:18:12\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "404",
            description = "Bucket not found",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"Bucket not found\"],"
                                + " \"status\": \"404\","
                                + " \"timestamp\": \"2022-06-07 16:18:12\"}")
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
  @GetMapping("/{uuid}")
  public ResponseEntity<BucketResponse> getBucket(@PathVariable UUID uuid) {
    BucketResponse bucketResponse = bucketService.getBucket(uuid);
    ResponseEntity<BucketResponse> bucketResponseEntity =
        new ResponseEntity<>(bucketResponse, HttpStatus.OK);
    return bucketResponseEntity;
  }

  @Operation(summary = "Get all buckets")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Buckets returned",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = BucketResponse.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "[{\"id\": \"a6fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + "\"name\": \"Name\","
                                + "\"companyId\": \"a6fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + "\"description\": \"Description\","
                                + "\"size\": \"1000\"}},"
                                + "{\"id\": \"u7yd987h-0a79-42dd-961s-7sfh564kdv2s\","
                                + "\"name\": \"Name2\","
                                + "\"companyId\": \"h5fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + "\"description\": \"Description2\","
                                + "\"size\": \"1200\"}]")
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
            responseCode = "500",
            description = "Internal Server Error",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = String.class),
                  examples = {@ExampleObject(value = "Internal Server Error")})
            })
      })
  @GetMapping
  public List<BucketResponse> getBuckets() {
    List<BucketResponse> listOfBuckets = bucketService.listAllBuckets();
    return listOfBuckets;
  }
}
