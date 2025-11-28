package kr.java.jpa.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="user_login")
//@Data
@NoArgsConstructor
@Getter @Setter
@ToString
@Builder
@AllArgsConstructor
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now(); // 현재 서버 시간
        // Audit
        // -> 트랜지션이나 여러 분산환경에서 작성 시에 꼬이는 문제를 해결하기 위해 '시간'을 기입
        // 1. DB 자체에서 지원하는 CURRENT_TIMESTAMP를 사용 -> auto ddl로 못만들어줌 (jpa에서는 권장하지 X)
        // 2. PrePersist -> 객체 생성이 아니라 영속성 컨텍스트에 들어가는 직전에 시간 기입...
        // 3. Listener -> Audit 설정을 하면 알아서 특정한 걸 상속한 Entity들에 알아서 Audit
    }

    // 1:1 관계의 주인 -> userLogin -> 외래키 보유
    @OneToOne(mappedBy = "userLogin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserInfo userInfo;
}
