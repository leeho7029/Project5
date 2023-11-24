CREATE DATABASE pro05;

USE pro05;

CREATE TABLE role(
                     role_id INT PRIMARY KEY AUTO_INCREMENT,
                     role VARCHAR(255) DEFAULT NULL
);

INSERT INTO ROLE VALUES(DEFAULT, 'ADMIN');
INSERT INTO ROLE VALUES(DEFAULT, 'EMP');
INSERT INTO ROLE VALUES(DEFAULT, 'TEACHER');
INSERT INTO ROLE VALUES(99, 'USER');

CREATE TABLE user(
                     user_id INT PRIMARY KEY AUTO_INCREMENT,			-- 회원 번호 : 자동증가
                     active INT DEFAULT 1, 									-- 회원 상태 [ 0 : 탈퇴, 1 : 활동중, 2: 활동 정지]
                     login_id VARCHAR(255) NOT NULL,						-- 회원 로그인 아이디
                     user_name VARCHAR(255) NOT NULL,						-- 회원 이름
                     password VARCHAR(300) NOT NULL,						-- 회원 비밀번호
                     email VARCHAR(50) NOT NULL,							-- 회원 이메일
                     tel VARCHAR(20) NOT NULL,								-- 회원 전화번호
                     addr1 VARCHAR(200),										-- 회원 기본 주소
                     addr2 VARCHAR(100),										-- 회원 상세 주소
                     postcode VARCHAR(10),									-- 회원 우편 번호
                     reg_date DATETIME DEFAULT CURRENT_TIMESTAMP(),	-- 회원 가입일
                     birth DATE,													-- 회원 생일
                     pt INT DEFAULT 50,										-- 회원 매너온도
                     visited INT DEFAULT 0,									-- 회원 방문 횟수
                     role_id INT NOT NULL DEFAULT 99						-- 회원 권한 등급
);

CREATE VIEW userlist AS(
                       SELECT u.user_id AS user_id, u.active AS ACTIVE, u.login_id AS login_id, u.user_name AS user_name, u.password AS PASSWORD, u.role_id AS role_id, r.role AS roleNm
                       FROM user u
                                LEFT JOIN role r ON u.role_id = r.role_id
                           );

CREATE TABLE market(
                       marketNo INT AUTO_INCREMENT PRIMARY KEY,	-- 상품 번호
                       title VARCHAR(100) NOT NULL,	-- 제목
                       price int NOT NULL,		-- 가격
                       content VARCHAR(5000),	-- 설명
                       login_id VARCHAR(255) NOT NULL,	-- 작성자 id
                       active INT DEFAULT 0 NOT NULL,	-- 거래 상태(거래 완료 여부)
                       readable INT DEFAULT 0 NOT NULL,
                       conditions varchar(20) NOT NULL,	-- 상품 상태(최상 상 중 하)
                       regdate DATETIME DEFAULT CURRENT_TIMESTAMP,	-- 등록일
                       selected_address VARCHAR(200),     -- 선택 주소
                       detail_address VARCHAR(100),        -- 상세 주소
                       xdata DOUBLE,                      -- x
                       ydata DOUBLE                      -- y
);

CREATE TABLE photos(
                       photo_no int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       marketNo INT,
                       saveFolder VARCHAR(300) NOT NULL,
                       originFile VARCHAR(300) NOT NULL,
                       saveFile VARCHAR(300) NOT NULL
);

CREATE TABLE mainphoto(
                          mainphoto_no int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          marketNo INT,
                          saveFolder VARCHAR(300) NOT NULL,
                          originFile VARCHAR(300) NOT NULL,
                          saveFile VARCHAR(300) NOT NULL
);






CREATE VIEW detaillist AS
SELECT
    m.marketNo,
    m.title,
    m.price,
    m.content,
    m.login_id,
    m.active,
    m.conditions,
    m.regdate,
    m.selected_address,
    m.detail_address,
    m.xdata,
    m.ydata,
    p.saveFolder AS saveFolder,
    p.originFile AS originFile,
    p.saveFile AS saveFile
FROM
    market m
        LEFT JOIN photos p ON m.marketNo = p.marketNo;


