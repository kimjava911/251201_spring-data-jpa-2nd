<%@ page import="kr.java.jpa.model.entity.UserInfo" %>
<%@ page import="kr.java.jpa.model.entity.Post" %>
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

    <% Post p = (Post) request.getAttribute("post"); %>
        <div>
            <p>제목 : <%= p.getTitle() %></p>
            <p>작성자 : <%= p.getAuthor().getNickname() %></p>
            <p>내용 : <%= p.getContent() %></p>
            <p>작성일시 : <%= p.getCreatedAt() %></p>
            <p>좋아요 수 : <%= p.getLikeCount() %></p>
            <p>좋아요한 사람들 : <%= p.getPostLikeList().stream()
                    .map((li) -> li.getUserInfo().getNickname()).toList() %></p>
            <form action="<%= request.getContextPath() %>/posts/<%= p.getId() %>/like" method="post">
                <button>
                    <% boolean isLiked = (boolean) request.getAttribute("isLiked"); %>
                    <%= isLiked ? "좋아요 취소" : "좋아요" %>
                </button>
            </form>
        </div>
</body>
</html>
