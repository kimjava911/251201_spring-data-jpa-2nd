package kr.java.jpa.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user_info")
//@Data
@NoArgsConstructor @AllArgsConstructor @Builder // 생성자 관련
@Getter @Setter // 접근자
//@ToString // 출력 -> 순환참조
//public class UserInfo {
public class UserInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 100)
    private String email;

//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    public void onCreate() {
//        this.createdAt = LocalDateTime.now();
//    }

    // OneToOne + UserLogin -> 1:1 연관관계 인식
    @OneToOne(fetch = FetchType.LAZY)
    // UserLogin ID를 어떤 컬럼명으로 도입할 것인가
    @JoinColumn(name = "login_id", unique = true, nullable = false)
    private UserLogin userLogin;

    // 연관관계 편의 메서드
    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
        if (userLogin != null) {
            userLogin.setUserInfo(this);
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
//                ", createdAt=" + createdAt +
                ", createdAt=" + getCreatedAt() +
                '}';
    }

    // 1:N 관계
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    // N:M 관계
    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostLike> postLikes = new ArrayList<>();
}