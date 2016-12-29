package spms.listeners;

// 서버에서 제공하는 DataSource 사용하기
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import spms.controls.LogInController;
import spms.controls.LogOutController;
import spms.controls.MemberAddController;
import spms.controls.MemberDeleteController;
import spms.controls.MemberListController;
import spms.controls.MemberUpdateController;
import spms.dao.MemberDao;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent event) {
    try {
      ServletContext sc = event.getServletContext();
      
      InitialContext initialContext = new InitialContext();
      DataSource ds = (DataSource)initialContext.lookup(
          "java:comp/env/jdbc/studydb");
      
      MemberDao memberDao = new MemberDao();
      //데이터소스를 dao에 주입
      memberDao.setDataSource(ds);
      System.out.println("memberDao에 데이터소스 주입완료");
 
      /*1. 공통저장소(sc) : dao
       *2. 공통저장소(sc) : controller(+dao)
       *3.
       * */
   // sc.setAttribute("memberDao", memberDao);
      
      //set이 자기자신을 리턴 안할때 
      MemberListController memberListController = new MemberListController();
      memberListController.setMemberDao(memberDao);
      sc.setAttribute("/member/list.do", memberListController);
      
     //set이 자기자신을 리턴할 때
      sc.setAttribute("/member/add.do", new MemberAddController().setMemberDao(memberDao));
      sc.setAttribute("/member/update.do", new MemberUpdateController().setMemberDao(memberDao));
      sc.setAttribute("/member/delete.do", new MemberDeleteController().setMemberDao(memberDao));
      sc.setAttribute("/auth/login.do", new LogInController().setMemberDao(memberDao));
      sc.setAttribute("/auth/logout.do", new LogOutController());
      
      
      
    } catch(Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {}
}
