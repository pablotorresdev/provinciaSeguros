# Weather Microservice

Author: Pablo Torres

Contact Email: pablotorresdev@gmail.com

## Project Description

This project implements a microservice that provides weather information through a REST API.
The weather data is obtained from the public APIs provided by AccuWeather. The
application retrieves the weather data, stores it in an H2 database, and returns the relevant information in JSON format.
The database is used as a cache and to avoid exceeding the API rate limits.

The microservice is built using Java 1.8, Spring Boot, Maven, and a h2 relational database.

- Java 1.8 or higher installed on your machine. If you haven't installed Java yet, you can download it from the official
  website: https://www.oracle.com/java/technologies/javase-downloads.html
- Maven installed on your machine. If you haven't installed Maven yet, you can download it from the official website: https://maven.apache.org/download.cgi

## Project Setup

To set up and configure the project, follow these steps:

1. Clone the Git repository: https://github.com/pablotorresdev/provinciaSeguros.git
2. Ensure Java 1.8 and Maven is installed on your system.
3. Build the project using Maven:
   ```
   mvn clean install
   ```

## Database Configuration

The microservice uses an H2 in-memory database. No additional configuration is required.

## Running the Application

To run the application, follow these steps:

1. Navigate to the project directory.
2. Run the following command:
   ```
   mvn spring-boot:run
   ```
3. The microservice will start, and the REST API will be available at `http://localhost:8080`.

## API Endpoints

The microservice provides the following REST API endpoints:

- `GET /weather`: Retrieves DB weather information for all locations.
- `GET /weather/{location}`: Retrieves DB weather information for a specific location.
- `GET /weather/current/{location}`: Retrieves current weather information for a specific location, even from AcuWeather or from DB.

The responses from the API endpoints are returned in JSON format and contain relevant weather information.

## Documentation

The code is documented using Javadoc. You can generate the code documentation using the following command:

```
mvn javadoc:javadoc
```

The generated documentation can be found in the `target/site/apidocs` directory.

## Unit Tests

The project includes unit tests to ensure the correctness of the implementation. The tests cover various scenarios, including edge cases and input/output validation.

To run the unit tests, use the following command:

```
mvn test
```

## Docker Setup and Deployment

### Prerequisites

- Java 1.8 or higher installed on your machine. If you haven't installed Java yet, you can download it from the official
  website: https://www.oracle.com/java/technologies/javase-downloads.html
- Maven installed on your machine. If you haven't installed Maven yet, you can download it from the official website: https://maven.apache.org/download.cgi
- Docker installed on your machine. If you haven't installed Docker yet, you can download it from the official website: https://www.docker.com/get-started

### Docker Configuration

1. Clone the Git repository to your local machine:

```
git clone https://github.com/pablotorresdev/provinciaSeguros.git
```

2. Navigate to the project directory:

```
cd .\provincia-seguros
```

3. Build the project using Maven:

```
mvn clean install
```

4. Build the Docker image:

```
docker build -t provincia-seguros .
```

This command builds a Docker image based on the provided Dockerfile.

### Running the Docker Container

1. Run the Docker container:

```
docker run -p 8080:8080 provincia-seguros
```

The container runs your Spring Boot application and maps port 8080 from the container to port 8080 on the host machine.

2. Access the application:

Open a web browser and navigate to `http://localhost:8080` to access the application.

### Additional Notes

- Make sure to update the application's configuration (e.g., API keys, database connection) according to your environment before building the Docker image.
- For more advanced Docker configurations (e.g., multi-stage builds, container orchestration), refer to the Docker documentation.

## Additional Details

- The microservice uses the AccuWeather API to fetch weather data. You need to sign up on the AccuWeather Developer Portal and obtain an API key to use the API. Update the
  configuration file with your API key.
- The microservice uses Spring RestTemplate to invoke the AccuWeather API. You can customize the RestTemplate configuration if needed.
- The H2 database is an in-memory database and does not persist data across application restarts.
- The project structure follows the recommended Spring Boot conventions.
- Additional configuration and customization options can be found in the application.properties file.

Please reach out to me if you have any questions or need further assistance.

Thank you.