CREATE VIEW totallist as
SELECT
    m.marketNo,
    m.title,
    m.price,
    m.content,
    m.login_id,
    m.active,
    m.conditions,
    m.regdate,
    m.selected_address,
    m.detail_address,
    m.xdata,
    m.ydata,
    p.saveFolder AS saveFolder,
    p.originFile AS originFile,
    p.saveFile AS saveFile,
    mp.saveFolder AS mainSaveFolder,
    mp.originFile AS mainOriginFile,
    mp.saveFile AS mainSaveFile
FROM
    market m
        LEFT JOIN photos p ON m.marketNo = p.marketNo
        LEFT JOIN mainphoto mp ON m.marketNo = mp.marketNo;




CREATE VIEW mainlist AS
SELECT
    m.marketNo AS marketNo,
    m.title,
    m.price,
    m.readable,
    m.content,
    m.login_id,
    m.active,
    m.conditions,
    m.regdate,
    m.selected_address,
    m.detail_address,
    m.xdata,
    m.ydata,
    mp.saveFolder AS saveFolder,
    mp.originFile AS originFile,
    mp.saveFile AS saveFile
FROM
    market m
        LEFT JOIN mainphoto mp ON m.marketNo = mp.marketNo;


CREATE TABLE request(
                        req_no INT AUTO_INCREMENT PRIMARY KEY,	-- 상품 번호
                        title VARCHAR(100) NOT NULL,	-- 제목
                        price int NOT NULL,		-- 가격
                        content VARCHAR(5000),	-- 설명
                        login_id VARCHAR(255) NOT NULL,	-- 작성자 id
                        active INT NOT NULL DEFAULT 0 ,	-- 거래 상태(거래 완료 여부)
                        readable INT DEFAULT 0 NOT NULL,
                        regdate DATETIME DEFAULT CURRENT_TIMESTAMP,	-- 등록일
                        addr VARCHAR(200) NOT NULL,
                        bookTitle VARCHAR(255) NOT NULL,
                        bookAuthor VARCHAR(255) NOT NULL,
                        publisher VARCHAR(255) NOT NULL,
                        bookImage VARCHAR(255) NOT NULL,
                        isbn VARCHAR(255) NOT NULL,
                        pubdate VARCHAR(255) NOT NULL,
                        discount VARCHAR(255) NOT NULL
);


CREATE TABLE notice(
                       no INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(300) NOT NULL,
                       content VARCHAR(1000) NOT NULL,
                       author INT,
                       regdate DATETIME DEFAULT CURRENT_TIME,
                       cnt INT DEFAULT 0
);

INSERT INTO notice VALUES (DEFAULT,'샘플 글 제목1  입니다.','여기는 샘플 글 1의 내용입니다.',1,DEFAULT, DEFAULT);
INSERT INTO notice VALUES (DEFAULT,'샘플 글 제목2  입니다.','여기는 샘플 글 2의 내용입니다.',1,DEFAULT, DEFAULT);
INSERT INTO notice VALUES (DEFAULT,'샘플 글 제목3  입니다.','여기는 샘플 글 3의 내용입니다.',1,DEFAULT, DEFAULT);
INSERT INTO notice VALUES (DEFAULT,'샘플 글 제목4  입니다.','여기는 샘플 글 4의 내용입니다.',1,DEFAULT, DEFAULT);
INSERT INTO notice VALUES (DEFAULT,'샘플 글 제목5  입니다.','여기는 샘플 글 5의 내용입니다.',1,DEFAULT, DEFAULT);
INSERT INTO notice VALUES (DEFAULT,'샘플 글 제목6  입니다.','여기는 샘플 글 6의 내용입니다.',1,DEFAULT, DEFAULT);

CREATE TABLE faq (
                     fno INT  PRIMARY KEY AUTO_INCREMENT ,
                     question VARCHAR(1000) NOT NULL,
                     author varchar(100),
                     answer VARCHAR(1000) NOT NULL,
                     cnt INT DEFAULT 0 NOT NULL
);

