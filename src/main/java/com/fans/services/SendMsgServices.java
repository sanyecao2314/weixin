package com.fans.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.fans.model.User;
import com.fans.util.FileUtil;
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
		String url = "https://file.wx.qq.com/cgi-bin/mmwebwx-bin/webwxuploadmedia?f=json";
		//TODO 未写完待实现
//		user
		try {
			Map<String, Object> params = new HashMap<>();
			user.addMedia_count();
			params.put("id", "WU_FILE_" + user.getMedia_count());
			BasicFileAttributes baseFileAttr = Files.readAttributes(path, BasicFileAttributes.class);
			params.put("name", path.getFileName());
			params.put("type", "image/png");
			params.put("lastModifieDate", new Date(baseFileAttr.lastModifiedTime().toMillis()));
			params.put("size", baseFileAttr.size());
			params.put("mediatype", "pic");
			params.put("uploadmediarequest", getUploadMediaRequest(user, path));
			params.put("webwx_data_ticket", user.getWebwx_data_ticket());
			params.put("pass_ticket", user.getPass_ticket());
//			params.put("filename", path.getFileName());
			
			HttpEntity httpEntity = HttpUtil.doPostUploadSSL(url, JSON.toJSONString(params), path.toFile(), response);
			return httpEntity.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Map<String, Object> getUploadMediaRequest(User user, Path path) throws IOException {
		BasicFileAttributes baseFileAttr = Files.readAttributes(path, BasicFileAttributes.class);
		Map<String, Object> mediaRequest = new HashMap<>();
		mediaRequest.put("UploadType", 2);
		mediaRequest.put("BaseRequest", user.getBaseRequest());
		mediaRequest.put("ClientMediaId", System.currentTimeMillis());
		mediaRequest.put("TotalLen", baseFileAttr.size());
		mediaRequest.put("StartPos", 0);
		mediaRequest.put("DataLen", baseFileAttr.size());
		mediaRequest.put("MediaType", 4);
		mediaRequest.put("FromUserName", user.getId());
		mediaRequest.put("ToUserName", user.getGroupId());
		mediaRequest.put("FileMd5", FileUtil.getFileMD5(path));
		return mediaRequest;
	}

	
	/**
	 * 发送图片消息
	 * 
	 * @param user
	 * @param mediaId
	 * @throws Exception 
	 */
	private void webwxsendmsgimg(User user, String mediaId) throws Exception {
		StringBuffer url = new StringBuffer("https://wx.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsgimg?fun=async&f=json&pass_ticket=%s");
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
