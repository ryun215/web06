package spms.controls;

import java.util.Map;

import spms.dao.MemberDao;
import spms.vo.Member;

public class MemberUpdateController implements Controller {
  @Override
  public String execute(Map<String, Object> model) throws Exception {
	  System.out.println("memberUpdateController execute 메소드 호출");
    MemberDao memberDao = (MemberDao)model.get("memberDao");
    
    if (model.get("member") == null) { 
    	System.out.println("모델에 담긴 member가 null일때");
      Integer no = (Integer)model.get("no");
      System.out.println("모델에 담긴 no값 : "+ no);
      Member member = memberDao.selectOne(no);
      System.out.println("member : "+member);
      model.put("member", member);
      return "/member/MemberUpdateForm.jsp";

    } else { 
      Member member = (Member)model.get("member");
      memberDao.update(member);
      return "redirect:list.do";
    }
  }
}
