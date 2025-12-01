package kr.java.jpa.controller;

import jakarta.servlet.http.HttpSession;
import kr.java.jpa.model.entity.UserInfo;
import kr.java.jpa.service.PostService;
import kr.java.jpa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public String postDetail(
            @PathVariable Long id,
            HttpSession session,
            Model model
    ) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";
        model.addAttribute("post", postService.getPostDetail(id));
        model.addAttribute("userInfo", loginUser);
        model.addAttribute("isLiked", postService.isLiked(loginUser.getId(), id));
        return "post/detail";
    }

    @PostMapping("/{id}/like")
    public String toggleLike(
            @PathVariable Long id,
            HttpSession session
    ) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";
        postService.toggleLike(loginUser.getId(), id);
        return "redirect:/posts/" + id;
    }

    @GetMapping("/search")
    public String postListByKeyword(HttpSession session,
                                    @RequestParam String keyword,
                                    Model model) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";
//        model.addAttribute("posts", postService.getAllPosts());
        model.addAttribute("posts", postService.getPostsByTitle(keyword));
        model.addAttribute("userInfo", loginUser);
        return "post/list"; // webapp/WEB-INF/views/post/list.jsp
    }


    @GetMapping
    public String postList(HttpSession session, Model model) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";
        model.addAttribute("posts", postService.getAllPosts());
        model.addAttribute("userInfo", loginUser);
        return "post/list"; // webapp/WEB-INF/views/post/list.jsp
    }

    @GetMapping("/new")
    public String createForm(
            HttpSession session
    ) {
        if (session.getAttribute("userInfo") == null) return "redirect:/login";
        return "post/form";
    }

    @PostMapping("/new")
    public String create(
            @RequestParam String title,
            @RequestParam String content,
            HttpSession session
    ) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";
        postService.createPost(title, content, loginUser.getId());
        return "redirect:/posts";
    }

    // 1. 통합 검색 (제목 또는 내용)
    @GetMapping("/search/all")
    public String searchAll(
            HttpSession session,
            @RequestParam String keyword,
            Model model
    ) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";

        // 제목 또는 내용에서 검색
        model.addAttribute("posts", postService.searchByTitleOrContent(keyword));
        model.addAttribute("userInfo", loginUser);
        model.addAttribute("searchType", "all");
        return "post/list";
    }

    // 2. 인기 게시글 조회
    @GetMapping("/popular")
    public String popularPosts(
            HttpSession session,
            @RequestParam(defaultValue = "5") Integer minLikes,
            Model model
    ) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";

        // 최소 좋아요 수 이상인 게시글 조회
        model.addAttribute("posts", postService.getPopularPosts(minLikes));
        model.addAttribute("userInfo", loginUser);
        model.addAttribute("minLikes", minLikes);
        return "post/popular";
    }

    // 3. 고급 검색
    @GetMapping("/search/advanced")
    public String advancedSearch(
            HttpSession session,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") Integer minLikes,
            Model model
    ) {
        UserInfo loginUser = (UserInfo) session.getAttribute("userInfo");
        if (loginUser == null) return "redirect:/login";

        // 키워드 + 최소 좋아요 수 조건 검색
        model.addAttribute("posts",
                postService.advancedSearch(keyword, minLikes));
        model.addAttribute("userInfo", loginUser);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minLikes", minLikes);
        return "post/list";
    }
}
