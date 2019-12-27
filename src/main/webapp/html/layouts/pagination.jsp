<%@ page import="com.delcache.component.UrlManager" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<%
    Map<String, Integer> pagination = (Map<String, Integer>) request.getAttribute("pagination");
    int total = pagination.get("total");
    int current = pagination.get("current");
    int totalPage = pagination.get("totalPage");
    int pageSize = pagination.get("pageSize");
    Integer[] sizes = new Integer[]{10, 20, 50, 100};

    int pageCount = 8;
    int leftCount = (int) Math.floor((double) pageCount / 2);
    int rightCount = leftCount;
    //偶数时
    if (pageCount % 2 == 0) {
        leftCount = rightCount - 1;
    }
    int startPage;
    int endPage;
    if (totalPage <= pageCount) {
        startPage = 1;
        endPage = totalPage;
    } else if (current + rightCount >= totalPage) {
        startPage = totalPage - pageCount + 1;
        endPage = totalPage;
    } else if (current - leftCount <= 0) {
        startPage = 1;
        endPage = pageCount;
    } else {
        startPage = current - leftCount;
        endPage = current + rightCount;
    }

%>
<div class="pagination-list">
    <div class="page-container">共<%= total %>条
        <select name="pageSize" style="margin-left: 1rem;margin-right: .5rem;" id="page-size">
            <% for (int i = 0; i < sizes.length; i++) {%>
            <option value="<%= sizes[i] %>" <%= sizes[i] == pageSize ? "selected" : ""%>><%= sizes[i] %>
            </option>
            <% }%>
        </select> 条/页
    </div>
    <ul class="pagination">
        <% if (endPage > 1) {
            if (current > 1) {
                params.put("page", String.valueOf(current - 1));
        %>
        <li>
            <a class="page-link" href="<%=UrlManager.createUrl(request.getRequestURI(),params)%>" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span><span class="sr-only">Previous</span>
            </a>
        </li>
        <li>
            <% params.put("page", "1");%>
            <a class="page-link" href="<%=UrlManager.createUrl(request.getRequestURI(),params)%>">首页</a>
        </li>
        <% }%>
        <%
            for (int i = startPage; i <= endPage; i++) {
                params.put("page", String.valueOf(i));
        %>
        <li class="<%= i==current?"active":""%>">
            <a class="page-link" href="<%=UrlManager.createUrl(request.getRequestURI(),params)%>">
                <%= i %>
            </a>
        </li>
        <% }%>
        <%
            if (current < totalPage) {
                params.put("page", String.valueOf(totalPage));
        %>
        <li>
            <a class="page-link" href="<%=UrlManager.createUrl(request.getRequestURI(),params)%>">末页</a>
        </li>
        <li>
            <% params.put("page", String.valueOf(current + 1));%>
            <a class="page-link" href="<%=UrlManager.createUrl(request.getRequestURI(),params)%>" aria-label="Next">
                <span aria-hidden="true">&raquo;</span><span class="sr-only">Next</span>
            </a>
        </li>
        <% }%>
        <% }%>
    </ul>
</div>