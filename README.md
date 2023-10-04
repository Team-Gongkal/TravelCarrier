 <img src="https://github.com/Team-Gongkal/TravelCarrier/assets/89832538/8fad71c0-63ea-49e1-8c1b-29e7d4ed6cb7"> &nbsp;
 ---
*함께쓰는 여행기록, TravelCarrier* :camera:

> 사진별로 추억을 저장하고, <br/>
통합적으로 여행을 기록하고, <br/>
친구와 함께 편집하고,<br/>
사람들에게 공유하고 싶은 당신에게

<br/>

TravelCarrier의 가장 큰 특징은 일반적인 게시글 종속성의 역전입니다. 일반적 게시판이 여러장의 첨부파일을 허용하는 시스템을 반전하여, 각 첨부파일이 각자의 제목과 본문, 그리고 댓글을 가질 수 있도록 하였습니다. 따라서 인스타그램 스토리에서 느꼈던 불편함(업로드시 수정이 불가능함)을 해결하고, 여행기록에 보다 적합한 사용자 경험을 제공합니다. 이제 사진 하나하나에 담긴 에피소드를 공유해보세요!

## Information
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FcheshireHYUN%2FTravelCarrier&count_bg=%2381C5E0&title_bg=%23D0D0D0&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)

배포 주소 : http://www.travelcarrier.site <br/>
개발기간 : 2023.03 ~ 2023.09
|Design&Publish|FrontEnd&BackEnd|
|:---:|:---:|
|[r:loor](https://github.com/CutieYundung)|[cheshire](https://github.com/cheshireHYUN)|
 



## Stacks
### Environment
<img src="https://img.shields.io/badge/AWS EC2-FF9900?style=flat-square&logo=amazonec2&logoColor=black"/> <img src="https://img.shields.io/badge/AWS S3-E34F26?style=flat-square&logo=amazons3&logoColor=white"/> <img src="https://img.shields.io/badge/AWS RDS-527FFF?style=flat-square&logo=amazonrds&logoColor=white"/> 
### Dev
<img src="https://img.shields.io/badge/java-007396?style=flat&logo=java&logoColor=white">&nbsp;<img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=flat&logo=springboot&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/JPA-4FC08D?style=flat&logo=jpa&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=thymeleaf&logoColor=white"/>


<img src="https://img.shields.io/badge/HTML-E34F26?style=flat&logo=html5&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/CSS-1572B6?style=flat&logo=css3&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/JQuery-0769AD?style=flat&logo=jquery&logoColor=white"/>


### Communication
<img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=git&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Github-181717?style=flat&logo=github&logoColor=white"/>&nbsp;<img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=notion&logoColor=white"/> 

## 아키텍처
 <img src="https://github.com/Team-Gongkal/TravelCarrier/assets/89832538/ddba4131-4dac-4626-b817-e231a186e3cf">

## 기본 기능
<b>📁 회원가입/로그인/로그아웃 + 소셜로그인</b>
  - Spring Security를 이용한 자체 회원가입 지원
  - OAuth를 이용한 소셜로그인(Google, Kakao, Naver) 지원
<br/>

<b>📁 위클리(대략적인 여행정보)등록 </b>
  - 대표사진으로 요약된 여행기록 조회
  - 태그된 친구에게 해당 기록의 편집 권한 부여
<br/>

<b>📁 데일리(일별 여행기록)를 등록</b>
  - 여행사진 등록 및 코멘트 작성
  - 등록한 사진에 대하여 리사이징
  - 각 사진에 대한 코멘트 슬라이드
  - 슬라이드의 버튼을 클릭하여 사진별로 댓글 작성
<br/>

<b>📁 댓글, 대댓글 등록</b>
  - 댓글 등록 및 댓글에 대한 대댓글 등록
<br/>

<b>📁 통합적 검색기능 제공</b>
  - 유저와 글에대한 검색 지원
<br/>

<b>📁 팔로잉/팔로워</b>
  - 팔로우 기능을 통해 글 접근권한 다각화
  - 팔로잉 유저에 한해 태그기능 지원
<br/>

<b>📁 화면별 튜토리얼 제공</b>
  - 첫 회원가입시 화면별 튜토리얼 자동실행
  - 이후엔 도움말 버튼 클릭시에만 실행(쿠키 이용하여 기록 저장)
<br/>

## Advanced 기능
<b>📁 직관적이고 편리한 데일리 편집</b>
  - jQuery-UI를 사용한 드래그앤 드랍 지원
  - 각 사진객체를 배열로 관리하여 데이터를 보장
<br/>

<b>📁 사이트에 최적화된 이미지 리사이징 기능 제공</b>
  - imgscalr 라이브러리를 이용한 이미지 리사이징 기능 제공
  - 리사이징 및 회전하여 최적화된 사진을 저장
  - 이 과정을 통해 사진용량이 획기적으로 줄어 서버 부담 감소
<br/>

<b>📁 실시간 알림기능 제공</b>
  - SSE를 사용, 댓글/태그/팔로우 이벤트에 대해 실시간 알림 지원
<br/>

<b>📁 글 접근권한 다각화</b>
  - 위클리 등록시 public,private,follow 권한설정으로 인한 팔로워 글접근 권한 다각화
  - 팔로워와는 별개로 동행인으로 태그되면 접근권한을 가질 수 있음
  - 다른 유저의 팔로잉, 팔로워 조회시 로그인유저 기준으로 버튼 활성화
<br/>

<b>📁 AWS S3로 사진 저장소 분리</b>
  - AWS S3에 저장함으로써 로컬서버 용량의 한계를 극복
  - 개발측에서 유저의 데이터에 접근할 수 있는 상황 차단, 즉 보안성 확보
<br/>



<details>
<summary>준비중</summary>
<div markdown="1">

## 화면 구성
### 로그인
### 메인
### 마이페이지
### 위클리
### 데일리
### 친구 프로필

</div>
</details>




