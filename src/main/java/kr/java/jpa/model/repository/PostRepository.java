package kr.java.jpa.model.repository;

import kr.java.jpa.model.entity.Post;
import kr.java.jpa.model.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시글 목록 조회 (작성자 정보 포함)
    @Query("SELECT p FROM Post p JOIN FETCH p.author ORDER BY p.createdAt DESC")
    List<Post> findAllWithAuthor();

    // 특정 사용자가 작성한 게시글 (1:N)
//    List<Post> findByAuthorId(Long authorId);
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    // 제목으로 검색 -> LIKE %keyword%
    List<Post> findByTitleContainingOrderByCreatedAtDesc(String keyword);

    // 게시글 상세 조회 (게시글 + 작성자 + 좋아요한 사용자 목록)
    @Query("""
    SELECT DISTINCT p FROM Post p
        JOIN FETCH p.author
        LEFT JOIN FETCH p.postLikeList pl
        LEFT JOIN FETCH pl.userInfo
        WHERE p.id = :id
    """)
    Optional<Post> findByIdWithDetails(@Param("id") Long id);

    // Query Method
    // 1. 제목과 내용으로 검색 + or 조건
    // findBy - 검색
    // Title (속성명) Containing (부분일치)
    // OR Content (속성명) Containing (부분일치)
    // OrderBy (정렬) CreatedAt (속성명) Desc (차순 - 내림차순 <- default 오름차순이므로 오름차순이면 명시하지 X)
    List<Post> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(
            String title, String content);

    // 2. 특정 기간 내 작성된 게시글 조회
    List<Post> findByCreatedAtBetweenOrderByCreatedAtDesc(
            LocalDateTime start, LocalDateTime end); // start부터(포함) end까지(포함)
    // findBy / CreatedAt / Between / OrderBy / CreatedAt / Desc

    // 3. 좋아요 수가 특정 값 이상인 게시글 조회
    List<Post> findByLikeCountGreaterThanEqualOrderByLikeCountDesc(Integer minCount);
    // findBy
    // LikeCount
    // GreaterThanEqual : GTE -> GreaterThan (초과, >), LessThan (미만, <)
    //                           GreaterThanEqual (이상, >=), LessThanEqual (이하, <=)
    // OrderByCreatedAtDesc

    // 4. 여러 작성자의 게시글 조회
    @Query("""
        SELECT p FROM Post p
                WHERE p.author.id
                        IN :authorIds
                ORDER BY p.createdAt DESC
        """)
    List<Post> findByAuthorInOrderByCreatedAtDesc(@Param("authorIds") List<Long> authorIds);

    // 5. 제목으로 게시글 존재 여부 확인
    boolean existsByTitle(String title);
    // existsBy -> T/F -> 존재여부

    // 6. 특정 작성자의 게시글 개수
    long countByAuthorId(Long authorId);
    // count -> 갯수 (집합연수)

    // 7. 복잡한 쿼리, Fetch -> 성능을 위한 쿼리
    @Query("""
        SELECT p FROM Post p
            JOIN FETCH p.author
            WHERE (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)
            AND p.likeCount >= :minLikes
            ORDER BY p.createdAt DESC
    """)
    List<Post> searchPosts(
            @Param("keyword") String keyword,
            @Param("minLikes") Integer minLikes
    );
}
