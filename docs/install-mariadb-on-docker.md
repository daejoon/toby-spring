# Install MariaDB on Docker

### Mariadb UTF-8 환경 파일 만들기
```
[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqldump]
default-character-set=utf8

[mysqld]
collation-server = utf8_unicode_ci
init-connect='SET NAMES utf8'
character-set-server = utf8
```
- 파일이름은 상관없다. 확장자만 `.cnf`로 만들어 준다.
- 저장 위치를 기억해둔다. 아래 docker 실행시킬때 옵션 인자로 사용한다.

### Docker Compose
```
$ docker-compose up -d
```

### Root로 접속
```
$ mysql -P3307 -h127.0.0.1 -uroot -p 
```

### springbook 데이터베이스 생성
```
$ create database springbook;
$ show databases;
```

### spring user 생성
```
$ create user 'spring'@'%' identified by 'book'
```

### spring user 권한 생성
```
$ grant all privileges on springbook.* to 'spring'@'%';
$ flush privileges;
$ select * from user where User = 'spring';
```

