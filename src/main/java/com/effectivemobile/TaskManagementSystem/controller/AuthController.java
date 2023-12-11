package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.dto.output.response.ApiResponseSingleOk;
import com.effectivemobile.TaskManagementSystem.dto.input.auth.LoginDto;
import com.effectivemobile.TaskManagementSystem.dto.output.auth.TokenDto;
import com.effectivemobile.TaskManagementSystem.dto.input.user.UserAuthDto;
import com.effectivemobile.TaskManagementSystem.exception.RequiredRequestParamIsMissingException;
import com.effectivemobile.TaskManagementSystem.exception.auth.UserLoginException;
import com.effectivemobile.TaskManagementSystem.model.CustomUserDetails;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.service.AuthService;
import com.effectivemobile.TaskManagementSystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Register New User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register New User",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseSingleOk.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If RequestBody is missing"),
            @ApiResponse(responseCode = "409", description = "If User with specify username or email already exist")
    })
    @PostMapping("/registration")
    public ResponseEntity<ApiResponseSingleOk> register(@Valid @RequestBody(required = false) UserAuthDto userAuthDto){
        if(userAuthDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param UserAuthDto is missing");

        User user = new User();
        user.setUsername(userAuthDto.getUsername());
        user.setPassword(userAuthDto.getPassword());
        user.setEmail(userAuthDto.getEmail());
        userService.registerNewUserAccount(user);
        return new ResponseEntity<>(new ApiResponseSingleOk(
                "Registration", "User '" + user.getUsername() + "' successfully created!"), HttpStatus.OK);
    }

    @Operation(summary = "LogIn")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LogIn",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenDto.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If RequestBody is missing"),
            @ApiResponse(responseCode = "401", description = "If username/password is wrong")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody(required = false) LoginDto loginRequest){
        if(loginRequest == null)
            throw new RequiredRequestParamIsMissingException("Required request param LoginDto is missing");

        Authentication authentication = authService.authenticateUser(loginRequest)
                .orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        //System.out.println("Logged in User returned [API]: " + customUserDetails.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = authService.generateToken(customUserDetails);
        TokenDto token = new TokenDto();
        token.setToken(jwtToken);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
