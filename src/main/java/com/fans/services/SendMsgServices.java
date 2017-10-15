package com.fans.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.fans.model.User;
import com.fans.util.HttpUtil;

/**
 * 发送消息
 * 
 * @author fans.fan
 *
 */
public class SendMsgServices {
	private static final Logger logger = Logger.getLogger(SendMsgServices.class.getName());
	
	CloseableHttpResponse response = null;

	/**
	 * 发送文字信息
	 * 
	 * @param user
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public void webwxsendmsg(User user, String content) throws Exception{
		StringBuffer url = new StringBuffer(user.getBaseUrl()).append("webwxsendmsg?");
		url.append("pass_ticket=").append(user.getPass_ticket());
		Map<String, Object> params = new HashMap<>();
		params.put("BaseRequest", user.getBaseRequest());
		params.put("Msg", user.getMsgRequest(content));
		HttpEntity entity = HttpUtil.doPostSSL(url.toString(), JSON.toJSONString(params), response);
		String entityStr = EntityUtils.toString(entity, "utf-8");
		HttpUtil.closeResponse(response);
		logger.info(entityStr);
	}
	
	/**
	 * 发送图片消息
	 * 
	 * @param user
	 * @param path
	 * @throws Exception 
	 */
	public void sendImg(User user, Path path) throws Exception {
		String mediaId = webwxuploadmedia(user, path);
		webwxsendmsgimg(user, mediaId);
	}

	/**
	 * 上传图片
	 * 
	 * @param user
	 * @param path
	 * @return
	 */
	private String webwxuploadmedia(User user, Path path) {
		String url = "https://file2.wx.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json";
		//TODO 未写完待实现
		return null;
	}
	
	/**
	 * 发送图片消息
	 * 
	 * @param user
	 * @param mediaId
	 * @throws Exception 
	 */
	private void webwxsendmsgimg(User user, String mediaId) throws Exception {
		StringBuffer url = new StringBuffer("https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsgimg?fun=async&f=json&pass_ticket=%s");
		url.append("pass_ticket=").append(user.getPass_ticket());
		Map<String, Object> params = new HashMap<>();
		params.put("BaseRequest", user.getBaseRequest());
		params.put("Msg", user.getMediaRequest(mediaId));
		HttpEntity entity = HttpUtil.doPostSSL(url.toString(), JSON.toJSONString(params), response);
		String entityStr = EntityUtils.toString(entity, "utf-8");
		HttpUtil.closeResponse(response);
		logger.info(entityStr);
	}
	
}
