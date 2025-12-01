<%@ page import="kr.java.jpa.model.entity.UserInfo" %>
<%@ page import="kr.java.jpa.model.entity.Post" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시글 상세</title>
</head>
<body>
<h1>게시글 상세</h1>
<%
    UserInfo ui = (UserInfo) session.getAttribute("userInfo");
    Post p = (Post) request.getAttribute("post");
    boolean isLiked = (boolean) request.getAttribute("isLiked");
%>
<nav>
    <a href="<%= request.getContextPath() %>/posts/page">목록으로</a><br>
    <a href="<%= request.getContextPath() %>/logout">로그아웃</a><br>
    <p>별명: <%= ui.getNickname() %></p>
</nav>

<div style="border: 1px solid #ddd; padding: 20px; margin: 20px 0;">
    <h2><%= p.getTitle() %></h2>
    <p>작성자: <%= p.getAuthor().getNickname() %></p>
    <hr>
    <div style="white-space: pre-wrap;">
        <%= p.getContent() %>
    </div>
    <hr>
    <p>작성일: <%= p.getCreatedAt() %></p>
    <% if (p.getUpdatedAt() != null &&
            !p.getUpdatedAt().equals(p.getCreatedAt())) { %>
    <p style="color: #666;">
        (최종 수정: <%= p.getUpdatedAt() %>)
    </p>
    <% } %>
    <p>좋아요: <strong><%= p.getLikeCount() %></strong>개</p>

    <!-- 좋아요 버튼 -->
    <form action="<%= request.getContextPath() %>/posts/<%= p.getId() %>/like"
          method="post" style="display: inline;">
        <button><%= isLiked ? "좋아요 취소" : "좋아요" %></button>
    </form>

    <!-- 수정 버튼 (작성자만) -->
    <% if (p.getAuthor().getId().equals(ui.getId())) { %>
    <a href="<%= request.getContextPath() %>/posts/<%= p.getId() %>/edit">
        <button type="button">수정</button>
    </a>
    <% } %>
</div>

<!-- 좋아요한 사람들 목록 -->
<div>
    <h3>좋아요한 사람들</h3>
    <%
        if (p.getPostLikeList().isEmpty()) {
    %>
    <p>아직 좋아요한 사람이 없습니다.</p>
    <%
    } else {
    %>
    <ul>
        <%
            for (var like : p.getPostLikeList()) {
        %>
        <li><%= like.getUserInfo().getNickname() %></li>
        <%
            }
        %>
    </ul>
    <%
        }
    %>
</div>

<!-- Audit 상세 정보 -->
<div style="margin-top: 30px; padding: 10px; background-color: #f5f5f5;
                border-radius: 5px; font-size: 0.9em;">
    <h4>상세 정보</h4>
    <p>작성일시: <%= p.getCreatedAt() %></p>
    <p>작성자 ID: <%= p.getCreatedBy() %></p>
    <% if (p.getUpdatedAt() != null) { %>
    <p>최종 수정일시: <%= p.getUpdatedAt() %></p>
    <p>최종 수정자 ID: <%= p.getUpdatedBy() %></p>
    <% } %>
</div>
</body>
</html>
