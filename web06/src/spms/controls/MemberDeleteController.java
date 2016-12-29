package spms.controls;

import java.util.Map;

import spms.dao.MemberDao;

public class MemberDeleteController implements Controller {
	 private MemberDao memberDao;
	  
	  //dao주입을 위한 set메소드
	  public MemberDeleteController setMemberDao(MemberDao memberDao){
		  this.memberDao = memberDao;
		return this;	  
	  }
  @Override
  public String execute(Map<String, Object> model) throws Exception {
   
    
    Integer no = (Integer)model.get("no");
  

    memberDao.delete(no);
    
    return "redirect:list.do";
  }
}
