<%@ page import="kr.java.jpa.model.entity.UserInfo" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>활성 사용자</title>
</head>
<body>
<h1>활성 사용자 (게시글 <%= request.getAttribute("minPosts") %>개 이상)</h1>
<%
    UserInfo ui = (UserInfo) session.getAttribute("userInfo");
%>
<nav>
    <a href="<%= request.getContextPath() %>/">홈으로</a><br>
    <a href="<%= request.getContextPath() %>/logout">로그아웃</a><br>
    <p>별명: <%= ui.getNickname() %></p>
</nav>

<!-- 필터 폼 -->
<form action="<%= request.getContextPath() %>/users/active" method="get">
    <label>최소 게시글 수:</label>
    <input type="number" name="minPosts"
           value="<%= request.getAttribute("minPosts") %>" min="1">
    <button>필터</button>
</form>
<hr>

<!-- 사용자 목록 -->
<%
    List<UserInfo> users = (List<UserInfo>) request.getAttribute("users");
    if (users.isEmpty()) {
%>
<p>조건에 맞는 사용자가 없습니다.</p>
<%
} else {
    for (UserInfo u : users) {
%>
<div style="border: 1px solid #ddd; padding: 10px; margin: 10px 0;">
    <p><strong><%= u.getNickname() %></strong></p>
    <p>이메일: <%= u.getEmail() %></p>
    <p>작성 게시글 수: <strong><%= u.getPosts().size() %></strong>개</p>
    <p>가입일: <%= u.getCreatedAt() %></p>
</div>
<%
        }
    }
%>
</body>
</html>