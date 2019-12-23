package com.sp.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.sp.member.Member;
import com.sp.member.MemberService;


public class LoginFailureHandler implements AuthenticationFailureHandler{
	@Autowired
	private MemberService service;
	
	private String defaultFailureUrl;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
        String userId = request.getParameter("userId");
        // String userPwd = request.getParameter("userPwd");
        
		String errorMsg = "아이디 또는 패스워드가 일치하지 않습니다.";
		try {
	        if(exception instanceof BadCredentialsException) {
	        	// 패스워드가 일치하지 않을 때 던지는 예외
	        	
	        	service.updateFailureCount(userId);
	        	int cnt = service.checkFailureCount(userId);
	        	if(cnt>=5) {
	        		// 계정 비활성화
	        		Map<String, Object> map = new HashMap<>();
	        		map.put("enabled", 0);
	        		map.put("userId", userId);
	        		service.updateMemberEnabled(map);
	        		
	        		// 비활성화 상태 저장
	        		Member dto = new Member();
	        		dto.setUserId(userId);
	        		dto.setStateCode(1);
	        		dto.setMemo("패스워드 5회 이상 실패");
	        		service.insertMemberState(dto);
	        	}
	        	
	        	errorMsg = "아이디 또는 패스워드가 일치하지 않습니다.";
	        } else if(exception instanceof InternalAuthenticationServiceException) {
	        	// 존재하지 않는 아이디일 때 던지는 예외
	        	errorMsg = "아이디 또는 패스워드가 일치하지 않습니다.";
	        } else if(exception instanceof DisabledException) {
	        	// 인증 거부 - 계정 비활성화(enabled=0)
	        	errorMsg = "계정이 비활성화되었습니다. 관리자에게 문의하세요.";
	        }
		} catch (Exception e) {
		}
        
        /*
         - spring security의 예외
           BadCredentialException : 비밀번호가 일치하지 않을 때 던지는 예외
           InternalAuthenticationServiceException : 존재하지 않는 아이디일 때 던지는 예외
           AuthenticationCredentialNotFoundException : 인증 요구가 거부됐을 때 던지는 예외
           LockedException : 인증 거부 - 잠긴 계정
           DisabledException : 인증 거부 - 계정 비활성화(enabled=0)
           AccountExpiredException : 인증 거부 - 계정 유효기간 만료
           CredentialExpiredException : 인증 거부 - 비밀번호 유효기간 만료
       */
		
        request.setAttribute("errorMsg", errorMsg);

        request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}
}
