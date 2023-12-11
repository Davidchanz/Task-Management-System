package com.effectivemobile.TaskManagementSystem.controller;

import com.effectivemobile.TaskManagementSystem.dto.input.comment.CommentInputDto;
import com.effectivemobile.TaskManagementSystem.dto.output.auth.TokenDto;
import com.effectivemobile.TaskManagementSystem.dto.output.comment.CommentDto;
import com.effectivemobile.TaskManagementSystem.exception.PageIllegalArgumentException;
import com.effectivemobile.TaskManagementSystem.exception.RequiredRequestParamIsMissingException;
import com.effectivemobile.TaskManagementSystem.exception.notFound.PageNotFoundException;
import com.effectivemobile.TaskManagementSystem.model.Comment;
import com.effectivemobile.TaskManagementSystem.model.Task;
import com.effectivemobile.TaskManagementSystem.service.CommentService;
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
import java.util.Set;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    public CommentService commentService;

    @Autowired
    public UserService userService;

    @Autowired
    public TaskService taskService;

    @Value("${app.page.size}")
    private Integer pageSize;

    @Operation(summary = "Get Comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Comment",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If {id} is missing"),
            @ApiResponse(responseCode = "404", description = "If Comment not found")
    })
    @Parameter(name = "id", description = "Comment id", example = "1")
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getComment(@Valid @PathVariable Long id){
        return new ResponseEntity<>(new CommentDto(commentService.findCommentById(id)), HttpStatus.OK);
    }

    @Operation(summary = "Get All Task's Comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Task's Comments",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))) }
            ),
            @ApiResponse(responseCode = "400", description = "If {id} is missing, page < 0"),
            @ApiResponse(responseCode = "404", description = "If page not found")
    })
    @Parameter(name = "id", description = "Task id", example = "1")
    @GetMapping("/task/{id}")
    public ResponseEntity<List<CommentDto>> getCommentsByTask(
            @Valid @PathVariable Long id,
            @Valid @RequestParam(defaultValue = "0", name = "page") Integer page){

        if(page < 0)
            throw new PageIllegalArgumentException("Page index must not be less than zero");

        Pageable pageable = PageRequest.of(page, pageSize);
        Task task = taskService.findTaskById(id);
        Page<Comment> commentsPage = commentService.findCommentsByTask(task, pageable);

        if(page > commentsPage.getTotalPages()-1)
            throw new PageNotFoundException("Tasks Page number [" + page + "] not found!");

        var result = commentsPage.getContent()
                .stream()
                .map(CommentDto::new)
                .toList();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Add Comment to Task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add Comment to Task",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDto.class)) }
            ),
            @ApiResponse(responseCode = "400", description = "If RequestBody, {id} is missing"),
            @ApiResponse(responseCode = "404", description = "If Task not found")
    })
    @Parameter(name = "id", description = "Task id", example = "1")
    @PostMapping("/add/{id}")
    public ResponseEntity<CommentDto> addCommentToTask(
            @Valid @RequestBody(required = false) CommentInputDto commentInputDto,
            @Valid @PathVariable Long id,
            Principal principal){
        if(commentInputDto == null)
            throw new RequiredRequestParamIsMissingException("Required request param CommentInputDto is missing");

        Comment comment = new Comment(commentInputDto);
        comment.setAuthor(userService.findUserByUserName(principal.getName()));
        comment.setTask(taskService.findTaskById(id));
        commentService.addNewComment(comment);
        return new ResponseEntity<>(new CommentDto(comment), HttpStatus.OK);
    }

}
