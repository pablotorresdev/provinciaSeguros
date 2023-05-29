# Weather Microservice

Author: Pablo Torres

Contact Email: pablotorresdev@gmail.com

## Project Description

This project implements a microservice that provides weather information through a REST API. The weather data is obtained from the public APIs provided by AccuWeather. The application retrieves the weather data, stores it in an H2 database, and returns the relevant information in JSON format.

The microservice is built using Java 1.8, Spring Boot, Maven, and a h2 relational database.

## Project Setup

To set up and configure the project, follow these steps:

1. Clone the Git repository: https://github.com/pablotorresdev/provinciaSeguros.git
2. Ensure Java 1.8 is installed on your system.
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

## Additional Details

- The microservice uses the AccuWeather API to fetch weather data. You need to sign up on the AccuWeather Developer Portal and obtain an API key to use the API. Update the configuration file with your API key.
- The microservice uses Spring RestTemplate to invoke the AccuWeather API. You can customize the RestTemplate configuration if needed.
- The H2 database is an in-memory database and does not persist data across application restarts.
- The project structure follows the recommended Spring Boot conventions.
- Additional configuration and customization options can be found in the application.properties file.

Please reach out to me if you have any questions or need further assistance.

Thank you.

