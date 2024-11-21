create database if not exists karim DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
use karim;

-- alter table attractions drop column content_id;
-- alter table attractions drop column map_level;
-- alter table attractions drop column tel;
-- alter table attractions drop column overview;
-- alter table attractions drop column homepage;
-- ALTER TABLE `attractions` CHANGE COLUMN `no` `id` INT NOT NULL AUTO_INCREMENT ;

-- 회원 테이블
drop table if exists member;
CREATE TABLE `member` (
  `id` bigint NOT NULL,
  `nickname` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
);

-- 관광지 테이블
create table attraction (
	id int,
    name varchar(500),
    address varchar(100),
    latitude decimal(20, 17),
    longitude decimal(20, 17)
);

-- 여행 계획 테이블
drop table if exists plan;
CREATE TABLE plan (
  `id` int primary key not null auto_increment, -- 계획 id
  `user_id` VARCHAR(45) NOT NULL, -- 계획 추가한 사용자
  `name` VARCHAR(45) NOT NULL, -- 계획 이름
  `start_date` DATE not null, -- 관광 시작 날짜
  `end_date` DATE not null -- 관광 마지막 날짜
);

drop table if exists place;
CREATE TABLE place (
  `id` int primary key not null auto_increment, -- 추가한 관광지의 id
  `plan_id` int NOT NULL, -- 계획의 id
  `attr_id` int NOT NULL, -- 관광지 no (PK)
  `plan_date` DATE not null, -- 관광지 방문 일자
  `ord` int not null -- 관광지 날짜별 방문 순서
);

-- 게시판 테이블
-- create table board (
-- 	id int primary key auto_increment,
--     title varchar(50) not null,
--     content varchar(10000) not null,
--     member_id varchar(30) not null,
--     hit int not null default 0,
--     upload_date timestamp default current_timestamp()
-- );

commit;