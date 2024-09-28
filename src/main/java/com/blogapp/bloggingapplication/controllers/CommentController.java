package com.blogapp.bloggingapplication.controllers;

import com.blogapp.bloggingapplication.Services.CommentService;
import com.blogapp.bloggingapplication.entities.Comment;
import com.blogapp.bloggingapplication.payloads.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @PostMapping("/user/{userid}/post/{postid}")
    public ResponseEntity<CommentDTO> addcomment(@RequestBody CommentDTO comment,
                                                 @PathVariable String postid,
                                                 @PathVariable String userid)throws ExecutionException, InterruptedException {
        System.out.println("User ID: " + userid);
        CommentDTO addedComment=this.commentService.addComment(comment, postid,userid);
        return new ResponseEntity<>(addedComment, HttpStatus.CREATED);

    }
@DeleteMapping("/{commentid}")
    public ResponseEntity<?> deletecomment(@PathVariable String commentid){
        this.commentService.deleteComment(commentid);
        String message="Comment with id "+ commentid+" deleted sucessfully";
        return new ResponseEntity<>(Map.of("message",message),HttpStatus.OK);
    }
}
