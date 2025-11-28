package kr.java.jpa.service;

import kr.java.jpa.model.entity.UserInfo;
import kr.java.jpa.model.entity.UserLogin;
import kr.java.jpa.model.repository.UserInfoRepository;
import kr.java.jpa.model.repository.UserLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor // 필수 생성자를 자동으로 만들어지게
@Transactional(readOnly = true) // 쓰기가 아닌 transaction을 기본으로
public class UserService {
    private final UserLoginRepository userLoginRepository;
    private final UserInfoRepository userInfoRepository;
    // 생성자 주입 시 final 안만들어도 되는 건 boot, data 기능 X. lombok.

    // 회원가입 -> 1:1 -> UserLogin + UserInfo
    @Transactional
    public void register(
            String username,
            String password,
            String nickname,
            String email
    ) {
        // 중복체크 -> unique sql 에러로 처리?
        if (userLoginRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다");
        }
        // 1. UserLogin
        UserLogin userLogin = UserLogin
                .builder()
                .username(username)
                .password(password)
                .build();
        userLoginRepository.save(userLogin);
        // 2. UserInfo
        UserInfo userInfo = UserInfo
                .builder()
                .nickname(nickname)
                .email(email)
                .build();
        userInfo.setUserLogin(userLogin); // 연관관계 편의 메서드
        // 3. UserInfo 저장 -> cascade UserLogin도 자동저장
        userInfoRepository.save(userInfo);
        // -> save. upsert -> 있으면 merge. 없으면 persist.
    }

    // login
    public UserInfo login(String username, String password) {
        // Optional -> null 처리
        UserLogin userLogin = userLoginRepository.findByUsername(username)
                .orElse(null); // orElse -> null일 경우에 대체할 default 값
                // orElseThrow -> 에러를 내는 것.
        if (userLogin == null || !userLogin.getPassword().equals(password)) {
            return null;
        }
        return userInfoRepository.findByUserLoginId(userLogin.getId())
                .orElse(null);
        // orElse(null)보다는 orElseThrow를 통해서 예외처리
    }

    // userInfo - userLogin
    public UserInfo getUserInfo(Long id) {
        return userInfoRepository.findByIdWithLogin(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));
    }

    public UserInfo getUserInfoWithPosts(Long id) {
        return userInfoRepository.findAuthorWithPosts(id);
    }

    public UserInfo getUserInfoWithLikedPosts(Long id) {
        return userInfoRepository.findAuthorWithLikedPosts(id);
    }
}
