package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;

import java.sql.SQLException;

public class MemberServiceV1 {

    // DB 연결 및 SQL 처리
    private final MemberRepositoryV1 memberRepository;

    public MemberServiceV1(MemberRepositoryV1 memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 계좌이체 메서드
    public void accountTransfer(String fromId, String toId, Integer money) throws SQLException {
        
        // 트랜잭션 시작점
        
        // 송신자 조회
        Member fromMember = memberRepository.findById(fromId);

        // 수신자 조회
        Member toMember = memberRepository.findById(toId);

        // 송신자 차감
        memberRepository.update(fromMember.getMemberId(), fromMember.getMoney() - money);
        
        // 오류 케이스를 일부러 만들어놓음
        validation(toMember);

        // 수신자 증가
        memberRepository.update(toMember.getMemberId(), toMember.getMoney() + money);

        // 트랜잭션 종료점
    }

    private void validation(Member member) {
        if (member.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
