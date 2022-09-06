package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("con1={}, class={}", con1, con1.getClass());
        log.info("con2={}, class={}", con2, con2.getClass());
    }

    // DriverManager 를 이용한 Connection 획득 부분을 스프링이 인터페이스화하여 제공한 클래스를 테스트
    @Test
    void driverManagerDataSource() throws SQLException {
        // DriverManagerDataSource 는 DataSource 인터페이스를 구현한 것이므로 DataSource 인터페이스를 타입으로 사용할 수 있다
        //DriverManagerDataSource ds = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(dataSource);
    }

    // 별도의 Connection Pool 인스턴스를 사용하여 Connection 획득 기능을 사용. 마찬가지로 DataSource 인터페이스를 구현한 것이다.
    @Test
    void dataSourceConnectionPool() throws SQLException, InterruptedException {
        // Hikari CP 를 이용한 커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        // 커넥션 획득
        useDataSource(dataSource);
        
        // 커넥션 풀에 커넥션 추가 로그 확인용 슬립 설정
        Thread.sleep(1000);
    }
    
    // 데이터소스로부터 커넥션을 획득하는 메서드
    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource.getConnection();
        log.info("con1={}, class={}", con1, con1.getClass());
        log.info("con2={}, class={}", con2, con2.getClass());
        Connection con3 = dataSource.getConnection();
        Connection con4 = dataSource.getConnection();
        Connection con5 = dataSource.getConnection();
        Connection con6 = dataSource.getConnection();
        Connection con7 = dataSource.getConnection();
        Connection con8 = dataSource.getConnection();
        Connection con9 = dataSource.getConnection();
    }

}
