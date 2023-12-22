## Getting Started

To run the API locally, follow these steps:

1. Clone the repository.
2. Install dependencies with `npm install`.
3. Configure the database and JWT secret in the `config.js` file.
4. Run the server with `npm run start`.

## Connect to database
```
//config.js
module.exports = {
    db: {
        host: 'YOUR_HOST',
        user: 'root',
        password: 'YOUR_PASSWORD',
        database: 'YOUR_DB_NAME',
    },
    jwtSecret: 'YOUR-SECRET-KEY',
};
```

## Endpoints

### Register
```
http://localhost:{PORT}/register
```
- **Endpoint:** `/register`
- **Method:** `POST`
- **Request Body:**
    ```json
    
    {
        "username": "example_user",
        "password": "example_password"
    }
       ```
- **Response:**
    - Success (201 Created):
        ```json
        {
            "message": "User registered successfully"
        }
        ```
    - Failure (400 Bad Request):
        ```json
        {
            "error": "Username already in use"
        }
        ```
### Login
```
http://localhost:{PORT}/Login
```
- **Endpoint:** `/login`
- **Method:** `POST`
- **Request Body:**
    ```json
    {
        "username": "example_user",
        "password": "example_password"
    }
    ```
- **Response:**
    - Success (200 OK):
        ```json
        {
            "token": "example_jwt_token"
        }
        ```
    - Failure (401 Unauthorized):
        ```json
        {
            "error": "Username or Password is incorrect"
        }
        ```
### Protected

- **Endpoint:** `/protected`
- **Method:** `GET`
- **Headers:**
    - `Authorization: Bearer example_jwt_token`
- **Response:**
    - Success (200 OK):
        ```json
        {
            "message": "Protected endpoint accessed successfully"
        }
        ```
    - Failure (401 Unauthorized):
        ```json
        {
            "error": "Unauthorized: Invalid token"
        }
        ```