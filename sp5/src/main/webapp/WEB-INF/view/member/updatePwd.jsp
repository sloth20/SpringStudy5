<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
   String cp = request.getContextPath();
%>

<style type="text/css">
.lbl {
   position:absolute; 
   margin-left:15px; margin-top: 17px;
   color: #999999; font-size: 11pt;
}
.loginTF {
  width: 450px; height: 35px;
  padding: 5px;
  padding-left: 15px;
  border:1px solid #999999;
  color:#333333;
  margin-top:5px; margin-bottom:5px;
  font-size:14px;
  border-radius:4px;
}
</style>
<script type="text/javascript">
	function bgLabel(ob, id) {
	    if(!ob.value) {
		    document.getElementById(id).style.display="";
	    } else {
		    document.getElementById(id).style.display="none";
	    }
	}

	function sendOk() {
        var f = document.pwdForm;

        var str = f.userPwd.value;
        if(!str) {
            alert("패스워드를 입력하세요. ");
            f.userPwd.focus();
            return;
        }

    	if(!/^(?=.*[a-z])(?=.*[!@#$%^*+=-]|.*[0-9]).{5,10}$/i.test(str)) { 
    		alert("패스워드는 5~10자이며 하나 이상의 숫자나 특수문자가 포함되어야 합니다.");
    		f.userPwd.focus();
    		return;
    	}
    	
    	if(f.userPwd.value != f.userPwd2.value) {
    		alert("패스워드가 일치하지 않습니다.");
    		f.userPwd.focus();
    		return;
    	}

        f.action = "<%=cp%>/member/updatePwd";
        f.submit();
	}
</script>

<div class="body-container">
    <div style="width:510px; margin: 0px auto; padding-top:90px;">
	
    	<div style="text-align: center;">
        	<span style="font-weight: bold; font-size:27px; color: #424951;">패스워드 변경</span>
        </div>
	
		<form name="pwdForm" method="post" action="">
		  <table style="width:100%; margin: 20px auto; padding:30px;  border-collapse: collapse; border: 1px solid #DAD9FF;">
		  <tr style="height:50px;">
		      <td style="padding-left: 0px; text-align: center;">
		          안전한 사용을 위하여 기존 패스워드를 변경하세요.
		      </td>
		  </tr>

		  <tr align="center" height="60">
		      <td>
		        &nbsp;
		        <label for="userPwd" id="lblUserPwd" class="lbl" >새로운 패스워드</label>
		        <input type="password" name="userPwd" id="userPwd" class="loginTF" maxlength="20" 
		                   tabindex="2"
                           onfocus="document.getElementById('lblUserPwd').style.display='none';"
                           onblur="bgLabel(this, 'lblUserPwd');">
		        &nbsp;
		      </td>
		  </tr>
		  <tr align="center" height="60">
		      <td>
		        &nbsp;
		        <label for="userPwd2" id="lblUserPwd2" class="lbl" >패스워드 확인</label>
		        <input type="password" name="userPwd2" id="userPwd2" class="loginTF" maxlength="20" 
		                   tabindex="2"
                           onfocus="document.getElementById('lblUserPwd2').style.display='none';"
                           onblur="bgLabel(this, 'lblUserPwd2');">
		        &nbsp;
		      </td>
		  </tr>
		  <tr style="height:30px;">
		      <td style="padding-left: 20px; text-align: left;">
		           ※ 5~10자 이내의 하나 이상의 숫자나 특수문자가 포함되어야 합니다.
		      </td>
		  </tr>
		  <tr align="center" height="65" > 
		      <td>
		        <button type="button" onclick="javascript:location.href='<%=cp%>';" class="btnConfirm" style="width: 230px;">다음에 변경</button>
		        <button type="button" onclick="sendOk();" class="btnConfirm" style="width: 230px;">변경 완료</button>
		      </td>
		  </tr>
		  <tr align="center" height="10" > 
		      <td>&nbsp;</td>
		  </tr>
	    </table>
		</form>
		           
	    <table style="width:100%; margin: 10px auto 0; border-collapse: collapse;">
		  <tr align="center" height="30" >
		    	<td><span style="color: blue;">${message}</span></td>
		  </tr>
		</table>
	</div>
</div>