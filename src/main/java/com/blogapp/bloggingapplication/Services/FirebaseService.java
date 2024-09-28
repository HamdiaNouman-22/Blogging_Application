package com.blogapp.bloggingapplication.Services;

import com.blogapp.bloggingapplication.entities.Comment;
import com.blogapp.bloggingapplication.entities.Post;
import com.blogapp.bloggingapplication.payloads.PageResponse;
import com.blogapp.bloggingapplication.payloads.PostDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class FirebaseService {
    @Autowired
    private ModelMapper modelMapper;


    protected Firestore getDbFirestore() {
        return FirestoreClient.getFirestore();
    }

    public String tokenizeTitle(String title) {
        if (title == null || title.isEmpty()) {
            return "";
        }
        Pattern pattern = Pattern.compile("\\W+");

        return Arrays.stream(pattern.split(title))
                .map(String::toLowerCase)
                .filter(token -> !token.isEmpty())
                .collect(Collectors.joining(","));
    }

    public <T> String saveDocument(String collectionName, String documentId, T entity) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = getDbFirestore();
        if (collectionName == null || collectionName.isEmpty()) {
            throw new IllegalArgumentException("Collection name cannot be null or empty");
        }
        if (documentId == null || documentId.isEmpty()) {
            throw new IllegalArgumentException("Document ID cannot be null or empty");
        }
        Class<?> entityclass = entity.getClass();
        if (collectionName.equals("posts") && entity instanceof Post) {


            DocumentReference documentRef = dbFirestore.collection(collectionName).document();
            String generatedId = documentRef.getId();
            Post post = (Post) entity;
            post.setPostId(generatedId);
            post.setTitleTokens(tokenizeTitle(post.getTitle()));
            // Save the post with the generated document ID
            ApiFuture<WriteResult> writeResultFuture = documentRef.set(post);

            // Get the result of the write operation
            WriteResult writeResult = writeResultFuture.get();
            System.out.println("Write time: " + writeResult.getUpdateTime());

            // Return the update time as a string
            return writeResult.getUpdateTime().toString();
        }
        if (collectionName.equals("comments")) {
            // Generate a new document with an auto-generated ID
            DocumentReference commentRef = dbFirestore.collection(collectionName).document();
            String commentId = commentRef.getId();  // Get the auto-generated ID
            // Optionally, you can add the ID to the comment entity if needed
            Comment comment = (Comment) entity;
            comment.setId(commentId); // Assuming 'Comment' class has a 'setId' method
            ApiFuture<WriteResult> document = commentRef.set(comment);
            WriteResult writeResult = document.get();
            return writeResult.getUpdateTime().toString();
        }

        // Generic case: save the entity with a provided documentId for other collections
        ApiFuture<WriteResult> document = dbFirestore.collection(collectionName).document(documentId).set(entity);
        WriteResult writeResult = document.get();
        return writeResult.getUpdateTime().toString();
    }

    public <T> T getDocument(String collectionName, String documentId, Class<T> tClass) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = getDbFirestore();
        DocumentSnapshot document = dbFirestore.collection(collectionName).document(documentId).get().get();
        return document.exists() ? document.toObject(tClass) : null;
    }

    public List<PostDTO> getPostByTitle(String collectionName, String Title) throws ExecutionException, InterruptedException {
        Firestore firestore = getDbFirestore();
        List<PostDTO> posts = new ArrayList<>();
        ApiFuture<QuerySnapshot> query = firestore.collection(collectionName).whereEqualTo("title", Title).get();
        QuerySnapshot querySnapshot = query.get();
        if (!querySnapshot.isEmpty()) {
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                PostDTO postDTO = document.toObject(PostDTO.class);
                postDTO.setPostId(document.getId());
                posts.add(postDTO);
            }
        } else {
            System.out.println("No post found with Title : " + Title);
        }
        return posts;

    }

    public <T> String updateDocument(String collectionName, String documentId, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = getDbFirestore();
        ApiFuture<WriteResult> writeResult = dbFirestore.collection(collectionName).document(documentId).update(updates);
        WriteResult result = writeResult.get();
        return result.getUpdateTime().toString();
    }

    public String deleteDocument(String collectionName, String userId) {
        Firestore dbFirestore = getDbFirestore();
        try {
            DocumentReference documentReference = dbFirestore.collection(collectionName).document(userId);
            DocumentSnapshot document = documentReference.get().get();
            if (!document.exists()) {
                return ("Document with id " + userId + " does not exists");
            }
            documentReference.delete().get();
            return ("Document with id: " + userId + " deleted sucessfully");
        } catch (
                InterruptedException |
                ExecutionException e) {
            e.printStackTrace();
            return ("Error deleting document " + e.getMessage());
        }
    }

    public <T> List<T> getAllDocument(String CollectionName, Class<T> classname) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = getDbFirestore();
        QuerySnapshot queryDocumentSnapshots = dbFirestore.collection(CollectionName).get().get();
        return queryDocumentSnapshots.getDocuments().stream().map(document -> document.toObject(classname)).collect(Collectors.toList());
    }

    public PageResponse getPagedDocuments(String collectionName, Integer pageNumber, Integer pageSize, String sortBy, String sortDir, Class<Post> clazz) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = getDbFirestore();
        CollectionReference collection = dbFirestore.collection(collectionName);

        // Create a query with sorting
        Query query = collection.orderBy(sortBy, sortDir.equalsIgnoreCase("asc") ? Query.Direction.ASCENDING : Query.Direction.DESCENDING);

        // Apply pagination
        Query paginatedQuery = query.limit(pageSize);

        // Fetch documents
        ApiFuture<QuerySnapshot> querySnapshot = paginatedQuery.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        // Convert documents to entities
        List<Post> posts = documents.stream()
                .map(document -> document.toObject(Post.class))
                .collect(Collectors.toList());

        // Convert entities to DTOs
        List<PostDTO> postDTOS = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        // Create PageResponse
        PageResponse pageResponse = new PageResponse();
        pageResponse.setContent(postDTOS);
        pageResponse.setPagenumber(pageNumber);
        pageResponse.setPagesize(pageSize);
        pageResponse.setTotalElements(postDTOS.size());
        pageResponse.setTotalPages((int) Math.ceil((double) postDTOS.size() / pageSize));
        pageResponse.setLastPage(postDTOS.size() < pageSize);

        return pageResponse;
    }

    public PageResponse getPostsByUser(String email, Pageable pageable) throws Exception {
        Firestore firestore = getDbFirestore();
        CollectionReference postsRef = firestore.collection("posts");
        Query query = postsRef.whereEqualTo("user.emailaddress", email);

        // Implement pagination
        query = query.limit(pageable.getPageSize()).offset(pageable.getPageNumber() * pageable.getPageSize());

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<PostDTO> posts = querySnapshot.get().toObjects(PostDTO.class);

        // Total count of posts for the user
        long totalPosts = postsRef.whereEqualTo("user.emailaddress", email).get().get().size();

        // Create PageResponse
        PageResponse pageResponse = new PageResponse();
        pageResponse.setContent(posts);
        pageResponse.setPagenumber(pageable.getPageNumber());
        pageResponse.setPagesize(pageable.getPageSize());
        pageResponse.setTotalElements(totalPosts);
        pageResponse.setTotalPages((long) Math.ceil((double) totalPosts / pageable.getPageSize()));
        pageResponse.setLastPage(pageable.getPageNumber() + 1 >= pageResponse.getTotalPages());

        return pageResponse;
    }

    // Method to fetch posts by userId with pagination
    public PageResponse getPostsByCategory(String id, Pageable pageable) throws Exception {
        Firestore firestore = getDbFirestore();
        CollectionReference postsRef = firestore.collection("posts");
        Query query = postsRef.whereEqualTo("category.categoryTitle", id);

        // Implement pagination
        query = query.limit(pageable.getPageSize()).offset(pageable.getPageNumber() * pageable.getPageSize());

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<PostDTO> posts = querySnapshot.get().toObjects(PostDTO.class);

        // Total count of posts for the user
        long totalPosts = postsRef.whereEqualTo("category.categoryTitle", id).get().get().size();

        // Create PageResponse
        PageResponse pageResponse = new PageResponse();
        pageResponse.setContent(posts);
        pageResponse.setPagenumber(pageable.getPageNumber());
        pageResponse.setPagesize(pageable.getPageSize());
        pageResponse.setTotalElements(totalPosts);
        pageResponse.setTotalPages((long) Math.ceil((double) totalPosts / pageable.getPageSize()));
        pageResponse.setLastPage(pageable.getPageNumber() + 1 >= pageResponse.getTotalPages());

        return pageResponse;
    }

    public PageResponse searchPostsByTitleWithPagination(String searchQuery, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = getDbFirestore();
        CollectionReference postsRef = dbFirestore.collection("posts");
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "title"; // Default sorting field
        }
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc"; // Default sorting direction
        }
        String tokenizedSearchQuery = tokenizeTitle(searchQuery);
        System.out.println(tokenizedSearchQuery);
        Query query = postsRef
                .whereGreaterThanOrEqualTo("titleTokens", tokenizedSearchQuery)
                .whereLessThanOrEqualTo("titleTokens", tokenizedSearchQuery + "\uf8ff")
