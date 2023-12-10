package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.dto.PriorityDto;
import com.effectivemobile.TaskManagementSystem.dto.StatusDto;
import com.effectivemobile.TaskManagementSystem.dto.output.response.ApiResponseSingleOk;
import com.effectivemobile.TaskManagementSystem.exception.RequiredRequestParamIsMissingException;
import com.effectivemobile.TaskManagementSystem.service.PriorityService;
import com.effectivemobile.TaskManagementSystem.service.StatusService;
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

    @GetMapping("/status/{id}")
    public ResponseEntity<StatusDto> getStatus(@Valid @PathVariable Long id){
        return new ResponseEntity<>(new StatusDto(statusService.findStatusById(id)), HttpStatus.OK);
    }

    @PostMapping("/status/add")
    public ResponseEntity<ApiResponseSingleOk> addNewStatus(@Valid @RequestBody(required = false) StatusDto statusDto){
        if(statusDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param StatusDto is missing");

        statusDto.setName(statusDto.getName());
        statusService.addNewStatus(statusDto);
        return new ResponseEntity<>(new ApiResponseSingleOk("Create Status", "New Status " + statusDto + " was created!"), HttpStatus.OK);
    }

    @GetMapping("/priority/{id}")
    public ResponseEntity<PriorityDto> getPriority(@Valid @PathVariable Long id){
        return new ResponseEntity<>(new PriorityDto(priorityService.findPriorityById(id)), HttpStatus.OK);
    }

    @PostMapping("/priority/add")
    public ResponseEntity<ApiResponseSingleOk> addNewPriority(@Valid @RequestBody(required = false) PriorityDto priorityDto){
        if(priorityDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param PriorityDto is missing");

        priorityDto.setName(priorityDto.getName());
        priorityService.addNewPriority(priorityDto);
        return new ResponseEntity<>(new ApiResponseSingleOk("Create Priority", "New Priority " + priorityDto + " was created!"), HttpStatus.OK);
    }
}
