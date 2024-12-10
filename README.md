
# Directa24 Back-End Developer Challenge

## Challenge Overview

In this challenge, the REST API contains information about a collection of movies released after the year 2010, directed by acclaimed directors. Given the threshold value, the goal is to use the API to get the list of the names of the directors with most movies directed. Specifically, the list of names of directors with movie count strictly greater than the given threshold. The list of names must be returned in alphabetical order.

### API Access

To access the collection of movies, perform an HTTP GET request to:

```
https://directa24-movies.wiremockapi.cloud/api/movies/search?page=<pageNumber>
```

Where `<pageNumber>` is an integer denoting the page of the results to return.

For example, GET request to:

```
https://directa24-movies.wiremockapi.cloud/api/movies/search?page=2
```

Will return the second page of the collection of movies. Pages are numbered from 1, so in order to access the first page, you need to ask for page number 1. The response to such a request is a JSON with the following 5 fields:

- `page`: The current page of the results.
- `per_page`: The maximum number of movies returned per page.
- `total`: The total number of movies on all pages of the result.
- `total_pages`: The total number of pages with results.
- `data`: An array of objects containing movies returned on the requested page.

Each movie record has the following schema:

- `Title`: Title of the movie.
- `Year`: Year the movie was released.
- `Rated`: Movie rating.
- `Released`: Movie release date.
- `Runtime`: Movie duration time in minutes.
- `Genre`: Movie genre.
- `Director`: Movie director.
- `Writer`: Movie writer(s).
- `Actors`: Movie actors.

## Task Description

The goal is to implement the function:

```java
List<String> getDirectors(int threshold)
```

Where `threshold` is an integer denoting the threshold value for the number of movies a person has directed.

The function must return a list of strings denoting the names of the directors whose number of movies directed is strictly greater than the given threshold. The directors' names in the list must be ordered in alphabetical order.

### Sample Input

```java
4
```

### Sample Output

```java
["Martin Scorsese", "Woody Allen"]
```

### Alternative: Spring Boot solution

Alternatively, you can create a Spring Boot project with a REST endpoint:

```
/api/directors?threshold=X
```

Where `X` is the threshold number.

Sample URL: `/api/directors?threshold=4`

Sample JSON response:

```json
{
  "directors": ["Martin Scorsese", "Woody Allen"]
}
```

---

## Bonus Features Implemented

- **Error Handling:** Added robust error handling with retries for API requests.
- **Caching:** Implemented caching for frequently requested movie data to improve performance.
- **Asynchronous API Calls:** Used asynchronous calls for fetching multiple pages concurrently to speed up data retrieval.
- **Sorting and Filtering:** Directors are now returned in alphabetical order and can be filtered by movie count.
- **Logging and Monitoring:** Integrated logging to track API calls and errors.
- **CI/CD Pipeline:** Set up a GitHub Actions pipeline for continuous testing and deployment.
- **Unit and Integration Testing:** Added test cases to verify the correctness of the solution, including edge cases.
- **API Rate Limiting:** Implemented rate limiting to avoid exceeding the external API's request limit.
- **Security:** Added API authentication to ensure secure access to the director data.

---

## Project Setup and Usage

### Requirements

- Java 11 or higher
- Maven

### 1. Clone the repository

First, clone the repository to your local machine:

```bash
git clone https://github.com/ebdk/directors_api.git
cd directors_api
```

### 2. Build the application

To build the application, use Maven:

```bash
./mvnw clean install
```

### 3. Run the application

To run the application locally, use the following command:

```bash
./mvnw spring-boot:run
```

### 4. Access the API

Once the application is running, you can access the API endpoint:

```
GET /api/directors?threshold=<thresholdValue>
```

Example:

```
GET /api/directors?threshold=4
```

The response will be a JSON object containing the list of directors who have directed more than the threshold number of movies.

Example Response:

```json
{
  "directors": ["Martin Scorsese", "Woody Allen"]
}
```

---

## Unit and Integration Testing

To run the tests, use Maven:

```bash
./mvnw test
```

The tests will ensure that the functionality works as expected and that the API correctly fetches and processes the movie data.

### Test Cases:

1. **Test valid threshold input**: Ensure that the API correctly returns directors with movie counts greater than the specified threshold.
2. **Test invalid threshold input**: Check if the system handles cases where no directors meet the threshold.
3. **Test API failure handling**: Verify that the system correctly handles failures like timeouts or invalid responses from the external movie API.

---

## CI/CD Pipeline

We use GitHub Actions to automate the build, test, and deployment process. The `.github/workflows/ci-cd.yml` file contains the pipeline configuration.

### GitHub Actions Configuration:

```yaml
name: CI/CD Pipeline

on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Build with Maven
        run: mvn clean install
      - name: Run tests
        run: mvn test
```

This pipeline will automatically trigger on push or pull request events to the `main` branch, running the tests and building the application.

---

## Future Improvements (Optional)

- **Implement a Frontend Application**: Build a simple frontend application to display the list of directors dynamically based on the API.
- **Refactor for Scalability**: If the data set grows significantly, consider implementing pagination or filtering options for the API to allow users to request specific data more efficiently.
- **Security**: Implement OAuth2 or JWT-based authentication for secure API access.

---

## Contributing

Feel free to fork this repository, make improvements, and open a pull request. Contributions are always welcome!

