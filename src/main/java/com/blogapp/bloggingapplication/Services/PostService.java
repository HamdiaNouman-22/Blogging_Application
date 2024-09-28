package com.blogapp.bloggingapplication.Services;

import com.blogapp.bloggingapplication.payloads.PageResponse;
import com.blogapp.bloggingapplication.payloads.PostDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface PostService {
    PostDTO createPost(PostDTO postDTO, String userid, String categoryId, MultipartFile file)throws ExecutionException, InterruptedException, IOException;
    PostDTO updatePost(PostDTO postDTO , String postid) throws ExecutionException, InterruptedException;
    String deletePost(String postid);
    PageResponse getAllPosts(Integer pageno, Integer pagesize,String sortBy,String sortdir)throws ExecutionException, InterruptedException;
    PostDTO getPostById(String postid)throws ExecutionException, InterruptedException;
    public PageResponse getPostByCategory(String categoryId,Pageable pageable) throws Exception;
    PageResponse getPostByUser(String userid, Pageable pageable)throws Exception;
    public List<PostDTO> getPostByTitle(String postid) throws ExecutionException, InterruptedException;
    PageResponse searchPosts(String searchQuery, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) throws ExecutionException, InterruptedException;

}
