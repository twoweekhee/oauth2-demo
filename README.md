## 🔐 OAuth2 Demo Project
This project is an OAuth2 social login demo application built with Spring Boot. It supports Google, Kakao, and Naver social login with JWT-based authentication system.

### 🚀 Key Features
- 🔑 Social Login (Google, Kakao, Naver)
- 🎫 JWT-based Authentication (Access Token, Refresh Token)
- 🔒 Security Configuration (CORS, CSRF)
- 🍪 Cookie-based Token Management
- 📝 User Terms Agreement Process

### 🛠 Tech Stack
Java 21
Spring Boot 3.4.5
Spring Security
Spring OAuth2 Client
JWT (jjwt 0.12.3)
MySQL
Redis
QueryDSL
Lombok

## 🔐 OAuth2 & Security Implementation Details

### OAuth2 Implementation
1. Social Login Configuration
Configure each social login provider (Google, Kakao, Naver) using Spring Security OAuth2 Client
Set required scope and user-info-uri for each provider
Handle social login user information through CustomOAuth2UserService
2. Authentication Process
Handle OAuth2 authentication requests through CustomAuthorizationRequestResolver
Issue JWT tokens on authentication success via OAuth2AuthenticationSuccessHandler
Handle authentication failures through OAuth2AuthenticationFailureHandler
3. Token Management
Implement JWT-based Access Token and Refresh Token
Store and manage Refresh Tokens using Redis
Secure token transmission through HttpOnly cookies

### Security Implementation
1. Security Configuration
```
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/private/**").authenticated()
                .anyRequest().denyAll())
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(endpoint -> endpoint
                    .authorizationRequestResolver(customAuthorizationRequestResolver))
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService))
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler))
            .build();
    }
}
```
2. Authentication Filters
LoginAuthFilter: JWT token validation and authentication handling
LogoutAuthFilter: Logout processing and token removal
3. CORS Configuration
```
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration);
    return source;
}
```
4. Security Enhancements
Stateless session management for reduced server load
CSRF protection disabled (REST API based)
XSS attack prevention through HttpOnly cookies
Enhanced security through Redis-based token management

## ⚙️ Environment Configuration
Required Environment Variables
properties
### OAuth2 Client Credentials
```
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
KAKAO_CLIENT_ID=your_kakao_client_id
KAKAO_CLIENT_SECRET=your_kakao_client_secret
NAVER_CLIENT_ID=your_naver_client_id
NAVER_CLIENT_SECRET=your_naver_client_secret
```
### JWT Keys
```
ACCESS_TOKEN_KEY=your_access_token_key
REFRESH_TOKEN_KEY=your_refresh_token_key
```
### Database
```
MYSQL_URL=jdbc:mysql://localhost:3306/oauth2_demo
MYSQL_USER_NAME=your_mysql_username
MYSQL_PASSWORD=your_mysql_password
```
## 🏗 Project Structure
```
src/main/java/com/example/oauth2demo/
├── auth/                 # Authentication related code
│   ├── application/     # Authentication service logic
│   ├── domain/         # Domain models
│   ├── dto/            # Data transfer objects
│   └── infrastructure/ # Infrastructure related code
├── common/             # Common configuration and utilities
├── terms/             # Terms related code
└── user/              # User related code
```

## 🔄 Authentication Process
1. Social Login Process
Client requests social login (/api/public/oauth2/{provider})
Redirect to OAuth2 authentication page
User authenticates with social login provider
Redirect to callback URL on successful authentication
Server obtains and processes user information
Issue JWT tokens and store in cookies
Redirect to main page
2. User Registration Process
Check for new user after successful social login
Redirect to terms agreement page for new users
Save user information:
Social account information (email, social ID, provider)
User role (ROLE_USER)
Save terms agreement information
Redirect to main page after registration completion
3. Logout Process
- Client requests logout (/api/public/logout)
- LogoutAuthFilter processes the request
- Delete Refresh Token from Redis
- Delete Access Token and Refresh Token cookies:
```
Cookie deletedAccessCookie = cookieProvider.generateDeletedAccessTokenCookie();
Cookie deletedRefreshCookie = cookieProvider.generateDeletedRefreshTokenCookie();
response.addCookie(deletedAccessCookie);
response.addCookie(deletedRefreshCookie);
```
- Clear SecurityContext
- Redirect to login page

4. Token Refresh Process
When Access Token expires (30 minutes)
Request new Access Token with Refresh Token
Validate Refresh Token in Redis
Issue new Access Token if valid
Store new Access Token in cookie
Maintain existing Refresh Token (2 days)

