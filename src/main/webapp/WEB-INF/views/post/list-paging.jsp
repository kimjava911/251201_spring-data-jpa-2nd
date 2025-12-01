<%@ page import="kr.java.jpa.model.entity.UserInfo" %>
<%@ page import="kr.java.jpa.model.entity.Post" %>
<%@ page import="org.springframework.data.domain.Page" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시글 목록 (페이징)</title>
    <style>
        .pagination {
            display: flex;
            gap: 5px;
            margin: 20px 0;
        }
        .pagination a, .pagination span {
            padding: 5px 10px;
            border: 1px solid #ddd;
            text-decoration: none;
            color: #333;
        }
        .pagination .current {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }
        .sort-options {
            margin: 10px 0;
        }
    </style>
</head>
<body>
<h1>게시글 목록 (페이징)</h1>
<%
    UserInfo ui = (UserInfo) session.getAttribute("userInfo");
    Page<Post> postsPage = (Page<Post>) request.getAttribute("postsPage");
    int currentPage = (Integer) request.getAttribute("currentPage");
    String sortBy = (String) request.getAttribute("sortBy");
    String direction = (String) request.getAttribute("direction");
%>
<nav>
    <a href="<%= request.getContextPath() %>/">홈으로</a><br>
    <a href="<%= request.getContextPath() %>/logout">로그아웃</a><br>
    <p>별명: <%= ui.getNickname() %></p>
</nav>

<!-- 정렬 옵션 -->
<div class="sort-options">
    <form action="<%= request.getContextPath() %>/posts/page" method="get">
        <label>정렬 기준:</label>
        <select name="sortBy">
            <option value="createdAt" <%= sortBy.equals("createdAt") ? "selected" : "" %>>
                작성일
            </option>
            <option value="likeCount" <%= sortBy.equals("likeCount") ? "selected" : "" %>>
                좋아요 수
            </option>
            <option value="title" <%= sortBy.equals("title") ? "selected" : "" %>>
                제목
            </option>
        </select>

        <label>정렬 방향:</label>
        <select name="direction">
            <option value="desc" <%= direction.equals("desc") ? "selected" : "" %>>
                내림차순
            </option>
            <option value="asc" <%= direction.equals("asc") ? "selected" : "" %>>
                오름차순
            </option>
        </select>

        <label>페이지 크기:</label>
        <select name="size">
            <option value="5">5개</option>
            <option value="10" selected>10개</option>
            <option value="20">20개</option>
        </select>

        <button>적용</button>
    </form>
</div>

<!-- 페이지 정보 표시 -->
<p>
    전체 <%= postsPage.getTotalElements() %>개 게시글 중
    <%= currentPage + 1 %> 페이지
    (총 <%= postsPage.getTotalPages() %>페이지)
</p>

<!-- 게시글 목록 -->
<%
    if (postsPage.isEmpty()) {
%>
<p>게시글이 없습니다.</p>
<%
} else {
    for (Post p : postsPage.getContent()) {
%>
<div style="border: 1px solid #ddd; padding: 10px; margin: 10px 0;">
    <h3><%= p.getTitle() %></h3>
    <p>작성자: <%= p.getAuthor().getNickname() %></p>
    <p>좋아요: <%= p.getLikeCount() %>개</p>
    <p>작성일: <%= p.getCreatedAt() %></p>
    <a href="<%= request.getContextPath() %>/posts/<%= p.getId() %>">
        자세히보기
    </a>
</div>
<%
        }
    }
%>

<!-- 페이징 네비게이션 -->
<div class="pagination">
    <!-- 이전 페이지 -->
    <% if (postsPage.hasPrevious()) { %>
    <a href="<%= request.getContextPath() %>/posts/page?page=<%= currentPage - 1 %>&sortBy=<%= sortBy %>&direction=<%= direction %>">
        이전
    </a>
    <% } %>

    <!-- 페이지 번호들 -->
    <%
        int totalPages = postsPage.getTotalPages();
        int startPage = Math.max(0, currentPage - 2);
        int endPage = Math.min(totalPages - 1, currentPage + 2);

        for (int i = startPage; i <= endPage; i++) {
            if (i == currentPage) {
    %>
    <span class="current"><%= i + 1 %></span>
    <%
    } else {
    %>
    <a href="<%= request.getContextPath() %>/posts/page?page=<%= i %>&sortBy=<%= sortBy %>&direction=<%= direction %>">
        <%= i + 1 %>
    </a>
    <%
            }
        }
    %>

    <!-- 다음 페이지 -->
    <% if (postsPage.hasNext()) { %>
    <a href="<%= request.getContextPath() %>/posts/page?page=<%= currentPage + 1 %>&sortBy=<%= sortBy %>&direction=<%= direction %>">
        다음
    </a>
    <% } %>
</div>

<!-- 페이지 이동 -->
<form action="<%= request.getContextPath() %>/posts/page" method="get" style="margin-top: 20px;">
    <input type="hidden" name="sortBy" value="<%= sortBy %>">
    <input type="hidden" name="direction" value="<%= direction %>">
    <label>페이지 이동:</label>
    <input type="number" name="page" min="0" max="<%= totalPages - 1 %>"
           value="<%= currentPage %>" style="width: 60px;">
    <button>이동</button>
</form>
</body>
</html>
