# <h1>배달 프렌드 BaedalFriend 📆 2022.11.04 ~ 2022.12.16</h1>

👉🏻 [<img src="https://img.shields.io/badge/배달프렌드-FF5B15?style=flat&logo=PWA&logoColor=white" height="23"/> Click!!](https://www.baedalfriend.app) 👈🏻  
<br>

# 👫 배달프렌드는
모집글을 등록하거나 검색하여 주변에서 함께 배달을 시킬 프렌드를 찾는다는 의미로,  
**" 비싼 배달비 및 최소배달금액 때문에 배달을 망설이는 사람들을 위한 공동 배달서비스 "** 입니다.  
<br/>

# 💡 프로젝트 개요

너무 먹고 싶은 음식을 결제하려고 보니 배달비가 터무니없이 비싸서 포기한 경험,  
😢 혼자 오붓하게 먹고 싶은데 최소 배달 금액이 너무 높아서 포기한 경험, 한번쯤은 있으시죠!  
같이 시킬 사람이 한 명만 더 있어도 배달 부담이 줄어들 거라는 아이디어에서 **‘배달프렌드’**가 탄생하였습니다.  
<br/>

# ⚙️ 프로젝트 주요 기능

### 반응형 웹/앱  
![반응형](https://user-images.githubusercontent.com/85330584/207781840-153a83a5-9875-4a7e-910b-046d8b591bbb.gif)  
- 디바이스 종류에 구애받지 않고 편안한 화면을 제공합니다.

### 간단하고 안전한 회원가입  
![image](https://user-images.githubusercontent.com/85330584/207784223-c2a8107d-70ea-4662-b746-31f9d451dc7c.png)  
- 카카오 소셜로그인을 통한 간단한 가입도 가능하지만, 자체적인 회원가입 또한 간편하고 안전하게 이용 가능합니다.

### 지도를 사용할 수 있는 게시물 작성  
![image](https://user-images.githubusercontent.com/85330584/207784412-325ca93b-1a8d-4084-abe6-e30c8800d8a8.png)  
- 배달 공동 주문에 필요한 항목을 폼에 간편하게 기입하여 업로드할 수 있습니다.  
- 카카오맵을 통해 음식점 장소와 만날 장소의 검색이 가능하며, 지도에 표시되는 위치로 선택이 가능합니다.

### 키워드 및 카테고리, 지도를 통한 반경 1km 이내의 게시물 맞춤 검색  
![image](https://user-images.githubusercontent.com/85330584/207784574-b9ada521-8c81-4dcf-af75-ff56ba75d4df.png)  
- 키워드와 카테고리로 구분된 검색 페이지에서 사용자가 찾고 싶은 모집 글을 쉽게 검색할 수 있습니다.  
- 주소를 설정해 주시면 ‘Nearby’를 통해 설정 주소 기반 1km 이내의 게시물을 확인할 수 있습니다.  
- 검색 결과의 데이터를 바탕으로 정렬이 가능합니다.

### 배프들과의 실시간 채팅  
![image](https://user-images.githubusercontent.com/85330584/207784671-adc6709f-339d-41ef-9a26-d587a2d4a890.png)  
- 게시물에 참여하면, 참여 중인 배프들과 채팅을 통해 배달 관련 정보 교환을 하여 성공적인 공동 주문이 가능합니다.

### 개인의 취향에 맞게 프로필 수정  
![image](https://user-images.githubusercontent.com/85330584/207784821-ba5c128e-065e-4895-8111-8a1ce01e3f0e.png)  
- 마이페이지에서 유저의 취향대로 프로필사진과 닉네임을 변경할 수 있고, 작성한 게시글을 조회할 수 있습니다.

---

# ♻️ 아키텍처
![image](https://github.com/user-attachments/assets/d67b51b0-1a83-42c7-8ccd-8ee95a459735)

---

## 배달프렌드 ERD 


![image](https://github.com/user-attachments/assets/e5aba761-9f22-4f15-90ff-ff08457f0304)



---

# 🛠 사용한 패키지
| 이름                          | 버전     | 설명                                                                                  |
|-------------------------------|----------|---------------------------------------------------------------------------------------|
| @sentry/react @sentry/tracing | ^7.23.0  | 실시간 로그 취합 및 분석 도구, 모니터링 플랫폼입니다.                                  |
| @redis/redis                  | ^6.0.16  | 레디스 Pub/Sub을 이용해 데이터를 전달하고 소켓 간 세션 정보를 공유합니다.              |
| @spring/spring-boot           | ^2.7.5   | AutoConfig와 빠른 서버 실행을 위한 내장 웹서버 톰캣 제공.                             |
| @springDataJPA/springDataJPA  | ^2.7.5   | 예상 가능한 코드 생략과 JPA의 편리한 사용을 지원합니다.                                |
| @lombok/lombok                | ^1.18.16 | 반복적인 메서드를 줄여주는 Annotation 기반의 라이브러리입니다.                        |
| @swagger/swagger              | ^3.0.0   | API 문서화를 지원하며 API 테스트도 가능한 도구입니다.                                  |
| @mysql/mysql                  | ^6.1.2   | 관계형 데이터베이스 MySQL을 사용했습니다.                                             |
| @S3/S3                        | ^2.2.6   | Amazon S3를 사용해 무제한 용량으로 파일을 업로드할 수 있습니다.                        |

---
<h3><b>🛠 Tech Stack 🛠</b></h3>
<p>
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/linux-FCC624?style=for-the-badge&logo=linux&logoColor=black">
  <img src="https://img.shields.io/badge/Jpa-232F3E?style=for-the-badge&logo=aws&logoColor=white">
   <img src="https://img.shields.io/badge/MySql-232F3E?style=for-the-badge&logo=aws&logoColor=white">
   <img src="https://img.shields.io/badge/Websocket-232F3E?style=for-the-badge&logo=aws&logoColor=white">
   <img src="https://img.shields.io/badge/Oath2-232F3E?style=for-the-badge&logo=aws&logoColor=white">
   <img src="https://img.shields.io/badge/Jwt-232F3E?style=for-the-badge&logo=aws&logoColor=white">
   <img src="https://img.shields.io/badge/KakaoLogin-232F3E?style=for-the-badge&logo=aws&logoColor=white">

  
  
<img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=aws&logoColor=white">

<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"/>
<img src="https://img.shields.io/badge/Sentry-362D59?style=flat&logo=Sentry&logoColor=white" height="23"/>


---

## 배달프렌드 API 명세서 


- **[⚙ API 명세서](https://github.com/BedalFriend/BaedalFriend-BE/wiki/API-%EB%AA%85%EC%84%B8%EC%84%9C)**  

---

# 👨‍👨‍👧‍👧배달프렌드 팀원 소개

| ⚜ 백두산         | 이호진             | 강소연             | 김재명           | 김정은         | 노희진          | 지영주          |
|------------------|--------------------|--------------------|------------------|----------------|-----------------|-----------------|
| [Github](https://github.com/BaekDoosan-maker) | [Github](https://github.com/kaifazhe99) | [Github](https://github.com/ssoyeon59) | [Github](https://github.com/JMKiim) | [Github](https://github.com/mingki831) | [Github](https://github.com/rohheejin) | [Behance](https://www.behance.net/yjj91179bea) |
| BE / Spring      | BE / Spring        | BE / Spring        | FE / React       | FE / React     | FE / React      | Designer        |