## 🔒 Security Settings
CORS Configuration: Allow cross-origin requests for API endpoints
CSRF Protection: Disabled for REST API based architecture
Session Management: Uses stateless approach
Token Management: Uses HttpOnly cookies

## 🧪 Testing
```
./gradlew test
```

### 🚀 How to Run
Set environment variables
Start MySQL and Redis servers
Run the application:
```
./gradlew bootRun
```
### 📝 API Documentation
Swagger UI: http://localhost:8080/api/swagger-ui/index.html
OpenAPI Documentation: http://localhost:8080/api/api-docs

### 🔑 Token Expiration Times
Access Token: 30 minutes
Refresh Token: 2 days

### 📋 Prerequisites
Java 21 or higher
MySQL 8.0 or higher
Redis 6.0 or higher
Gradle 7.0 or higher

### 🤝 Contributing
Fork the repository
Create your feature branch (git checkout -b feature/AmazingFeature)
Commit your changes (git commit -m 'Add some AmazingFeature')
Push to the branch (git push origin feature/AmazingFeature)
Open a Pull Request

### 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

### 📞 Contact
Author: Your Name
Email: your.email@example.com
Project Link: https://github.com/yourusername/oauth2-demo

---

# 🔐 OAuth2 Demo Project

이 프로젝트는 Spring Boot를 사용한 OAuth2 소셜 로그인 데모 애플리케이션입니다. Google, Kakao, Naver 소셜 로그인을 지원하며, JWT 기반의 인증 시스템을 구현했습니다.

## 🚀 주요 기능

- 🔑 소셜 로그인 (Google, Kakao, Naver)
- 🎫 JWT 기반 인증 (Access Token, Refresh Token)
- 🔒 보안 설정 (CORS, CSRF)
- 🍪 쿠키 기반 토큰 관리
- 📝 사용자 약관 동의 프로세스

## 🛠 기술 스택

- Java 21
- Spring Boot 3.4.5
- Spring Security
- Spring OAuth2 Client
- JWT (jjwt 0.12.3)
- MySQL
- Redis
- QueryDSL
- Lombok

## 🔐 OAuth2 & Security 구현 상세

### OAuth2 구현 방식

1. **소셜 로그인 설정**
   - Spring Security OAuth2 Client를 사용하여 각 소셜 로그인 제공자(Google, Kakao, Naver) 설정
   - 각 제공자별로 필요한 scope와 user-info-uri 설정
   - CustomOAuth2UserService를 통해 소셜 로그인 사용자 정보 처리

2. **인증 프로세스**
   - CustomAuthorizationRequestResolver를 통한 OAuth2 인증 요청 처리
   - OAuth2AuthenticationSuccessHandler에서 인증 성공 시 JWT 토큰 발급
   - OAuth2AuthenticationFailureHandler에서 인증 실패 처리

3. **토큰 관리**
   - JWT 기반의 Access Token과 Refresh Token 구현
   - Redis를 사용한 Refresh Token 저장 및 관리
   - HttpOnly 쿠키를 통한 안전한 토큰 전송

### Security 구현 방식

1. **보안 설정**
   ```java
   @Configuration
   public class SecurityConfig {
       @Bean
       public SecurityFilterChain securityFilterChain(HttpSecurity http) {
           return http
               .cors(cors -> cors.configurationSource(corsConfigurationSource()))
               .csrf(AbstractHttpConfigurer::disable)
               .formLogin(AbstractHttpConfigurer::disable)
               .sessionManagement(session -> 
                   session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(authorize -> authorize
                   .requestMatchers("/public/**").permitAll()
                   .requestMatchers("/private/**").authenticated()
                   .anyRequest().denyAll())
               .oauth2Login(oauth2 -> oauth2
                   .authorizationEndpoint(endpoint -> endpoint
                       .authorizationRequestResolver(customAuthorizationRequestResolver))
                   .userInfoEndpoint(userInfo -> userInfo
                       .userService(customOAuth2UserService))
                   .successHandler(oAuth2AuthenticationSuccessHandler)
                   .failureHandler(oAuth2AuthenticationFailureHandler))
               .build();
       }
   }
   ```

2. **인증 필터**
   - LoginAuthFilter: JWT 토큰 검증 및 인증 처리
   - LogoutAuthFilter: 로그아웃 처리 및 토큰 삭제

