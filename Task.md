Task: Implement a Concurrent Library Management System with RESTful API

You have 45 minutes to complete this exercise. Please follow best practices for coding, including proper design patterns, SOLID principles, concurrency handling, and comprehensive testing. Submit your solution to a public GitHub repository.

Requirements:

1. Design and implement a `Book` class with the following attributes:
   - ISBN (String)
   - Title (String)
   - Author (String)
   - Publication Year (int)
   - Available Copies (int)

2. Implement a thread-safe `Library` class that manages a collection of books concurrently. It should have the following methods:
   - `addBook(Book book)`: Adds a new book to the library
   - `removeBook(String isbn)`: Removes a book from the library by ISBN
   - `findBookByISBN(String isbn)`: Returns a book by its ISBN
   - `findBooksByAuthor(String author)`: Returns a list of books by a given author
   - `borrowBook(String isbn)`: Decreases the available copies of a book by 1
   - `returnBook(String isbn)`: Increases the available copies of a book by 1

3. Implement a simple in-memory cache for frequently accessed books to improve performance.

4. Create a RESTful API using Spring Boot (or a framework of your choice) that exposes the Library functionality. Include endpoints for all the operations mentioned above.

5. Implement proper error handling and use appropriate HTTP status codes for different scenarios.

6. Use dependency injection and follow SOLID principles in your design.

7. Implement comprehensive unit and integration tests, aiming for high code coverage.

8. Use Java 8+ features where appropriate (e.g., streams, lambdas, Optional).

Bonus (if time permits):
- Implement a basic rate limiting mechanism for the API endpoints.
- Add simple authentication to the API using JWT tokens.

Evaluation Criteria:
- Proper use of design patterns and SOLID principles
- Effective concurrent programming techniques
- RESTful API design and implementation
- Error handling and appropriate use of HTTP status codes
- Code organization, readability, and commenting
- Quality and coverage of unit and integration tests
- Effective use of Java 8+ features
- Bonus points for implementing caching, rate limiting, or authentication

Submission Instructions:

1. Create a new public repository on GitHub.
2. Implement your solution and commit your code to the repository.
3. Include a README.md file with:
   - Instructions on how to run your application
   - API documentation (endpoints, request/response formats)
   - Any assumptions or design decisions you made
   - Explanation of any additional features or optimizations you implemented
4. Ensure your repository includes:
   - All source code files
   - Unit and integration tests
   - Any configuration files necessary to run the application
5. Commit your changes and push to the remote repository.
6. Share the link to your public GitHub repository for review.
7. DO NOT USE Generative AI – we use algorithms to detect this and your results will be voided if we detect this or copied code.

Good luck!