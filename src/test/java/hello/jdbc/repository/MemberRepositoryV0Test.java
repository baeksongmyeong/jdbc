package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void crud() throws SQLException {

        // delete
        repository.delete("memberV0");

        // delete validate
        Assertions.assertThatThrownBy(() -> repository.findById("memberV0")).isInstanceOf(NoSuchElementException.class);

        // save
        Member member = new Member("memberV0", 10000);
        repository.save(member);

        // select
        Member findMember = repository.findById(member.getMemberId());
        log.info("findMember={}", findMember);

        // valid
        Assertions.assertThat(findMember).isEqualTo(member);
        Assertions.assertThat(findMember).isNotSameAs(member);

        // update
        repository.update("memberV0", 20000);

        // select
        Member updateMember = repository.findById(member.getMemberId());
        log.info("updateMember={}", updateMember);

        Assertions.assertThat(updateMember.getMoney()).isEqualTo(20000);
    }

}