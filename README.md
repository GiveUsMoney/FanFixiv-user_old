# FanFixiv 인증 서버

> 아직 미완성된 문서입니다.

## 컨벤션

```
# 파일, 변수, 함수, 클래스명
CamelCase
# db
snake_case (테이블 앞에는 무조건 tb_를 붙입니다)
```

## 기술 스택

- Spring boot

## 실행

> 빌드 후 진행해 주시길 바랍니다.

```
java -jar build/libs/auth-<version>.jar
```

## 빌드

```
./gradlew build
```

## 테스트

> 테스트의 경우 e2e 테스트만 작성합니다.

```
./gradlew test
```