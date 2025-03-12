# algo-analyzer-back

백준 문제 분석기 백엔드 서버

## 프로젝트 소개

이 프로젝트는 백준 문제에 대한 정보를 solved.ac API를 통해 조회하고, 문제를 분석하는 백엔드 서버입니다. 분석 요청은 FastAPI로 구성된 AI 서버에 전달되어 처리되며, 결과는 MongoDB Atlas에 저장됩니다. RESTful API를 통해 프론트엔드 또는 기타 클라이언트 애플리케이션과 통신합니다.

## 기능

- **문제 정보 조회**
  - 백준 사이트에서 문제 상세(문제 설명, 입력/출력 예시, 시간/메모리 제한 등) 크롤링 수행

- **문제 분석 요청 및 결과 처리**
  - 문제 정보를 기반으로 FastAPI 서버에 문제 분석 요청을 전달합니다.
  - FastAPI 서버로부터 분석 결과(시간 복잡도, 공간 복잡도, 알고리즘 유형 등)를 수신 후 MongoDB Atlas에 저장하고, API 응답으로 반환합니다.

- **RESTful API 제공**
  - 외부 클라이언트(예: 프론트엔드, 크롬 확장프로그램)와의 통신을 위한 다양한 RESTful 엔드포인트 제공

## 기술 스택

- **백엔드 프레임워크**: Java 17, Spring Boot 3.x
- **데이터 저장소**: MongoDB Atlas (Spring Data MongoDB)
- **API 연동**: solved.ac API, FastAPI (문제 분석 서버)
- **Docker**: Dockerfile 및 Docker Compose를 활용하여 서비스 컨테이너화
- **기타**: Spring Data, SLF4J, Lombok, Jsoup(크롤링)

## 환경 설정

### MongoDB Atlas 연결

- `src/main/resources/application.yml` 파일에서 MongoDB 연결 URI를 환경 변수로부터 동적으로 설정합니다.
- 실제 연결을 위해 다음 환경 변수를 설정해야 합니다:
  - `DB_USERNAME` : MongoDB Atlas 계정 사용자명
  - `DB_PASSWORD` : MongoDB Atlas 계정 비밀번호
- URI 예시:
  ```yaml
  spring:
    data:
      mongodb:
        uri: "mongodb+srv://${DB_USERNAME}:${DB_PASSWORD}@cluster0.aelwn.mongodb.net/algoanalyzer?retryWrites=true&w=majority&appName=Cluster0"
        database: algoanalyzer
  ```

### AI 서버 (FastAPI) 연동

- `python.api.base-url` 설정을 통해 FastAPI 서버의 주소를 지정합니다.
- 일반적으로 Docker Compose에서는 `AI_SERVER_URL` 환경 변수를 설정하여 사용합니다.
- 예시:
  ```yaml
  python:
    api:
      base-url: ${AI_SERVER_URL}  # 예: http://ai-server:5050
  ```

### 기타 설정

- `logging` 레벨 설정을 통해 디버그 및 오류 로그 출력을 제어합니다.
- Docker Compose와 환경 변수 파일(.env)을 활용하여 민감 정보를 관리합니다.

## Docker Compose

프로젝트는 Docker Compose를 통해 다음과 같이 구성됩니다:

- **app**: Spring Boot로 빌드된 백엔드 서버  
  - 포트: 8080  
  - 환경 변수: `SPRING_DATA_MONGODB_URI` (Atlas 연결용), `AI_SERVER_URL` 등
- **ai-server**: 문제 분석을 담당하는 FastAPI 서버 (별도의 Dockerfile/디렉토리로 구성)

`docker-compose.yml` 예시:
```yaml
version: '3.8'

services:
  app:
    container_name: backend
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      SPRING_DATA_MONGODB_URI: "mongodb+srv://${DB_USERNAME}:${DB_PASSWORD}@cluster0.aelwn.mongodb.net/algoanalyzer?retryWrites=true&w=majority&appName=Cluster0"
      AI_SERVER_URL: "http://ai-server:5050"
    depends_on:
      - ai-server

  ai-server:
    container_name: ai-server
    build:
      context: ../algo-analyzer-ai  # AI 서버의 디렉토리 경로
      dockerfile: Dockerfile
    ports:
      - "5050:5050"
```

## 실행 방법

1. **환경 변수 설정**  
   `.env` 파일 또는 환경변수를 통해 `DB_USERNAME`, `DB_PASSWORD`, `AI_SERVER_URL` 등의 값을 설정합니다.

2. **Docker Compose로 빌드 및 실행**  
   다음 명령어로 프로젝트를 실행합니다:
   ```bash
   docker-compose up --build
   ```

3. **로컬 실행 (IDE 또는 Gradle)**  
   Maven 또는 Gradle을 활용하여 `AlgoAnalyzerApplication.java`를 실행할 수 있습니다.

## 폴더 구조

```
algo-analyzer-back/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── algoanalyzer/
│   │   │           ├── AlgoAnalyzerApplication.java
│   │   │           ├── common/         # 공통 설정 및 예외 처리
│   │   │           ├── domain/         # 문제, 분석, API 등 도메인 관련 코드
│   │   │           └── ... 
│   │   └── resources/
│   │       └── application.yml         # 설정 파일 (MongoDB, 서버 포트, API 연동 등)
│   └── test/                           # 테스트 코드
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## 주의사항

- **MongoDB Atlas IP 화이트리스트**: Atlas 클러스터에 접속하기 위해 현재 머신 혹은 서버의 IP 주소를 Atlas에서 허용해야 합니다.
- **데이터 보안**: 환경 변수 및 민감 정보는 별도의 `.env` 파일로 관리하고 Git 저장소에는 포함시키지 않도록 주의합니다.

## 추가 정보

- 문제 조회, 분석, 저장 등 기능은 REST API 엔드포인트(`/api/problems`, `/api/analysis/problem` 등)를 통해 제공됩니다.
- 문제 분석 결과는 분석 요청 후 MongoDB에 캐싱되어, 동일한 문제에 대한 요청 시 DB에서 조회됩니다.
- FastAPI 서버와의 연동을 통해 문제에 대한 다양한 분석 결과(알고리즘 유형, 시간/공간 복잡도 등)를 제공받습니다.