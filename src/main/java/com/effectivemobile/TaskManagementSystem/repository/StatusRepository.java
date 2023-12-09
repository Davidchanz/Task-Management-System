package com.effectivemobile.TaskManagementSystem.repository;

import com.effectivemobile.TaskManagementSystem.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
