package kr.java.jpa.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
//@ToString
//public class Post {
public class Post extends BaseEntity { // 작성자/수정자, 작성일시/수정일시
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content; // columnDefinition
    // Long/long -> BIGINT
    // int -> INT
    // String -> VARCHAR

    @Column(name = "like_count")
    private Integer likeCount = 0;

//    @Column(name = "created_at", updatable = false)
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//    }

    // N:1 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false) // Unique X -> 1:N, N:1...
    private UserInfo author;

    // 연관관계 편의 메서드
    public void setAuthor(UserInfo author) {
        this.author = author;
        if (author != null) {
            author.getPosts().add(this);
        }
    }

    // M:N <- 중간 엔티티
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private final List<PostLike> postLikeList = new ArrayList<>();

    // 비즈니스 로직 (count)
    public void increaseLikeCount() {
        if (likeCount == null) {
            likeCount = 0;
        }
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
