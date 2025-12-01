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
<h2>환영합니다, <%= ui.getNickname() %>님!</h2>
<h3>게시글 관련</h3>
<a href="<%= request.getContextPath() %>/posts">전체 글 목록</a><br>
<a href="<%= request.getContextPath() %>/posts/new">글 작성</a><br>
<a href="<%= request.getContextPath() %>/posts/popular">인기 게시글</a><br>

<h3>사용자 관련</h3>
<a href="<%= request.getContextPath() %>/profile">내 프로필</a><br>
<a href="<%= request.getContextPath() %>/users/search">사용자 검색</a><br>
<a href="<%= request.getContextPath() %>/users/active">활동적인 사용자</a><br>
<h3>계정</h3>
<a href="<%= request.getContextPath() %>/logout">로그아웃</a><br>
<% } %>
</body>
</html>