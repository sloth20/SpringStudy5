<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>
<style type="text/css">
.ui-widget-header{
	background: none;
	border: none;
	height: 35px;
	line-height: 35px;
	border-bottom: 1px solid #cccccc;
	border-radius: 0px;
}


</style>

<script type="text/javascript">
$(function(){
	$("#highCategory").change(function(){
		$("form select[name=categoryNum]").find("option").remove();
		$("form select[name=categoryNum]").append("<option value=''>::과목 선택::</option>");
	
		if(!$(this).val()){
			return false;	
		}
		var num = $(this).val();
		
		$.ajax({
			type:"GET"
			,url:"<%=cp%>/sbbs/listsubcategory"
			,data:{categoryNum:num}
			,dataType:"JSON"
			,success:function(data){
				 for(var i=0;i<data.list.length;i++){
					 var categoryNum = data.list[i].categoryNum;
					 var category = data.list[i].category;
					 $("form select[name=categoryNum]").append("<option value='"+categoryNum+"'>"+category+"</option>");
				 }
			}
		});
	});
	
});
	
function sendOk() {
    var f = document.boardForm;

	var str = f.subject.value;
    if(!str) {
        alert("제목을 입력하세요. ");
        f.subject.focus();
        return;
    }

	str = f.content.value;
    if(!str) {
        alert("내용을 입력하세요. ");
        f.content.focus();
        return;
    }

	f.action="<%=cp%>/sbbs/${mode}";

    f.submit();
}

$(function(){
	$("#btnCategoryUpdate").click(function(){
		$("#category-dialog").dialog({
			modal:true,
			height:300,
			width:450,
			title:'카테고리 수정',
			close:function(event,ui){
				
			}
			
		});
	});
	
	// $('#category-dialog').dialog("close"); // 창 종료
});





</script>


<div class="body-container" style="width: 700px;">
	<div class="body-title">
		<h3><i class="fas fa-chalkboard-teacher"></i> 스터디 질문과 답변 </h3>
	</div>

	<div>
 		<div>
 		<form name="boardForm" method="post" enctype="multipart/form-data">
			  <table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			   <tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">분&nbsp;&nbsp;&nbsp;&nbsp;류</td>
			      <td style="padding-left:10px;"> 
			      	<select id="highCategory" class="selectField">
						<option value="">::분류 선택::</option>
						<c:forEach var="vo" items="${listCategory}">
							<option value="${vo.categoryNum}" ${vo.categoryNum==dto.groupCategoryNum?"selected='selected'":"" }>${vo.category }</option>
						</c:forEach>
					</select>
					<select name="categoryNum" class="selectField">
						
						<option value="">::과목 선택::</option>
						<c:forEach var = "vo" items = "${subListCategory}">
							<option value="${vo.categoryNum}" ${vo.categoryNum==dto.categoryNum?"selected='selected'":"" }>${vo.category }</option>
						</c:forEach>
						
						
					</select>
		 			<button type="button" class="btn" id="btnCategoryUpdate">변경</button>
		 		
		 		
		 		
		 		</td>
			  </tr>
				<tr align="left" height="40" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.subject}">
			      </td>
			  </tr>
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">작성자</td>
			      <td style="padding-left:10px;"> 
			          ${sessionScope.member.userName}
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center; padding-top:5px;" valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:5px 0px 5px 10px;"> 
			        <textarea name="content" rows="12" class="boxTA" style="width: 95%;">${dto.content}</textarea>
			      </td>
			  </tr>
			  
			
			  </table>
			
			  <table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" class="btn" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='<%=cp%>/sbbs/list';">${mode=='update'?'수정취소':'등록취소'}</button>
			         <c:if test="${mode=='update'}">
			         	 <input type="hidden" name="num" value="${dto.num}">
			         	 <input type="hidden" name="page" value="${page}">
			        </c:if>
			      </td>
			    </tr>
			  </table>
			</form>
    	</div>
    	
    	<div id="category-dialog" style="display: none;">
    		<p>카테고리 등록 및 삭제</p>
    	</div>
    	
    	
	</div>
</div>