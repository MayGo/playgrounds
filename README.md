# Playground REST API

This is a Spring Boot application that provides a REST API for managing play sites and kids in a playground.

## Functionality

The API provides the following functionality:

- Creating and managing play sites in the playground. Play sites consist of attractions such as double swings, carousel, slide, and a ball pit. Different combinations of attractions can be added to create different play sites.
- Adding kids to play sites. Each kid has a name, age, and ticket number. The API ensures that the maximum capacity of the play site is not exceeded.
- Enqueuing kids in a queue when a play site is full. The API registers queues on play sites and allows kids to wait in the queue if they accept.
- Removing kids from play sites or queues.
- Providing play site utilization at the current moment, measured in percentage.
- Providing the total visitor count during the current day on all play sites. Resets at midnight.

What is not in the scope:

- Authentication and authorization
- Pagination
- Persistence of data
- Error handling
- Logging
- Health Checks, Monitoring and metrics
- Caching
- Rate limiting
- API versioning
- Internationalization
- CORS

## Usage

To run the application, make sure you have Java and Maven installed. Then, navigate to the project directory and run the following command:

```
mvn spring-boot:run
```

The application will start running on [http://localhost:8080](http://localhost:8080).

## Testing

The application has a suite of unit and integration tests. You can run the tests using the following command:

```
mvn test
```

## API Documentation

API docs can be found at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
It provides detailed information about the available endpoints and their usage.
API docs json can be found at [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Configuration

The application can be configured by modifying the `application.properties` file in the `resources` directory. You can change settings such as the server port and database configuration.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more information.
