package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DBConnectionUtil {

    public static Connection getConnection() {
        // 프로젝트 생성시 의존성에 H2 Database 를 추가하였다.
        // 의존성 추가를 통해 해당 데이터베이스의 드라이버(JDBC 구현체) 가 프로젝트에 라이브러리로 추가된다.
        // DriverManager 는 프로젝트에 라이브러리로 등록되어 있는 드라이버들(JDBC 구현체) 의 목록을 자동으로 알고 있다.
        // 이 목록을 하나씩 호출하여, URL 과 매칭되는 드라이버(JDBC 구현체) 로부터 해당 DB 의 Connection 을 얻어온다.
        // Connection 객체의 이름은 DB 벤더사별로 다르다.
        // H2 는 org.h2.jdbc 패키지 내에 JdbcConnection 클래스의 인스턴스가 Connection 으로 제공된다.
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
            log.info("getConnection={}, getClass={}", connection, connection.getClass());
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return connection;
    }
}
