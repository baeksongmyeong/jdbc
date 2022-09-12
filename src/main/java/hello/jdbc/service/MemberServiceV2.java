package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

// Transaction 처리를 위해 Service 계층에서 Connection 을 획득하고, Repository 에 제공 후, Connection 을 반환하는 형태로 구성
@Slf4j
public class MemberServiceV2 {

    // DB 연결 및 SQL 처리
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;


    public MemberServiceV2(DataSource dataSource, MemberRepositoryV2 memberRepository) {
        this.dataSource = dataSource;
        this.memberRepository = memberRepository;
    }

    // 계좌이체 메서드
    public void accountTransfer(String fromId, String toId, Integer money) throws SQLException {

        // Connection 획득
        Connection conn = dataSource.getConnection();

        try {

            // 트랜잭션 시작
            conn.setAutoCommit(false);

            log.info("업무 로직 트랜잭션 시작 ----------------------------------------------------------------------------------------------------");

            // 업무 처리
            bizLogic(conn, fromId, toId, money);

            log.info("업무 로직 트랜잭션 종료 ----------------------------------------------------------------------------------------------------");

            // 트랜잭션 정상 종료 - Commit
            conn.commit();

        } catch (Exception e) {

            // 트랜잭션 비정상 종료 - Rollback
            conn.rollback();
            log.error("트랜잭션 처리중 예외 발생", e);
            throw new IllegalStateException(e);

        } finally {

            // Connection 반환
            releaseConnection(conn);

        }
    }

    private void bizLogic(Connection conn, String fromId, String toId, Integer money) throws SQLException {
        // 송신자 조회
        Member fromMember = memberRepository.findById(conn, fromId);

        // 수신자 조회
        Member toMember = memberRepository.findById(conn, toId);

        // 송신자 차감
        memberRepository.update(conn, fromMember.getMemberId(), fromMember.getMoney() - money);

        // 오류 케이스를 일부러 만들어놓음
        validation(toMember);

        // 수신자 증가
        memberRepository.update(conn, toMember.getMemberId(), toMember.getMoney() + money);
    }

    // AutoCommit 자동으로 변경
    // Connection 반환
    private void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(true); // AutoCommit 상태 자동으로 변경
                conn.close();
            } catch (Exception e) {
                log.error("Connection Close 예외 발생", e);
            }
        }
    }

    private void validation(Member member) {
        if (member.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
