<%@ page import="kr.java.jpa.model.entity.UserInfo" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>사용자 검색</title>
</head>
<body>
<h1>사용자 검색</h1>
<%
    UserInfo ui = (UserInfo) session.getAttribute("userInfo");
%>
<nav>
    <a href="<%= request.getContextPath() %>/">홈으로</a><br>
    <a href="<%= request.getContextPath() %>/logout">로그아웃</a><br>
    <p>별명: <%= ui.getNickname() %></p>
</nav>

<!-- 검색 폼 -->
<form action="<%= request.getContextPath() %>/users/search" method="get">
    <input type="text" name="keyword" placeholder="닉네임 검색"
           value="<%= request.getParameter("keyword") != null ?
                       request.getParameter("keyword") : "" %>">
    <button>검색</button>
</form>
<hr>

<!-- 검색 결과 -->
<%
    List<UserInfo> users = (List<UserInfo>) request.getAttribute("users");
    if (users != null) {
        if (users.isEmpty()) {
%>
<p>검색 결과가 없습니다.</p>
<%
} else {
%>
<h2>검색 결과 (<%= users.size() %>명)</h2>
<%
    for (UserInfo u : users) {
%>
<div style="border: 1px solid #ddd; padding: 10px; margin: 10px 0;">
    <p><strong><%= u.getNickname() %></strong></p>
    <p>이메일: <%= u.getEmail() %></p>
    <p>가입일: <%= u.getCreatedAt() %></p>
</div>
<%
            }
        }
    }
%>
</body>
</html>