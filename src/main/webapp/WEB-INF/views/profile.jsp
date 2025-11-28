<%@ page import="kr.java.jpa.model.entity.UserInfo" %>
<%@ page import="kr.java.jpa.model.entity.Post" %>
<%@ page import="kr.java.jpa.model.entity.PostLike" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>JPA 게시판 프로젝트</title>
</head>
<body>
<h1>JPA 게시판 프로젝트</h1>
<%
    UserInfo ui = (UserInfo) session.getAttribute("userInfo");
%>
    <nav>
        <a href="<%= request.getContextPath() %>/logout">로그아웃</a><br>
        <a href="<%= request.getContextPath() %>/posts/new">글 작성</a><br>
        <p> 별명 : <%= ui.getNickname() %> </p>
    </nav>

    <h2>작성글</h2>
    <% UserInfo withPosts = (UserInfo) request.getAttribute("withPosts");
        for (Post p : withPosts.getPosts()) {
    %>
        <div>
            <p>제목 : <%= p.getTitle() %></p>
            <p>작성자 : <%= p.getAuthor().getNickname() %></p>
            <p>내용 : <%= p.getContent() %></p>
            <a href="<%= request.getContextPath() %>/posts/<%= p.getId() %>">자세히보기</a>
            <hr>
        </div>
    <%
        }
    %>

<h2>좋아요한 글</h2>
<% UserInfo withLikedPosts = (UserInfo) request.getAttribute("withLikedPosts");
    for (Post p : withLikedPosts.getPostLikes()
            .stream().map(PostLike::getPost).toList()) {
%>
<div>
    <p>제목 : <%= p.getTitle() %></p>
    <p>작성자 : <%= p.getAuthor().getNickname() %></p>
    <p>내용 : <%= p.getContent() %></p>
    <a href="<%= request.getContextPath() %>/posts/<%= p.getId() %>">자세히보기</a>
    <hr>
</div>
<%
    }
%>
</body>
</html>
