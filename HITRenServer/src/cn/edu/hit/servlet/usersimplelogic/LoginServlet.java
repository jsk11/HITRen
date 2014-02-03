package cn.edu.hit.servlet.usersimplelogic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.hit.kit.LogKit;
import cn.edu.hit.logic.MessageLogic;
import cn.edu.hit.logic.UserSimpleLogic;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		MemController.setUserInfo(11, "asdsad123");
//		String s = MemController.getUserInfo(11);
//		System.out.println(s);
		
		try {
//			AccountManager.createAccount(35, "123");
			MessageLogic.likeTheMessage(35, 40);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String data = request.getParameter("data");
		data = new String(data.getBytes("ISO8859_1"),"utf-8");
		String email = "";
		String password = "";
		try {
			JSONObject json = new JSONObject(data);
			email = json.get("email").toString();
			LogKit.debug(email);
			password = json.get("password").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			UserSimpleLogic.login(email, password);
			out.print(UserSimpleLogic.retData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
