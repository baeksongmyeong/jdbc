package hello.jdbc.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
public class Member {
    private String memberId;
    private Integer money;

    public Member() {
    }

    public Member(String memberId, Integer money) {
        this.memberId = memberId;
        this.money = money;
    }

    @Override
    public String toString() {
        return "Member{memberId='" + memberId + '\'' + ", money=" + money + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId) && Objects.equals(money, member.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, money);
    }
}
