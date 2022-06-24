package com.htecgroup.skynest.controller;

import com.htecgroup.skynest.model.response.BucketResponse;
import com.htecgroup.skynest.model.response.ErrorMessage;
import com.htecgroup.skynest.model.response.UserResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/buckets")
@AllArgsConstructor
@Log4j2
@Tag(name = "Bucket API", description = "Bucket-related operations")
public class BucketsController {

  private BucketService bucketService;

  @Operation(summary = "Get bucket with given uuid")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Bucket returned",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"id\": \"a6fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + " \"email\": \"username@gmail.com\","
                                + "  \"name\": \"Name\","
                                + "  \"surname\": \"Surname\","
                                + "  \"phoneNumber\": \"38166575757\","
                                + "  \"address\": \"Local address\","
                                + "  \"roleName\": \"role_worker\","
                                + "  \"enabled\": \"false\","
                                + "  \"verified\": \"true\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "403",
            description = "Unauthorized request",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"Access denied\"],"
                                + " \"status\": \"403\","
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
                            "{\"messages\":[\"User not found\"],"
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
  @PreAuthorize(
      "hasAuthority(T(com.htecgroup.skynest.model.entity.RoleEntity).ROLE_ADMIN) or hasAuthority(T(com.htecgroup.skynest.model.entity.RoleEntity).ROLE_WORKER)")
  @GetMapping("/{uuid}")
  public ResponseEntity<BucketResponse> getBucket(@PathVariable UUID uuid) {
    BucketResponse bucketResponse = bucketService.getBucket(uuid);
    ResponseEntity<BucketResponse> bucketResponseEntity =
        new ResponseEntity<>(bucketResponse, HttpStatus.OK);
    return bucketResponseEntity;
  }
}
