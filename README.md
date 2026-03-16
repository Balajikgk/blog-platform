# Blog Platform REST API

A production-grade Blog Platform REST API built with Spring Boot 3, implementing clean architecture, SOLID principles, and enterprise coding standards.

## Features

- Spring Boot 3+ with Java 17
- Spring Security with JWT-based authentication
- Spring Data JPA with PostgreSQL
- GraphQL support using Spring GraphQL
- Swagger/OpenAPI documentation with springdoc
- MapStruct for DTO mapping
- Lombok for reducing boilerplate code
- Clean Architecture with proper layer separation
- SOLID Principles implementation
- Comprehensive Testing with JUnit 5 and Mockito
- Global Exception Handling
- Pagination Support
- Role-based Access Control

## Tech Stack

- Java 17
- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- Spring Security
- Spring GraphQL
- PostgreSQL
- Maven
- Lombok
- MapStruct
- Swagger/OpenAPI
- JUnit 5
- Mockito

## Architecture

The project follows clean layered architecture with clear separation of concerns:

```
com.blogplatform
├── config              # Configuration classes (Security, Swagger)
├── controller          # REST Controllers (API endpoints)
├── service             # Service interfaces (business contracts)
├── service.impl        # Service implementations (business logic)
├── repository          # JPA repositories (data access)
├── entity              # JPA entities (database models)
├── dto                 # Data Transfer Objects (API contracts)
├── mapper              # MapStruct mappers (object mapping)
├── exception           # Exception handling
├── security            # Security components (JWT, authentication)
├── graphql             # GraphQL resolvers
├── util                # Utility classes (ApiResponse)
└── BlogPlatformApplication
```

### Design Patterns Implemented

#### 1. Repository Pattern
- Clean separation between business logic and data access
- PostRepository, UserRepository, CategoryRepository, CommentRepository
- Uses Spring Data JPA for standard CRUD operations

#### 2. Service Layer Pattern
- Business logic encapsulation with interface-implementation separation
- PostService interface with PostServiceImpl implementation
- Transaction management with @Transactional

#### 3. DTO Pattern
- Data Transfer Objects for API communication
- PostRequestDTO, PostResponseDTO, CommentDTO, CategoryDTO
- Prevents entity exposure and enables API versioning

#### 4. Mapper Pattern
- MapStruct for efficient object mapping
- PostMapper, CommentMapper for entity-DTO conversions
- Reduces boilerplate code and mapping errors

#### 5. Dependency Injection
- Constructor injection throughout the application
- @RequiredArgsConstructor with Lombok for clean code

#### 6. Strategy Pattern in Security
- JWT-based authentication strategy
- Configurable security filters and providers

## API Design Principles

### 1. RESTful Design
- Resource-based URLs (/posts, /categories, /comments)
- HTTP methods semantics (GET, POST, PUT, DELETE)
- Proper HTTP status codes

### 2. Consistent Response Format
All API responses follow the ApiResponse structure:
```java
{
  "success": boolean,
  "message": "string",
  "data": T,
  "timestamp": "LocalDateTime",
  "path": "string"
}
```

### 3. Pagination
- Spring Data Pageable for consistent pagination
- Default page size: 10, configurable
- Sortable results with customizable fields

### 4. Error Handling
- Global exception handler with @RestControllerAdvice
- Structured error responses with proper HTTP status codes
- Comprehensive logging for debugging

### 5. Security
- JWT-based stateless authentication
- Role-based access control
- Method-level security with @PreAuthorize

## Workflow and Data Flow

### 1. Request Processing Flow
```
Client Request -> Security Filter -> Controller -> Service -> Repository -> Database
                     |
                 Response <- Mapper <- Entity <- JPA <- Database
```

### 2. Authentication Flow
```
1. User credentials -> /auth/login
2. Validate credentials -> Generate JWT token
3. Client includes token in Authorization header
4. JwtAuthenticationFilter validates token
5. Security context populated
6. Request proceeds to controller
```

