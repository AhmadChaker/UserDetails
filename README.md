# Spring Boot User Details Application

This web application can be used to query and update user details.

## Quick start

To quickly dive into using the application browse to the project base and type in: 
```maven
mvn spring-boot:run
```
A Postman collection has been included in the postman folder.

## Documentation
To view the api documentation in swagger-ui [click here](http://localhost:8080/swagger-ui.html)

## Key Assumptions
- We are inferring that there is no need to store historical information regarding users and addresses i.e. the database tables are only to contain the latest details for the user, as a result there is a 1-1 mapping between Users and Addresses. This will easily allow the Users or the Address table to be joined with other tables in the future
- Addresses are mandatory as are most other properties (documented in the swagger-ui link)
- A UserId and EmployeeId are taken to be synonyms.
- Item 3) in the spec sheet specifies to "validate the user id is numeric", however for practical reasons it makes more sense for this to be restricted to an integer so this is restricted.
- Multiple authorisation roles have been built in. For simplicity the usernames and roles are in the same table, in practice this would not be the case there would be a many to many relationship between usernames and roles.
- In a standard production deployment the site would be secured with via a certificate (HTTPS), in this case to prevent certificate errors the site is is hosted on http

## Credentials
Non Privileged user: 

achaker Mars2021

Privileged user: 

achakerAdmin Jupiter2022