-- faq 더미 데이터 추가
INSERT INTO faq(question, answer) VALUES('웹사이트에 어떻게 가입하나요?', '회원 가입 페이지에서 필요한 정보를 입력하여 가입할 수 있습니다.');
INSERT INTO faq(question, answer) VALUES('게시물을 어떻게 작성하나요?', '커뮤니티 페이지의 "글 작성" 버튼을 클릭하여 게시물을 작성할 수 있습니다.');
INSERT INTO faq(question, answer) VALUES('다른 회원들의 게시물에 댓글을 달고 싶어요. 어떻게 해야 하나요?','게시물의 하단에 있는 "댓글 작성" 영역에 댓글을 입력하여 등록할 수 있습니다.');
INSERT INTO faq(question, answer) VALUES('제가 작성한 글이 갑자기 사라졌어요', '부적절한 내용은 관리자가 예고 없이 삭제할 수 있습니다. 커뮤니티 규칙을 지켜주세요:)');
INSERT INTO faq(question, answer) VALUES('개인 정보 보호 정책은 무엇인가요?', '웹사이트의 개인 정보 보호 정책은 "회원가입의 회원약관동의" 페이지에서 확인할 수 있습니다.');
INSERT INTO faq(question, answer) VALUES('문의사항이 있을 때 어떻게 연락할 수 있나요?', '고객지원의 "QnA" 페이지에서 문의글을 작성하여 문의할 수 있습니다.');
INSERT INTO faq(question, answer) VALUES('어떤 유형의 교육자료를 제공하나요?', '초등학교, 중학교 및 고등학교 학생들을 위한 교과서, 참고서 및 워크북을 포함한 다양한 교육자료를 제공합니다.');
INSERT INTO faq(question, answer) VALUES('책은 실물 및 디지털 형식으로 모두 제공되나요?', '네, 대부분의 책은 실물 및 디지털 형식으로 제공되며 다양한 학습 선호도를 고려합니다.');
INSERT INTO faq(question, answer) VALUES('책이나 주제에 대한 제안을 할 수 있나요?', '물론입니다! 저희는 모든 제안을 소중히 생각합니다. 고객 지원팀에 의견을 보내주시면 됩니다.');
INSERT INTO faq(question, answer) VALUES('책은 정기적으로 업데이트되나요?', '네, 정확성과 관련성을 보장하기 위해 최신 교과서 및 참고 자료의 최신 판을 제공하기 위해 노력하고 있습니다.');


