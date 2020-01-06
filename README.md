# Toby's Spring 3
> 토비의 스프링 3 책의 내용을 직접 타이핑 해본다.

## 사전 조건
- 책은 `토비의 스프링 3`로 진행한다.
- 책의 버전은 3.0.3.RELEASE 였는데 현재 최선 버전인 5.2.2.RELEASE로 진행한다.
- Databases는 MySQL이 아닌 MariaDB를 사용하고 Docker로 구동한다.
- [Install MariaDB on Docker](docs/install-mariadb-on-docker.md)

## 1장 오브젝트 이해 관계
```
create table users (
    id varchar(10) primary key,
    name varchar(20) not null,
    password varchar(10) not null
)
```

## 5장 서비스 추상화
```
alter table users add column level int not null;
alter table users add column login int not null;
alter table users add column recommend int not null;
```