package kr.java.jpa.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="user_info")
//@Data
@NoArgsConstructor @AllArgsConstructor @Builder // 생성자 관련
@Getter @Setter // 접근자
@ToString // 출력
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 100)
    private String email;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_id", unique = true, nullable = false)
    private UserLogin userLogin;

    // 연관관계 편의 메서드
    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
        if (userLogin != null) {
            userLogin.setUserInfo(this);
        }
    }
}