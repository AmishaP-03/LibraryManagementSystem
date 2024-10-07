# Library System API

## Description
This is a RESTful API for managing a simple library system, allowing users to perform CRUD operations on books and authors.

## Requirements
- Java 8 or later
- Maven or Gradle

## Running the application

1. Clone the repository:

   ```bash
   git clone https://github.com/AmishaP-03/LibraryManagementSystem.git

2. Navigate to the project directory
3. Build the project using Maven
   ```bash
   mvn clean install
   
4. Run the application
   ```bash
   mvn spring-boot:run
   
5. Once the application is running, you can access the API documentation at:
    ```bash
   http://localhost:8080/swagger-ui.html

## Testing the application

1. Open Postman.
2. Import the collection by navigating to **File > Import**.
3. Select the `Postman_collection.json` file located in the root of this project.
4. Once imported, you will see all the endpoints under the "Library System API" collection in Postman.
5. Modify the requests as needed and hit **Send** to test the API.

