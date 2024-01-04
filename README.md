# aws-ecs-demo

### Overview
This repository contains two spring boot microservices `users` and `products`. 
Both microservices expose health end points `users/api/v1/health` and `products/api/v1/health` respectively.

`health` API makes call to products health API and sends health status of products microservice as well.
`products` health API just sends its own heath status.

This repository is created to demonstrate deployment of microservices in AWS ECS using Fargate launch type.
1. Build and push Docker image to ECR or Docker Hub
2. Create ECS cluster
3. Create task definitions for users and products microservices
4. Create ECS service for users and products microservices

### Prerequisites
Ensure you have the following prerequisites installed and configured:

- **Java 17**
  ```bash
  brew install --cask adoptopenjdk/openjdk/adoptopenjdk17
  ```

- **Docker**
  ```bash
  brew install docker
  ```

### Clone the repository:
    git clone https://github.com/murariwalake/aws-ecs-demo.git
   

## Deployment in AWS ECS using fargate launch type
### Step 1: Build and push docker image service to docker hub
### For products service
1. Build the project:
    ```bash
    cd /aws-ecs-demo/product
    ./gradlew clean build
    ```

2. Build the Docker image:
    ```bash
    docker build -t murariwalake/products:1 .
    ```
3. Push the docker image to dockerhub
    ```bash
    docker push murariwalake/products:1
    ```

3. Run the Docker container in local:
    ```bash
    docker run -d -p 8080:8080 murariwalake/products:1
    ```
4. Access the service using below URL
    ```bash
    curl http://localhost:8080/products/api/v1/health
    ```

### For Users service
1. Build the project:
    ```bash
    cd /aws-ecs-demo/user
    ./gradlew clean build
    ```

2. Build the Docker image:
    ```bash
    docker build -t murariwalake/users:1 .
    ```
3. Push the docker image to dockerhub
    ```bash
    docker push murariwalake/users:1
    ```

3. Run the Docker container in local:
    ```bash
    docker run -d -p 8081:8080 murariwalake/users:1
    ```
4. Access the service using below URL
    ```bash
    curl http://localhost:8081/users/api/v1/health
    ```


### Step 2: Create ECS cluster `ecs-demo-cluster` in AWS console
selecting only fargate as launch type and leave all other settings as default.

### Step 3: Create ECS task definition `products-task-definition`
1. Launch type as `Fargate`.
2. CPU = 0.5 vCPU and Memory = 1 GB.
3. Mention the image URI of the Docker image `murariwalake/products:6`.
4. Container port mapping as `8080`.
5. Rest of the settings as default.

### Step 4: Create ECS service `products-service`
1. Select Compute options as `Launch type`.
2. Select family as `products-task-definition`.
3. Networking
   1. Create new security group `products-service-sg` allowing all traffic from anywhere (Will update this later to allow only any traffic from `ecs-demo-alb-sg`).
4. Load balancing
   1. Create new ALB `ecs-demo-alb`
   2. Create a new HTTP listener on port `8080`.
   3. Create a new target group `products-service-tg`.
   4. Health check path as `/products/api/v1/health` (missing this may cause ECS to report its health as unhealthy, resulting in continuous task creation/termination).
   5. Note: This ALB `ecs-demo-alb` will use security `products-service-sg` itself. We have 
5. Access the service using below URL
    ```bash
    curl http://<ALB DNS name>:8080/products/api/v1/health
    ```

### Step 5: Create ECS task definition `users-task-definition`
1. Launch type as `Fargate`.
2. CPU = 0.5 vCPU and Memory = 1 GB.
3. Mention the image URI of the Docker image `murariwalake/users:6`.
4. Container port mapping as `8080`.
5. Add environment variable `product.service.url=<ecs-demo-alb DNS name>:8080/products`
6. Rest of the settings as default.

### Step 6: Create ECS service `users-service`
1. Select Compute options as `Launch type`.
2. Select family as `users-task-definition`.
3. Networking
   1. Create new security group `users-service-sg` allowing all traffic from anywhere (Will update this later to allow only any traffic from `ecs-demo-alb-sg`).
4. Load balancing
   1. Select existing ALB `ecs-demo-alb`
   2. Select existing HTTP listener on port `8080`.
   3. Create a new target group `users-service-tg`.
   4. Path pattern as `/users/*` and evaluation order as `1`.
   5. Health check path as `/users/api/v1/health` (missing this may cause ECS to report its health as unhealthy, resulting in continuous task creation/termination).
5. Access the service using below URL
    ```bash
    curl http://<ALB DNS name>:8080/users/api/v1/health
    ```
