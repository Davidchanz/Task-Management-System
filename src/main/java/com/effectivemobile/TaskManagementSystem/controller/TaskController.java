package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.dto.StatusDto;
import com.effectivemobile.TaskManagementSystem.dto.input.task.TaskInputDto;
import com.effectivemobile.TaskManagementSystem.dto.output.comment.CommentDto;
import com.effectivemobile.TaskManagementSystem.dto.output.response.ApiResponseSingleOk;
import com.effectivemobile.TaskManagementSystem.dto.output.task.TaskDto;
import com.effectivemobile.TaskManagementSystem.exception.AccessToResourceDeniedException;
import com.effectivemobile.TaskManagementSystem.exception.PageIllegalArgumentException;
import com.effectivemobile.TaskManagementSystem.exception.RequiredRequestParamIsMissingException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.PageNotFoundException;
import com.effectivemobile.TaskManagementSystem.model.Comment;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.service.PriorityService;
import com.effectivemobile.TaskManagementSystem.service.StatusService;
import com.effectivemobile.TaskManagementSystem.service.TaskService;
import com.effectivemobile.TaskManagementSystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    public TaskService taskService;

    @Autowired
    public UserService userService;

    @Autowired
    public StatusService statusService;

    @Autowired
    public PriorityService priorityService;

    @Value("${app.page.size}")
    private Integer pageSize;

    @Operation(summary = "Get Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Task",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If {id} is missing"),
            @ApiResponse(responseCode = "404", description = "If Task not found")
    })
    @Parameter(name = "id", description = "Task id", example = "1")
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@Valid @PathVariable Long id){
        return new ResponseEntity<>(new TaskDto(taskService.findTaskById(id)), HttpStatus.OK);
    }

    @Operation(summary = "Add New Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add New Task",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskDto.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If RequestBody is missing"),
            @ApiResponse(responseCode = "404", description = "If Status, Priority not found")
    })
    @PostMapping("/add")
    public ResponseEntity<TaskDto> addNewTask(
            @Valid @RequestBody(required = false) TaskInputDto taskInputDto,
            Principal principal){
        if(taskInputDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param TaskInputDto is missing");

        Task task = new Task(taskInputDto);
        task.setAuthor(userService.findUserByUserName(principal.getName()));
        task.setExecutor(userService.findUserByUserName(taskInputDto.getExecutor()));
        task.setStatus(statusService.findStatusById(taskInputDto.getStatus()));
        task.setPriority(priorityService.findPriorityById(taskInputDto.getPriority()));
        taskService.addNewTask(task);
        return new ResponseEntity<>(new TaskDto(task), HttpStatus.OK);
    }

    @Operation(summary = "Update Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Task",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseSingleOk.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If RequestBody, {id} is missing"),
            @ApiResponse(responseCode = "403", description = "If current User is Not Task's Author"),
            @ApiResponse(responseCode = "404", description = "If Task, Status, Priority not found")
    })
    @Parameter(name = "id", description = "Task id", example = "1")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponseSingleOk> updateTask(
            @Valid @PathVariable Long id,
            @Valid @RequestBody(required = false)
            TaskInputDto taskInputDto,
            Principal principal){
        if(taskInputDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param TaskInputDto is missing");
        User currentUser = userService.findUserByUserName(principal.getName());
        Task task = taskService.findTaskById(id);
        task.setExecutor(userService.findUserByUserName(taskInputDto.getExecutor()));
        task.setStatus(statusService.findStatusById(taskInputDto.getStatus()));
        task.setPriority(priorityService.findPriorityById(taskInputDto.getPriority()));
        taskService.updateTask(currentUser, task, taskInputDto);
        return new ResponseEntity<>(new ApiResponseSingleOk(
                "Update Task", "Task [" + taskInputDto.getTitle() + "] was updated"), HttpStatus.OK);
    }

    @Operation(summary = "Delete Task", description = "Delete Task if it is yours")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete Task",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseSingleOk.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If {id} is missing"),
            @ApiResponse(responseCode = "403", description = "If current User is Not Task's Author")
    })
    @Parameter(name = "id", description = "Task id", example = "1")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseSingleOk> deleteTask(@Valid @PathVariable Long id, Principal principal){
        User currentUser = userService.findUserByUserName(principal.getName());
        Task task = taskService.deleteTask(currentUser, id);
        return new ResponseEntity<>(new ApiResponseSingleOk(
                "Delete Task", "Task [" + task.getTitle() + "] was deleted!"), HttpStatus.OK);
    }

    @Operation(summary = "Get All Author's Tasks",
            description = "Get All Tasks with was created by User with username {username}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Author's Tasks",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskDto.class))) }
            ),
            @ApiResponse(responseCode = "400", description = "If {username} is missing, page < 0"),
            @ApiResponse(responseCode = "404", description = "If page not found")
    })
    @Parameter(name = "username", description = "Author's username", example = "admin")
    @GetMapping("/author/{username}")
    public ResponseEntity<List<TaskDto>> getTasksByAuthor(
            @Valid @PathVariable String username,
            @Valid @RequestParam(defaultValue = "0", name = "page") Integer page){
        if(page < 0)
            throw new PageIllegalArgumentException("Page index must not be less than zero");

        Pageable pageable = PageRequest.of(page, pageSize);
        User author = userService.findUserByUserName(username);
        Page<Task> tasksPage = taskService.findTasksByAuthor(author, pageable);

        if(page > tasksPage.getTotalPages()-1)
            throw new PageNotFoundException("Tasks Page number [" + page + "] not found!");

        var result = tasksPage.getContent()
                .stream()
                .map(TaskDto::new)
                .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Get All Tasks with specify Executor",
            description = "Get All Tasks which have relation with Executor with username {username}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Tasks with specify Executor",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskDto.class))) }
            ),
            @ApiResponse(responseCode = "400", description = "If {username} is missing, page < 0"),
            @ApiResponse(responseCode = "404", description = "If page not found")
    })
    @Parameter(name = "username", description = "Executor's username", example = "admin")
    @GetMapping("/executor/{username}")
    public ResponseEntity<List<TaskDto>> getTasksByExecutor(
            @Valid @PathVariable String username,
            @Valid @RequestParam(defaultValue = "0", name = "page") Integer page){

        if(page < 0)
            throw new PageIllegalArgumentException("Page index must not be less than zero");

        Pageable pageable = PageRequest.of(page, pageSize);
        User executor = userService.findUserByUserName(username);
        Page<Task> tasksPage = taskService.findTasksByExecutor(executor, pageable);

        if(page > tasksPage.getTotalPages()-1)
            throw new PageNotFoundException("Tasks Page number [" + page + "] not found!");


        var result = tasksPage.getContent()
                .stream()
                .map(TaskDto::new)
                .toList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Update Task's Status by Executor",
            description = "Update Task Status if current user is Task's Executor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Task's Status by Executor",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseSingleOk.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If RequestBody, {id} is missing"),
            @ApiResponse(responseCode = "403", description = "If current User is Not Task's Executor"),
            @ApiResponse(responseCode = "404", description = "If Task, Status not found")
    })
    @Parameter(name = "id", description = "Task id", example = "1")
    @PutMapping("/update/status/{id}")
    public ResponseEntity<ApiResponseSingleOk> updateTaskStatusByExecutor(
            @Valid @PathVariable Long id,
            @Valid @RequestBody(required = false) StatusDto statusDto,
            Principal principal){

        if(statusDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param StatusDto is missing");
        User currentUser = userService.findUserByUserName(principal.getName());
        Task task = taskService.findTaskById(id);
        if(!Objects.equals(task.getExecutor().getId(), currentUser.getId()))
            throw new AccessToResourceDeniedException("You don't have rights to update Status Task with id [" + id + "]");

        task.setStatus(statusService.findStatusByName(statusDto.getName()));
        taskService.saveTask(task);
        return new ResponseEntity<>(new ApiResponseSingleOk(
                "Update Task Status", "Task [" + task.getTitle() + "] status was updated"), HttpStatus.OK);
    }

}
