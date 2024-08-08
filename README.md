# 🛒 전자 상거래 플랫폼 (쇼핑몰)

## ❓ 프로젝트 설명
구매자회원과 판매자회원이 있는 쇼핑몰 사이트

### 📖  레퍼런스
✓ [쿠팡](https://www.coupang.com)

✓ [무신사](https://www.musinsa.com)

## 👀 프로젝트 기능
<details>
<summary>
회원관련
</summary>

- 로그인
- 로그아웃
- 회원가입 (구매자)
- 회원가입 (판매자)
- 회원정보 조회 (구매자)
- 회원정보 조회 (판매자)
- 비밀번호 변경
- 회원정보 변경
- 회원탈퇴

</details>

<details>
<summary>
상품관련
</summary>

- 상품등록 (판매자)
- 상품정보 변경 (판매자)
- 상품삭제 (판매자)
- 본인이 등록한 상품목록 전체조회 (판매자)
- 상품검색
- 특정 상품정보 조회

</details>

<details>
<summary>
결제관련
</summary>

- 결제생성
- 결제취소
- 결제상태 변경
- 결제조회

</details>

<details>
<summary>
주문관련
</summary>

- 주문생성 (구매자)
- 주문상태 변경 (판매자)
- 특정 주문번호의 주문정보 조회
- 특정 상품번호의 모든 주문정보 조회 (판매자)
- 본인의 주문 목록 전체 조회 (구매자)
- 주문취소 (구매자)
- 구매결정 (구매자)

</details>


## 📅  일정
<details>
<summary>
0주차 (~7/17)
</summary>

- 프로젝트 전체적인 구상
    - 도메인 구상
    - 기능 구상
    - 일정 계획

</details>

<details>
<summary>
1주차 (7/18 ~ 7/24)
</summary>

- 데이터베이스 설계 및 생성
- 프로젝트 생성
- User, Seller, Authority, UserAuthority Entity 추가
- 회원가입 (구매자) 구현
- 회원가입 (판매자) 구현
- 로그인 구현
- 로그아웃 구현

</details>

<details>
<summary>
2주차 (7/25 ~ 7/31)
</summary>

- 회원정보 조회 (구매자) 구현
- 회원정보 조회 (판매자) 구현
- 회원탈퇴
- 비밀번호 변경
- 회원정보 변경
- 인증 및 권한 (Spring Security)
- Category, Product Entity 추가
- 상품등록
- 특정 상품정보 조회
- 상품삭제
- 상품정보 변경
- 본인이 등록한 상품목록 전체조회
- 상품검색
- Payment Entity 추가
- 결제생성
- 결제취소
- 결제상태 변경
- 결제조회

</details>

<details>
<summary>
3주차 (8/1 ~ 8/7)
</summary>

- 전역예외처리 추가 및 커스텀 예외 처리
- SpringSecurity 예외 처리
- Purchase Entity 추가
- 주문생성
- 주문상태 변경
- 특정 주문번호의 주문정보 조회
- 특정 상품번호의 모든 주문정보 조회
- 본인의 주문 목록 전체 조회
- 주문취소
- 구매결정

</details>

<details>
<summary>
4주차 (8/8 ~ 8/11)
</summary>

- readme 추가
- swagger 추가
- 전체 코드보완 및 정리
- 테스트 코드 작성

</details>


## 💻 프로젝트 설계

### [데이터베이스 설계]
<details>
<summary>ERD</summary>
<img width="500" alt="스크린샷 2024-08-06 오후 5 18 59" src="https://github.com/user-attachments/assets/b97a5b3b-edb3-4864-9d30-ab06b19d2906">
</details>

<details>
<summary>
스키마
</summary>

### 회원 (user)
| 컬럼명        | 타입           | 제약조건                                  |
| ------------- | -------------- | ----------------------------------------- |
| id            | BIGINT         | PK, AI                                    |
| email         | VARCHAR(100)   | UQ, NN, UK_USER_EMAIL                     |
| password      | VARCHAR(255)   | NN                                        |
| name          | VARCHAR(50)    | NN                                        |
| phone_number  | VARCHAR(20)    | NN                                        |
| address       | VARCHAR(255)   | NN                                        |
| created_at    | TIMESTAMP      | DEFAULT CURRENT_TIMESTAMP, NN             |

### 판매자회원 (seller)
| 컬럼명          | 타입           | 제약조건                             |
| --------------- | -------------- | ------------------------------------ |
| id              | BIGINT         | PK, AI                               |
| user_id         | BIGINT         | FK, UQ, NN, UK_SELLER_USER           |
| company_name    | VARCHAR(100)   | NN                                    |
| business_number | VARCHAR(20)    | UQ, NN, UK_SELLER_BUSINESS_NUMBER    |

### 권한 (authority)
| 컬럼명 | 타입         | 제약조건                       |
| ------ | ------------ | ------------------------------ |
| id     | BIGINT       | PK, AI                         |
| type   | VARCHAR(20)  | UQ, NN, UK_AUTHORITY_TYPE      |

### 유저권한 (user_authority)
| 컬럼명       | 타입   | 제약조건                      |
| ------------ | ------ | ----------------------------- |
| id           | BIGINT | PK, AI                        |
| user_id      | BIGINT | FK, NN, UQ(복합)              |
| authority_id | BIGINT | FK, NN, UQ(복합)              |

### 상품 카테고리 (category)
| 컬럼명 | 타입         | 제약조건                   |
| ------ | ------------ | -------------------------- |
| id     | BIGINT       | PK, AI                     |
| name   | VARCHAR(100) | UQ, NN, UK_CATEGORY_NAME   |

### 상품 (product)
| 컬럼명        | 타입                   | 제약조건                                       |
| ------------- | ---------------------- | ---------------------------------------------  |
| id            | BIGINT                 | PK, AI                                         |
| category_id   | BIGINT                 | FK                                             |
| seller_id     | BIGINT                 | FK                                             |
| name          | VARCHAR(255)           | NN                                             |
| image         | VARCHAR(255)           |                                                |
| description   | TEXT                   |                                                |
| price         | BIGINT UNSIGNED        | NN                                             |
| shipping_cost | INT UNSIGNED           | NN                                             |
| stock         | INT UNSIGNED           | NN                                             |
| status        | ENUM('판매예정', '판매중', '판매중지', '품절') | NN, DEFAULT '판매예정'         |
| created_at    | TIMESTAMP              | DEFAULT CURRENT_TIMESTAMP, NN                  |
| updated_at    | TIMESTAMP              | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, NN |

### 결제 (payment)
| 컬럼명    | 타입                 | 제약조건                                        |
| --------- | -------------------- | ---------------------------------------------- |
| id        | BIGINT               | PK, AI                                         |
| amount    | BIGINT UNSIGNED      | NN                                             |
| method    | ENUM('카드', '계좌이체') | NN                                          |
| status    | ENUM('정상결제', '환불완료') | NN, DEFAULT '정상결제'                    |
| created_at| TIMESTAMP            | DEFAULT CURRENT_TIMESTAMP, NN                  |
| updated_at| TIMESTAMP            | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, NN |

### 주문 (purchase)
| 컬럼명           | 타입                   | 제약조건                                       |
| ---------------- | ---------------------- | --------------------------------------------- |
| id               | BIGINT                 | PK, AI                                        |
| product_id       | BIGINT                 | FK, NN                                        |
| user_id          | BIGINT                 | FK, NN                                        |
| seller_id        | BIGINT                 | FK, NN                                        |
| payment_id       | BIGINT                 | FK, NN                                        |
| price            | BIGINT UNSIGNED        | NN                                            |
| quantity         | INT UNSIGNED           | NN                                            |
| shipping_cost    | INT UNSIGNED           | NN                                            |
| shipping_address | VARCHAR(255)           | NN                                            |
| request          | VARCHAR(255)           |                                               |
| status           | ENUM('결제완료', '배송중', '구매확정', '취소요청', '취소완료', '반품요청', '반품완료', '교환요청', '교환완료') | NN, DEFAULT '결제완료' |
| created_at       | TIMESTAMP              | DEFAULT CURRENT_TIMESTAMP, NN                  |
| updated_at       | TIMESTAMP              | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, NN |
</details>


### [기술설명]
<details>
  <summary>기술 스택</summary>

- **Java 17**
- **Spring Boot 3.3.2**
    - **Spring Boot Starter Data JPA**
    - **Spring Boot Starter Web**
    - **Spring Boot Starter Validation**
    - **Spring Boot Starter Security**
- **SpringDoc OpenAPI 2.3.0**
- **MySQL**
- **Lombok**

</details>

## 📕 API문서
[API문서](https://krSeonghyeon.github.io/shop-project/shopapi-docs.html)
