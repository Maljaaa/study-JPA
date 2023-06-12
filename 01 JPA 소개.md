# JPA 소개
"왜 실무에서 테이블 설계는 다들 열심히 하면서 제대로 된 객체 모델링은 하지 않을까?",  
"왜 객체 지향의 장점을 포기하고 객체를 단순히 테이블에 맞추어 데이터 전달 역할만 하도록 개발할까?" 라는 의문이 들었다.  
  
그러던 중 객체와 관계형 데이터베이스 간의 차이를 중간에서 해결해주는 **ORM(Object-Relational Mapping)** 프레임워크를 알게 되었다.  
  
JPA는 지루하고 반복적인 CRUD SQL을 알아서 처리해줄 뿐만 아니라 객체 모델링과 관계형 데이터베이스 사이의 차이점도 해결해주었다.  
  
JPA를 사용해서 얻은 가장 큰 성과는 애플리케이션을 **SQL이 아닌 객체 중심**으로 개발하니 생산성과 유지보수가 확연히 좋아졌고  
테스트를 작성하기도 편리해진 점이다. 이런 장점 덕분에 버그도 많이 줄어들었다.  
또한 코드를 거의 수정하지 않고 데이터베이스를 손쉽게 변경할 수 있었다.  
  
귀찮은 문제들은 JPA에게 맡기고 더 좋은 객체 모델링과 더 많은 테스트를 작성하는 데 우리의 시간을 보내자.  
**개발자는 SQL 매퍼가 아니다.**  

