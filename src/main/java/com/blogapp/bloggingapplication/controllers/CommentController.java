package com.blogapp.bloggingapplication.controllers;

import com.blogapp.bloggingapplication.Services.CommentService;
import com.blogapp.bloggingapplication.entities.Comment;
import com.blogapp.bloggingapplication.payloads.CommentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Endpoints for managing comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Operation(summary = "Add a comment", description = "Add a comment to a post by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment added successfully",
                    content = @Content(schema = @Schema(implementation = CommentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/user/{userid}/post/{postid}")
    public ResponseEntity<CommentDTO> addcomment(@RequestBody CommentDTO comment,
                                                 @PathVariable String postid,
                                                 @PathVariable String userid)throws ExecutionException, InterruptedException {
        System.out.println("User ID: " + userid);
        CommentDTO addedComment=this.commentService.addComment(comment, postid,userid);
        return new ResponseEntity<>(addedComment, HttpStatus.CREATED);

    }
    @Operation(summary = "Delete a comment", description = "Delete a comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
@DeleteMapping("/{commentid}")
    public ResponseEntity<?> deletecomment(@PathVariable String commentid){
        this.commentService.deleteComment(commentid);
        String message="Comment with id "+ commentid+" deleted sucessfully";
        return new ResponseEntity<>(Map.of("message",message),HttpStatus.OK);
    }
}
