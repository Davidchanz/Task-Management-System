package com.effectivemobile.TaskManagementSystem;

import com.effectivemobile.TaskManagementSystem.config.SecurityConfig;
import com.effectivemobile.TaskManagementSystem.config.TestConfig;
import com.effectivemobile.TaskManagementSystem.controller.AbstractTest;
import com.effectivemobile.TaskManagementSystem.controller.UserController;
import com.effectivemobile.TaskManagementSystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ UserController.class })
@Import({ SecurityConfig.class, TestConfig.class })
public class MethodTest extends AbstractTest {

    @MockBean
    UserService userService;

    @InjectMocks
    UserController userController;

    @BeforeEach
    public void setUp() {
        super.setUp(userController);
    }

    @Test
    void MethodNotFound_ExceptionThrow() throws Exception{
        this.mvc.perform(get("/api/badpath")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void MethodIsNotAllowed_ExceptionThrow() throws Exception{
        this.mvc.perform(post("/api/user")
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isMethodNotAllowed())
                .andReturn();
    }
}