3. **CORS 설정**
   ```java
   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration configuration = new CorsConfiguration();
       configuration.setAllowedOrigins(Arrays.asList("*"));
       configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
       configuration.setAllowedHeaders(Arrays.asList("*"));
       configuration.setAllowCredentials(true);
       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
       source.registerCorsConfiguration("/api/**", configuration);
       return source;
   }
   ```

4. **보안 강화**
   - Stateless 세션 관리로 서버 부하 감소
   - CSRF 보호 비활성화 (REST API 기반)
   - HttpOnly 쿠키를 통한 XSS 공격 방지
   - Redis를 사용한 토큰 관리로 보안성 강화

## ⚙️ 환경 설정

### 필수 환경 변수

```properties
# OAuth2 Client Credentials
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
KAKAO_CLIENT_ID=your_kakao_client_id
KAKAO_CLIENT_SECRET=your_kakao_client_secret
NAVER_CLIENT_ID=your_naver_client_id
NAVER_CLIENT_SECRET=your_naver_client_secret

# JWT Keys
ACCESS_TOKEN_KEY=your_access_token_key
REFRESH_TOKEN_KEY=your_refresh_token_key

# Database
MYSQL_URL=jdbc:mysql://localhost:3306/oauth2_demo
MYSQL_USER_NAME=your_mysql_username
MYSQL_PASSWORD=your_mysql_password
```

## 🏗 프로젝트 구조

```
src/main/java/com/example/oauth2demo/
├── auth/                 # 인증 관련 코드
│   ├── application/     # 인증 서비스 로직
│   ├── domain/         # 도메인 모델
│   ├── dto/            # 데이터 전송 객체
│   └── infrastructure/ # 인프라 관련 코드
├── common/             # 공통 설정 및 유틸리티
├── terms/             # 약관 관련 코드
└── user/              # 사용자 관련 코드
```

## 🔄 인증 프로세스

### 1. 소셜 로그인 과정
1. 클라이언트에서 소셜 로그인 요청 (`/api/public/oauth2/{provider}`)
2. OAuth2 인증 페이지로 리다이렉트
3. 사용자가 소셜 로그인 제공자에서 인증
4. 인증 성공 시 콜백 URL로 리다이렉트
5. 서버에서 사용자 정보 획득 및 처리
6. JWT 토큰 발급 및 쿠키에 저장
7. 메인 페이지로 리다이렉트

### 2. 회원가입 과정
1. 소셜 로그인 성공 후 신규 사용자 확인
2. 신규 사용자인 경우 약관 동의 페이지로 리다이렉트
3. 사용자 정보 저장
   - 소셜 계정 정보 (이메일, 소셜 ID, 제공자)
   - 사용자 역할 (ROLE_USER)
4. 약관 동의 정보 저장
5. 회원가입 완료 후 메인 페이지로 리다이렉트

### 3. 로그아웃 과정
1. 클라이언트에서 로그아웃 요청 (`/api/public/logout`)
2. LogoutAuthFilter에서 요청 처리
3. Redis에서 Refresh Token 삭제
4. Access Token과 Refresh Token 쿠키 삭제
   ```java
   Cookie deletedAccessCookie = cookieProvider.generateDeletedAccessTokenCookie();
   Cookie deletedRefreshCookie = cookieProvider.generateDeletedRefreshTokenCookie();
   response.addCookie(deletedAccessCookie);
   response.addCookie(deletedRefreshCookie);
   ```
5. SecurityContext 초기화
6. 로그인 페이지로 리다이렉트

### 4. 토큰 갱신 과정
1. Access Token 만료 시 (30분)
2. Refresh Token으로 새로운 Access Token 요청
3. Redis에서 Refresh Token 유효성 검증
4. 유효한 경우 새로운 Access Token 발급
5. 새로운 Access Token을 쿠키에 저장
6. 기존 Refresh Token 유지 (2일)

## 🔒 보안 설정

- CORS 설정: API 엔드포인트에 대한 크로스 오리진 요청 허용
- CSRF 보호: REST API 기반으로 CSRF 비활성화
- 세션 관리: Stateless 방식 사용
- 토큰 관리: HttpOnly 쿠키 사용

## 🧪 테스트

```bash
./gradlew test
```

## 🚀 실행 방법

1. 환경 변수 설정
2. MySQL 및 Redis 서버 실행
3. 애플리케이션 실행:
```bash
./gradlew bootRun
```

## 📝 API 문서

- Swagger UI: `http://localhost:8080/api/swagger-ui/index.html`
- OpenAPI 문서: `http://localhost:8080/api/api-docs`

## 🔑 토큰 만료 시간

- Access Token: 30분
- Refresh Token: 2일
