<%@ page import="kr.java.jpa.model.entity.UserInfo" %>
<%@ page import="kr.java.jpa.model.entity.Post" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>인기 게시글</title>
</head>
<body>
<h1>인기 게시글 (좋아요 <%= request.getAttribute("minLikes") %>개 이상)</h1>
<%
    UserInfo ui = (UserInfo) session.getAttribute("userInfo");
%>
<nav>
    <a href="<%= request.getContextPath() %>/posts">전체 글 목록</a><br>
    <a href="<%= request.getContextPath() %>/logout">로그아웃</a><br>
    <p>별명: <%= ui.getNickname() %></p>
</nav>

<!-- 좋아요 필터 폼 -->
<form action="<%= request.getContextPath() %>/posts/popular" method="get">
    <label>최소 좋아요 수:</label>
    <input type="number" name="minLikes"
           value="<%= request.getAttribute("minLikes") %>" min="0">
    <button>필터</button>
</form>
<hr>

<!-- 게시글 목록 -->
<%
    List<Post> posts = (List<Post>) request.getAttribute("posts");
    if (posts.isEmpty()) {
%>
<p>조건에 맞는 게시글이 없습니다.</p>
<%
} else {
    for (Post p : posts) {
%>
<div style="border: 1px solid #ddd; padding: 10px; margin: 10px 0;">
    <h3><%= p.getTitle() %></h3>
    <p>작성자: <%= p.getAuthor().getNickname() %></p>
    <p>좋아요: <strong><%= p.getLikeCount() %></strong>개</p>
    <p>내용: <%= p.getContent().length() > 100 ?
            p.getContent().substring(0, 100) + "..." :
            p.getContent() %></p>
    <p>작성일: <%= p.getCreatedAt() %></p>
    <a href="<%= request.getContextPath() %>/posts/<%= p.getId() %>">
        자세히보기
    </a>
</div>
<%
        }
    }
%>
</body>
</html>