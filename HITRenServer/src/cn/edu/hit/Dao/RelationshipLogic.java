package cn.edu.hit.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.hit.kit.LogKit;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class RelationshipLogic {
	public static JSONObject retData;
	
	public static boolean createRelationship(int uid) throws JSONException {
		retData = new JSONObject();
		BasicDBObject obj = new BasicDBObject();
		obj.put(UserConstant.UID, uid);
		ArrayList<BasicDBObject> list = new ArrayList<BasicDBObject>();
		BasicDBObject defalut = new BasicDBObject();
		defalut.put(Relationship.GNAME, "default");
		list.add(defalut);
		obj.put(Relationship.CONCERNLIST, list);
		obj.put(Relationship.SEQ, 1);
		boolean ret = DBController.addObj(Relationship.COLLNAME, obj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	public static boolean addGroup(int uid, String gname) throws JSONException {
		retData = new JSONObject();
		BasicDBObject oldObj = new BasicDBObject();
		oldObj.put(UserConstant.UID, uid);
		BasicDBObject newObj = new BasicDBObject();
		newObj.put("$addToSet", new BasicDBObject().append(Relationship.CONCERNLIST, 
				new BasicDBObject().append(Relationship.GNAME, gname).append(Relationship.USERLIST, new ArrayList<String>())));
		newObj.put("$inc", new BasicDBObject().append(Relationship.SEQ, 1));
		boolean ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	public static boolean deleteGroup(int uid, String gname) throws JSONException {
		retData = new JSONObject();
		BasicDBObject obj1 = new BasicDBObject();
		obj1.put("uid", uid);
		obj1.put("concernlist.gname", gname);
		BasicDBObject obj2 = new BasicDBObject();
		obj2.put("concernlist.$.userlist", 1);
		DBObject retObj = DBController.queryOne(Relationship.COLLNAME, obj1, obj2);
		if (retObj == null) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		JSONObject json = new JSONObject(retObj.toString());
		json = new JSONObject(json.getJSONArray(Relationship.CONCERNLIST).get(0).toString());
		
		BasicDBObject oldObj = new BasicDBObject();
		oldObj.put(UserConstant.UID, uid);
		BasicDBObject newObj = new BasicDBObject();
		oldObj.put("concernlist.gname", "default");
		newObj = new BasicDBObject();
		JSONArray arr = json.getJSONArray("userlist");
		ArrayList<Integer> users = new ArrayList<Integer>();
		for (int i = 0; i < arr.length(); i++)
			users.add(arr.getInt(i));
		newObj.put("$addToSet", new BasicDBObject().append("concernlist.$.userlist",new BasicDBObject().append("$each", users)));
		boolean ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		
		oldObj = new BasicDBObject();
		oldObj.put(UserConstant.UID, uid);
		newObj = new BasicDBObject();
		newObj.put("$pull", new BasicDBObject().append(Relationship.CONCERNLIST, new BasicDBObject().append(Relationship.GNAME, gname)));
		newObj.put("$inc", new BasicDBObject().append(Relationship.SEQ, 1));
		ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	public static boolean renameGroup(int uid, String gname1, String gname2) throws JSONException {
		retData = new JSONObject();
		BasicDBObject oldObj = new BasicDBObject();
		oldObj.put(UserConstant.UID, uid);
		oldObj.put("concernlist.gname", gname1);
		BasicDBObject newObj = new BasicDBObject();
		newObj.put("$set", new BasicDBObject().append("concernlist.$.gname", gname2));
		newObj.put("$inc", new BasicDBObject().append(Relationship.SEQ, 1));
		boolean ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	public static boolean copyUsersToGroup(int uid, ArrayList<Integer> users, String gname) throws JSONException {
		retData = new JSONObject();
		BasicDBObject oldObj = new BasicDBObject();
		oldObj.put(UserConstant.UID, uid);
		oldObj.put("concernlist.gname", gname);
		BasicDBObject newObj = new BasicDBObject();
		newObj.put("$addToSet", new BasicDBObject().append("concernlist.$.userlist", new BasicDBObject().append("$each", users)));
		newObj.put("$inc", new BasicDBObject().append(Relationship.SEQ, 1));
		boolean ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	public static boolean deleteUserFromGroup(int uid, ArrayList<Integer> users, String gname) throws JSONException {
		retData = new JSONObject();
		BasicDBObject oldObj = new BasicDBObject();
		oldObj.put(UserConstant.UID, uid);
		oldObj.put("concernlist.gname", gname);
		BasicDBObject newObj = new BasicDBObject();
		newObj.put("$pullAll", new BasicDBObject().append("concernlist.$.userlist", users));
		newObj.put("$inc", new BasicDBObject().append(Relationship.SEQ, 1));
		boolean ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	public static boolean moveUsersFromGroupToGroup(int uid, ArrayList<Integer>users, String gname1, String gname2) throws JSONException {
		boolean ret = RelationshipLogic.copyUsersToGroup(uid, users, gname2);
		if (!ret)
			return false;
		ret = RelationshipLogic.deleteUserFromGroup(uid, users, gname1);
		return ret;
	}
	
	// 之后可以把addtoset改成push
	public static boolean concernUserInGroup(int uid, String group, int uid1) throws JSONException {
		retData = new JSONObject();
		BasicDBObject oldObj = new BasicDBObject();
		oldObj.put(Relationship.UID, uid);
		oldObj.put("concernlist.gname", group);
		BasicDBObject newObj = new BasicDBObject();
		newObj.append("$addToSet", new BasicDBObject().append("concernlist.$.userlist", uid1));
		newObj.put("$inc", new BasicDBObject().append(Relationship.SEQ, 1));
		boolean ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		
		//被关注者的followlist里要加入关注他的人
		ret = RelationshipLogic.addAfollowerToUid(uid, uid1);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	// 之后可以把addtoset改成push
	public static boolean addAfollowerToUid(int followerid, int uid) throws JSONException {
		retData = new JSONObject();
		BasicDBObject oldObj = new BasicDBObject();
		oldObj.put(Relationship.UID, uid);
		BasicDBObject newObj = new BasicDBObject();
		newObj.put("$addToSet", new BasicDBObject().append(Relationship.FOLLOWLIST, followerid));
		newObj.put("$inc", new BasicDBObject().append(Relationship.SEQ, 1));
		boolean ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	public static boolean removeAfollowerFromUid(int followid, int uid) throws JSONException {
		retData = new JSONObject();
		BasicDBObject oldObj = new BasicDBObject();
		oldObj.put(Relationship.UID, uid);
		BasicDBObject newObj = new BasicDBObject();
		newObj.put("$pull", new BasicDBObject().append(Relationship.FOLLOWLIST, followid));
		newObj.put("$inc", new BasicDBObject().append(Relationship.SEQ, 1));
		boolean ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	public static boolean moveUsersToBlacklist(int uid, ArrayList<Integer> users) throws JSONException {
		retData = new JSONObject();
		BasicDBObject oldObj = new BasicDBObject();
		oldObj.put(Relationship.UID, uid);
		BasicDBObject newObj = new BasicDBObject();
		newObj.put("$addToSet", new BasicDBObject().append(Relationship.BLACKLIST, new BasicDBObject().append("$each", users)));
		newObj.put("$inc", new BasicDBObject().append(Relationship.SEQ, 1));
		boolean ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	public static boolean recoverUsersToBlacklist(int uid, ArrayList<Integer> users) throws JSONException {
		retData = new JSONObject();
		BasicDBObject oldObj = new BasicDBObject();
		oldObj.put(Relationship.UID, uid);
		BasicDBObject newObj = new BasicDBObject();
		newObj.put("$pullAll", new BasicDBObject().append(Relationship.BLACKLIST, users));
		newObj.put("$inc", new BasicDBObject().append(Relationship.SEQ, 1));
		boolean ret = DBController.update(Relationship.COLLNAME, oldObj, newObj);
		if (!ret) {
			retData.put(HttpData.SUC, false);
			return false;
		}
		retData.put(HttpData.SUC, true);
		return true;
	}
	
	public static boolean downloadRelationshipInfo(int uid,int seq) throws JSONException {
		retData = new JSONObject();
		BasicDBObject obj = new BasicDBObject();
		obj.put(Relationship.UID, uid);
		obj.put(Relationship.SEQ, new BasicDBObject().append("$gt", seq));
		DBObject retObj = DBController.queryOne(Relationship.COLLNAME, obj);
		if (retObj == null) {
			retData.put(HttpData.SUC, false);
			retData.put(HttpData.INFO, "newest");
			return false;
		}
		retData.put(HttpData.SUC, true);
		retData.put(HttpData.DATA, retObj);
		return true;
	}
	
	public static Map<String, ArrayList<Integer>> getRelationshipOfUser(int uid) throws JSONException {
		BasicDBObject obj1 = new BasicDBObject();
		obj1.put(Relationship.UID, uid);
		BasicDBObject obj2 = new BasicDBObject();
		obj2.put(Relationship.CONCERNLIST, 1);
		DBObject retObj = DBController.queryOne(Relationship.COLLNAME, obj1, obj2);
		if (retObj == null)
			return null;
		Map<String, ArrayList<Integer>> retMap = new HashMap<String, ArrayList<Integer>>();
		JSONArray jsonArray = new JSONObject(retObj.toString()).getJSONArray(Relationship.CONCERNLIST);
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = new JSONObject(jsonArray.get(i).toString());
			String gname = json.getString(Relationship.GNAME);
			JSONArray users0 = json.getJSONArray(Relationship.USERLIST);
			ArrayList<Integer> users = new ArrayList<Integer>();
			for (int j = 0; j < users0.length(); j++)
				users.add(users0.getInt(j));
			retMap.put(gname, users);
		}
		return retMap;
//		return retObj;
	}
}
