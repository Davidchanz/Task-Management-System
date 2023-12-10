package com.effectivemobile.TaskManagementSystem.service;

import com.effectivemobile.TaskManagementSystem.dto.input.task.TaskInputDto;
import com.effectivemobile.TaskManagementSystem.dto.input.user.UserAuthDto;
import com.effectivemobile.TaskManagementSystem.exception.AccessToResourceDeniedException;
import com.effectivemobile.TaskManagementSystem.exception.exist.EmailAlreadyExistException;
import com.effectivemobile.TaskManagementSystem.exception.exist.UserAlreadyExistException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.TaskNotFoundException;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.repository.TaskRepository;
import com.effectivemobile.TaskManagementSystem.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("TEST")
public class UserServiceTest {

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void setUp(){
        userService.userRepository = userRepository;
    }

    @Test
    void UserWithUserNameNotFound_ExceptionThrow(){
        doReturn(Optional.empty()).when(userRepository).findByUsername(any(String.class));
        doCallRealMethod().when(userService).findUserByUserName(any(String.class));

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> userService.findUserByUserName("username"));
        String expectedMessage = "Could not found a user with given name";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void Register_UserNameAlreadyExist_ExceptionThrow(){
        User user = new User();
        user.setUsername("TestUsername");

        doCallRealMethod().when(userService).registerNewUserAccount(any(User.class));
        when(userService.userExists(any(String.class))).thenReturn(true);

        Exception exception = assertThrows(UserAlreadyExistException.class, () -> userService.registerNewUserAccount(user));
        String expectedMessage = "User with username: '" + user.getUsername() + "' is already exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void Register_EmailAlreadyExist_ExceptionThrow(){
        User user = new User();
        user.setEmail("testEmail@email.com");

        doCallRealMethod().when(userService).registerNewUserAccount(any(User.class));
        when(userService.userExists(any(String.class))).thenReturn(false);
        when(userService.emailExists(any(String.class))).thenReturn(true);

        Exception exception = assertThrows(EmailAlreadyExistException.class, () -> userService.registerNewUserAccount(user));
        String expectedMessage = "User with email: '" + user.getEmail() + "' is already exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
