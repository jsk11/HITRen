package cn.edu.hit.servlet.usersimplelogic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.hit.dao.DataReader;
import cn.edu.hit.dao.MemController;
import cn.edu.hit.dao.MemWorker;
import cn.edu.hit.kit.DataKit;
import cn.edu.hit.kit.FileKit;
import cn.edu.hit.kit.LogKit;
import cn.edu.hit.logic.MessageLogic;
import cn.edu.hit.logic.UserSimpleLogic;
import cn.edu.hit.openfire.AccountManager;
import cn.edu.hit.servlet.kit.BaseServlet;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends BaseServlet {
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
//		MemWorker.setUserInfo(11, "asdsad123");
//		String s = MemWorker.getUserInfo(11);
//		System.out.println(s);
//		Logger logger = Logger.getLogger(LoginServlet.class);
//		logger.error("asdasd");
//		logger.debug("xxxxx");
//		try {
//			PrintWriter out = response.getWriter();
//			DataReader.getLeastUserInfo(35);
//			DataReader.getLeastUserInfo(28);
//			DataReader.getLeastUserInfo(29);
//			DataReader.getMessageInfo(40);
////			DataReader.getMessageInfo(11);
////			out.print(DataKit.getData(request.getReader()));
//			
////			AccountManager.createAccount(36, "123");
////			MessageLogic.cancelLikeTheMessage(35, 40);
////			MessageLogic.likeTheMessage(35, 40);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String data = DataKit.getDataFromClient(request.getReader());
		String email = "";
		String password = "";
		try {
			JSONObject json = new JSONObject(data);
			email = json.get("email").toString();
			password = json.get("password").toString();
			UserSimpleLogic.login(email, password);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(UserSimpleLogic.retData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
