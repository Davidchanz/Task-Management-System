package com.effectivemobile.TaskManagementSystem.repository;

import com.effectivemobile.TaskManagementSystem.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority, Long> {
    Optional<Priority> findByName(String name);
}
