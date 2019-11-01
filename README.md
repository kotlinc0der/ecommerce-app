# eCommerce Application

In this project, the main focus was on: 
- Demonstrating correct handling of authorization with proper security using JWT.
- Writing tests and meeting an acceptable code coverage level.
- Identifying the correct metrics for logging, in order to monitor a system.
- Indexing metrics to Splunk.
- Demonstrating configuration and automation of the CI/CD pipeline.



## Features
This project supports the following operations:
* User Creation/Loading
* Item Loading
* Adding/Removing Items to/from Cart 
* Submitting/Searching Orders for a user

## Instructions

In order to use the main apis of the app, you have to acquire a 
JWT by following the steps below:

- Create a user by issuing a user creation request to the App's '/api/user/create' endpoint: 
```
POST /api/user/create 
{
    "username": "test",
    "password": "somepassword",
    "confirmPassword": "somepassword"
}
```
- Login using Spring's default '/login' endpoint:

```
POST /login 
{
    "username": "test",
    "password": "somepassword"
}
```

If the passed credentials are valid, you should expect a 200 OK with an Authorization header which looks like "Bearer \<data\>".<br> 
This "Bearer \<data\>" is a JWT and must be sent as an Authorization header for all other requests. If it's not present, endpoints should return 401 Unauthorized. If it's present and valid, the endpoints should function as normal.

## How To Run

Execute the following commands:
    
    ```
    $ mvn clean package
    ```
    
    ```
    $ java -jar target/ecommerce-app-0.0.1-SNAPSHOT.jar
    ```
    
## Documentation

Check out Swagger UI: http://localhost:8080/swagger-ui.html