package spms.controls;

import java.util.Map;

import javax.servlet.http.HttpSession;

import spms.dao.MemberDao;
import spms.vo.Member;

public class LogInController implements Controller {
  @Override
  public String execute(Map<String, Object> model) throws Exception {
    if (model.get("loginInfo") == null) { // 입력폼을 요청할 때
    	System.out.println("입력폼 요청");
    	return "/auth/LogInForm.jsp";
      
    } else { // 회원 등록을 요청할 때
    	System.out.println("회원등록요청");
      MemberDao memberDao = (MemberDao)model.get("memberDao"); 
      Member loginInfo = (Member)model.get("loginInfo");
      System.out.println("loginInfo.getEmail :" +loginInfo.getEmail());
      System.out.println("loginInfo.getPassword :" +loginInfo.getPassword());
      Member member = memberDao.exist(
          loginInfo.getEmail(), 
          loginInfo.getPassword());
      System.out.println("member객체 생성 확인 :"+member);
      
      if (member != null) {
        HttpSession session = (HttpSession)model.get("session");
        session.setAttribute("member", member);
        return "redirect:../member/list.do";
      } else {
        return "/auth/LogInFail.jsp";
      }
    }
  }
}
