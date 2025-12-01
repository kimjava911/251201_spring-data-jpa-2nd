<%@ page import="kr.java.jpa.model.entity.UserInfo" %>
<%@ page import="kr.java.jpa.model.entity.Post" %>
<%@ page import="org.springframework.data.domain.Page" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>검색 결과</title>
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
        }
    </style>
</head>
<body>
<h1>검색 결과</h1>
<%
    UserInfo ui = (UserInfo) session.getAttribute("userInfo");
    Page<Post> postsPage = (Page<Post>) request.getAttribute("postsPage");
    String keyword = (String) request.getAttribute("keyword");
%>
<nav>
    <a href="<%= request.getContextPath() %>/posts/page">전체 목록</a><br>
    <a href="<%= request.getContextPath() %>/">홈으로</a><br>
</nav>

<!-- 검색 폼 -->
<form action="<%= request.getContextPath() %>/posts/search/page" method="get">
    <input type="text" name="keyword" value="<%= keyword %>" placeholder="검색어 입력">
    <button>검색</button>
</form>

<!-- 검색 결과 정보 -->
<p>
    '<%= keyword %>' 검색 결과: 총 <%= postsPage.getTotalElements() %>개
    (<%= postsPage.getNumber() + 1 %> / <%= postsPage.getTotalPages() %>페이지)
</p>

<!-- 게시글 목록 -->
<%
    if (postsPage.isEmpty()) {
%>
<p>검색 결과가 없습니다.</p>
<%
} else {
    for (Post p : postsPage.getContent()) {
%>
<div style="border: 1px solid #ddd; padding: 10px; margin: 10px 0;">
    <h3><%= p.getTitle() %></h3>
    <p>작성자: <%= p.getAuthor().getNickname() %></p>
    <p>작성일: <%= p.getCreatedAt() %></p>
    <a href="<%= request.getContextPath() %>/posts/<%= p.getId() %>">
        자세히보기
    </a>
</div>
<%
        }
    }
%>

<!-- 페이징 -->
<div class="pagination">
    <% if (postsPage.hasPrevious()) { %>
    <a href="?keyword=<%= keyword %>&page=<%= postsPage.getNumber() - 1 %>">이전</a>
    <% } %>

    <%
        for (int i = 0; i < postsPage.getTotalPages(); i++) {
            if (i == postsPage.getNumber()) {
    %>
    <span class="current"><%= i + 1 %></span>
    <%
    } else {
    %>
    <a href="?keyword=<%= keyword %>&page=<%= i %>"><%= i + 1 %></a>
    <%
            }
        }
    %>

    <% if (postsPage.hasNext()) { %>
    <a href="?keyword=<%= keyword %>&page=<%= postsPage.getNumber() + 1 %>">다음</a>
    <% } %>
</div>
</body>
</html>