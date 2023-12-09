package com.effectivemobile.TaskManagementSystem.mapper;

import com.effectivemobile.TaskManagementSystem.dto.TaskUpdateDto;
import com.effectivemobile.TaskManagementSystem.model.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "executor", ignore = true)
    void updateTaskFromDto(TaskUpdateDto dto, @MappingTarget Task entity);
}
