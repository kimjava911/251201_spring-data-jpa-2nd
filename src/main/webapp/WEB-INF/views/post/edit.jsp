<%@ page import="kr.java.jpa.model.entity.UserInfo" %>
<%@ page import="kr.java.jpa.model.entity.Post" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시글 수정</title>
</head>
<body>
<h1>게시글 수정</h1>
<%
    UserInfo ui = (UserInfo) session.getAttribute("userInfo");
    Post post = (Post) request.getAttribute("post");
%>
<nav>
    <a href="<%= request.getContextPath() %>/posts/<%= post.getId() %>">취소</a><br>
    <a href="<%= request.getContextPath() %>/">홈으로</a><br>
</nav>

<form action="<%= request.getContextPath() %>/posts/<%= post.getId() %>/edit"
      method="post">
    <div>
        <label>제목:</label><br>
        <input type="text" name="title" value="<%= post.getTitle() %>"
               style="width: 500px;" required>
    </div>
    <div style="margin-top: 10px;">
        <label>내용:</label><br>
        <textarea name="content" rows="10" style="width: 500px;" required><%= post.getContent() %></textarea>
    </div>
    <div style="margin-top: 10px;">
        <button type="submit">수정</button>
    </div>
</form>

<!-- Audit 정보 표시 -->
<hr>
<div style="color: #666; font-size: 0.9em;">
    <p>작성일: <%= post.getCreatedAt() %></p>
    <p>작성자 ID: <%= post.getCreatedBy() %></p>
    <% if (post.getUpdatedAt() != null) { %>
    <p>최종 수정일: <%= post.getUpdatedAt() %></p>
    <p>최종 수정자 ID: <%= post.getUpdatedBy() %></p>
    <% } %>
</div>
</body>
</html>
