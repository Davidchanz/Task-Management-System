package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.dto.*;
import com.effectivemobile.TaskManagementSystem.dto.response.ApiResponse;
import com.effectivemobile.TaskManagementSystem.dto.response.ApiResponseSingleOk;
import com.effectivemobile.TaskManagementSystem.exception.RequiredRequestParamIsMissingException;
import com.effectivemobile.TaskManagementSystem.model.Comment;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.model.User;
import com.effectivemobile.TaskManagementSystem.service.PriorityService;
import com.effectivemobile.TaskManagementSystem.service.StatusService;
import com.effectivemobile.TaskManagementSystem.service.TaskService;
import com.effectivemobile.TaskManagementSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    public TaskService taskService;

    @Autowired
    public UserService userService;

    @Autowired
    public StatusService statusService;

    @Autowired
    public PriorityService priorityService;

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTask(@Valid @PathVariable Long id){
        return new ResponseEntity<>(new TaskDto(taskService.findTaskById(id)), HttpStatus.OK);
    }

    @PostMapping("/task/add")
    public ResponseEntity<TaskDto> addNewTask(@Valid @RequestBody(required = false) TaskUpdateDto taskUpdateDto, Principal principal){
        if(taskUpdateDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param TaskUpdateDto is missing");

        Task task = new Task(taskUpdateDto);
        task.setAuthor(userService.findUserByUserName(principal.getName()));
        task.setExecutor(userService.findUserByUserName(taskUpdateDto.getExecutor()));
        task.setStatus(statusService.findStatusById(taskUpdateDto.getStatus()));
        task.setPriority(priorityService.findPriorityById(taskUpdateDto.getPriority()));
        taskService.addNewTask(task);
        return new ResponseEntity<>(new TaskDto(task), HttpStatus.OK);
    }

    @PutMapping("/task/update/{id}")
    public ResponseEntity<ApiResponse> updateTask(@Valid @PathVariable Long id, @Valid @RequestBody(required = false) TaskUpdateDto taskUpdateDto, Principal principal){
        if(taskUpdateDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param TaskUpdateDto is missing");
        User currentUser = userService.findUserByUserName(principal.getName());

        taskService.updateTask(currentUser, id, taskUpdateDto);
        return new ResponseEntity<>(new ApiResponseSingleOk("Update Task", "Task [" + taskUpdateDto.getTitle() + "] was updated"), HttpStatus.OK);
    }

    @DeleteMapping("/task/delete/{id}")
    public ResponseEntity<ApiResponse> deleteTask(@Valid @PathVariable Long id, Principal principal){
        User currentUser = userService.findUserByUserName(principal.getName());
        Task task = taskService.deleteTask(currentUser, id);
        return new ResponseEntity<>(new ApiResponseSingleOk("Delete Task", "Task [" + task.getTitle() + "] was deleted!"), HttpStatus.OK);
    }

    @GetMapping("/task/author/{id}")
    public ResponseEntity<List<TaskDto>> getTasksByAuthor(@Valid @PathVariable String username){
        User author = userService.findUserByUserName(username);
        List<Task> tasks = taskService.findTasksByAuthor(author);
        var result = tasks
                .stream()
                .map(TaskDto::new)
                .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/task/executor/{id}")
    public ResponseEntity<List<TaskDto>> getTasksByExecutor(@Valid @PathVariable String username){
        User executor = userService.findUserByUserName(username);
        List<Task> tasks = taskService.findTasksByExecutor(executor);
        var result = tasks
                .stream()
                .map(TaskDto::new)
                .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/task/comments/{id}")
    public ResponseEntity<List<CommentDto>> getTaskComments(@Valid @PathVariable Long id){
        Set<Comment> comments = taskService.findTaskCommentsById(id);
        var result = comments
                .stream()
                .map(CommentDto::new)
                .toList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /*@PostMapping("/task/vote/up/{id}")
    public ResponseEntity<ApiResponse> upVote(@Valid @PathVariable Long id, Principal principal){
        User currentUser = userService.findUserByUserName(principal.getName());
        Task task = taskService.findTaskById(id);
        quoteStateService.addUpVoteQuoteState(task, currentUser);
        return new ResponseEntity<>(new ApiResponseSingleOk("Up Vote", "Up Vote for Quote [" + task.getText() + "] by user [" + currentUser.getUsername() + "]"), HttpStatus.OK);
    }

    @PostMapping("/quote/vote/down/{id}")
    public ResponseEntity<ApiResponse> downVote(@Valid @PathVariable Long id, Principal principal){
        User currentUser = userService.findUserByUserName(principal.getName());
        Task task = taskService.findTaskById(id);
        quoteStateService.addDownVoteQuoteState(task, currentUser);
        return new ResponseEntity<>(new ApiResponseSingleOk("Down Vote", "Down Vote for Quote [" + task.getText() + "] by user [" + currentUser.getUsername() + "]"), HttpStatus.OK);
    }

    @GetMapping("/quote/top10")
    public ResponseEntity<List<TaskDto>> getTop10Quotes(){
        var top10Quotes = quoteStateService.getTop10QuoteStates()
                .stream()
                .map(quoteStateDto ->
                        new TaskDto(quoteStateDto.getTask()))
                .toList();
        return new ResponseEntity<>(top10Quotes, HttpStatus.OK);
    }

    @GetMapping("/quote/worse10")
    public ResponseEntity<List<TaskDto>> getWorse10Quotes(){
        var top10Quotes = quoteStateService.getWorse10QuoteStates()
                .stream()
                .map(quoteStateDto ->
                        new TaskDto(quoteStateDto.getTask()))
                .toList();
        return new ResponseEntity<>(top10Quotes, HttpStatus.OK);
    }

    @GetMapping("/quote/graph/{id}")
    public ResponseEntity<GraphDto> getQuoteGraph(@Valid @PathVariable Long id){
        Task task = taskService.findTaskById(id);
        var votes = quoteStateService.getQuoteVoteGraphData(task);
        GraphDto graphDto = new GraphDto();
        List<VoteDto> data = new ArrayList<>();
        int rating = 0;
        data.add(new VoteDto(task.getCreatedOn(), rating));
        for(var vote: votes){
            rating += vote.getVoteValue();
            data.add(new VoteDto(vote.getVotedOn(), rating));
        }
        graphDto.setData(data);
        graphDto.setMaxRating(data.get(data.size()-1).getVoteValue());
        graphDto.setMinRating(data.get(0).getVoteValue());
        graphDto.setMaxTime(data.get(data.size()-1).getVotedOn());
        graphDto.setMinTime(data.get(0).getVotedOn());
        return new ResponseEntity<>(graphDto, HttpStatus.OK);
    }*/
}
