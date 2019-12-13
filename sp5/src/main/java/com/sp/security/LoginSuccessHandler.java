package com.sp.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.sp.member.Member;
import com.sp.member.MemberService;
import com.sp.member.SessionInfo;

// 로그인 성공후 세션 및 쿠키등의 처리를 할 수 있다.
// 로그인 전 정보를 Cache : 로그인 되지 않은 상태에서 로그인 상태에서만 사용할 수 있는 페이지로 이동할 경우 로그인 페이지로 이동하고 로그인 후 로그인 전 페이지로 이동
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Autowired
	private MemberService memberService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req,
			HttpServletResponse resp, Authentication authentication)
			throws ServletException, IOException {
		HttpSession session=req.getSession();
		
		// System.out.println(authentication.getName()); // 로그인 아이디
		 
		try {
			// 로그인 날짜 변경
			memberService.updateLastLogin(authentication.getName());
		} catch (Exception e) {
		}
		
		// 로그인 정보 저장
		Member member=memberService.readMember(authentication.getName());
		SessionInfo info=new SessionInfo();
		info.setUserId(member.getUserId());
		info.setUserName(member.getUserName());
		
		session.setAttribute("member", info);	

		super.onAuthenticationSuccess(req, resp, authentication);
	}
}
