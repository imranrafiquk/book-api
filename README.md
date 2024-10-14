# book-library-api
## Build & run
    mvn clean package spring-boot:run
## Api Documentation 

### Endpoints
| Endpoint                       | Method | Summary                                  | Description                                            |
|--------------------------------|--------|------------------------------------------|--------------------------------------------------------|
| /api/book                      | POST   | Add a new book                          | Add a new book to the library                          |
| /api/book/seed                 | POST   | DEBUG Seed some books                   | Seed some books into the library                        |
| /api/book/return/{isbn}       | POST   | Return a book                           | Return a borrowed book                                  |
| /api/book/borrow/{isbn}       | POST   | Borrow a book by the ISBN               | Borrow a book                                          |
| /api/book/findByISBN/{isbn}   | GET    | Get a book by the ISBN                  | Find a book in the library by the ISBN                 |
| /api/book/findByAuthor/{author}| GET    | Get a book by the author                | Find a book in the library by the author               |
| /api/book/{isbn}              | DELETE | Delete book                             | Remove a book from the library                          |

### Debug Seed data
| ISBN               | Title                                        | Author                         | Publication Year | Copies |
|--------------------|----------------------------------------------|--------------------------------|-------------------|--------|
| 978-1-61729-008-1  | Cracking the Coding Interview                 | Gayle Laakmann McDowell       | 2021              | 5      |
| 978-0-321-54573-0  | Elements of Programming Interviews            | Adnan Aziz                    | 2012              | 3      |
| 978-0-9961281-0-3  | System Design Interview – An Insider's Guide  | Alex Xu                       | 2020              | 4      |
| 978-1-59327-000-1  | The Geek's Guide to Interviews                | T. D. Pankaj                  | 2021              | 5      |

### OpenAPI
#### Running locally
http://localhost:8080/swagger-ui/index.html

#### Publicly available 
https://8080-imranrafiqu-booklibrary-2tbihzvzgz0.ws-eu116.gitpod.io/swagger-ui/index.html


## Assumptions
* Cannot add a book if one already exists with the same ISBN
* Cannot borrow a book that has no copies remaining
* Cannot return a book that doesn't exist in the library
* Overall it's pretty simple and limited to cover the basic exercise 
  * No user authentication / roles
  * More books can be returned than were avaialable
  * Books cannot be updated
  * A borrowed book may be deleted which then make the book impossible to return 


## Additional Features
* There is a debug endpoint to seed some books
* Thread safe and concurrent as per the exercise
* Time did not permit the completion of bonus features jwt authentication or rate limiting 