-- QNA
CREATE TABLE qna(
                    qno int PRIMARY KEY AUTO_INCREMENT,   			-- 번호
                    title VARCHAR(100) NOT NULL,   					-- 제목
                    content VARCHAR(10000) NOT NULL,   				-- 내용`
                    author VARCHAR(16),   								-- 작성자
                    resdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 등록일
                    lev INT DEFAULT 0, 									-- 질문(0), 답변(1)
                    par INT DEFAULT 0);													-- 질문(자신 레코드의 qno), 답변(질문의 글번호)

-- 질문글
INSERT INTO qna VALUES (DEFAULT, '어떤 주제의 강의를 들어야 할까요?', '공부하고자 하는 분야에 적합한 강의 주제를 선택하기 위한 조언이 필요합니다.', 'test111' ,DEFAULT, DEFAULT, 1);
INSERT INTO qna VALUES (DEFAULT, '동영상 학습에서 집중력을 높이는 방법이 뭐가 있나요?', '동영상 강의를 보면서 집중력을 유지하고 향상시키는 방법에 대한 조언을 원합니다.', 'test11' ,DEFAULT, DEFAULT, 2);
INSERT INTO qna VALUES (DEFAULT, '어떻게 강의 동영상을 효율적으로 정리하고 학습할 수 있을까요?', '강의 동영상을 효과적으로 정리하고 학습에 활용하는 방법에 대한 조언이 필요합니다.', 'test12' ,DEFAULT, DEFAULT, 3);
INSERT INTO qna VALUES (DEFAULT, '동영상 강의 시간을 어떻게 최적화할 수 있을까요?', '동영상 강의를 효율적으로 활용하고, 공부 시간을 최적화하는 방법을 알고 싶습니다.', 'test122' ,DEFAULT, DEFAULT, 4);
INSERT INTO qna VALUES (DEFAULT, '동영상 강의를 볼 때 주의력을 유지하는 방법이 뭐가 있을까요?', '동영상 강의를 보면서 주의력을 높이고, 정보를 효과적으로 이해하는 방법에 대한 조언을 찾고 있습니다.', 'test21 ' ,DEFAULT, DEFAULT, 5);
INSERT INTO qna VALUES (DEFAULT, '강의 동영상을 저장하고 오프라인에서 어떻게 시청할 수 있을까요?', '강의 동영상을 저장하고, 인터넷 연결 없이 어떻게 시청할 수 있는 방법을 알고 싶습니다.', 'test92' ,DEFAULT, DEFAULT, 6);
INSERT INTO qna VALUES (DEFAULT, '동영상 강의를 활용하여 스스로 학습 스케줄을 계획하는 방법이 뭐가 있나요?', '동영상 강의를 활용하여 학습 일정을 계획하고, 학습 목표를 달성하는 방법에 대한 조언을 원합니다.', 'test61' ,DEFAULT, DEFAULT, 7);
INSERT INTO qna VALUES (DEFAULT, '강의 동영상을 더 깊이 이해하기 위한 질문 및 논의 점을 어떻게 찾을 수 있을까요?', '동영상 강의를 더 깊이 이해하고, 관련 질문을 찾는 방법을 알려주세요.', 'test81' ,DEFAULT, DEFAULT, 8);
INSERT INTO qna VALUES (DEFAULT, '동영상 강의를 보면서 메모를 어떻게 작성하고 정리할 수 있을까요?', '동영상 강의를 보면서 효과적인 메모 작성과 정리 방법을 알고 싶습니다.', 'test91' ,DEFAULT, DEFAULT,9);
INSERT INTO qna VALUES (DEFAULT, '동영상 강의를 효과적으로 검색하고 필요한 내용을 찾는 방법이 뭐가 있을까요?', '동영상 강의를 효과적으로 검색하고, 원하는 내용을 빠르게 찾는 방법을 알려주세요.', 'test71' ,DEFAULT, DEFAULT,10);

-- 답변글
INSERT INTO qna VALUES (DEFAULT, '어떤 주제의 강의를 들어야 할까요?','강의 주제를 선택할 때, 관심 있는 분야나 목표에 따라 선택하는 것이 중요합니다. 관심 있는 주제에 대한 학습은 더 효과적일 수 있으며, 목표를 달성하는데 도움이 됩니다.','admin', DEFAULT, 1,1);
INSERT INTO qna VALUES (DEFAULT, '동영상 학습에서 집중력을 높이는 방법이 뭐가 있나요?','집중력을 높이기 위해 정해진 시간 동안 주의를 집중하고, 학습 환경을 조절하고 휴식을 취하는 등의 방법을 사용할 수 있습니다.','admin', DEFAULT, 1,2);
INSERT INTO qna VALUES (DEFAULT, '어떻게 강의 동영상을 효과적으로 정리하고 학습할 수 있을까요?','동영상 강의를 정리하기 위해 요약 노트를 작성하고, 중요한 부분을 표시하며, 관련 자료를 정리하는 것이 도움이 됩니다.','admin', DEFAULT, 1,3);
INSERT INTO qna VALUES (DEFAULT, '동영상 강의 시간을 어떻게 최적화할 수 있을까요?','동영상 강의 시간을 최적화하기 위해 목표를 설정하고, 공부 시간을 일정하게 유지하며, 분량을 나누어 학습하는 것이 중요합니다.','admin', DEFAULT, 1,4);
INSERT INTO qna VALUES (DEFAULT, '동영상 강의를 볼 때 주의력을 유지하는 방법이 뭐가 있을까요?','주의력을 유지하기 위해 학습 공간을 정리하고, 동영상을 공부 목적으로 시청하며, 주의력 향상을 위한 기술을 사용할 수 있습니다.','admin', DEFAULT, 1,5);
INSERT INTO qna VALUES (DEFAULT, '강의 동영상을 저장하고 오프라인에서 어떻게 시청할 수 있을까요?','동영상을 저장하기 위해 온라인 다운로더를 사용하고, 저장된 동영상을 오프라인 모드에서 시청할 수 있는 앱을 활용할 수 있습니다.','admin', DEFAULT, 1,6);
INSERT INTO qna VALUES (DEFAULT, '동영상 강의를 활용하여 스스로 학습 스케줄을 계획하는 방법이 뭐가 있나요?','학습 스케줄을 계획하기 위해 목표를 설정하고, 시간을 할당하며, 주간 계획을 세우는 것이 도움이 됩니다.','admin', DEFAULT, 1,7);
INSERT INTO qna VALUES (DEFAULT, '강의 동영상을 더 깊이 이해하기 위한 질문 및 논의 점을 어떻게 찾을 수 있을까요?','동영상을 더 깊이 이해하기 위해 관련 서적을 찾거나, 동영상에서 제기된 질문을 따라가며 더 많은 정보를 탐구할 수 있습니다.','admin', DEFAULT, 1,8);
INSERT INTO qna VALUES (DEFAULT, '동영상 강의를 보면서 메모를 어떻게 작성하고 정리할 수 있을까요?','메모를 작성하고 정리하기 위해 중요한 내용을 요약하고, 주요 포인트를 강조하며, 메모를 주기적으로 정리하는 것이 도움이 됩니다.','admin', DEFAULT, 1,9);
INSERT INTO qna VALUES (DEFAULT, '동영상 강의를 효과적으로 검색하고 필요한 내용을 찾는 방법이 뭐가 있나요?','동영상을 검색하기 위해 키워드를 사용하고, 정확한 제목 또는 주제를 입력하며, 검색 결과를 필터링하는 방법을 사용하여 원하는 내용을 빠르게 찾을 수 있습니다.','admin', DEFAULT, 1,10);

CREATE TABLE report (
                        report_id INT PRIMARY KEY AUTO_INCREMENT, -- 신고 번호
                        marketNo INT,
                        req_no int,
                        title varchar(100),-- 게시글 번호
                        login_id  VARCHAR(255),
                        reporter VARCHAR(16), -- 신고자
                        reason VARCHAR(300), -- 이유
                        report_date DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE chatRoom(
                         roomId BIGINT PRIMARY KEY AUTO_INCREMENT,			-- 채팅방 자동증가
                         productId INT NOT NULL,									-- 상품 아이디
                         productTable VARCHAR(20) NOT NULL,						-- 상품 테이블
                         buyerId VARCHAR(255) NOT NULL,							-- 구매자 희망자
                         regDate DATETIME DEFAULT CURRENT_TIMESTAMP()		-- 채팅방 생성일
);


CREATE VIEW chatRoomView AS (
                            SELECT
                                r.roomId AS roomId,
                                r.productId AS productId,
                                r.productTable AS productTable,
                                r.buyerId AS buyerId,
                                u.user_name AS buyerName,
                                u.active AS buyerActive,
                                r.regDate AS regDate
                            FROM chatRoom r
                                     LEFT JOIN user u ON r.buyerId = u.login_id
                                );
CREATE TABLE chatList(
                         chatId BIGINT PRIMARY KEY AUTO_INCREMENT,			-- 채팅 내역 번호 자동 증가
                         senderId VARCHAR(255) NOT NULL,							-- 채팅 보내는 사람 아이디
                         sendDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,		-- 채팅 보낸 일자
                         message TEXT NOT NULL,										-- 채팅 내역
                         readYn BOOLEAN DEFAULT FALSE,							-- 읽음 여부
                         roomId BIGINT NOT NULL										-- 채팅방 번호
);
CREATE VIEW chatListView AS (SELECT r.chatId AS chatId, r.sendDate AS sendDate, r.message AS message, r.readYn AS readYn, r.roomId AS roomId, r.senderId AS senderId, u.user_name AS userName FROM chatList r LEFT JOIN user u ON r.senderId = u.login_id);
CREATE TABLE likes (
                       lno INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                       login_id VARCHAR(20) NOT NULL,
                       marketNo INT,
                       req_no INT,
                       liketime DATETIME DEFAULT CURRENT_TIMESTAMP
);