//                .whereArrayContains("titleTokens", tokenizedSearchQuery)
                .orderBy(sortBy, sortDir.equalsIgnoreCase("asc") ? Query.Direction.ASCENDING : Query.Direction.DESCENDING)
                .limit(pageSize);

        // Apply pagination
        Query paginatedQuery = query
                .offset(pageNumber * pageSize);

        // Fetch documents
        ApiFuture<QuerySnapshot> querySnapshot = paginatedQuery.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        List<Post> posts = documents.stream()
                .map(document -> {
                    Post post = document.toObject(Post.class);
                    post.setPostId(document.getId());  // Set Firestore document ID as postId
                    return post;
                })
                .collect(Collectors.toList());

        List<PostDTO> postDTOS = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
        // Create PageResponse
        PageResponse pageResponse = new PageResponse();
        pageResponse.setContent(postDTOS);
        pageResponse.setPagenumber(pageNumber);
        pageResponse.setPagesize(pageSize);
        pageResponse.setTotalElements(postDTOS.size()); // Firestore doesn’t support document count with queries
        pageResponse.setTotalPages((int) Math.ceil((double) postDTOS.size() / pageSize));
        pageResponse.setLastPage(postDTOS.size() < pageSize);  // Determine if this is the last page

        return pageResponse;

    }

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        String bucketName = "blogging-application-d96d3.appspot.com";
        var bucket = StorageClient.getInstance().bucket(bucketName);

        BlobInfo blobInfo = BlobInfo.newBuilder(bucket.getName(), fileName).build();
        bucket.create(blobInfo.getBlobId().getName(), file.getInputStream(), file.getContentType());
        String downloadUrl = String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", bucket.getName(), fileName);
        return downloadUrl;
    }
}

