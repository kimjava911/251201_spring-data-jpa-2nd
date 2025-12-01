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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // 필수 생성자를 자동으로 만들어지게
@Transactional(readOnly = true) // 쓰기가 아닌 transaction을 기본으로
public class PostService {
    private final UserInfoRepository userInfoRepository;
    private final PostRepository postRepository;

    // 1. 페이징된 게시글 목록 조회
    public Page<Post> getPostsPage(int page, int size, String sortBy, String direction) {
        // Sort 객체 생성 (정렬 기준과 방향)
        // equalsIgnoreCase -> 대소문자 구분 X
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending(); // 기본설정을 이걸로 두겠다
        // ID 기준

        // Pageable 객체 생성 (페이지 번호, 크기, 정렬)
        Pageable pageable = PageRequest.of(page, size, sort);
        // page : 나눠진 chunk -> 몇 번째 묶음을 불러오겠냐
        // size : 몇 개씩 나누겠냐

        // 페이징 쿼리 실행
        return postRepository.findAllWithAuthor(pageable);
    }

    // 2. 검색 + 페이징
    public Page<Post> searchPostsPage(String keyword, int page, int size) {
        // 기본 정렬: 최신순
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        // Sort.by -> attribute 지정
        return postRepository.findByTitleContaining(keyword, pageable);
    }

    // 3. 사용자별 게시글 + 페이징
    public Page<Post> getUserPostsPage(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdAt").descending());
        return postRepository.findByAuthorId(userId, pageable);
    }


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
                .likeCount(0)
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

    // 게시글 상세조회
    public Post getPostDetail(Long id) {
        return  postRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));
    }

    // Query Method -> Service Layer

    // 1. 통합 검색 (제목 or 내용)
    public List<Post> searchByTitleOrContent(String keyword) {
        return postRepository
                .findByTitleContainingOrContentContainingOrderByCreatedAtDesc(
                        keyword, keyword
                ); // 2번 언급되는게 거슬린다 -> jpql
    }
    // 2. 기간별 게시글 조회
    public List<Post> searchByCreatedAt(LocalDateTime start, LocalDateTime end) {
        return postRepository
                .findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
    }
    // 3. 인기 게시글 조회 (좋아요 수 기준)
    public List<Post> getPopularPosts(Integer minLikes) {
        return postRepository
                .findByLikeCountGreaterThanEqualOrderByLikeCountDesc(minLikes);
    }
    // 4. 고급 검색 (키워드 + 최소 좋아요 수)
    public List<Post> advancedSearch(String keyword, Integer minLikes) {
        return postRepository.searchPosts(keyword, minLikes);
    }
    // 5. 게시글 존재 여부 확인
    public boolean existsPostByTitle(String title) {
        return postRepository.existsByTitle(title);
    }
    // 6. 사용자별 게시글 개수 조회
    public long countUserPosts(Long userId) {
        return postRepository.countByAuthorId(userId);
    }
}
