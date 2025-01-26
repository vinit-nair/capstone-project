# capstone-project

# GoPay Wallet Android App

A modern digital wallet application that allows users to send money, request payments, and manage transactions securely.

## Features

- User Authentication (Login/Registration)
- Secure Session Management
- Transaction Management
  - Send Money
  - Request Money
  - Spot Cash
- Transaction History
- Real-time Balance Updates
- Profile Management

## Technical Requirements

- Android Studio Hedgehog | 2023.1.1 or higher
- JDK 11 or higher
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 35
- Kotlin version: 1.9.0 or higher

## Project Setup

1. Clone the repository:

bash
git clone [repository-url]

2. Open Android Studio and select "Open an Existing Project"

3. Navigate to the project directory and click "OK"

4. Wait for the Gradle sync to complete

5. Configure your backend URL in `NetworkModule.kt`:

kotlin
private const val BASE_URL = "http://YOUR_SERVER_IP:8081/"

## Architecture

The app follows MVVM (Model-View-ViewModel) architecture pattern and uses:

- **Data Binding**: For binding UI components to data sources
- **LiveData**: For observable data holder classes
- **ViewModel**: For managing UI-related data
- **Repository Pattern**: For abstracting data sources
- **Retrofit**: For API communication
- **OkHttp**: For HTTP client
- **Coroutines**: For asynchronous programming

## Project Structure

plaintext
app/
├── build.gradle.kts # Project level build file
├── src/
│ └── main/
│ ├── java/
│ │ └── com.example.gopaywallet/
│ │ ├── data/ # Data layer
│ │ │ ├── api/ # API interfaces
│ │ │ ├── model/ # Data models
│ │ │ └── repository/ # Repositories
│ │ ├── di/ # Dependency injection
│ │ ├── ui/ # UI layer
│ │ │ ├── auth/ # Authentication screens
│ │ │ ├── home/ # Home screen
│ │ │ └── transactions/ # Transaction screens
│ │ └── utils/ # Utility classes
│ └── res/ # Resources
kotlin

## Key Components

### Authentication
- `LoginActivity`: Handles user login
- `RegistrationActivity`: Handles new user registration
- `SessionManager`: Manages user session and tokens

### Home Screen
- `HomeActivity`: Main dashboard with balance and recent transactions
- `HomeViewModel`: Manages home screen data
- `TransactionAdapter`: Displays transaction list

### Transactions
- `TransactionsFragment`: Shows full transaction history
- `CreateTransactionActivity`: Handles new transactions
- `TransactionViewModel`: Manages transaction data

## Network Configuration

The app communicates with a Spring Boot backend server. Configure the connection:

1. For Local Development (Android Emulator):
private const val BASE_URL = "http://10.0.2.2:8081/"

2. For Physical Device (Same Network):
private const val BASE_URL = "http://YOUR_COMPUTER_IP:8081/"

3. For Production:
private const val BASE_URL = "https://your-production-server.com/"

## Security

- Encrypted SharedPreferences for sensitive data
- JWT token-based authentication
- HTTPS for production environments
- Input validation and sanitization

## Testing

Run tests using:

## Building

Generate debug APK:

bash
./gradlew assembleDebug

Generate release APK:

bash
./gradlew assembleRelease

## Dependencies

```kotlin
dependencies {
    // AndroidX - Core Android components and backwards compatibility
    implementation(libs.androidx.core.ktx)        // Kotlin extensions for core Android features
    implementation(libs.androidx.appcompat)       // Backward compatibility for newer Android features
    implementation(libs.material)                 // Material Design components
    implementation(libs.androidx.activity)        // Modern Android Activity APIs
    implementation(libs.androidx.constraintlayout)// Flexible layout system

    // Lifecycle - Manage UI state and handle Android lifecycle events
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")    // Lifecycle-aware components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")  // UI state management
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")   // Observable data holder

    // Retrofit & OkHttp - Network communication
    implementation("com.squareup.retrofit2:retrofit:2.9.0")             // REST API client
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")       // JSON parsing
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")   // Network logging

    // Security - Data protection
    implementation("androidx.security:security-crypto:1.1.0-alpha06")   // Encrypted data storage

    // UI Components - Enhanced user interface
    implementation("androidx.recyclerview:recyclerview:1.3.2")          // Efficient list display
    implementation("com.google.android.material:material:1.9.0")        // Material Design components
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0") // Pull-to-refresh
}
```

## Common Issues

1. Connection Timeout
   - Verify server is running
   - Check IP address configuration
   - Ensure device and server are on same network

2. Authentication Failures
   - Check token expiration
   - Verify credentials
   - Clear app data and retry

3. Transaction Issues
   - Verify sufficient balance
   - Check network connection
   - Validate input data

## Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## License

[Your License Here]

## Contact

[Your Contact Information]


## Screenshots

### Authentication Screens
![Login Screen](screenshots/login_screen.png)
- Clean and intuitive login interface with email and password fields
- "Forgot Password?" link for password recovery
- "Create Account" option for new users

![Registration Screen](screenshots/registration_screen.png)
- Full name, email, phone number, and password fields
- Password strength indicators
- Terms and conditions acceptance

### Main Screens
![Home Dashboard](screenshots/home_screen.png)
- Prominent balance display at the top
- Quick action buttons for Send, Request, and Spot Cash
- Recent transactions with transaction type icons

![Transaction History](screenshots/transactions_screen.png)
- Chronological list of all transactions
- Color-coded amounts (green for received, red for sent)
- Transaction details including date, time, and status

## API Documentation

### Authentication Endpoints

#### Login
```http
POST /auth/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123"
}

Response:
{
    "token": "jwt_token_here",
    "user": {
        "id": 1,
        "fullName": "John Doe",
        "email": "user@example.com"
    }
}
```

#### Register
```http
POST /auth/register
Content-Type: application/json

{
    "fullName": "John Doe",
    "email": "user@example.com",
    "phoneNumber": "+1234567890",
    "password": "password123"
}

Response:
{
    "token": "jwt_token_here",
    "user": {
        "id": 1,
        "fullName": "John Doe",
        "email": "user@example.com"
    }
}
```

### Transaction Endpoints

#### Get User Transactions
```http
GET /api/transactions?userId={userId}&page={page}&pageSize={pageSize}
Authorization: Bearer {jwt_token}

Response:
{
    "content": [
        {
            "id": 1,
            "title": "Payment to Jane",
            "amount": 100.00,
            "type": "SEND",
            "dateTime": "2024-01-26T10:30:00"
        }
    ],
    "totalPages": 1,
    "totalElements": 1
}
```

## Backend Server Setup

### Prerequisites
- Java 17 or higher
- Maven 3.8.x or higher
- MySQL 8.0 or higher

### Setup Steps

1. Clone the backend repository:
```bash
git clone https://github.com/vinit-nair/be_capstone_project.git
```

2. Configure database in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gopay_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Create the database:
```sql
CREATE DATABASE gopay_db;
```

4. Build and run the application:
```bash
mvn clean install
java -jar target/gopay-backend-1.0.0.jar
```

## Code Examples

### Authentication Implementation
```kotlin
// Login example
private fun performLogin(email: String, password: String) {
    viewModelScope.launch {
        try {
            val response = userRepository.login(email, password)
            sessionManager.saveAuthToken(response.token)
            sessionManager.saveUserId(response.user.id)
            navigateToHome()
        } catch (e: Exception) {
            handleError(e)
        }
    }
}
```

### Transaction Creation
```kotlin
// Create new transaction
private fun createTransaction(amount: BigDecimal, recipientId: Long) {
    viewModelScope.launch {
        try {
            val request = TransactionRequest(
                title = "Payment to User",
                amount = amount,
                type = TransactionType.SEND,
                recipientId = recipientId
            )
            val transaction = transactionRepository.createTransaction(
                userId = sessionManager.getUserId(),
                request = request
            )
            handleSuccess(transaction)
        } catch (e: Exception) {
            handleError(e)
        }
    }
}
```

## Troubleshooting Guide

### Android App Issues

#### 1. Build Failures
```bash
# Clean and rebuild the project
./gradlew clean
./gradlew build
```

#### 2. Network Issues
- Check `NetworkModule.kt` configuration
- Verify backend server is running
- Check device/emulator network connection

#### 3. Database Issues
```sql
-- Reset user table
TRUNCATE TABLE users;

-- Reset transactions table
TRUNCATE TABLE transactions;
```

### Common Error Solutions

1. JWT Token Expired
```kotlin
// Implement token refresh
private fun refreshToken() {
    val refreshToken = sessionManager.getRefreshToken()
    // Call refresh token API
}
```

2. Network Timeout
```kotlin
// Increase timeout in NetworkModule
.connectTimeout(60, TimeUnit.SECONDS)
.readTimeout(60, TimeUnit.SECONDS)
```

3. Data Binding Issues
```kotlin
// Enable data binding in build.gradle.kts
buildFeatures {
    dataBinding = true
}
```

## Performance Optimization Tips

1. Image Loading
```kotlin
// Use Glide for efficient image loading
Glide.with(context)
    .load(imageUrl)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .into(imageView)
```

2. RecyclerView Optimization
```kotlin
// Implement efficient item diffing
class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) =
        oldItem.id == newItem.id
    
    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) =
        oldItem == newItem
}
```