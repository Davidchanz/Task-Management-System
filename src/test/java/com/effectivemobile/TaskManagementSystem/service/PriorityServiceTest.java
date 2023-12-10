package com.effectivemobile.TaskManagementSystem.service;

import com.effectivemobile.TaskManagementSystem.dto.PriorityDto;
import com.effectivemobile.TaskManagementSystem.dto.StatusDto;
import com.effectivemobile.TaskManagementSystem.exception.exist.PriorityAlreadyExistException;
import com.effectivemobile.TaskManagementSystem.exception.exist.StatusAlreadyExistException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.PriorityNotFoundException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.StatusNotFoundException;
import com.effectivemobile.TaskManagementSystem.model.Priority;
import com.effectivemobile.TaskManagementSystem.model.Status;
import com.effectivemobile.TaskManagementSystem.repository.PriorityRepository;
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
public class PriorityServiceTest {

    @MockBean
    PriorityService priorityService;

    @MockBean
    PriorityRepository priorityRepository;

    @BeforeEach
    void setUp(){
        priorityService.priorityRepository = priorityRepository;
    }

    @Test
    void PriorityWithIdNotFound_ExceptionThrow(){
        doCallRealMethod().when(priorityService).findPriorityById(any(Long.class));
        doReturn(Optional.empty()).when(priorityRepository).findById(any(Long.class));

        Exception exception = assertThrows(PriorityNotFoundException.class, () -> priorityService.findPriorityById(1L));
        String expectedMessage = "Priority with id [" + 1 + "] not found!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void PriorityWithNameAlreadyExist_ExceptionThrow(){
        PriorityDto priorityDto = new PriorityDto();
        priorityDto.setName("TestPriorityName");

        doCallRealMethod().when(priorityService).addNewPriority(any(PriorityDto.class));
        doReturn(Optional.empty()).when(priorityRepository).findByName(any(String.class));
        when(priorityRepository.findByName(any(String.class))).thenReturn(Optional.of(new Priority()));

        Exception exception = assertThrows(PriorityAlreadyExistException.class, () -> priorityService.addNewPriority(priorityDto));
        String expectedMessage = "Priority with name [" + priorityDto.getName() + "] already exist!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
