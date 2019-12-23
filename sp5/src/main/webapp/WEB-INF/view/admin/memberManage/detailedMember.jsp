<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<form id="deteailedMemberForm" name="deteailedMemberForm" method="post">
<table style="margin: 10px auto 0px; width: 100%; border-spacing: 1px; background: #cccccc">
<tr height="37" style="background: #ffffff;">
    <td align="right" width="17%" style="padding-right: 7px;"><label style="font-weight: 900;">아이디</label></td>
    <td align="left" width="33%" style="padding-left: 5px;"><span>${dto.userId}</span></td>
    <td align="right" width="17%" style="padding-right: 7px;"><label style="font-weight: 900;">회원번호</label></td>
    <td align="left" width="33%" style="padding-left: 5px;"><span>${dto.memberIdx}</span></td>
</tr>
<tr height="37" style="background: #ffffff;">
    <td align="right" style="padding-right: 9px;"><label style="font-weight: 900;">이름</label></td>
    <td align="left" style="padding-left: 5px;"><span>${dto.userName}</span></td>
    <td align="right" style="padding-right: 9px;"><label style="font-weight: 900;">생년월일</label></td>
    <td align="left" style="padding-left: 5px;"><span>${dto.birth}</span></td>
</tr>
<tr height="37" style="background: #ffffff;">
    <td align="right" style="padding-right: 9px;"><label style="font-weight: 900;">전화번호</label></td>
    <td align="left" style="padding-left: 5px;"><span>${dto.tel}</span></td>
    <td align="right" style="padding-right: 9px;"><label style="font-weight: 900;">이메일</label></td>
    <td align="left" style="padding-left: 5px;"><span>${dto.email}</span></td>
</tr>
<tr height="37" style="background: #ffffff;">
    <td align="right" style="padding-right: 9px;"><label style="font-weight: 900;">회원가입일</label></td>
    <td align="left" style="padding-left: 5px;"><span>${dto.created_date}</span></td>
    <td align="right" style="padding-right: 9px;"><label style="font-weight: 900;">최근로그인</label></td>
    <td align="left" style="padding-left: 5px;"><span>${dto.last_login}</span></td>
</tr>

<tr height="37" style="background: #ffffff;">
    <td align="right" width="17%" style="padding-right: 9px;"><label style="font-weight: 900;">권한</label></td>
    <td align="left" colspan="3" style="padding-left: 5px;">
		<c:forEach var="vo" items="${listAuthority}" varStatus="vs">
			${vo.authority}
			<c:if test="${! vs.first && ! vs.last}">,&nbsp;</c:if>
		</c:forEach>		
    </td>
</tr>

<tr height="37" style="background: #ffffff;">
    <td align="right" width="17%" style="padding-right: 9px;"><label style="font-weight: 900;">계정상태</label></td>
    <td align="left" colspan="3" style="padding-left: 5px;">
		${dto.enabled==1?"활성":"잠금"}
		<c:if test="${dto.enabled==0 && not empty memberState}">, ${memberState.memo}(${memberState.registration_date})</c:if>
    </td>
</tr>

</table>

<input type="hidden" name="memberIdx" value="${dto.memberIdx}">
<input type="hidden" name="userId" value="${dto.userId}">
</form>