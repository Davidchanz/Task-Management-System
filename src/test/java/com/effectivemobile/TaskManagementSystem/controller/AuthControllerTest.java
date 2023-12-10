package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.dto.input.auth.LoginDto;
import com.effectivemobile.TaskManagementSystem.dto.input.user.UserAuthDto;
import com.effectivemobile.TaskManagementSystem.exception.auth.UserLoginException;
import com.effectivemobile.TaskManagementSystem.exception.exist.EmailAlreadyExistException;
import com.effectivemobile.TaskManagementSystem.exception.exist.UserAlreadyExistException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.effectivemobile.TaskManagementSystem.config.SecurityConfig;
import com.effectivemobile.TaskManagementSystem.config.TestConfig;
import com.effectivemobile.TaskManagementSystem.exception.RequiredRequestParamIsMissingException;
import com.effectivemobile.TaskManagementSystem.model.CustomUserDetails;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.service.AuthService;
import com.effectivemobile.TaskManagementSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ AuthController.class })
@Import({ SecurityConfig.class, TestConfig.class })
public class AuthControllerTest extends AbstractTest{

    @MockBean
    AuthService authService;

    @MockBean
    UserService userService;

    @MockBean
    Authentication authentication;

    @InjectMocks
    AuthController authController;

    @BeforeEach
    public void setUp() {
        super.setUp(authController);
    }

    @Test
    void Login_Success() throws Exception {
        when(authService.authenticateUser(any(LoginDto.class))).thenReturn(Optional.ofNullable(authentication));
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(authService.generateToken(any(CustomUserDetails.class))).thenReturn(jwtTokenProvider.generateToken(customUserDetails));

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("admin");
        loginDto.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(loginDto);

        MvcResult result = this.mvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void Login_CantLoginUser_ExceptionThrow() throws Exception {
        when(authService.authenticateUser(any(LoginDto.class))).thenReturn(Optional.empty());

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("admin");
        loginDto.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(loginDto);

        this.mvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserLoginException))
                .andExpect(result -> assertEquals("Couldn't login user [" + loginDto + "]", result.getResolvedException().getMessage()));
    }

    @Test
    void Register_Success() throws Exception {
        UserAuthDto userAuthDto = new UserAuthDto();
        userAuthDto.setEmail("admin@email.com");
        userAuthDto.setUsername("admin");
        userAuthDto.setMatchingPassword("password");
        userAuthDto.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userAuthDto);

        MvcResult result = this.mvc.perform(post("/api/auth/registration")
                        .contentType(APPLICATION_JSON_UTF8).content(requestJson))
                .andExpect(status().isOk())
                .andReturn();
    }

}
