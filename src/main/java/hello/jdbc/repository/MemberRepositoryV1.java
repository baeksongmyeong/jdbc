package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;


// DriverManager 가 아닌, DataSource 사용 방식으로 변경
// JdbcUtils 사용
@Slf4j
public class MemberRepositoryV1 {

    private final DataSource dataSource;

    @Autowired
    public MemberRepositoryV1(DataSource dataSource) {
        log.info("datasource={}, getClass={}", dataSource, dataSource.getClass());
        this.dataSource = dataSource;
    }

    // member 테이블 insert
    public Member save(Member member) throws SQLException {

        // sql
        String sql = "insert into member (member_id, money) values ( ?, ? )";

        // connection 인터페이스
        Connection conn = null;
        // statement 인터페이스
        PreparedStatement pstmt = null;
        // resultSet 인터페이스
        ResultSet rs = null;

        try {
            // 내가 만든 DriverManager 를 통한 Connection 획득 메서드
            conn = this.getConnection();

            log.info("connection={}", conn.getClass());

            // Connection 에서 Statement 생성
            pstmt = conn.prepareStatement(sql);

            log.info("pstmt={}", pstmt.getClass());

            // Statement 에 Parameter 설정
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());

            // Statement 실행
            int resultCnt = pstmt.executeUpdate();
            log.info("resultCnt={}", resultCnt);

            // 반환
            return member;
            
        } catch (SQLException e) {
            log.error("DB error", e);
            throw e;
        } finally {
            // JDBC 인스턴스 반환은 finally 에서 실행
            this.close(conn, pstmt, rs);
        }
    }

    // member 테이블 select
    public Member findById(String memberId) throws SQLException {

        String sql = "select * from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                // 조회된 결과가 없는 경우, 예외 throw
                throw new NoSuchElementException("member not found memberId=" + memberId);
            } 

        } catch (SQLException e) {
            log.error("DB error", e);
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // member 테이블 update
    public void update(String memberId, Integer money) throws SQLException {

        String sql = "update member set money = ? where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultCnt = pstmt.executeUpdate();
            log.info("resultCnt={}", resultCnt);
        } catch (SQLException e) {
            log.error("DB error", e);
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // member 테이블 delete
    public void delete(String memberId) throws SQLException {

        String sql = "delete from member where member_id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = this.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, memberId);
            int resultCnt = pstmt.executeUpdate();
            log.info("resultCnt={}", resultCnt);
        } catch (SQLException e) {
            log.error("DB error", e);
            throw e;
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // DataSource 를 이용하여 Connection 을 얻는 메서드
    private Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("connection={}, getClass={}", connection, connection.getClass());
        return connection;
    }

    // JDBC Connection, Statement, ResultSet 인스턴스 종료
    // 역순으로 종료
    private void close( Connection conn, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(conn);
    }
}
