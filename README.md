

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

(관리자/작가 로그인 시, 웹툰관리메뉴 노출)<br>
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


<br/>
<br/>
<br/>


# 추가예정 부가 기능
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
- 구성환경 : Spring boot, Spring security, jwt, React.js, React-router, JPA, Hibernate, Mysql, Ant Design(React UI library), Spring Cloud



<br/>
<br/>
<br/>




# 필수 설치
- JAVA 11 
- maven
- React & npm

⭐환경변수 설정 필수!!!
<br>

- MySQL (localhost:3306)
- Mongo DB (localhost:27017) -> 참고: https://m.blog.naver.com/wideeyed/221815886721

⭐두가지 DB서버 구동중인지 확인!!
<br>
<br>

1) git clone https://github.com/sujin0970/joy_test
2) Mysql > create database cheezetoon;
3) MongoDB >  use testdb
4) 코드수정
<br>: backend\user-service\src\main\resourcesspring application.properties 파일에서 datasource.password수정
<br>: backend\webtoon-service\src\main\resourcesspring application.properties 파일에서 datasource.password수정
<br>: backend\cash-service\src\main\resourcesspring application.properties 파일에서 datasource.password수정
5) 빌드-실행
<br>: backend 폴더안의 config-service, ecommerce, api-gateway-service, user-service, webtoon-service, cash-service (총 6개의 프로젝트 빌드-실행)
<br>: ⭐ 위에 적힌대로 빌드-실행순서를 반드시❗❗❗❗❗❗❗ 지켜야함
<br>: 예시
<br>: $ cd backend\user-service
<br>: $ mvn package
<br>: $ mvn spring-boot:run

6) Mysql > use  cheezetoon;
   <br> INSERT IGNORE INTO roles(name) VALUES('ROLE_AUTHOR');
   <br> INSERT IGNORE INTO roles(name) VALUES('ROLE_ADMIN');
   <br> INSERT IGNORE INTO roles(name) VALUES('ROLE_USER');
   
   
<br> <br> 
[💚NFT-node-server 구동💚]<br>
7) $ cd backend\NFT-node-server <br>
8) $ npm install<br>
9) $ npm start<br>
10) localhost:8000/NFT-node-service 이동 시, Hello/ 출력<br>
11) ⭐ NFT-node-server 의 api 는 /NFT-node-service/\** 형식만을 지원한다. 자세한 사항은 backend\NFT-node-server\router\main.js 참고<br>
12) Node는 Express 패키지로 서버를 구성했음<br>


<br><br>
[🔻주의사항🔻]
- 페이지 회원가입 후에는 모두 유저로 저장됨. 따라서, DB에서 직접 수정해야함
- webtoon-service 폴더 mvn package 이후, target 폴더가 생성되어야함. ===> target/classes/static/ toons,thumbnail,uploads 3개의 폴더가 있어야 정상적으로 파일이 업로드 된다.
- 몽고DB의 데이터를 확인하고 싶을 때는, mongodb> use testdb 이후 mongodb> db.cashRecord.find(); 명령어 
- ⭐⭐ 아직 서버작업이 덜 끝남❗ 
- ⭐⭐ 캐시충전 & 웹툰대여 (api :8000/cash-service/pushmongo) -> 몽고DB에 cashrecord 는 생성이 되나, Mysql User 테이블의 Cash는 변화가 없음 ====> 이 작업은 이번주내로 마무리하고 다시 github에 올릴 예정



