package spms.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.controls.Controller;
import spms.controls.LogInController;
import spms.controls.LogOutController;
import spms.controls.MemberAddController;
import spms.controls.MemberDeleteController;
import spms.controls.MemberListController;
import spms.controls.MemberUpdateController;
import spms.vo.Member;

@SuppressWarnings("serial")
@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet {
  @Override
  protected void service(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html; charset=UTF-8");
    String servletPath = request.getServletPath();
    try {
    
    	Controller controller = null;
    	Map<String, Object> model = new HashMap<String,Object>();
    	//model안에 memberDao주입
    	ServletContext sc = this.getServletContext();
    	model.put("memberDao", sc.getAttribute("memberDao"));
    	
    	
      if ("/member/list.do".equals(servletPath)) {
       controller = new MemberListController();
      } else if ("/member/add.do".equals(servletPath)) {
       controller = new MemberAddController();
        if (request.getParameter("email") != null) {
          model.put("member", new Member()
            .setEmail(request.getParameter("email"))
            .setPassword(request.getParameter("password"))
            .setName(request.getParameter("name")));
        }
      } else if ("/member/update.do".equals(servletPath)) {
    	  System.out.println("업데이트");
    	  controller = new MemberUpdateController();
       	System.out.println("리퀘스트에 담긴 no값 :"+request.getParameter("no"));
       	System.out.println("리퀘스트에 담긴 email값 :"+request.getParameter("email"));
       	System.out.println("리퀘스트에 담긴 name값 :"+request.getParameter("name"));
        if (request.getParameter("email") != null) {
        	System.out.println("email이 null이 아닐때");
          model.put("member", new Member()
            .setNo(Integer.parseInt(request.getParameter("no")))
            .setEmail(request.getParameter("email"))
            .setName(request.getParameter("name")));
          System.out.println("모델에 입력된 member키의 값 :" +model.get("member"));
        }else if(request.getParameter("email") == null){
        	int no = Integer.parseInt(request.getParameter("no"));
        	model.put("no", no);
        }
       
      } else if ("/member/delete.do".equals(servletPath)) {
        controller = new MemberDeleteController();
        int no = Integer.parseInt(request.getParameter("no"));
    	model.put("no", no);
        
        
      } else if ("/auth/login.do".equals(servletPath)) {
    	  System.out.println("로그인.do 요청");
        controller = new LogInController();
       
        if(request.getParameter("email")!=null && request.getParameter("password")!=null){
        	String email = request.getParameter("email");
            String password = request.getParameter("password");
            System.out.println("입력된 email확인 : "+email);
            System.out.println("입력된 password확인 : "+password);
           
            model.put("loginInfo", new Member().setEmail(email).setPassword(password));
            model.put("session", request.getSession());
        }else{
        	
        }
        
        
        
      } else if ("/auth/logout.do".equals(servletPath)) {
    	  controller = new LogOutController();
    	  model.put("session", request.getSession());
    	 
      }
      
    
      //컨트롤러 호출을 통해 view이름을 리턴받는다.
      
      String viewUrl = controller.execute(model);
      System.out.println("URL주소 확인 :"+viewUrl);
      //Map안의 내용을 request.attribute에 옮겨야함.
      //model의 키 갯수만큼 for문을통해 request.attribute에 세팅
      for(String key : model.keySet()){
    	  System.out.println("model의 키값 조회 :"+model.get(key));
    	  request.setAttribute(key, model.get(key));
      }
      
      if (viewUrl.startsWith("redirect:")) {
    	  System.out.println("리다이렉트 요청발생");
        response.sendRedirect(viewUrl.substring(9));
        return;
        
      } else {
    	   System.out.println("인클루드 요청발생");
        RequestDispatcher rd = request.getRequestDispatcher(viewUrl);
        rd.include(request, response);
      }
      
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute("error", e);
      RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
      rd.forward(request, response);
    }
  }
}
