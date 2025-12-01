package kr.java.jpa.controller;

import jakarta.servlet.http.HttpSession;
import kr.java.jpa.model.entity.UserInfo;
import kr.java.jpa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";
        model.addAttribute("withPosts", userService.getUserInfoWithPosts(loginUser.getId()));
        model.addAttribute("withLikedPosts", userService.getUserInfoWithLikedPosts(loginUser.getId()));
        return "profile";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String nickname,
            @RequestParam String email,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.register(username, password, nickname, email);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        UserInfo userInfo = userService.login(username, password);
        if (userInfo == null) {
            redirectAttributes.addFlashAttribute(
                    "error", "아이디 또는 비밀번호가 일치하지 않습니다");
            return "redirect:/login";
        }
        session.setAttribute("userInfo", userInfo);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
//        session.removeAttribute("userInfo");
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping
    public String index() {
        return "index";
    }

    // 1. 사용자 검색 페이지
    @GetMapping("/users/search")
    public String searchUsers(
            HttpSession session,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";

        // 키워드가 있으면 검색, 없으면 빈 리스트
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("users",
                    userService.searchUsersByNickname(keyword));
        }
        model.addAttribute("userInfo", loginUser);
        return "users/search";
    }

    // 2. 활성 사용자 목록
    @GetMapping("/users/active")
    public String activeUsers(
            HttpSession session,
            @RequestParam(defaultValue = "3") int minPosts,
            Model model
    ) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";

        // 최소 게시글 수 이상인 활동적인 사용자 조회
        model.addAttribute("users", userService.getActiveUsers(minPosts));
        model.addAttribute("userInfo", loginUser);
        model.addAttribute("minPosts", minPosts);
        return "users/active";
    }
}
