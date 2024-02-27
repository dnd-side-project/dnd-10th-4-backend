<section align="center">
    <img src="https://github.com/dnd-side-project/dnd-10th-4-frontend/assets/50488780/fca65f80-0c00-41d1-b39e-b15a0e1ab2ae" width="150" height="150" /> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <img src="https://github.com/dnd-side-project/dnd-10th-4-frontend/assets/50488780/55b7dc98-3977-43a2-9eaa-c828b5e9d845" height="150" />
</section>

<br/>

## 내 마음 속 바다

![image](https://github.com/dnd-side-project/dnd-10th-4-backend/assets/86222503/a464480f-5b55-493c-8ced-2db925025785)

언제나 힐링이 필요한 순간, 바다의 풍경이 감정적인 안정을 가져다주는 영감에서 출발한 서비스에요.
혹시 마음 속 고민이 있지만 주변 지인에게 부담을 줄까봐 쉽게 털어놓지 못했던 경험이 있지는 않으셨나요?

**🌊 내 마음 속의 바다**에서는 남들에게 털어놓지 못하는 이야기를 물병에 담아 바다에 띄워 보낼 수 있어요.
바다를 건너 물병을 받은 상대방은 당신이 누구인지 알 수 없고, 비슷한 고민을 했을지도 모르는 상대방으로부터 위로의 답장을 주고 받을 수 있어요.

<br/>

## 목차

- [핵심 기능](#핵심-기능)
    * [1. 편지 작성](#1-편지-작성)
    * [2. 답장 하기](#2-답장-하기)
    * [3. 보관 하기](#3-보관-하기)
- [배포 주소](#배포-주소)
- [시연 영상](#시연-영상)
- [기술 스택](#기술-스택)
- [팀 소개](#팀-소개)

<br/>

## 핵심 기능

<img align="right" src="https://github.com/dnd-side-project/dnd-10th-4-frontend/assets/50488780/a822556c-f7e9-466e-8688-1f4947a85bb5" height="500" />

### 1. 편지 작성

나와 비슷한 고민을 했을 낯선이에게 나의 이야기를 담은 편지를 보내요.
> 편지를 받는 대상의 나이,성별, 그리고 나의 고민을 선택하여  
> 나와 비슷한 고민을 했을 누군가에게 나의 이야기를 담은 편지를 보낼 수 있어요.

### 2. 답장 하기

빠르게, 하지만 진중하게 진심 어린 편지를 보내요.
> 48시간안에 답장을 하지 않으면 편지는 다시 바다로 흘러가요. 흘러온 편지에 대한 답장은 단 한번만 가능해요.  
> 주어진 시간 안에 빠르게, 그리고 한번의 답장으로 더 진중하게 진심어린 위로를 나눌 수 있어요.

### 3. 보관 하기

간직하고픈 편지는 보관함에 보관해요.
> 나에게 큰 위로가 되었던, 혹은 마음에 와닿았던 편지는 내 보관함에 저장하고 언제든지 다시 읽을 수 있어요.  
> 마음에 들지 않거나 불편한 편지는 신고할 수 있어요. 내 마음 속 바다에서는 힐링과 따뜻함만을 느낄 수 있도록 도와드려요.

![image](https://github.com/dnd-side-project/dnd-10th-4-backend/assets/86222503/ee0f946e-27da-48f5-9035-39d5b4eff28c)
![image1](https://github.com/dnd-side-project/dnd-10th-4-backend/assets/86222503/2592e77f-5728-44e1-afa0-1fecb72c1c82)
![image2](https://github.com/dnd-side-project/dnd-10th-4-backend/assets/86222503/5c817f2d-dc46-4058-8aa6-c8d3f656eb03)
![image3](https://github.com/dnd-side-project/dnd-10th-4-backend/assets/86222503/808b7525-5649-486f-9823-d08da9115cce)
![image4](https://github.com/dnd-side-project/dnd-10th-4-backend/assets/86222503/2be03327-5c1c-4fe8-9354-60b434eeccff)


<br/>

## 배포 주소

🖥️ 서비스 배포 주소 - https://sea-of-my-heart.vercel.app/

🖥 서버 배포 주소 - https://oceanletter.site/

🎨 Storybook 주소 - https://main--65a6c73d536a3c43b7c5c9bb.chromatic.com/

<br/>


<br/>

## 시연 영상

https://github.com/dnd-side-project/dnd-10th-4-frontend/assets/50488780/c40ebba9-2ee6-44c0-b44c-f922d0e3bf00

<br/>

## Back-end 기술 스택 & 아키텍처 구조

![image](https://github.com/dnd-side-project/dnd-10th-4-backend/assets/86222503/84967062-e1a3-48dc-9d20-7e1b6596d5fb)

- IDE : Intellij
- Language : Java 17
- Build : Gradle
- Framework : Spring Boot 3.2
- Infrastructure : Spring Data JPA, Querydsl
- Auth : Kakao OAuth2 Client, JWT, Spring Security
- Database : MySQL, Redis
- API Docs : Notion
- CI / CD : AWS/EC2, Docker, Git-Action

## Front-end 기술 스택

<p align="center">
    <img width=800" src="https://github.com/dnd-side-project/dnd-10th-4-frontend/assets/98106371/6cd41355-c863-4212-a5cb-1c20735052eb" />
</p>

- 패키지 매니저: npm (최신 LTS 버전)
- 개발 언어: TypeScript
- 번들러: Vite
- 라이브러리: React 18
- 상태 관리 : Zustand
- 스타일 라이브러리: Emotion
- 데이터 패칭: axios, TanStack Query
- Form 관리: React Hook Form, zod
- 배포: Vercel, Chromatic
- UI 문서화: Storybook
  <br/>

## 팀 소개

<table align="center">
    <tbody>
        <tr>
            <td>
                <a href="https://github.com/bbearcookie">
                    <img src="https://avatars.githubusercontent.com/bbearcookie" width="100" height="100"/>
                </a>
            </td>
            <td>
                <a href="https://github.com/easyhyun00">
                    <img src="https://avatars.githubusercontent.com/easyhyun00" width="100" height="100"/>
                </a>  
            </td>
            <td>
                <a href="https://github.com/Dongwoongkim">
                    <img src="https://avatars.githubusercontent.com/Dongwoongkim" width="100px" height="100px"/>
                </a>
            </td>
            <td>
                <a href="https://github.com/shinjaewon99">
                    <img src="https://avatars.githubusercontent.com/shinjaewon99" width="100px" height="100px"/>
                </a>  
            </td>
            <td><img src="https://placehold.co/100" width="100px" height="100px"/></td>
            <td><img src="https://placehold.co/100" width="100px" height="100px"/></td>
        </tr>
        <tr>
            <th>
                <a href="https://github.com/bbearcookie">이상훈</a>
            </th>
            <th>
                <a href="https://github.com/easyhyun00">이지현</a>
            </th>
            <th>
                <a href="https://github.com/Dongwoongkim">김동웅</a>
            </th>
            <th>
                <a href="https://github.com/shinjaewon99">신재원</a>
            </th>
            <th>
                신지예
            </th>
            <th>
                박예원
            </th>
        </tr>
        <tr>
            <th>
                FrontEnd
            </th>
            <th>
                FrontEnd
            </th>
            <th>
                BackEnd
            </th>
            <th>
                BackEnd
            </th>
            <th>
                Design
            </th>
            <th>
                Design
            </th>
        </tr>
    </tbody>
</table>
