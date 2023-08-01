package cafitac.circuitbreakerpractice.application;

import static org.assertj.core.api.Assertions.assertThat;

import cafitac.circuitbreakerpractice.member.application.MemberService;
import cafitac.circuitbreakerpractice.member.domain.Member;
import cafitac.circuitbreakerpractice.member.domain.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Test
    void getMember() {
        // given
        final Member member = memberRepository.save(Member.builder()
            .username("username")
            .password("password")
            .build());

        // when
        final Member findMember = memberService.getMember(member.getId());

        // then
        assertThat(findMember.getUsername()).isEqualTo("common");
    }
}
