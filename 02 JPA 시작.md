# JPA 시작
> 저는 Intellij와 H2 데이터베이스를 사용했습니다.

## 라이브러리와 프로젝트 구조

* 사용한 라이브러리

```java
implementation 'org.springframework.boot:spring-boot-starter'
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
runtimeOnly 'com.h2database:h2'
```

* 프로젝트 구조

```java
src/main
|-- java
|   |-- come.example.studyJPA.start
|       |-- JpaMain
|       |-- Member
|-- resources
|   |-- META-INF
|       |-- persistence.xml
|   |-- application.properties
| 
build.gradle
```
> META-INF라는 폴더가 있으면 특별한 설정없이 JPA가 persistence.xml을 인식한다.

## 객체 매핑 시작
![클래스와 테이블 매핑](https://lh3.googleusercontent.com/pw/ACtC-3dR9wnUXYz7fApGFwk79lfrcIlAwFvraFhEmDrNzTNZi3hvyrp2J0xzNnhUO3Aqn2QTXXB7Ftgl4ebAW1eL2FayGP2UBKxWsjRt80jji1CwHaLuRbmDw_6GMAUQwKy2cCY0oFkF6cAdyrKQ6YWZthPQMg=w1053-h243-no?authuser=0)

* @Entity  
  - @Entity가 사용된 클래스를 엔티티 클래스라고 한다.

* @Table  
  - 엔티티 클래스에 매핑할 테이블 정보를 알려준다.

* @Id  
  - 엔티티 클래스의 필드를 테이블의 기본 키(Primary key)에 매핑한다.






