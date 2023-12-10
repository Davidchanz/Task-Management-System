package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.advice.ExceptionHandlerAdvice;
import com.effectivemobile.TaskManagementSystem.config.SecurityConfig;
import com.effectivemobile.TaskManagementSystem.config.TestConfig;
import com.effectivemobile.TaskManagementSystem.model.*;
import com.effectivemobile.TaskManagementSystem.security.JwtAuthenticationEntryPoint;
import com.effectivemobile.TaskManagementSystem.security.JwtTokenProvider;
import com.effectivemobile.TaskManagementSystem.service.CustomUserDetailsService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.security.Principal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Import({ SecurityConfig.class, TestConfig.class })
public abstract class AbstractTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    public MockMvc mvc;

    @MockBean
    public CustomUserDetailsService customUserDetailsService;

    @MockBean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public JwtTokenProvider jwtTokenProvider;

    public String token;

    public HttpHeaders headers;

    @Mock
    public Principal principal;

    public User user;

    public User anotherUser;

    public Task task;

    public Status status;

    public Priority priority;

    public Comment comment;

    public CustomUserDetails customUserDetails = getCustomUserDetails();

    {
        user = getUser();
        anotherUser = getAnotherUser();
        status = getStatus();
        priority = getPriority();
        task = getTask();
        comment = getComment();
    }

    public void setUp(Object controller){
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new ExceptionHandlerAdvice()).build();

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        token = jwtTokenProvider.generateToken(customUserDetails);
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
    }

    public CustomUserDetails getCustomUserDetails(){
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setEmail("admin@email.com");
        customUserDetails.setPassword("password");
        customUserDetails.setUsername("admin");
        customUserDetails.setId(1L);
        return customUserDetails;
    }

    public User getUser(){
        User user = new User();
        user.setId(1L);
        user.setPassword("password");
        user.setEmail("email");
        user.setUsername("TestUser");
        return user;
    }

    public User getAnotherUser(){
        User user = new User();
        user.setId(2L);
        user.setPassword("password");
        user.setEmail("gmail");
        user.setUsername("anotherTestUser");
        return user;
    }

    public Status getStatus(){
        Status status = new Status();
        status.setId(1L);
        status.setName("Open");
        return status;
    }

    public Priority getPriority(){
        Priority priority = new Priority();
        priority.setId(1L);
        priority.setName("Low");
        return priority;
    }

    public Comment getComment(){
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");
        comment.setTask(task);
        comment.setAuthor(user);
        comment.setCreatedOn(Instant.now());
        comment.setLastUpdatedOn(Instant.now());
        return comment;
    }

    public Task getTask(){
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Task For Test");
        task.setAuthor(user);
        task.setExecutor(anotherUser);
        task.setStatus(status);
        task.setPriority(priority);
        task.setCreatedOn(Instant.now());
        task.setLastUpdatedOn(Instant.now());
        return task;
    }
}
