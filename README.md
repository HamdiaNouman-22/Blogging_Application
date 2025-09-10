# Blogging Application

## Project Overview  
A Spring Boot backend for a blogging platform, providing a RESTful API with JWT-based authentication.  
Users can register, manage posts, categories, and comments, and upload images via Firebase Storage, with role-based access control for enhanced security.  

## Key Features  

### User Management  
- Register/login  
- Role-based authorization (NORMAL_USER / ADMIN_USER)  
- Update/delete accounts  

### Post Management  
- CRUD operations for posts  
- Search and pagination  
- Category assignment  
- Image uploads via Firebase Storage  

### Category & Comment Management  
- CRUD operations for categories  
- CRUD operations for comments  

### Security  
- JWT authentication  
- Role-based access control  
- Spring Security integration  

### API Documentation  
- Fully documented with Swagger/OpenAPI  

## Tech Stack  
- **Backend:** Spring Boot  
- **Database:** Firestore (NoSQL)  
- **File Storage:** Firebase Storage  
- **Authentication:** JWT  
- **API Documentation:** Swagger/OpenAPI  

## Swagger UI  
View API Docs in Swagger Editor:  
[Swagger UI](https://editor.swagger.io/?url=https://raw.githubusercontent.com/HamdiaNouman-22/Blogging_Application/refs/heads/master/src/main/java/com/blogapp/bloggingapplication/docs/api-docs.json)  
