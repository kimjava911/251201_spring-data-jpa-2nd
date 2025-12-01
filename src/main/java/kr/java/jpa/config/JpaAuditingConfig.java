package kr.java.jpa.config;

import jakarta.servlet.http.HttpSession;
import kr.java.jpa.model.entity.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Configuration
@EnableJpaAuditing // JPA Auditing 활성화 -> BaseTimeEntity, BaseEntity
// BaseTimeEntity -> createdAt, updatedAt
public class JpaAuditingConfig {
    // 현재 로그인한 사용자 ID를 받아서 -> 해당 ID를 현재 작성/수정 ID로 넣고 싶다

    // auditor -> ID

    // 현재 로그인한 사용자의 ID를 반환하는 메서드
    @Bean
    public AuditorAware<Long> auditorProvider() {
        return () -> {
            // 현재 HTTP 요청 가져오기
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes == null) {
                return Optional.empty();  // 요청이 없으면 빈 값
            }

            // 세션에서 로그인 사용자 정보 가져오기
            HttpSession session = attributes.getRequest().getSession(false);
            if (session == null) {
                return Optional.empty();  // 세션이 없으면 빈 값
            }

            UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
            if (userInfo == null) {
                return Optional.empty();  // 로그인하지 않았으면 빈 값
            }

            // 로그인한 사용자의 ID 반환
            return Optional.of(userInfo.getId());
            // Optional<Long> -> isPresent -> 해당 ID를 작성자/수정자 ID로 넣음
        };
    }
}
