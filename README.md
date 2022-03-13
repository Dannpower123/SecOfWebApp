# Security of Web Applications REST-API

Java REST-API for Security of Web Applications

# Run command

To run the backend, you'll have to type in the following command:

```
./mvnw spring-boot:run
```

# Security

Secured by JWT. How to deal with it?
Well, First sign in with Admin Account:
```
POST https://localhost:9966/pvh/api/auth/signin

{
  "password": "testtest",
  "username": "test"
}
```

You'll get an Bearer Token, which you will need to provide to get access on Endpoints.
With that Token you are able to sign up Customers:

```
POST https://localhost:9966/pvh/api/customer

{
  "emasil": "test1@2ffst.com",
  "nachname": "dall",
  "vorname": "daniel"
}
```

Or get a Customer
```
GET https://localhost:9966/pvh/api/customer/1
```

Or delete a Customer (only with admin account):

```
DELETE https://localhost:9966/pvh/api/customer/1
```



