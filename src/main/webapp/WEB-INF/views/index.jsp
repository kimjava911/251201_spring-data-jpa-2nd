<%@ page import="kr.java.jpa.model.entity.UserInfo" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>JPA 게시판 프로젝트</title>
</head>
<body>
    <h1>JPA 게시판 프로젝트</h1>
    <%
        UserInfo ui = (UserInfo) session.getAttribute("userInfo");
        if (ui == null) {
    %>
        <a href="<%= request.getContextPath() %>/register">회원가입</a><br>
        <a href="<%= request.getContextPath() %>/login">로그인</a><br>
    <% } else { %>
        <a href="<%= request.getContextPath() %>/logout">로그아웃</a><br>
        <a href="<%= request.getContextPath() %>/profile">프로필</a><br>
        <p> 별명 : <%= ui.getNickname() %> </p>
        <a href="<%= request.getContextPath() %>/posts">글 목록</a><br>
        <a href="<%= request.getContextPath() %>/posts/new">글 작성</a><br>
    <% } %>
</body>
</html>
