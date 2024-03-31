# Fujitsu Trial Task

## Setup Instructions

This project is built with Java 17.

### Prerequisites

- Java JDK 17

### Building the Project

Navigate to the project's root directory and run:

```
./gradlew build
```

### Running the Project

To start the application, execute:

```
./gradlew bootRun
```

## API Endpoints

### Delivery Fee Calculation

**GET** `/delivery-fee`

Calculates the delivery fee based on the specified city and vehicle type.

- **Query Parameters:**
    - `city` - the city where the delivery is to be made.
    - `vehicleType` - the type of vehicle used for delivery. 

### API Documentation

API documentation can also be found at http://localhost:8080/swagger-ui/index.html