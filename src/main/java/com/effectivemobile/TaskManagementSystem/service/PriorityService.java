package com.effectivemobile.TaskManagementSystem.service;

import com.effectivemobile.TaskManagementSystem.dto.PriorityDto;
import com.effectivemobile.TaskManagementSystem.exception.exist.PriorityAlreadyExistException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.PriorityNotFoundException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.StatusNotFoundException;
import com.effectivemobile.TaskManagementSystem.model.Priority;
import com.effectivemobile.TaskManagementSystem.repository.PriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriorityService {

    @Autowired
    public PriorityRepository priorityRepository;

    public Priority findPriorityById(Long id) {
        return priorityRepository.findById(id).orElseThrow(() -> new PriorityNotFoundException("Priority with id [" + id + "] not found!"));
    }

    public void addNewPriority(PriorityDto priorityDto) {
        if(priorityRepository.findByName(priorityDto.getName()).isPresent())
            throw new PriorityAlreadyExistException("Priority with name [" + priorityDto.getName() + "] already exist!");

        priorityRepository.save(new Priority(priorityDto));
    }
}
