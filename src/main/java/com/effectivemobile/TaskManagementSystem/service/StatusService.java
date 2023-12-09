package com.effectivemobile.TaskManagementSystem.service;

import com.effectivemobile.TaskManagementSystem.exception.notFound.StatusNotFoundException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.TaskNotFoundException;
import com.effectivemobile.TaskManagementSystem.model.Status;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    @Autowired
    public StatusRepository statusRepository;

    public Status findStatusById(Long id) {
        return statusRepository.findById(id).orElseThrow(() -> new StatusNotFoundException("Status with id [" + id + "] not found!"));
    }
}
