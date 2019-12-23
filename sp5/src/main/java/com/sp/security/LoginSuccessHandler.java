package com.sp.security;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.sp.member.Member;
import com.sp.member.MemberService;
import com.sp.member.SessionInfo;

// 로그인 성공후 세션 및 쿠키등의 처리를 할 수 있다.
// 로그인 전 정보를 Cache : 로그인 되지 않은 상태에서 로그인 상태에서만 사용할 수 있는 페이지로 이동할 경우 로그인 페이지로 이동하고 로그인 후 로그인 전 페이지로 이동
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();
    private String defaultUrl;
    
	@Autowired
	private MemberService memberService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		HttpSession session=request.getSession();
		
		// System.out.println(authentication.getName()); // 로그인 아이디
		
		try {
			// 로그인 날짜 변경
			memberService.updateLastLogin(authentication.getName());
			
			// 패스워드 실패 횟수 초기화
			memberService.updateFailureCountReset(authentication.getName());
		} catch (Exception e) {
		}
		
		// 로그인 정보 저장
		Member member=memberService.readMember(authentication.getName());
		SessionInfo info=new SessionInfo();
		info.setUserId(member.getUserId());
		info.setUserName(member.getUserName());
		session.setAttribute("member", info);	

		try {
	        Date endDate = new Date();
	        long gap;
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date modifyDate = formatter.parse(member.getModify_date());
            gap=(endDate.getTime() - modifyDate.getTime()) / (24 * 60 * 60* 1000);
            if(gap >= 90) {
            	// 패스워드 변경이 90일 이상인 경우
            	String targetUrl = "/member/updatePwd";
            	redirectStratgy.sendRedirect(request, response, targetUrl);
            	return;
            }
		} catch (Exception e) {
		}
		
		// redirect 설정
		resultRedirectStrategy(request, response, authentication);
	}

	protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        
        if(savedRequest!=null) {
        	// 권한이 필요한 페이지에 접근했을 경우(게시판 등)
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStratgy.sendRedirect(request, response, targetUrl);
        } else {
        	// 직접 로그인 url로 이동했을 경우
            redirectStratgy.sendRedirect(request, response, defaultUrl);
        }
    }
	
	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}	
}
