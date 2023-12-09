package com.effectivemobile.TaskManagementSystem.repository;

import com.effectivemobile.TaskManagementSystem.model.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepository extends JpaRepository<Priority, Long> {
}
