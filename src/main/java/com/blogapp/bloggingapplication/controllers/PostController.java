package com.blogapp.bloggingapplication.controllers;

import com.blogapp.bloggingapplication.Constants;
import com.blogapp.bloggingapplication.Services.FirebaseService;
import com.blogapp.bloggingapplication.Services.PostService;
import com.blogapp.bloggingapplication.payloads.PageResponse;
import com.blogapp.bloggingapplication.payloads.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/posts")
@Validated
@Tag(name = "Posts", description = "Endpoints for managing posts")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    FirebaseService firebaseService;
    @Value("${project.image}")
    private String path;

    @Operation(summary = "Create a post", description = "Create a post under a specific user and category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized to create this post")
    })
    @RequestMapping("/user/{userid}/category/{categoryid}/posts")
    public ResponseEntity<?> createPost(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "imageName", required = false) MultipartFile file,
            @PathVariable String userid,
            @PathVariable String categoryid,
            Authentication authentication

    ) throws ExecutionException, InterruptedException, IOException {
        String authenticatedEmail = authentication.getName();
        if (!authenticatedEmail.equals(userid)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The emailAddress does not match the one you authorize yourself with");
        }
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(title);
        postDTO.setContent(content);
        PostDTO createpost = this.postService.createPost(postDTO, userid, categoryid, file);

        return new ResponseEntity<>(createpost, HttpStatus.CREATED);
    }

    @Operation(summary = "Get posts by user", description = "Retrieve paginated posts by a specific user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    })
    @RequestMapping("/user/{userid}/posts")
    public ResponseEntity<PageResponse> getPostsByUser(@PathVariable String userid, @RequestParam(defaultValue = "0") int page) throws Exception {
        Pageable pageable = PageRequest.of(page, 5);
        PageResponse posts = this.postService.getPostByUser(userid, pageable);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @Operation(summary = "Get posts by category", description = "Retrieve paginated posts under a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    })
    @RequestMapping("/category/{categoryid}/posts")
    public ResponseEntity<PageResponse> getPostsByCategory(@PathVariable String categoryid, @RequestParam(defaultValue = "0") int page) throws Exception {
        Pageable pageable = PageRequest.of(page, 5);
        PageResponse posts = this.postService.getPostByCategory(categoryid, pageable);
        return new ResponseEntity<PageResponse>(posts, HttpStatus.OK);
    }

    @Operation(summary = "Get all posts", description = "Retrieve all posts with pagination and sorting options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    })
    @RequestMapping("/")
    public ResponseEntity<PageResponse> getAllPosts(
            @RequestParam(value = "pagenumber", defaultValue = Constants.pagenumber, required = false) Integer pagenumber,
            @RequestParam(value = "pagesize", defaultValue = Constants.pagesize, required = false) Integer pagesize,
            @RequestParam(value = "sortBy", defaultValue = Constants.sortBy, required = false) String sortBy,
            @RequestParam(value = "sortdir", defaultValue = Constants.sortdir, required = false) String sortdir) throws ExecutionException, InterruptedException {
        PageResponse posts = this.postService.getAllPosts(pagenumber, pagesize, sortBy, sortdir);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @Operation(summary = "Get post by title", description = "Retrieve a post by its title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully")
    })
    @RequestMapping("/{postid}")
    public ResponseEntity<List<PostDTO>> getPostByTitle(@PathVariable String postid) throws ExecutionException, InterruptedException {
        List<PostDTO> post = this.postService.getPostByTitle(postid);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @Operation(summary = "Update a post", description = "Update a post by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized to update this post")
    })
    @PutMapping("/{postid}")
    public ResponseEntity<?> updatePost(@RequestBody PostDTO postDTO, @PathVariable String postid, Authentication authentication) throws ExecutionException, InterruptedException {
        PostDTO Post = this.postService.getPostById(postid);
        System.out.println(Post.getUser().getEmailaddress());
        String authenticatedEmail = authentication.getName();
        if (!authenticatedEmail.equals(Post.getUser().getEmailaddress())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this post");
        }
        PostDTO post = this.postService.updatePost(postDTO, postid);
        return new ResponseEntity<PostDTO>(post, HttpStatus.OK);
    }

    @Operation(summary = "Delete a post", description = "Delete a post by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Unauthorized to delete this post")
    })
    @DeleteMapping("/{postid}")
    public ResponseEntity<?> deletePost(@PathVariable String postid, Authentication authentication) throws ExecutionException, InterruptedException {
        PostDTO postDTO = this.postService.getPostById(postid);
        String authenticatedEmail = authentication.getName();
        if (!postDTO.getUser().getEmailaddress().equals(authenticatedEmail) && !authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this post.");
        }
        this.postService.deletePost(postid);
        return new ResponseEntity<>(Map.of("message", "Post deleted sucessfully"), HttpStatus.OK);
    }

    @Operation(summary = "Search posts", description = "Search posts by keywords")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    @GetMapping("/search/{keywords}")
    public ResponseEntity<PageResponse> searchPost(@PathVariable String keywords, @RequestParam(value = "pagenumber", defaultValue = "0", required = false) Integer pagenumber, @RequestParam(value = "pagesize", defaultValue = "5", required = false) Integer pagesize) throws ExecutionException, InterruptedException {
        PageResponse result = this.postService.searchPosts(keywords, pagenumber, pagesize, null, null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Upload post image", description = "Upload an image for posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid image file")
    })
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()) {
            return new ResponseEntity<>("Please upload an image file", HttpStatus.BAD_REQUEST);
        }

        String downloadUrl = firebaseService.uploadImage(imageFile);
        return new ResponseEntity<>(downloadUrl, HttpStatus.OK);
    }
}

