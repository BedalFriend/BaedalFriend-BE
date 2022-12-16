
# <h1>배달 프렌드 BaedalFriend 📆 2022.11.04 ~ 2022.12.16</h1>


👉🏻 [<img src="https://img.shields.io/badge/배달프렌드-FF5B15?style=flat&logo=PWA&logoColor=white" height="23"/> Click!!](https://www.baedalfriend.app) 👈🏻
<br>
# 👫 배달프렌드는
모집글을 등록하거나 검색하여 주변에서 함께 배달을 시킬 프렌드를 찾는다는 의미로,

**" 비싼 배달비 및 최소배달금액 때문에 배달을 망설이는 사람들을 위한 공동 배달서비스 "** 입니다.

<br/>
# 💡 프로젝트 개요

🥲 너무 먹고 싶은 음식을 결제하려고 보니 배달비가 터무니 없이 비싸서 포기한 경험,

😢 혼자 오붓하게 먹고 싶은데 최소 배달 금액이 너무 높아서 포기한 경험, 한번 쯤은 있으시죠!

같이 시킬 사람이 한명만 더 있어도 배달 부담이 줄어들 거라는 아이디어에서

**‘배달프렌드’**
가 탄생하였습니다.

<br/>

# ⚙️ 프로젝트 주요 기능

배달 프렌드분들의 더욱 편리한 매칭을 위해 **위치 기반**으로 서비스를 제공합니다.

주소를 등록해주시면 가까운 곳에서 열리고 있는 배달 모집글의

🔍 **검색, 🌏 위치 확인, 💬 프렌드들과 채팅**이 가능합니다.

<br/>

<details>
<summary>

### &nbsp;반응형 웹/앱

</summary>

![반응형](https://user-images.githubusercontent.com/85330584/207781840-153a83a5-9875-4a7e-910b-046d8b591bbb.gif)

- 디바이스 종류에 구애받지 않고 편안한 화면을 제공합니다.
</details>

<details>
<summary>

### &nbsp;간단하고 안전한 회원가입

</summary>

![image](https://user-images.githubusercontent.com/85330584/207784223-c2a8107d-70ea-4662-b746-31f9d451dc7c.png)

- 카카오 소셜로그인을 통한 간단한 가입도 가능하지만, 자체적인 회원가입 또한 간편하고 안전하게 이용 가능합니다.
</details>

<details>
<summary>

### &nbsp;지도를 사용할 수 있는 게시물 작성

</summary>

![image](https://user-images.githubusercontent.com/85330584/207784412-325ca93b-1a8d-4084-abe6-e30c8800d8a8.png)

- 배달 공동 주문에 필요한 항목을 폼에 간편하게 기입하여 업로드할 수 있습니다.
- 카카오맵을 통해 음식점 장소와 만날 장소의 검색이 가능하며, 지도에 표시되는 위치로 선택이 가능합니다.
</details>

<details>
<summary>

### &nbsp;키워드 및 카테고리, 지도를 통한 반경 1km 이내의 게시물 맞춤 검색

</summary>

![image](https://user-images.githubusercontent.com/85330584/207784574-b9ada521-8c81-4dcf-af75-ff56ba75d4df.png)

- 키워드와 카테고리로 구분된 검색 페이지에서 사용자가 찾고 싶은 모집 글을 쉽게 검색할 수 있습니다.
- 주소를 설정해 주시면 ‘Nearby’를 통해 설정 주소 기반 1km 이내의 게시물을 확인할 수 있습니다.
- 검색 결과의 데이터를 바탕으로 정렬이 가능합니다.
</details>

<details>
<summary>

### &nbsp;배프들과의 실시간 채팅

</summary>

![image](https://user-images.githubusercontent.com/85330584/207784671-adc6709f-339d-41ef-9a26-d587a2d4a890.png)

- 게시물에 참여하면, 참여 중인 배프들과 채팅을 통해 배달 관련 정보 교환을 하여 성공적인 공동 주문이 가능합니다.
</details>

<details>
<summary>

### &nbsp;개인의 취향에 맞게 프로필 수정

</summary>

![image](https://user-images.githubusercontent.com/85330584/207784821-ba5c128e-065e-4895-8111-8a1ce01e3f0e.png)

- 마이페이지에서 유저의 취향대로 프로필사진과 닉네임을 변경할 수 있고, 작성한 게시글을 조회할 수 있습니다.
</details>

<br/>


# ♻️ 아키텍처

![image](https://myawsbucketds.s3.ap-northeast-2.amazonaws.com/5bd10535-4015-4c2f-a338-cd00701cbf29.png)

<br/>

# 🛠 사용한 패키지
|이름|버전|설명|
|---|---|---|
|@sentry/react @sentry/tracing|^7.23.0|실시간 로그 취합 및 분석 도구, 모니터링 플랫폼입니다.<br/>더 나아가서 발생한 로그들을 시각화 도구로 쉽게 분석할 수 있어 사용했습니다.|
|@redis/redis|^6.0.16| 메시지큐역할로 레디스를 사용했고 백그라운드에서 실행하여 실시간 채팅기능을 구현했습니다.|
|@spring/spring-boot|^2.7.5| AutoConfig로 복잡한 설정을 자동화해주고, 내장 웹서버인 톱캣으로 빠르게 서버 실행이 가능하도록 해주고 , 실행가능한 JAR로 개발가능하고, 자주 사용되는 라이브러리들의 버전관리자동화를 해주는 편리함때문에 사용했습니다.|
|@springDataJPA/springDataJPA|^2.7.5|  Spring framework에서 JPA를 편리하게 사용할 수 있도록 예상가능하고 반복적인 코드들을 줄여줘서 빠른개발을 도와주기 때문에 사용했습니다.|
|@lombok/lombok|^1.18.16|  저희는 Lombok을 지원하는 IDE인 인텔리J를 사용했고, Java의 라이브러리로 반복되는 메소드를 Annotation을 사용해서 자동으로 작성해주는 Lombok은 DTO나 Model, Entity가 가지는 프로퍼티에 대해 매번 Getter나 Setter, 생성자를 작성해줘야 하는 반복적인 코드를 줄여주기 때문에 사용했습니다.|
|@swagger/swagger|^3.0.0|  Spring REST Docs 보다 쉽게 만들수 있고, 프론트엔드분들과 협업시 api를 쉽게 확인할 수 있도록 컬러풀한 문서화가 가능하고, 문서 화면에서 바로바로 API를 테스트 할 수 있는 Swagger를 사용했습니다.|
|@mysql/mysql|^6.1.2| 데이터 베이스는 관계형 데이터베이스인 mySql을 사용했습니다.|
|@S3/S3|^2.2.6| Amazaon S3는 내구성이 좋고, 용도에 따른 접속관리를 통해 안정성 또한 좋습니다. 사실상 무제한적인 용량으로 많은 인원이 저희 플랫폼에 가입할경우 문제없이 프로필 이미지 파일을 업로드 할 수 있도록 Amazon S3를 사용했습니다. |
---
<h3 align="center"><b>🛠 Tech Stack 🛠</b></h3>
<p>
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/linux-FCC624?style=for-the-badge&logo=linux&logoColor=black">
<img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=aws&logoColor=white">
<img src="https://camo.githubusercontent.com/fd0243cd3a19485c4f3e82eba48aa53c2b13c41bd87164fc77fa3498ec09d2bd/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f616d617a6f6e73332d3536394133313f7374796c653d666f722d7468652d6261646765266c6f676f3d616d617a6f6e7333266c6f676f436f6c6f723d7768697465"> <img src="https://camo.githubusercontent.com/5309f68ce19176455b37914291b345bd7af797286bbf86aaabdc23d398e93586/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f617773206563322d3037433136303f7374796c653d666f722d7468652d6261646765266c6f676f3d616d617a6f6e65617773266c6f676f436f6c6f723d7768697465">
  <img src="https://camo.githubusercontent.com/c0f71772804c86d0f144ce923027aff25e8d761c6b791d2de6698607e21c5465/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f677261646c652d3032333033413f7374796c653d666f722d7468652d6261646765266c6f676f3d677261646c65266c6f676f436f6c6f723d7768697465">
  <img src="https://camo.githubusercontent.com/c1fc168684171582321954905e8b9dc4f59810243ed85e645f3b7938ee3145cb/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f6d7973716c2d3434373941313f7374796c653d666f722d7468652d6261646765266c6f676f3d6d7973716c266c6f676f436f6c6f723d7768697465">
  <img src="https://camo.githubusercontent.com/54a2f74f3cbb3cb810faa417fb9a56b4d947be01e868ab624b3f251a1062257b/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f67697468756220616374696f6e732d3230383846463f7374796c653d666f722d7468652d6261646765266c6f676f3d67697468756220616374696f6e73266c6f676f436f6c6f723d7768697465">
  <img src="https://camo.githubusercontent.com/a831a652fb5370367ee71ae4255e39623b9edf7e60ffbcf7ba356b1d82a09538/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f737072696e672064617461206a70612d4632384431413f7374796c653d666f722d7468652d6261646765266c6f676f3d737072696e67646174616a7061266c6f676f436f6c6f723d7768697465">
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"/>
<img src="https://img.shields.io/badge/Sentry-362D59?style=flat&logo=Sentry&logoColor=white" height="23"/>
---

<br>
<h3 align="center"><b><a href="https://github.com/BedalFriend/BaedalFriend-BE/wiki/API-%EB%AA%85%EC%84%B8%EC%84%9C">⚙ API 명세서 ⚙</a></b></h3>

---

<br>
<h3 align="center"><b><a href="https://github.com/orgs/BedalFriend/projects/1/views/1"> 개발 진행상황 </a></b></h3>

---
<br>
<h3 align="center"><b> Team </b></h3>
# 👨‍👨‍👧‍👧배달프렌드 팀원 소개

|백두산|이호진|강소연|김재명|김정은|노희진|지영주|
|---|---|---|---|---|---|---|
|[Github](https://github.com/BaekDoosan-maker)|[Github](https://github.com/kaifazhe99)|[Github](https://github.com/BaekDoosan-maker)|[Github](https://github.com/JMKiim)|[Github](https://github.com/mingki831)|[Github](https://github.com/rohheejin)|[Behance](https://www.behance.net/yjj91179bea)|
|BE / Spring|BE / Spring|BE / Spring|FE / React| FE / React |FE / React|Designer|
---
