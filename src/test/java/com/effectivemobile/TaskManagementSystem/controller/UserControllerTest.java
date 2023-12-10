package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.dto.input.user.UserAuthDto;
import com.effectivemobile.TaskManagementSystem.dto.output.user.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.effectivemobile.TaskManagementSystem.config.SecurityConfig;
import com.effectivemobile.TaskManagementSystem.config.TestConfig;
import com.effectivemobile.TaskManagementSystem.exception.RequiredRequestParamIsMissingException;
import com.effectivemobile.TaskManagementSystem.mapper.UserMapper;
import com.effectivemobile.TaskManagementSystem.mapper.UserMapperImpl;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.repository.UserRepository;
import com.effectivemobile.TaskManagementSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ UserController.class })
@Import({ SecurityConfig.class, TestConfig.class })
class UserControllerTest extends AbstractTest{

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @InjectMocks
    UserMapper userMapper = new UserMapperImpl();

    @InjectMocks
    UserController userController;

    @BeforeEach
    public void setUp() {
        super.setUp(userController);
        userService.userRepository = userRepository;
        userService.userMapper = userMapper;
    }
    @Test
    void GetUser_Success() throws Exception {
        when(principal.getName()).thenReturn(user.getUsername());
        when(userService.findUserByUserName(user.getUsername())).thenReturn(user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(new UserDto(user)).replaceAll("[\\t\\s]", "");

        var result = this.mvc.perform(get("/api/user")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(requestJson, result.getResponse().getContentAsString());
    }

    @Test
    void UpdateUser_Success() throws Exception {
        when(principal.getName()).thenReturn(user.getUsername());
        doCallRealMethod().when(userService).updateUser(any(String.class), any(UserAuthDto.class));
        doCallRealMethod().when(userService).findUserByUserName(any(String.class));
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any(String.class));
        doReturn(user).when(userRepository).save(any(User.class));

        UserAuthDto userAuthDto = new UserAuthDto();
        userAuthDto.setUsername("boris!");
        userAuthDto.setEmail("admin@email.com");
        userAuthDto.setMatchingPassword("password");
        userAuthDto.setPassword("password");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(userAuthDto);

        var result = this.mvc.perform(put("/api/user/update")
                        .contentType(APPLICATION_JSON_UTF8).content(requestJson)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString("User [" + user.getUsername() + "] updated!"));
        assertEquals(userAuthDto.getUsername(), user.getUsername());
    }
}