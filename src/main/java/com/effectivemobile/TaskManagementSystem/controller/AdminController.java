package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.dto.PriorityDto;
import com.effectivemobile.TaskManagementSystem.dto.StatusDto;
import com.effectivemobile.TaskManagementSystem.dto.output.response.ApiResponseSingleOk;
import com.effectivemobile.TaskManagementSystem.exception.RequiredRequestParamIsMissingException;
import com.effectivemobile.TaskManagementSystem.service.PriorityService;
import com.effectivemobile.TaskManagementSystem.service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    public StatusService statusService;

    @Autowired
    public PriorityService priorityService;

    @Operation(summary = "Get Status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Status",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StatusDto.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If {id} is missing"),
            @ApiResponse(responseCode = "404", description = "If Status not found")
    })
    @Parameter(name = "id", description = "Status id", example = "1")
    @GetMapping("/status/{id}")
    public ResponseEntity<StatusDto> getStatus(@Valid @PathVariable Long id){
        return new ResponseEntity<>(new StatusDto(statusService.findStatusById(id)), HttpStatus.OK);
    }

    @Operation(summary = "Add New Status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add New Status",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseSingleOk.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If RequestBody is missing"),
            @ApiResponse(responseCode = "409", description = "If Status with specify name already exist")
    })
    @PostMapping("/status/add")
    public ResponseEntity<ApiResponseSingleOk> addNewStatus(@Valid @RequestBody(required = false) StatusDto statusDto){
        if(statusDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param StatusDto is missing");

        statusService.addNewStatus(statusDto);
        return new ResponseEntity<>(new ApiResponseSingleOk(
                "Create Status", "New Status " + statusDto.getName() + " was created!"), HttpStatus.OK);
    }

    @Operation(summary = "Get Priority")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Priority",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PriorityDto.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If {id} is missing"),
            @ApiResponse(responseCode = "404", description = "If Priority not found")
    })
    @Parameter(name = "id", description = "Priority id", example = "1")
    @GetMapping("/priority/{id}")
    public ResponseEntity<PriorityDto> getPriority(@Valid @PathVariable Long id){
        return new ResponseEntity<>(new PriorityDto(priorityService.findPriorityById(id)), HttpStatus.OK);
    }

    @Operation(summary = "Add New Priority")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add New Priority",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseSingleOk.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If RequestBody is missing"),
            @ApiResponse(responseCode = "409", description = "If Priority with specify name already exist")
    })
    @PostMapping("/priority/add")
    public ResponseEntity<ApiResponseSingleOk> addNewPriority(@Valid @RequestBody(required = false) PriorityDto priorityDto){
        if(priorityDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param PriorityDto is missing");

        priorityService.addNewPriority(priorityDto);
        return new ResponseEntity<>(new ApiResponseSingleOk(
                "Create Priority", "New Priority " + priorityDto.getName() + " was created!"), HttpStatus.OK);
    }
}