### 3. Post Creation Workflow
```
POST /posts/create
- Validate JWT token
- Validate request DTO
- Map DTO to Entity
- Set author from authenticated user
- Validate category exists
- Save entity via repository
- Map entity to response DTO
- Return success response
```

### 4. Error Handling Workflow
```
Exception occurs -> GlobalExceptionHandler -> Log error -> Format ApiResponse -> Return appropriate HTTP status
```

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Running the Application

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on http://localhost:8123

## API Documentation

### Swagger UI
Access the interactive API documentation at:
- Swagger UI: http://localhost:8123/swagger-ui.html
- OpenAPI JSON: http://localhost:8123/api-docs

## Authentication

The API uses JWT-based authentication. To access protected endpoints:

1. Register a new user:
```bash
POST /auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "password123"
}
```

2. Login to get JWT token:
```bash
POST /auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

3. Use the token in Authorization header:
```bash
Authorization: Bearer <your-jwt-token>
```

## API Endpoints

### Authentication
- POST /auth/register - Register new user
- POST /auth/login - User login

### Posts
- POST /posts/create - Create new post (authenticated)
- GET /posts/paginated - Get all posts (paginated)
- GET /posts/{id} - Get post by ID
- PUT /posts/{id} - Update post 
- DELETE /posts/{id} - Delete post 
- GET /posts/search?keyword={keyword} - Search posts

### Categories
- POST /categories - Create new category
- GET /categories - Get all categories
- GET /categories/{id} - Get category by ID
- PUT /categories/{id} - Update category
- DELETE /categories/{id} - Delete category

### Comments
- POST /posts/{postId}/comments - Create comment (authenticated)
- GET /posts/{postId}/comments - Get comments for post
- PUT /posts/{postId}/comments/{commentId} - Update comment
- DELETE /posts/{postId}/comments/{commentId} - Delete comment 

## GraphQL Queries and Mutations

### Queries
```graphql
query {
  getAllPosts {
    id
    title
    content
    authorUsername
    categoryName
    createdAt
  }
}

query {
  getPostById(id: 1) {
    id
    title
    content
    authorUsername
    categoryName
    createdAt
  }
}
```

### Mutations
```graphql
mutation {
  createPost(postInput: {
    title: "New Post"
    content: "Post content"
    categoryId: 1
  }) {
    id
    title
    content
    authorUsername
    categoryName
    createdAt
  }
}
```
## Security Features

- JWT-based authentication with configurable expiration
- Role-based access control (USER, ADMIN)
- Password encryption with BCrypt
- CORS configuration for cross-origin requests
- CSRF protection (disabled for API)
- Input validation with Jakarta Validation
- SQL injection prevention through JPA

## Performance Optimizations

- Database indexing on frequently queried fields
- Lazy loading for entity relationships
- Pagination support for large datasets
- Connection pooling via HikariCP
- Query optimization with JPA @Query annotations
- DTO mapping to prevent over-fetching

## Database Schema

The application uses the following main tables:
- users - User information with roles
- categories - Blog categories
- posts - Blog posts with author and category relationships
- comments - Post comments with user relationships

### Entity Relationships
- User to Post (One-to-Many)
- User to Comment (One-to-Many)
- Post to Comment (One-to-Many)
- Category to Post (One-to-Many)

## Deployment

### Environment Variables
- DATABASE_URL - PostgreSQL connection URL
- DATABASE_USERNAME - Database username
- DATABASE_PASSWORD - Database password
- JWT_SECRET - JWT secret key

### Docker Deployment (Future)
```bash
# Build Docker image
docker build -t blog-platform .

# Run container
docker run -p 8123:8123 blog-platform
```

## Monitoring and Logging

- Application logs: logs/blog-platform.log
- Structured logging with SLF4J
- Debug logging for SQL and security
- Request/Response logging for API calls

## License

This project is licensed under the MIT License - see the LICENSE file for details.