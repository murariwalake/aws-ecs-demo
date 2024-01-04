# Products Micro-service

## Overview
This repository contains two spring boot microservices `users` and `products` utilizing Gradle (`gradlew`). 
Both microservices expose health end points `users/api/v1/health` and `products/api/v1/health` respectively.

users health API also makes call to products health API and sends health status of products microservice as well.


