package com.htecgroup.skynest.controller;

import com.htecgroup.skynest.model.request.CompanyAddRequest;
import com.htecgroup.skynest.model.request.CompanyEditRequest;
import com.htecgroup.skynest.model.response.CompanyResponse;
import com.htecgroup.skynest.model.response.ErrorMessage;
import com.htecgroup.skynest.service.CompanyService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.htecgroup.skynest.util.UrlUtil.COMPANY_CONTROLLER_URL;

@RestController
@RequestMapping(COMPANY_CONTROLLER_URL)
@AllArgsConstructor
@Log4j2
@Tag(name = "Company API", description = "Company-related operations")
public class CompanyController {

  private CompanyService companyService;

  @Operation(summary = "Add a new company")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully added a new company",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = CompanyResponse.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"id\": \"a6fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + "  \"pib\": \"123456789\","
                                + "  \"name\": \"Name\","
                                + "  \"address\": \"Local address\","
                                + "  \"phoneNumber\": \"38166575757\","
                                + "  \"email\": \"username@gmail.com\","
                                + "  \"tierName\": \"basic\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "400",
            description = "Input fields are not in valid format",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"pib format not valid\","
                                + " \"name cannot be null or empty\","
                                + " \"address cannot be null or empty\","
                                + " \"email format is not valid\","
                                + " \"phoneNumber format not valid\"],"
                                + " \"status\": \"400\","
                                + " \"timestamp\": \"2022-06-03 16:18:12\"}")
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
            description = "Tier doesn't exist",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"Tier doesn't exist\"],"
                                + " \"status\": \"409\","
                                + " \"timestamp\": \"2022-06-03 16:18:12\"}")
                  })
            }),
        @ApiResponse(
            responseCode = "409",
            description = "Pib, email or phone number already in use",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"This email is already in use\"],"
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
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content =
          @Content(
              schema = @Schema(implementation = CompanyAddRequest.class),
              mediaType = "application/json",
              examples = {
                @ExampleObject(
                    value =
                        "{\"email\": \"username@gmail.com\","
                            + " \"pib\": \"123456789\","
                            + "  \"name\": \"Name\","
                            + "  \"tierName\": \"basic\","
                            + "  \"phoneNumber\": \"38166575757\","
                            + "  \"address\": \"Local address\"}")
              }))
  @PreAuthorize("hasAuthority(T(com.htecgroup.skynest.model.entity.RoleEntity).ROLE_ADMIN)")
  @PostMapping
  public ResponseEntity<CompanyResponse> addCompany(
      @Valid @RequestBody CompanyAddRequest companyAddRequest) {
    ResponseEntity<CompanyResponse> responseEntity =
        new ResponseEntity<>(companyService.addCompany(companyAddRequest), HttpStatus.OK);

    log.info("Company {} was successfully created", companyAddRequest.getName());
    return responseEntity;
  }

  @Operation(summary = "Edit current user's company")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully edited a company",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = CompanyResponse.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"id\": \"a6fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + "  \"pib\": \"123456789\","
                                + "  \"name\": \"Name\","
                                + "  \"address\": \"Local address\","
                                + "  \"phoneNumber\": \"38166575757\","
                                + "  \"email\": \"username@gmail.com\","
                                + "  \"tierName\": \"basic\"}")
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
            description = "User is not a part of any company",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"User is not a part of any company\"],"
                                + " \"status\": \"404\","
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
  @PreAuthorize("hasAuthority(T(com.htecgroup.skynest.model.entity.RoleEntity).ROLE_ADMIN)")
  @PutMapping
  public ResponseEntity<CompanyResponse> editCompany(
      @Valid @RequestBody CompanyEditRequest companyEditRequest) {
    CompanyResponse companyResponse = companyService.editCompany(companyEditRequest);
    log.info("Company {} was successfully edited", companyResponse.getName());
    return ResponseEntity.ok(companyResponse);
  }

  @Operation(summary = "Get current user's company")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully received company info",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = CompanyResponse.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"id\": \"a6fd6d95-0a60-43ff-961f-2b9b2ff72f95\","
                                + "  \"pib\": \"123456789\","
                                + "  \"name\": \"Name\","
                                + "  \"address\": \"Local address\","
                                + "  \"phoneNumber\": \"38166575757\","
                                + "  \"email\": \"username@gmail.com\","
                                + "  \"tierName\": \"basic\"}")
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
            responseCode = "404",
            description = "User is not a part of any company",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorMessage.class),
                  examples = {
                    @ExampleObject(
                        value =
                            "{\"messages\":[\"User is not a part of any company\"],"
                                + " \"status\": \"404\","
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
  @GetMapping
  public ResponseEntity<CompanyResponse> getMyCompany() {
    CompanyResponse companyResponse = companyService.getMyCompany();
    return ResponseEntity.ok(companyResponse);
  }
}
