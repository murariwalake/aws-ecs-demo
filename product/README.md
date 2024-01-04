# Products Micro-service

## Overview
This repository contains two spring boot microservices `users` and `products` utilizing Gradle (`gradlew`). 
Both microservices expose health end points `users/api/v1/health` and `products/api/v1/health` respectively.

users health API also makes call to products health API and sends health status of products microservice as well.

This repository is created to demonstrate deployment of microservices in AWS ECS using Fargate launch type.

## Prerequisites
Ensure you have the following prerequisites installed and configured:

- **Java 17**
  ```bash
  brew install --cask adoptopenjdk/openjdk/adoptopenjdk17
  ```

- **Docker**
  ```bash
  brew install docker
  ```

## Deployment in Local Environment

Follow these steps to deploy the application locally:

1. **Clone the repository:**
    ```bash
    git clone <git repo url>
    cd product
    ```

2. **Build the project:**
    ```bash
    ./gradlew clean build
    ```

3. **Build the Docker image:**
    ```bash
    docker build -t murariwalake/products:1 .
    ```

4. **Run the Docker container:**
    ```bash
    docker run -d -p 8080:8080 murariwalake/products:1
    ```

### Health Check

Make a health check request using `curl`:
```bash
curl http://localhost:8080/products/api/v1/health
```

## Deployment in AWS ECS using fargate launch type
1. **Build and push Docker image to ECR or Docker Hub**
2. **Create ECS cluster:** (Skip if already created one)
    - Select only Fargate launch type and leave all other settings as default.
3. **Create ECS task definition `products`:**
    1. Mention the image URI of the Docker image `murariwalake/products:6`.
    2. Mention container port mapping as `8080`.
    3. Leave all other settings as default.
4. **Create ECS service:**
    1. Select Compute options as `Launch type`.
    2. Launch type as `Fargate`.
    3. Application type as `Service`.
    4. Select task definition `products` created in step 3.
    5. Under network configuration, create/select existing security group. This should allow HTTP traffic from ALB on port 8080.
    6. Create a new ALB `ecs-demo-elb` (ensure SG of ALB allows HTTP traffic from the internet).
        1. Create a new HTTP listener on port `8080`.
        2. Create a new target group `products-service-tg`.
        3. Health check path as `/products/api/v1/health` (missing this may cause ECS to report its health as unhealthy, resulting in continuous task creation/termination).
    7. Create the service by clicking on the `Create` button.
    8. Use ALB DNS name to access the service `<ALB DNS name>:8080/products/api/v1/health`.