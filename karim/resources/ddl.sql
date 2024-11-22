create database if not exists karim DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
use karim;

-- 회원 테이블
drop table if exists member;
CREATE TABLE member (
  id BIGINT NOT NULL,
  nickname VARCHAR(20) NOT NULL,
  profile_image_url VARCHAR(2083),
  PRIMARY KEY (id)
);

-- 관광지 테이블
drop table if exists attraction;
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
  `user_id` bigint NOT NULL, -- 계획 추가한 사용자
  `name` VARCHAR(45) NOT NULL, -- 계획 이름
  `cost` bigint,
  `content` VARCHAR(5000),
  `start_date` DATE not null, -- 관광 시작 날짜
  `end_date` DATE not null -- 관광 마지막 날짜
);

drop table if exists place;
CREATE TABLE place (
  `id` int primary key not null auto_increment, -- 추가한 관광지의 id
  `plan_id` int NOT NULL, -- 계획의 id
  `attr_id` int NOT NULL, -- 관광지 no (PK)
  `plan_date` DATE not null, -- 관광지 방문 일자
  `ord` int not null, -- 관광지 날짜별 방문 순서
  `cost` bigint default 0,
  `content` VARCHAR(5000) default ""
);

-- 게시판 테이블
drop table if exists board;
create table board (
	id int primary key not null auto_increment,
    user_id bigint not null,
    place_id int not null,
    title varchar(50) not null,
    content varchar(10000) not null,
    hit int not null default 0,
    upload_date timestamp default current_timestamp()
);

drop table if exists file;
create table `file` (
	id int primary key not null auto_increment,
    board_id int not null,
    save_folder VARCHAR(45) NULL,
	original_file VARCHAR(50) NULL,
	save_file VARCHAR(50) NULL
);
ALTER TABLE `karim`.`file`
  ADD CONSTRAINT `fk_board_id`
  FOREIGN KEY (`board_id`)
  REFERENCES `karim`.`board` (`id`)
  ON DELETE CASCADE;
-- ex: 게시글별 파일 목록 가져오기
-- select save_folder, original_file, save_file from file where board_id = 1;

drop table if exists comment;
create table `comment` (
	id int primary key not null auto_increment,
    user_id bigint not null,
    board_id int not null,
    content varchar(1000) not null
);
ALTER TABLE `karim`.`comment`
  ADD CONSTRAINT `fk_comment_board_id`
  FOREIGN KEY (`board_id`)
  REFERENCES `karim`.`board` (`id`)
  ON DELETE CASCADE;

-- 고객센터 테이블
drop table if exists help;
CREATE TABLE help (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(500) NOT NULL,
    answer TEXT
);

INSERT INTO help (question, answer) VALUES
('이 서비스는 무료인가요?', '네, 이 서비스는 완전히 무료로 제공됩니다.'),
('내 질문에 대한 답변은 얼마나 빨리 받을 수 있나요?', '보통 질문에 대한 답변은 24시간 이내에 받을 수 있습니다.'),
('회원 탈퇴는 어떻게 할 수 있나요?', '내 정보 페이지에서 "회원 탈퇴" 버튼을 클릭하면 탈퇴가 가능합니다.'),
('다른 사용자와 직접 연락할 수 있나요?', '현재로서는 사용자 간 직접 연락 기능은 제공되지 않습니다.'),
('이 서비스는 어떤 기술로 개발되었나요?', '이 서비스는 Java Spring Boot와 Vue.js를 사용하여 개발되었습니다.'),
('문의 사항이 있습니다. 어디로 연락하면 되나요?', 'timotheekim10@gmail.com으로 이메일을 보내주시면 됩니다.');

commit;