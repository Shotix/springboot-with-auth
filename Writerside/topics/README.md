# Spring Boot Backend with JWT Authentication

## Overview

This repository offers a starting point for a Spring Boot backend application featuring JWT authentication. It includes user login, registration processes, and secure token management. The project uses Spring Security along with a dual token system.

## Features

- **Spring Boot**: Robust framework for building Java applications.
- **JWT Authentication**: Secure mechanism using JSON Web Tokens.
- **User Login/Registration**: Endpoints to manage user credentials.
- **MongoDB Integration**: Data storage powered by MongoDB.
- **Input Validation**: Managed through Spring Boot's validation starter.
- **Actuator**: Application monitoring and management.
- **Spring Security**: Securing endpoints and controlling access.
- **Structured Logging**: Logstash encoder for improved log management.
- **Utility Libraries**: Common utilities for simplified coding tasks.

## Technologies Used

- **Java 23** (Note: currently, only features up to Java 17 are utilized)
- **Spring Boot 3.4.2**
- **Maven**
- **MongoDB**
- **JWT (JSON Web Token)**
- **Logstash**
- **Lombok**
- **MapStruct**
- **Apache Commons**
- **CycloneDX**

## Prerequisites

- Java 23 (or Java 17 for current feature support)
- Maven
- MongoDB

## Setup Instructions

### Install and Configure MongoDB

Download and install MongoDB Community from the official website: [](https://www.mongodb.com/try/download/community).  
It is recommended to create a new database and user via MongoDB Compass. Add the database name and credentials to the \`application.properties\` or \`application.yml\` file.

### Installation Steps

1. **Clone the repository**:
    ```sh
    git clone https://github.com/yourusername/springboot-with-auth.git
    cd springboot-with-auth
    ```

2. **Build the project**:
    ```sh
    mvn clean install
    ```

3. **Run the application**:
    ```sh
    mvn spring-boot:run
    ```

### Configuration

Configure the application in the `src/main/resources/application.properties` (or `application.yml`) file. 
This file controls the connection details, JWT secrets, and other application-specific parameters.

### JWT Authentication and Token Management

The authentication system uses two tokens for security:
- **authToken**: Used for accessing protected endpoints.
- **refreshToken**: Stored as a secure cookie; it is used to generate a new `authToken` when the current one expires. This token is invalidated after use and regenerated. 

Note: Update JWT secrets using the provided `JwtSecretGenerator` utility before deployment.

### Guide for Users

1. **Change the JWT Secrets**:
   - Use the `JwtSecretGenerator` to generate new secrets.
   - Update the secrets in the `application.properties` or `application.yml` file.

    ```sh
    java -jar JwtSecretGenerator.jar
    ```

2. **Switch Cookie Settings to Secure**:
   - Ensure that the `refreshToken` cookie settings are set to `secure`. Currently this needs to be done manually in the specific code blocks.

### Endpoints

- **User Registration**: `POST /api/v1/users/register`
- **User Login**: `POST /api/v1/user/login`
- **Refresh Token**: `POST /api/v1/auth/refresh` (Required valid refresh token)
- **Protected Endpoint**: `GET /api/v1/users/me` (Requires JWT token --> Gets personal user data)

### Example Requests

#### User Registration

```sh
curl -X POST http://localhost:8080/api/v1/users/register -H "Content-Type: application/json" -d '{"username": "testuser", "password": "password"}'
```

#### User Login

```sh
curl -X POST http://localhost:8080/api/v1/users/login -H "Content-Type: application/json" -d '{"username": "testuser", "password": "password"}'
```

#### Refresh Token

```sh
curl -X POST http://localhost:8080/api/v1/auth/refresh -H "Cookie: refreshToken=<your_refresh_token>"
```

#### Access Protected Endpoint

```sh
curl -X GET http://localhost:8080/api/v1/users/me -H "Authorization: Bearer <your_jwt_token>"
```

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0.html) file for details.

## Author

- [**Tim Niklas Tenger**](https://www.linkedin.com/in/tim-niklas-tenger-5b44231b4/)

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for any changes.

## Acknowledgements

- [Spring Boot](https://spring.io/projects/spring-boot)
- [JWT](https://jwt.io/)
- [MongoDB](https://www.mongodb.com/)
- [Logstash](https://www.elastic.co/logstash)
- [Lombok](https://projectlombok.org/)
- [MapStruct](https://mapstruct.org/)
- [Apache Commons](https://commons.apache.org/)
- [Backend Architecture Inspired by Jojoooo1](https://github.com/Jojoooo1/project-assignment)
