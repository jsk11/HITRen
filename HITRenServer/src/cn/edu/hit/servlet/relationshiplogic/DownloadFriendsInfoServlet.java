package cn.edu.hit.servlet.relationshiplogic;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.hit.kit.DataKit;
import cn.edu.hit.logic.RelationshipLogic;

/**
 * Servlet implementation class DownloadFriendsInfoServlet
 * 参数，uids
 */
@WebServlet("/DownloadFriendsInfoServlet")
public class DownloadFriendsInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadFriendsInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String data = DataKit.getDataFromClient(request.getReader());
		try {
			JSONObject json = new JSONObject(data);
			JSONArray users0 = json.getJSONArray("uids");
			ArrayList<Integer> users = new ArrayList<Integer>();
			for (int i = 0; i < users0.length(); i++)
				users.add(users0.getInt(i));
			RelationshipLogic.downloadFriendsInfo(users);
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(RelationshipLogic.retData);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
