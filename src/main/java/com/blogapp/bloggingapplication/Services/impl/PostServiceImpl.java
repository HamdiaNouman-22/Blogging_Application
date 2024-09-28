package com.blogapp.bloggingapplication.Services.impl;

import com.blogapp.bloggingapplication.Services.FirebaseService;
import com.blogapp.bloggingapplication.Services.PostService;
import com.blogapp.bloggingapplication.Services.UserService;
import com.blogapp.bloggingapplication.entities.Category;
import com.blogapp.bloggingapplication.entities.Post;
import com.blogapp.bloggingapplication.entities.User;
import com.blogapp.bloggingapplication.exceptions.ResourceNotFoundException;
import com.blogapp.bloggingapplication.payloads.PageResponse;
import com.blogapp.bloggingapplication.payloads.PostDTO;
import com.blogapp.bloggingapplication.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    UserService userService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    FirebaseService firebaseService;
    private static Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Override
    public PostDTO createPost(PostDTO postDTO, String userid, String categoryId, MultipartFile imageFile) throws ExecutionException, InterruptedException, IOException {
        User user = this.firebaseService.getDocument("users", userid, User.class);
        System.out.println(user.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + userid);
        }

        Category category = this.firebaseService.getDocument("categories", categoryId, Category.class);
        System.out.println(category.getCategoryTitle());
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }
        Post post = this.modelMapper.map(postDTO, Post.class);
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = firebaseService.uploadImage(imageFile);
            post.setImageName(imageUrl);
        } else {
            post.setImageName("default.png");
        }
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);
        post.setTitleTokens(this.firebaseService.tokenizeTitle(post.getTitle()));
        try {
            String updateTime = firebaseService.saveDocument("posts",post.getPostId(), post);
            System.out.println("Post saved successfully with update time: " + updateTime);
        } catch (
                ExecutionException |
                InterruptedException e) {
            logger.error("Error saving post to document " + e);
        }
        return this.modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, String postid)  throws ExecutionException, InterruptedException{
        Map<String,Object> updates=new HashMap<>();
        if (postDTO.getTitle() != null) {
            updates.put("username", postDTO.getTitle());
        }
        if(postDTO.getContent()!=null){
            updates.put("content", postDTO.getContent());
        }
        if(postDTO.getAddedDate()!=null){
            updates.put("content", postDTO.getAddedDate());
        }
        if(postDTO.getCategory()!=null){
            updates.put("content", postDTO.getCategory());
        }
        if(postDTO.getImageName()!=null){
            updates.put("content", postDTO.getImageName());
        }
        firebaseService.updateDocument("posts",postid,updates);
        PostDTO post = firebaseService.getDocument("posts", postid, PostDTO.class);
        return post;
    }

    @Override
    public String deletePost(String postid) {
        return (firebaseService.deleteDocument("posts",postid));
    }

    @Override
    public PageResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortdir) throws ExecutionException, InterruptedException{
        PageResponse pageResponse = firebaseService.getPagedDocuments("posts", pageNumber, pageSize, sortBy, sortdir, Post.class);
        List<PostDTO> postDTOS = pageResponse.getContent().stream()
                .map(post -> this.modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
        PageResponse response = new PageResponse();
        response.setContent(postDTOS);
        response.setPagenumber(pageNumber);
        response.setPagesize(pageSize);
        response.setTotalElements(postDTOS.size());
        response.setTotalPages((int) Math.ceil((double) postDTOS.size() / pageSize));
        response.setLastPage(postDTOS.size() < pageSize);

        return response;
    }

    @Override
    public List<PostDTO> getPostByTitle(String postid) throws ExecutionException, InterruptedException{
        List<PostDTO> posts=firebaseService.getPostByTitle("posts",postid);
        return posts;
    }
    @Override
    public PostDTO getPostById(String postid) throws ExecutionException, InterruptedException{
        PostDTO post=firebaseService.getDocument("posts",postid,PostDTO.class);
        return post;
    }

    @Override
    public PageResponse getPostByCategory(String categoryId,Pageable pageable) throws Exception{
        return firebaseService.getPostsByCategory(categoryId,pageable);
    }
    @Override
    public PageResponse getPostByUser(String userid,Pageable pageable) throws Exception{
        return firebaseService.getPostsByUser(userid,pageable);
    }
    @Override
    public PageResponse searchPosts(String searchQuery, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) throws ExecutionException, InterruptedException {
        PageResponse pageResponse = firebaseService.searchPostsByTitleWithPagination(searchQuery, pageNumber, pageSize, sortBy, sortDir);
        return pageResponse;
    }

}
