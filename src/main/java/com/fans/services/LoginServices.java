package com.fans.services;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fans.model.User;
import com.fans.util.FunctionUtil;
import com.fans.util.HttpUtil;

/**
 * 微信網頁版的主要功能
 * 
 * @author fans.fan
 *
 */
public class LoginServices {

	private static final Logger logger = Logger.getLogger(LoginServices.class.getName());

	CloseableHttpResponse response = null;
	
	public void loginWeiXin(User user){
		
	}

	/**
	 * 获取uuid
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void getUUID(User user) throws Exception {
		logger.info("getUUID");
		StringBuffer url = new StringBuffer("https://login.weixin.qq.com/jslogin?");
		url.append("appId=wx782c26e4c19acffb");
		url.append("&fun=new");
		url.append("&_=").append(System.currentTimeMillis());
		HttpEntity entity = HttpUtil.doPostSSL(url.toString(), "", response);
		String entityStr = EntityUtils.toString(entity, "utf-8");
		HttpUtil.closeResponse(response);
		logger.info(entityStr);
		int start = entityStr.indexOf("\"");
		int end = entityStr.lastIndexOf("\"");
		String uuid = entityStr.substring(start + 1, end);
		user.setUuid(uuid);
	}

	/**
	 * 获取登陆二维码
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void genQRCode(User user) throws Exception {
		logger.info("genQRCode");
		String url = "https://login.weixin.qq.com/qrcode/" + user.getUuid();
		Map<String, Object> params = new HashMap<>();
		params.put("t", "webwx");
		params.put("_", System.currentTimeMillis());
		FunctionUtil.showLoginImg(url, params);
	}

	/**
	 * 等待扫码确认登陆
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public boolean waitForLogin(User user) throws Exception {
		logger.info("waitForLogin");
		StringBuffer url = new StringBuffer("https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login?");
		url.append("tip=1");
		url.append("&uuid=").append(user.getUuid());
		url.append("&_=").append(System.currentTimeMillis());
		HttpEntity entity = HttpUtil.doPostSSL(url.toString(), "", response);
		String entityStr = EntityUtils.toString(entity, "utf-8");
		HttpUtil.closeResponse(response);
		// window.code=200;
		// window.redirect_uri="https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxnewloginpage?ticket=ASo0z14ROF7Mmj0D8z933RUN@qrticket_0&uuid=oeT6rPJeEQ==&lang=zh_CN&scan=1497070835";
		logger.info(entityStr);
		int start = entityStr.indexOf("=") + 1;
		int end = entityStr.indexOf(";");
		String code = entityStr.substring(start, end);
		if ("201".equals(code)) {
			logger.info("登陆成功");
			return true;
		} else if ("200".equals(code)) {
			start = entityStr.indexOf("\"") + 1;
			end = entityStr.lastIndexOf("\"");
			String rUrl = entityStr.substring(start, end) + "&fun=new";
			user.setrUrl(rUrl);
			int index = rUrl.lastIndexOf("/") + 1;
			user.setBaseUrl(rUrl.substring(0, index));
			logger.info("登陆成功");
			return true;
		} else if ("408".equals(code)) {
			logger.info("登陆超时!");
		} else {
			logger.info("登陆异常!");
		}
		return true;
	}

	public void login(User user) {
		logger.info("login");
		String res = HttpUtil.doGet(user.getrUrl());
//		<error><ret>0</ret><message></message><skey>@crypt_415fe54d_d87b836baacae5c117d0b1d58d535b93</skey><wxsid>GfdekP9KPG4IhFE2</wxsid><wxuin>1448148081</wxuin><pass_ticket>xMhzw%2BMb%2B8n6DFEjSPoI3iFdKs46ZTG5Lb4S4ETaOM3tUEoPtPDQtisgSGatRO9O</pass_ticket><isgrayscale>1</isgrayscale></error>
		logger.info("res=" + res);
		FunctionUtil.readXml(res, user);
	}
	
	/**
	 * 初始化,获取联系人等信息
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void webwxinit(User user) throws Exception{
		logger.info("webwxinit");
		StringBuffer url = new StringBuffer(user.getBaseUrl());
		url.append("webwxinit?");
		url.append("pass_ticket=").append(user.getPass_ticket());
		url.append("&lang=").append("zh_CN");
		url.append("&r=").append(System.currentTimeMillis());
		Map<String, Object> params = new HashMap<>();
		params.put("BaseRequest", user.getBaseRequest());
		HttpEntity entity = HttpUtil.doPostSSL(url.toString(), JSON.toJSONString(params), response);
		String entityStr = EntityUtils.toString(entity, "utf-8");
		HttpUtil.closeResponse(response);
		logger.info(entityStr);
		@SuppressWarnings("unchecked")
		Map<String, Object> mapResponse = JSON.parseObject(entityStr, HashMap.class);
		user.setId(((JSONObject)mapResponse.get("User")).getString("UserName"));
		JSONArray jsonArray = ((JSONObject)mapResponse.get("SyncKey")).getJSONArray("List");
		StringBuffer syncBuffer = new StringBuffer();
		for (Object object : jsonArray) {
			JSONObject jsonObj = (JSONObject) object;
			syncBuffer.append(jsonObj.getLong("Val"))
			.append("_")
			.append(jsonObj.getLong("Key"))
			.append("|");
		}
		user.setSyncKey(syncBuffer.substring(0, syncBuffer.length() - 1));
		jsonArray = ((JSONObject)mapResponse.get("SyncKey")).getJSONArray("ContactList");
		for (Object object : jsonArray) {
			JSONObject jsonObj = (JSONObject) object;
			if(user.getGroupName().equals(jsonObj.getString("NickName"))){
				user.setGroupId(jsonObj.getString("UserName"));
				break;
			}
		}
		
	}

}
