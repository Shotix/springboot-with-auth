# Spring Boot Backend with JWT Authentication

## Overview

This repository provides a starting point for a Spring Boot backend application with integrated JWT authentication using `UsernamePasswordAuthentication`. It includes basic user login and registration functionalities.

## Features

- **Spring Boot**: A robust framework for building Java applications.
- **JWT Authentication**: Secure authentication mechanism using JSON Web Tokens.
- **User Login/Registration**: Basic endpoints for user management.
- **MongoDB**: Integration with MongoDB for data storage.
- **Validation**: Input validation using Spring Boot's validation starter.
- **Actuator**: Monitoring and management of the application.
- **Security**: Spring Security for securing the application.
- **Logging**: Logstash encoder for structured logging.
- **Utilities**: Various utility libraries for common tasks.

## Technologies Used

- **Java 23**
- **Spring Boot 3.4.2**
- **Maven**
- **MongoDB**
- **JWT (JSON Web Token)**
- **Logstash**
- **Lombok**
- **MapStruct**
- **Apache Commons**
- **CycloneDX**

## Getting Started

### Prerequisites

- Java 23
- Maven
- MongoDB

### Installation

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

The application can be configured using the `application.properties` or `application.yml` file located in the `src/main/resources` directory.

### Endpoints

- **User Registration**: `POST /api/auth/register`
- **User Login**: `POST /api/auth/login`
- **Protected Endpoint**: `GET /api/protected` (Requires JWT token)

### Example Requests

#### User Registration

```sh
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"username": "testuser", "password": "password"}'
```

#### User Login

```sh
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"username": "testuser", "password": "password"}'
```

### Access Protected Endpoint

```sh
curl -X GET http://localhost:8080/api/protected -H "Authorization: Bearer <your_jwt_token>"
```

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0.html) file for details.

## Author

- **Tim Niklas Tenger**

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