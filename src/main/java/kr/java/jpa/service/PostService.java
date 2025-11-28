package kr.java.jpa.service;

import kr.java.jpa.model.entity.Post;
import kr.java.jpa.model.entity.PostLike;
import kr.java.jpa.model.entity.UserInfo;
import kr.java.jpa.model.entity.UserLogin;
import kr.java.jpa.model.repository.PostLikeRepository;
import kr.java.jpa.model.repository.PostRepository;
import kr.java.jpa.model.repository.UserInfoRepository;
import kr.java.jpa.model.repository.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor // 필수 생성자를 자동으로 만들어지게
@Transactional(readOnly = true) // 쓰기가 아닌 transaction을 기본으로
public class PostService {
    private final UserInfoRepository userInfoRepository;
    private final PostRepository postRepository;

//    게시글 목록 조회 (N:1)
    public List<Post> getAllPosts() {
//        return postRepository.findAll();
        return postRepository.findAllWithAuthor(); // N+1 문제 방지를 위한 코딩
    }

    // 게시글 작성 (1:N)
    @Transactional
    public Long createPost(String title, String content, Long authorId) {
        UserInfo author = userInfoRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));
        Post post = Post.builder()
                .title(title)
                .content(content)
                .build();
        post.setAuthor(author); // 연관관계 편의 메서드
        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    // 검색
    public List<Post> getPostsByTitle(String keyword) {
        return postRepository.findByTitleContainingOrderByCreatedAtDesc(keyword);
    }

    private final PostLikeRepository postLikeRepository;

    // 좋아요 토글 (M:N) ON <-> OFF.
    @Transactional
    public boolean toggleLike(Long userInfoId, Long postId) {
        // 존재 여부
        UserInfo userInfo = userInfoRepository.findById(userInfoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));
        // Like
        if (isLiked(userInfoId, postId)) {
            // 좋아요 취소
            postLikeRepository.deleteByUserInfoIdAndPostId(userInfoId, postId);
            post.decreaseLikeCount(); // post repo를 안써도 자동으로 dirty checking
            return false; // 좋아요 상태가 false로 변환
        } else {
            // 좋아요 추가
            PostLike postLike = PostLike.createPostLike(userInfo, post);
            postLikeRepository.save(postLike);
            post.increaseLikeCount();
            return true;
        }
    }

    public boolean isLiked(Long userInfoId, Long postId) {
        return postLikeRepository
                .existsByUserInfoIdAndPostId(userInfoId, postId);
    }
}
