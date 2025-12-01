package kr.java.jpa.model.repository;

import kr.java.jpa.model.entity.UserInfo;
import kr.java.jpa.model.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

   // findByIdWithLogin <- 직접 JPQL로 작성
   @Query(
           """
            SELECT ui FROM UserInfo ui
                JOIN FETCH ui.userLogin
            WHERE ui.id = :id
            """)
   // DB 레벨에서 미리 JOIN 해달라
   Optional<UserInfo> findByIdWithLogin(@Param("id") Long id);

   // UserLogin - id
   Optional<UserInfo> findByUserLoginId(Long loginId);

   // 작성한 게시글 목록
    @Query(
            """
            SELECT DISTINCT ui FROM UserInfo ui
                JOIN FETCH ui.posts
            WHERE ui.id = :authorId
            """
    )
    UserInfo findAuthorWithPosts(@Param("authorId") Long authorId);

   // 좋아요한 게시글 목록
   @Query(
           """
           SELECT DISTINCT ui FROM UserInfo ui
               JOIN FETCH ui.postLikes pl
               JOIN FETCH pl.post
           WHERE ui.id = :userId
           """
   )
   UserInfo findAuthorWithLikedPosts(@Param("userId") Long userId);

   // Query Method

    // 1. 이메일로 사용자 조회
   Optional<UserInfo> findByEmail(String email);

   // 2. 닉네임으로 검색 (부분 일치)
   List<UserInfo> findByNicknameContaining(String keyword);

   // 3. 특정 가입한 사용자 조회
    @Query(
        """
        SELECT ui FROM UserInfo ui
            WHERE ui.createdAt BETWEEN :start AND :end
            ORDER BY ui.createdAt DESC
        """
    )
   List<UserInfo> findByUserRegisteredBetween(
       @Param("start") LocalDateTime start,
       @Param("end") LocalDateTime end
   );

   // 4. 게시글 수가 특정 개수 이상인 활성 사용자
   @Query(
           """
           SELECT ui FROM UserInfo ui
               WHERE SIZE(ui.posts) >= :minPosts
               ORDER BY SIZE(ui.posts) DESC
           """
   )
   // SIZE -> jpql 내장 함수
   List<UserInfo> findActiveUsers(
           @Param("minPosts") int minPosts
   );
}
