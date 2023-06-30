# JPA 시작
> 나는 Intellij와 H2 데이터베이스를 사용했다.

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

## 객체 매핑 시작
![클래스와 테이블 매핑](https://lh3.googleusercontent.com/pw/ACtC-3dR9wnUXYz7fApGFwk79lfrcIlAwFvraFhEmDrNzTNZi3hvyrp2J0xzNnhUO3Aqn2QTXXB7Ftgl4ebAW1eL2FayGP2UBKxWsjRt80jji1CwHaLuRbmDw_6GMAUQwKy2cCY0oFkF6cAdyrKQ6YWZthPQMg=w1053-h243-no?authuser=0)

* @Entity  
  - @Entity가 사용된 클래스를 엔티티 클래스라고 한다.

* @Table  
  - 엔티티 클래스에 매핑할 테이블 정보를 알려준다.

* @Id  
  - 엔티티 클래스의 필드를 테이블의 기본 키(Primary key)에 매핑한다.
 
* @Column
  - 필드를 컬럼에 매핑한다.
 
* 매핑 정보가 없는 필드
  - 매핑 어노테이션을 생략하면 필드명을 사용해서 컬럼명으로 매핑한다.(ex. @Column(name="AGE"))
 
## persistence.xml 설정
META-INF라는 폴더가 있으면 특별한 설정없이 JPA가 persistence.xml을 인식한다.  
javax가 jakarta로 변경되었다.

* persistence.xml 파일 내용
```java
<?xml version="1.0" encoding="UTF-8" ?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence" version="3.1">
    <persistence-unit name="studyJPA">
<!--        gradle 필수 조건-->
        <class>com.example.studyJPA.start.Member</class>
        <properties>
<!--            필수 속성-->
            <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="jakarta.persistence.jdbc.user" value="sa"/>
            <property name="jakarta.persistence.jdbc.password" value=""/>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>

<!--            옵션-->
            <property name="hibernate.show_sql" value="true"/>  // SQL 출력
            <property name="hibernate.format_sql" value="true"/>  // SQL을 출력할 때 보기 쉽게 정렬
            <property name="hibernate.use_sql_comments" value="true"/>  // 쿼리를 출력할 때 주석도 함께 출력
            <property name="hibernate.id.new_generator_mappings" value="true"/>  // JPA 표준에 맞춘 새로운 키 생성 전략을 사용

        </properties>
    </persistence-unit>
</persistence>
```

`<persistence xmlns="https://jakarta.ee/xml/ns/persistence" version="3.1">`    
xml 네임스페이스와 사용할 JPA 버전을 명시한다.  
  
`<persistence-unit name="studyJPA">`    
JPA 설정은 영속성 유닛(persistence-unit)이라는 것부터 시작하는데 일반적으로 연결할 데이터베이스당 하나의 영속성 유닛을 등록한다.  
그래서 해당 폴더이름과 같이 'studyJPA'로 이름을 맞춰줘야 한다. 
  
`<class>com.example.studyJPA.start.Member</class>`    
gradle에서 사용하기 위해서는 JPA에서 사용할 엔티티 클래스를 지정해야 하므로 위와 같은 코드를 추가해줘야 한다.  
  
* 데이터베이스 방언
  - 방언 : SQL 표준을 지키지 않거나 특정 데이터베이스만의 고유한 기능을 JPA에서는 방언이라고 한다.
데이터 타입, 함수명, 페이징 처리 등이 DB마다 조금씩 다르다.
그래서 대부분의 JPA 구현체들은 이런 문제를 해결하기 위해 다양한 DB 방언 클래스를 제공한다.
![방언](https://blog.kakaocdn.net/dn/cunU3I/btqEGTU1B8W/cEefmBqjuHKKbsSgQykj7k/img.png)

## 애플리케이션 개발
* 엔티티 매니저 설정
![엔티티 매니저 생성 과정](https://velog.velcdn.com/images/amoeba25/post/d69020f8-d619-43d9-aee0-2bdf1ad2205e/image.png)
  - 엔티티 매니저 팩토리 생성  
  앤티티 매니저 팩토리는 애플리케이션 전체에서 **딱 한번만 생성하고 공유해서 사용**해야 한다.  
  객체를 만들고 커넥션 풀도 생성하기 때문에 비용이 아주 크기 때문이다.

  - 엔티티 매니저 생성  
  엔티티 매니저를 사용해서 엔티티를 데이터베이스에 **등록/수정/삭제/조회**할 수 있다.
  엔티티 매니저는 데이터베이스 커넥션과 밀접한 관계가 있으므로 스레드 간에 **공유하거나 재사용하면 안 된다**.  

  - 종료  
  사용이 끝난 엔티티 매니저는 반드시 종료해야 한다.

* 트랜잭션 관리  
  트랜잭션을 시작하려면 엔티티 매니저에서 트랜잭션 API를 받아와야 한다.  
  트랜잭션 API를 사용해서 비즈니스 로직이 정상 동작하면 `커밋(commit)`하고 예외가 발생하면 `롤백(rollback)`한다.  
  
* 비즈니스 로직  
엔티티 매니저를 통해 데이터베이스에 등록, 수정, 삭제, 조회한다.
```java
//비즈니스 로직
    private static void logic(EntityManager entityManager){
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("승민");
        member.setAge(26);

        // 등록
        entityManager.persist(member);

        // 수정
        member.setAge(24);

        // 한 건 조회
        Member findMember = entityManager.find(Member.class, id);
        System.out.println("findMember = " + findMember.getUsername() + ", age = " + findMember.getAge());

        // 목록 조회
        List<Member> memberList = entityManager.createQuery("select m from Member m", Member.class)
                .getResultList();
        System.out.println("memberList.size = " + memberList.size());

        // 삭제
        entityManager.remove(member);
    }
```

* JPQL  
```java
// 목록 조회
TypedQuery<member> query = em.createQuery("select m from Member m", Member.class);
List<Member> members = query.getResultList();
```

검색 조건이 포함된 SQL을 사용할 때 JPA는 JPQL(Java Persistence Query Language)라는 쿼리 언어로 문제를 해결한다.  
**JPQL vs SQL**  
JPQL은 **엔티티 객체**를 대상으로 쿼리한다.  
SQL은 **데이터베이스 테이블**을 대상으로 쿼리한다.  
JPQL은 데이터베이스 테이블을 전혀 알지 못한다.  

> 여기서 from Member는 회원 엔티티를 객체를 말하는 것이지 MEMBER 테이블이 아니다!!  
  









