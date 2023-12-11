package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.config.SecurityConfig;
import com.effectivemobile.TaskManagementSystem.config.TestConfig;
import com.effectivemobile.TaskManagementSystem.dto.PriorityDto;
import com.effectivemobile.TaskManagementSystem.dto.StatusDto;
import com.effectivemobile.TaskManagementSystem.dto.input.task.TaskInputDto;
import com.effectivemobile.TaskManagementSystem.dto.output.task.TaskDto;
import com.effectivemobile.TaskManagementSystem.service.PriorityService;
import com.effectivemobile.TaskManagementSystem.service.StatusService;
import com.effectivemobile.TaskManagementSystem.service.TaskService;
import com.effectivemobile.TaskManagementSystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ AdminController.class })
@Import({ SecurityConfig.class, TestConfig.class })
public class AdminControllerTest extends AbstractTest{

    @MockBean
    StatusService statusService;

    @MockBean
    PriorityService priorityService;

    @InjectMocks
    AdminController adminController;

    @BeforeEach
    public void setUp() {
        super.setUp(adminController);
    }

    @Test
    void GetStatus_Success() throws Exception{
        doReturn(status).when(statusService).findStatusById(any(Long.class));

        StatusDto statusDto = new StatusDto(status);

        var result = this.mvc.perform(get("/api/admin/status/{id}", 1)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString(statusDto.getName()));
    }

    @Test
    void AddNewStatus_Success() throws Exception{
        doNothing().when(statusService).addNewStatus(any(StatusDto.class));

        StatusDto statusDto = new StatusDto(status);
        statusDto.setName("TestStatusName");

        String requestJson = ow.writeValueAsString(statusDto);

        var result = this.mvc.perform(post("/api/admin/status/add")
                        .contentType(APPLICATION_JSON_UTF8).content(requestJson)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString("New Status " + statusDto.getName() + " was created!"));
    }

    @Test
    void GetPriority_Success() throws Exception{
        doReturn(priority).when(priorityService).findPriorityById(any(Long.class));

        PriorityDto priorityDto = new PriorityDto(priority);

        var result = this.mvc.perform(get("/api/admin/priority/{id}", 1)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString(priority.getName()));
    }

    @Test
    void AddNewPriority_Success() throws Exception{
        doNothing().when(priorityService).addNewPriority(any(PriorityDto.class));

        PriorityDto priorityDto = new PriorityDto(priority);
        priorityDto.setName("TestPriorityName");

        String requestJson = ow.writeValueAsString(priorityDto);

        var result = this.mvc.perform(post("/api/admin/priority/add")
                        .contentType(APPLICATION_JSON_UTF8).content(requestJson)
                        .headers(headers)
                        .principal(principal))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), containsString("New Priority " + priorityDto.getName() + " was created!"));
    }
}
