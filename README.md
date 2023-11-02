# JOY-webtoon
#### 이화여대 사이버보안 2021-1 캡스톤 디자인 프로젝트 더미API 및 초기 레이아웃

---

<br><br>

# 개발 환경
- 개발환경 : Windows 10 Enterprise x64
- 개발도구 : intelliJ, Github
- 구성환경 : Spring boot, Spring security, Spring cloud, jwt, React.js, React-router, JPA, Hibernate, Mysql, MongoDB, RabbitMQ, GCP file Storage

<br/>


# Install
- JAVA 11 
- maven
- React & npm
- <a href = https://heodolf.tistory.com/50 >erlang + RabbitMQ</a>
- MySQL (localhost:3306)
- Mongo DB (localhost:27017)
- RabbitMQ (localhost:5672)

<br/>

# Execute
1) git clone 
2) DB 데이터베이스 생성 쿼리 실행<br>
   Mysql > create database joytoon;<br>
   Mysql > create database joyuser;<br>
   MongoDB >  use testdb;
3) 코드수정 (DB 패스워드 설정)
<br>: backend\user-service\src\main\resourcesspring application.properties 파일에서 datasource.password수정
<br>: backend\webtoon-service\src\main\resourcesspring application.properties 파일에서 datasource.password수정

4) BackEnd 서버 빌드-실행 (실행순서를 반드시 지켜야함)
- config-service
- ecommerce
- api-gateway-service
- user-service
- webtoon-service
- cash-service

```
$ cd backend\user-service
$ mvn package
$ mvn spring-boot:run
```

5) DB 데이터베이스 생성 쿼리 실행<br>
   Mysql > use  joyuser;<br>
   Mysql > INSERT IGNORE INTO roles(name) VALUES('ROLE_AUTHOR');<br>
   Mysql > INSERT IGNORE INTO roles(name) VALUES('ROLE_ADMIN');<br>
   Mysql > INSERT IGNORE INTO roles(name) VALUES('ROLE_USER');<br>


6) NFT-node-server 구동<br/>
```
$ cd backend\NFT-node-server <br>
$ npm install<br>
$ npm start<br>
```
   

7) FrontEnd 구동<br/>
```
$ cd frontend
$ npm install & start
```
   


<br><br>
[🔻주의사항🔻]
- 페이지 회원가입 후에는 모두 유저로 저장됨. 따라서, MySQL joyuser database 에서 쿼리를 통해 직접 (관리자/작가/유저) 역할을 수정해야함
- 몽고DB의 데이터를 확인하고 싶을 때는, mongodb> use testdb 이후 mongodb> db.cashRecord.find(); 명령어 실행
- NFT-node-server 의 api 는 /NFT-node-service/\** 형식만을 지원한다.  backend\NFT-node-server\router\main.js 참고<br>



