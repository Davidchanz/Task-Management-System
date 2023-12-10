package com.effectivemobile.TaskManagementSystem.service;

import com.effectivemobile.TaskManagementSystem.dto.StatusDto;
import com.effectivemobile.TaskManagementSystem.dto.input.task.TaskInputDto;
import com.effectivemobile.TaskManagementSystem.exception.AccessToResourceDeniedException;
import com.effectivemobile.TaskManagementSystem.exception.exist.StatusAlreadyExistException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.StatusNotFoundException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.TaskNotFoundException;
import com.effectivemobile.TaskManagementSystem.model.Status;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.repository.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("TEST")
public class StatusServiceTest {

    @MockBean
    StatusService statusService;

    @MockBean
    StatusRepository statusRepository;

    @BeforeEach
    void setUp(){
        statusService.statusRepository = statusRepository;
    }

    @Test
    void StatusWithIdNotFound_ExceptionThrow(){
        doCallRealMethod().when(statusService).findStatusById(any(Long.class));
        doReturn(Optional.empty()).when(statusRepository).findById(any(Long.class));

        Exception exception = assertThrows(StatusNotFoundException.class, () -> statusService.findStatusById(1L));
        String expectedMessage = "Status with id [" + 1 + "] not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void StatusWithNameNotFound_ExceptionThrow(){
        String statusTestName = "TestStatusName";
        doCallRealMethod().when(statusService).findStatusByName(any(String.class));
        doReturn(Optional.empty()).when(statusRepository).findByName(any(String.class));

        Exception exception = assertThrows(StatusNotFoundException.class, () -> statusService.findStatusByName(statusTestName));
        String expectedMessage = "Status with name [" + statusTestName + "] not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void StatusWithNameAlreadyExist_ExceptionThrow(){
        StatusDto statusDto = new StatusDto();
        statusDto.setName("TestStatusName");

        doCallRealMethod().when(statusService).addNewStatus(any(StatusDto.class));
        doReturn(Optional.empty()).when(statusRepository).findByName(any(String.class));
        when(statusRepository.findByName(any(String.class))).thenReturn(Optional.of(new Status()));

        Exception exception = assertThrows(StatusAlreadyExistException.class, () -> statusService.addNewStatus(statusDto));
        String expectedMessage = "Status with name [" + statusDto.getName() + "] already exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
