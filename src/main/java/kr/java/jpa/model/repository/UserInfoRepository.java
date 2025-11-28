package kr.java.jpa.model.repository;

import kr.java.jpa.model.entity.UserInfo;
import kr.java.jpa.model.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
