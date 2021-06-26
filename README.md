

# 핵심 기능
[일반인]
- 웹툰 메인화면
- 요일별 웹툰
- 웹툰/에피소드 썸네일
- 회원가입

[유저]
- 로그인
- 캐시 충전/환불
- 캐시 사용내역조회
- 웹툰 대여권 구매
- 에피소드 뷰어 (JWT/대여권 확인후,차단/이동)
- 별점 등록, 수정, 별점 평균 출력
- 선호작품 등록, 삭제
- 댓글 등록, 수정, 삭제
- 대여웹툰 조회(3일간 대여가능)

(관리자/작가 로그인 시, 웹툰관리메뉴 노출)<br><br>

[작가]
- 자신의 웹툰 등록, 수정, 삭제
- 자신의 웹툰 별 에피소드 등록, 수정, 삭제

[관리자]
- 모든 웹툰/에피소드 등록,수정,삭제


<br/>
<br/>
<br/>


# 추가예정 구현 기술
- Kafka
- ELK
- 도커배포
- 쿠버네티스 + Jenkins
- REDIS + JWT
- Naver Cloud Storage 파일 서버 연동
- Hadoop
- Spark
- Hive
- JMeter

<br/>
<br/>
<br/>


# 추가예정 구현 기능
- 인기순/별점순/거래량순 만화순위
- 만화 추천
- 베댓 기능 구현
- 웹툰검색기능
- 완결/연재 구분
- 프로필정보 수정/ 회원탈퇴
- 회원가입 이메일인증
- OAuth



<br/>
<br/>
<br/>



# 개발 환경
- 개발환경 : Windows 10 Enterprise x64
- 개발도구 : intelliJ, Github
- 구성환경 : Spring boot, Spring security, jwt, React.js, React-router, JPA, Hibernate, Mysql, MongoDB, RabbitMQ, Spring Cloud, GCP file Storage



<br/>
<br/>
<br/>




# 필수 설치
- JAVA 11 
- maven
- React & npm
- <a href = https://heodolf.tistory.com/50 >erlang + RabbitMQ</a>

⭐환경변수 설정 필수!!!
<br>

- MySQL (localhost:3306)
- Mongo DB (localhost:27017)
- RabbitMQ (localhost:5672)

⭐세가지 서버 구동중인지 확인!!
<br>
<br>

1) git clone https://github.com/sujin0970/joy_test
2) Mysql > create database joytoon;<br>
   Mysql > create database joytoon;
3) MongoDB >  use testdb
4) 코드수정
<br>: backend\user-service\src\main\resourcesspring application.properties 파일에서 datasource.password수정
<br>: backend\webtoon-service\src\main\resourcesspring application.properties 파일에서 datasource.password수정

5) backend 서버 빌드-실행
- config-service
- ecommerce
- api-gateway-service
- user-service
- webtoon-service
- cash-service 
- 📌 위에 적힌대로 빌드-실행순서를 반드시 지켜야함
<br> Ex)
<br>: $ cd backend\user-service
<br>: $ mvn package
<br>: $ mvn spring-boot:run

6) Mysql > use  cheezetoon;
   <br> INSERT IGNORE INTO roles(name) VALUES('ROLE_AUTHOR');
   <br> INSERT IGNORE INTO roles(name) VALUES('ROLE_ADMIN');
   <br> INSERT IGNORE INTO roles(name) VALUES('ROLE_USER');
   
   
<br> <br> 
[💚NFT-node-server 구동💚]<br>
 :  $ cd backend\NFT-node-server <br>
 :  $ npm install<br>
 :  $ npm start<br>
 :  localhost:8000/NFT-node-service 이동 시, Hello/ 출력<br>
 
 -  ⭐ NFT-node-server 의 api 는 /NFT-node-service/\** 형식만을 지원한다.  backend\NFT-node-server\router\main.js 참고<br>



<br><br>
[🔻주의사항🔻]
- 페이지 회원가입 후에는 모두 유저로 저장됨. 따라서, MySQL joyuser database 에서 쿼리를 통해 직접 수정해야함
- 몽고DB의 데이터를 확인하고 싶을 때는, mongodb> use testdb 이후 mongodb> db.cashRecord.find(); 명령어 
- ⭐ 서버분리 및 api gateway 연결작업 



