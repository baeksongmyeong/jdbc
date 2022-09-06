package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;


// Driver Manager 를 통한 DB Connection 획득, SQL 전송, ResultSet 수신
@Slf4j
public class MemberRepositoryV0 {

    // member 테이블 insert
    public Member save(Member member) throws SQLException {

        log.info("SAVE =================================================================");

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

        log.info("SELECT =================================================================");

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

        log.info("UPDATE =================================================================");

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

        log.info("DELETE =================================================================");

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

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

    // JDBC Connection, Statement, ResultSet 인스턴스 종료
    // 역순으로 종료
    private void close( Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {rs.close();} catch (SQLException e) {log.error("ResultSet close error", e);}
        }

        if (stmt != null) {
            try {stmt.close();} catch (SQLException e) {log.error("Statement close error", e);}
        }

        if (conn != null) {
            try {conn.close();} catch (SQLException e) {log.error("Connection close error", e);}
        }
    }

}
