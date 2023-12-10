package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.dto.output.response.ApiResponse;
import com.effectivemobile.TaskManagementSystem.dto.output.response.ApiResponseSingleOk;
import com.effectivemobile.TaskManagementSystem.dto.input.user.UserAuthDto;
import com.effectivemobile.TaskManagementSystem.dto.output.user.UserDto;
import com.effectivemobile.TaskManagementSystem.exception.RequiredRequestParamIsMissingException;
import com.effectivemobile.TaskManagementSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser(Principal principal){
        return new ResponseEntity<>(new UserDto(userService.findUserByUserName(principal.getName())), HttpStatus.OK);
    }

    @PutMapping("/user/update")
    public ResponseEntity<ApiResponse> updateUser(Principal principal, @Valid @RequestBody(required = false) UserAuthDto userAuthDto){
        if(userAuthDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param UserAuthDto is missing");

        userService.updateUser(principal.getName(), userAuthDto);
        return new ResponseEntity<>(new ApiResponseSingleOk("Update User", "User [" + userAuthDto.getUsername() + "] updated!"), HttpStatus.OK);
    }
}
