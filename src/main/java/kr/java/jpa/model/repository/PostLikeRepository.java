package kr.java.jpa.model.repository;

import kr.java.jpa.model.entity.Post;
import kr.java.jpa.model.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    // M:N 관계 확인
    boolean existsByUserInfoIdAndPostId(Long userId, Long postId);

    // M:N 관계 삭제
    void deleteByUserInfoIdAndPostId(Long userId, Long postId);

    // M:N 관계 조회
    Optional<PostLike> findByUserInfoIdAndPostId(Long userId, Long postId);

    // 좋아요 수
    long countByPostId(Long postId); // count. <- 특정한 게시물의 좋아요 개수
}