* **[1. SQL을 직접 다룰 때 발생하는 문제](#SQL을-직접-다룰-때-발생하는-문제)**       
    * **[1-1. 반복, 반복 그리고 반복](#반복-반복-반복)**
    * **[1-2. SQL에 의존적인 개발](#SQL에-의존적인-개발)**
    * **[1-3. JPA와 문제해결](#JPA와-문제해결)**   
* **[2. 패러다임 불일치](#패러다임-불일치)**       
    * **[2-1. 상속](#상속)**
    * **[2-2. 연관관계](#연관관계)**
    * **[2-3. 객체 그래프 탐색](#객체-그래프-탐색)**
    * **[2-4. 비교](#비교)**
    * **[2-5. 정리](#정리)**
* **[3. JPA를 이용한 패러다임 해결](#JPA를-이용한-패러다임-해결)**       
    * **[3-1. JPA 소개](#JPA-소개)**
    * **[3-2. 왜 JPA를 사용해야 하는가?](#왜-JPA를-사용해야-하는가?)**


___
## SQL을 직접 다룰 때 발생하는 문제점
![JDBC API와 SQL](https://images.velog.io/images/yu-jin-song/post/c0ea3b9a-51a5-45d3-a167-5a5a2fe3750d/JDBC_API%EC%99%80_SQL.png)  

### 반복, 반복 그리고 반복
CRUD 기능 개발 중 조회 기능의 예시는 다음과 같다.

1. 조회 SQL 작성
2. JDBC API를 사용해서 SQL 실행
3. 조회 결과를 객체로 매핑

이처럼 CRUD 에서 DAO(데이터 접근 계층) 개발은 `반복적인 작업`을 수행해야 한다.

🚀 DB가 아닌 자바 컬렉션을 활용한다면?
```
DB가 아닌 Collection에 보관한다면,

List<Member> list = new ArrayList();
list.add(member);

우리는 단순히 리스트에 추가하기만 하면 된다. 그걸 원한다.
```
### SQL에 의존적인 개발
SQL에 모든 것을 의존하는 상황에서는 개발자들이 엔티티를 신뢰하고 사용할 수 없다.  
대신에 DAO를 열어서 어떤 SQL이 실행되고 어떤 객체들이 함께 조회되는지 일일이 확인해야 한다.  
물리적으로 SQL과 JDBC API를 데이터 접근 계층에 숨기는 데 성공했을지는 몰라도 논리적으로는 엔티티와 아주 강한 의존관계를 가지고 있다.  

애플리케이션에서 SQL을 직접 다룰 때 발생하는 문제점을 요약하면 다음과 같다.  

1. 진정한 의미의 계층 분할이 어렵다.
2. 엔티티를 신뢰할 수 없다.
3. SQL에 의존적인 개발을 피하기 어렵다.


### JPA와 문제 해결
JPA를 사용하면 객체를 데이터베이스에 저장하고 관리할 때, 개발자가 직접 SQL을 작성하는 것이 아니라  
**JPA가 제공하는 API**를 사용하면 된다.

* 저장기능 - persist()
```java
jpa.persist(member);  // 저장
```

* 조회 기능 - find()
```java
String emeberId = "helloId";
Member member = jpa.find(Member.class, memberId); //조회
```

* 수정 기능
```java
Member member = jpa.find(Member.class, memberId);
member.setName("이름변경"); //수정
```

* 연관된 객체 조회
```java
Member member = jpa.find(Member.class, memberId);
Team team = member.getTeam(); //연관된 객체 조회
```

___
## 패러다임의 불일치
객체지향 프로그래밍은 **추상화, 캡슐화, 정보은닉, 상속, 다형성 등 시스템의 복잡성을 제어할 수 있는 다양한 장치**들을 제공한다.  
그래서 현대의 복잡한 애플리케이션은 대부분 객체지향 언어로 개발한다.  
  
관계형 데이터베이스는 **데이터 중심으로 구조화**되어 있고, 집합적인 사고를 요구한다.  
그리고 객체지향에서 이야기하는 추상화, 상속, 다형성 같은 개념이 없다.  
  
객체와 관계형 데이터베이스는 지향하는 목적이 서로 다르므로 두르이 기능과 표현 방법도 다르다.  
이것을 `객체와 관계형 데이터베이스의 패러다임 불일치 문제`라 한다.  
따라서 객체 구조를 테이블 구조에 저장하는 데는 한계가 있다.  

### 상속
객체는 상속이라는 기능을 갖고 있지만 테이블은 상속이라는 기능이 없다.  
  
![객체 상속 모델](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRRnrBtPiFT6u-1l6v8xsuZ3C3cjJmIN7Rb6D2eNzGu71gCsoipxt293QAAzaEShPxDq4U&usqp=CAU)  
  
데이터베이스 모델링에서 이야기하는 슈퍼타입-서브타입 관계를 사용하면 객체 상속과 가장 유사한 형태로 테이블을 설계할 수 있다.  
  
![테이블 모델](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTTKrHp3X6uHrIA0k95S7cn09VzgV8fSqX22NArWNIsVav9RIeD3eWLuITqA48W6fiwe38&usqp=CAU)
  
예를 들어 Album을 조회환다면 ITEM과 Album 테이블을 조인해서 조회한 다음 그 결과로 Album 객체를 생성해야 한다.  
이런 과정이 모두 **패러다임의 불일치를 해결하려고 소모하는 비용**이다.  
  
#### JPA와 상속
JPA를 사용해서 Item을 상속한 Album 객체를 저장하는 예시는 다음과 같다.  
  
1. persist() 메소드를 사용해서 객체 저장  

```java
jpa.persist(album);
```

2. JPA는 다음 SQL을 실행해서 객체를 두 테이블에 나누어 저장  

```sql
INSERT INTO ITEM ...
INSERT INTO ALBUM ...
```

3. find() 메소드를 사용해서 객체 조회  

```java
String albumId = "id100";
Album album = jpa.find(Album.class, albumId);
```

4. JPA는 두 테이블을 조인해서 필요한 데이터를 조회하고, 그 결과를 반환
```sql
SELECT I.*, A.*
  FROM ITEM I
  JOIN ALBUM A ON I.ITEM_ID = A.ITEM_ID;
```

### 연관관계
객체는 **참조**를 사용해서 다른 객체와 연관관계를 가지고 **참조에 접근**해서 연관된 객체를 조회한다.  
객체는 **참조가 있는 방향으로만 조회**할 수 있다.  
Memer -> Team일 때 member.getTeam()은 가능하지만 team.getMember()는 불가능하다.  
  
테이블은 **외래 키**를 사용해서 다른 테이블과 연관관계를 가지고 **조인**을 사용해서 연관된 테이블을 조회한다.  
  
#### 객체를 테이블에 맞추어 모델링
```java
class Member {
  String id;
  Long teamId;
  String username;
}

class Team {
  Long id;
  String name;
}
```
객체를 테이블에 맞추어 모델링하면 객체를 테이블에 **저장하거나 조회할 때는 편리**하다.  
객체는 참조가 있는 방향으로만 조회할 수 있으므로 객체의 참조를 잘 보관해야하는 어려움이 있다.  
따라서 TEAM_ID 외래 키의 값을 그대로 보관하는 teamId 필드에는 문제가 있다.  
이런 방식을 따르면 객체지향의 특징을 잃어버리게 된다.  

#### 객체지향 모델링
```java
class Member {
  String id;
  Team team;
  String username;
  
  Team getTeam() {
    return team;
  }
}

class Team {
  Long id;
  String name;
}
```
연관성에 대한 문제는 해결된다.  
하지만 객체를 테이블에 **저장하거나 조회하기가 쉽지 않다**.  
왜냐하면 하나의 객체를 쪼개서 각각의 테이블에 나누는 로직을 생성해주어야 하기 때문이다.  

**객체 모델**은 외래 키가 필요 없고 단지 **참조**만 있으면 된다.  
**테이블**은 참조가 필요 없고 **외래 키**만 있으면 된다.

#### JPA와 연관관계
```java
member.setTeam(team); //회원과 팀 연관관계 설정
jpa.persist(member);  //회원과 연관관계 함께 저장
```
  
객체를 조회할 때 외래 키를 참조로 변환하는 일도 JPA가 처리해준다.  
```java
Member member = jpa.find(Member.class, memberid);
Team team = member.getTeam();
```

### 객체 그래프 탐색
```
Member---Team
  |
Order--- ...
  |
Delivery--- ...
```
  
SQL을 직접 다루면 처음 실행하는 SQL에 따라 객체 그래프를 어디까지 탐색할 수 있는지 정해진다.  
다른 객체 그래프는 데이터가 없으므로 null 값을 가지고 이는 탐색을 할 수 없게 한다.
결국 메소드를 상황에 따라 여러 벌 만들어서 사용해야 한다.  

```java
memberDAO.getMember();
memberDAO.getMemberWithTeam();
memberDAO.getMemberWithOrderWithDelivery();
```

#### JPA와 객체 그래프 탐색
JPA를 사용하면 객체 그래프를 마음껏 탐색할 수 있다.  
```java
member.getOrder().getOrderItem() ...
```

🚀 지연 로딩    
JPA는 연관된 객체를 사용하는 시점에 SQL 실행 -> 객체 신뢰 가능  
-> 실제 객체를 사용하는 시점까지 **데이터베이스 조회를 미룬다.**
그래서 Member만 조회한다면 즉시 로딩이 좋겠지만, 연관관계가 있는 Todo까지 조회한다면 지연로딩이 적합하다.  

JPA는 지연 로딩을 투명하게 처리한다. 메소드의 구현 부분에 JPA와 관련된 어떤 코드도 직접 사용하지 않는다.  

```java
Order order = member.getOrder();
order.getOrderDate(); //Order를 사용하는 시점에 SELECT ORDER SQL
```

### 비교
데이터베이스는 기본 키의 값으로 각 로우(row)를 구분한다.   
  
객체는 동일성 비교와 동등성 비교라는 두 가지 비교 방법이 있다.  
- 동일성 비교 : `==`, 객체 인스턴스의 주소 값을 비교
- 동등성 비교 : `equals()`, 객체 내부의 값을 비교

### JPA와 비교
JPA는 같은 트랜잭션일 때 **같은 객체가 조회되는 것을 보장**한다.
이는 객체 측면에서 볼 때 다른 인스턴스인 것이 아니라... 컬렉션에 보관한 것 같은 효과를 보인다.  

### 정리
객체 모델과 관계형 데이터베이스 모델은 지향하는 패러다임이 서로 다르다.  
문제는 이 패러다임의 차이를 극복하려고 개발자가 너무 많은 시간과 코드를 소비한다.  
정교한 객체 모델링을 할수록 패러다임의 불일치 문제가 더 커진다.  

JPA는 패러다임의 불일치 문제를 해결해주고 정교한 객체 모델링을 유지하게 도와준다.  

___
## JPA란 무엇인가?
🚀 `JPA(Java Persistence API)` : 자바 진영의 ORM 기술 표준  
![JPA](https://velog.velcdn.com/images%2Fjsj3282%2Fpost%2F68edf917-50c1-44f1-978b-3115b6362841%2Fimage.png)  
🚀 `ORM(Object-Relational Mapping)` : 객체와 관계형 데이터베이스 매핑  
SQL을 개발자 대신 생성해서 데이터베이스에 전달해준다.  
다양한 패러다임의 불일치 문제들도 해결해준다.  
  
따라서 객체 측면에서는 정교한 객체 모델링을 할 수 있고, 관계형 데이터베이스는 그에 맞게 모델링하면 된다.  
그리고 매핑 방법만 ORM 프레임워크에게 알려주면 된다.

다양한 ORM 프레임워크 중 자바 진영에서 가장 많이 사용되는 것은 **하이버네이트 프레임워크**이다.  

### JPA 소개
![JPA 표준 인터페이스와 구현체](https://velog.velcdn.com/images%2Ffordevelop%2Fpost%2Fbee7eb5d-0c94-4406-96ce-b8ce49f6059d%2Fimage.png)  
`EJB(Enterprise Java Beans)` -> `Hibernate`  
더 가볍고 실용적인데다 기술 성숙도도 높았다.  
  
**JPA**는 **자바 ORM 기술에 대한 API 표준 명세**다.  
쉽게 이해하면 인터페이스를 모아둔 것이다.  
따라서 JPA를 구현한 **ORM 프레임워크**를 선택해야하는데 우리는 가장 대중적인 **Hibernate**를 사용한다.  
  
### 왜 JPA를 사용해야 하는가?
JPA를 사용해야 하는 이유는 다음과 같다.  
1. 생산성  
자바 컬렉션에 객체를 저장하듯이 JPA에게 저장할 객체를 전달하면 된다.  
따라서 지루하고 반복적인 코드와 CURD용 SQL을 개발자가 직접 작성하지 않아도 된다.  
더 나아가서 DDL 문을 자동으로 생성해주는 기능이 있는데, 이는 DB 설계 중심의 패러다임을 객체 설계 중심으로 역전시킬 수 있다.  
  
2. 유지보수  
필드를 추가하거나 삭제해도 수정해야 할 코드가 줄어든다.  
따라서 개발자가 작성해야 했던 SQL과 API 코드를 JPA가 대신 처리해주므로 유지보수해야 하는 코드 수가 줄어든다.  
또한 객체지향 언어의 장점을 활용하여 유연하고 유지보수하기 좋은 도메인 모델을 편리하게 설계할 수 있다.  

3. 패러다임의 불일치 해결  
JPA는 상속, 연관관계, 객체 그래프 탐색, 비교하기와 같은 패러다임의 불일치 문제를 해결해준다.  
  
4. 성능  
JPA는 애플리케이션과 DB사이에서 다양한 성능 최적화 기회를 제공한다.  
```java
String memberId = "helloId";
Member member1 = jpa.(memberId);
Member member2 = jpa.(memberId);
```
같은 회원을 두 번 조회하는 코드의 일부분이다.  
만약 **JDBC API**를 사용했다면 **두 번** 통신했을 것이다.  
하지만 **JPA**를 사용하면 객체를 재사용하기 때문에 **한 번**만 통신한다.  
  

___
## 정리
**[맨 위로](#JPA-소개)**
