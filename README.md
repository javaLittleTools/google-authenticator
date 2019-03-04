# API

## Generate New User

- **GET** /qrcode/{email}

  return a rqcode for scan

## Show Verify Code

- **GET** /totp/{email}

  return a map contains ```currentCode``` and `nextCode`  

  **if has error**, it will return a map contains `error`

## Add User

- **POST** /user

  ```java
  @Data
  @Entity
  @Table(name = "user")
  public class User {
  
      @Id()
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Integer id;
  
      private String email;
  
      private String secret;
  }
  ```

- this method has no return 

## Get User's Info

- **GET** /user/{email}

  return user's info

## Get User's Secret Key

- **GET** /user/secret/{email}

  return user's secret key

## Verify Code

- **GET** /verify/{email}/{code}

- **POST** /verify

  ```java
  @Data
  public class Verify {
  
      private String email;
  
      private Long code;
  }
  ```