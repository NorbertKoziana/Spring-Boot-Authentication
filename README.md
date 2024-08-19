# Spring Boot Authentication & Authorization

This is a Spring Boot backend project focused on providing authentication and authorization functionalities. 

## Features

- **User Registration & Login**: Users can register and log in using their email and password.
- **Email Verification**: Sends a verification email to users to confirm their email addresses.
- **OAuth2 Integration**: Supports login with Google and Facebook accounts.
- **Admin Controls**: Admin users can block other users.
- **Session Management**: Sessions are stored in Redis for scalability.
- **CSRF Protection**: Secures the application against Cross-Site Request Forgery attacks.
- **User Information Endpoint**: Provides details about the currently logged-in user.
- **Role-Based Access Control**: Endpoints are restricted based on user roles (Admin, User).
- **Unit & Integration Tests** Application is partly tested using JUnit 5, Mockito, MockMvc.

## Endpoints Overview

- **Authentication:**
  - `POST /auth/register` - Register a new user.
  - `POST /auth/login` - Log in with an existing account (email and password).
  - `GET /oauth2/authorization/google` - Log in with Google.
  - `GET /oauth2/authorization/facebook` - Log in with Facebook.
  - `POST /auth/logout` - Log out from your account.
  - `GET /auth/confirm` - Confirm email address after registration.
  
- **Password Management:**
  - `POST /user/password/reset` - Initiate password reset (sends token via email).
  - `POST /user/password/set` - Set a new password using the token sent to your email.

- **User Information:**
  - `GET /user/info` - Get information about the currently logged-in user.
  
- **Access Control:**
  - `GET /` - Public endpoint available to all users.
  - `GET /private` - Endpoint available only to logged-in users.
  - `GET /admin` - Endpoint available only to users with admin role.

## Running the Application

Running application locally requires additional configuration due to the use of OAuth2 and Google SMTP for sending emails.

### Prerequisites

- Java 17+
- Postman (for testing the endpoints)
- Docker (for running Redis and MySQL)

### Initial Configuration

If you choose to run the application locally make sure to replace following properties in `application.properties` file:

- **Google SMTP Configuration:**
  - in `spring.mail.username` replace *${SPRING_MAIL_USERNAME}* with your email username
  - in `spring.mail.password` replace *${SPRING_MAIL_PASSWORD}* with your email password

- **Google OAuth2 Configuration:**
  - `spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}`
  - `spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}`

- **Facebook OAuth2 Configuration:**
  - `spring.security.oauth2.client.registration.facebook.client-id=${FACEBOOK_CLIENT_ID}`
  - `spring.security.oauth2.client.registration.facebook.client-secret=${FACEBOOK_CLIENT_SECRET}`

### Using the Application (Walkthrough)

#### a) Register a New Account
- `POST /auth/register` - Create a local account. An email will be sent to confirm the provided email address.

Use the following JSON body in Postman to register a new account. It's recommended to use a real email address because you will need to confirm access to this email to log in. If you prefer not to use your real data, you might want to use a temporary email service (e.g. 10 Minute Mail):

```json
{
    "firstName": "Your Firstname",
    "lastName": "Your Lastname",
    "email": "Your Email Address",
    "password": "Your Password"
}
```

#### b) Log In
- `POST /auth/login` - Log in using a local account (email and password).
- `GET /oauth2/authorization/google` - Log in using a Google account.
- `GET /oauth2/authorization/facebook` - Log in using a Facebook account.
To log in using a local account, send a Postman request with the following JSON body:
```json
{
    "email":"YourEmailAddress",
    "password": "YourPassword"
}
```
For OAuth2 logins (Google or Facebook) I recommend to use your browser instead of Postman.

When logging in with an external account (using OAuth2) for the first time, a local account will be created without a password. You can later log in using the same or another external account associated with the same email address, or you can set a password (see section e) Reset or Change Password) to enable local login.

#### c) Log Out
- `POST /auth/logout` - Log out from your account.

#### d) Confirm Your Email Address
- `GET /auth/confirm` - Confirms that the email address provided during registration belongs to you. This is typically done by clicking the link sent to you in the verification email after registration.
![image](https://github.com/user-attachments/assets/da5982e7-3464-4b1c-a1a1-0ecd41b8acc2)


You will have 15 minutes to confirm your email. If you need the system to resend the confirmation email (e.g., if you accidentally deleted it), you can send a POST request to /auth/register with the same email as before, and a new confirmation email will be sent.

#### e) Reset or Change Password
- `POST /user/password/reset` - Initiate a password reset.
Send request  with the following JSON like this:
```json
{
    "email": "Your Email"
}
```
After that you should receive email with token that will let you set new password:
![image](https://github.com/user-attachments/assets/9b792478-cde0-474d-99db-eeecb0447948)

- `PATCH /user/password/set` - Set a new password using the token.
Send request  with the following JSON like this:
```json
{
    "token": "Token that you received on email",
    "newPassword": "Your New Password",
    "repeatPassword": "Your New Password"
}
```

#### f) View User Information
- `GET /user/info` - Get information about the currently logged-in user.

#### g) Test Role-Based Access Control
- `GET /` - Public endpoint available to everyone.
- `GET /private` - Accessible only to logged-in users.
- `GET /admin` - Accessible only to users with admin authority.

## Tests

Note that integration tests require Google SMTP and OAuth2 secrets, so they might fail if these are not specified in `application.properties`.
