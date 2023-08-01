package cafitac.circuitbreakerpractice.member.application;

import cafitac.circuitbreakerpractice.member.domain.Member;
import cafitac.circuitbreakerpractice.member.domain.MemberRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @HystrixCommand(fallbackMethod = "getCommonMember", commandProperties = {
        @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
        @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
        @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "20"),
        @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
        @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000")
        // 10 초 동안 10번 호출 중 20% 실패시(2번 실패시) 10초간 fallback 메소드 호출
        // 단, 해당 메소드가 3초 안에 끝나지 않을시 fallback 메소드 호출
    })
    public Member getMember(final Long memberId) {
        try {
            Thread.sleep(4000);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return memberRepository.findById(memberId)
            .orElseThrow(RuntimeException::new);
    }

    private Member getCommonMember(final Long memberId) {
        return Member.builder()
            .username("common")
            .password("password")
            .build();
    }
}
