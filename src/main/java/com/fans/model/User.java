package com.fans.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.fans.util.FunctionUtil;

public class User {

	String id;
	
	String uuid;

	String rUrl;

	String baseUrl;

	String wxuin;

	String wxsid;

	Integer uin;

	String sid;

	String skey;

	String deviceId;

	String pass_ticket;
	
	String syncKey;
	
	String tmpUser;
	
	String groupId;
	
	String groupName = "福利小丸子123群";
	
	int media_count;
	
	String webwx_data_ticket;
	
	String webwx_auth_ticket;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSyncKey() {
		return syncKey;
	}

	public void setSyncKey(String syncKey) {
		this.syncKey = syncKey;
	}

	public String getTmpUser() {
		return tmpUser;
	}

	public void setTmpUser(String tmpUser) {
		this.tmpUser = tmpUser;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getrUrl() {
		return rUrl;
	}

	public void setrUrl(String rUrl) {
		this.rUrl = rUrl;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getWxuin() {
		return wxuin;
	}

	public void setWxuin(String wxuin) {
		this.wxuin = wxuin;
	}

	public String getWxsid() {
		return wxsid;
	}

	public void setWxsid(String wxsid) {
		this.wxsid = wxsid;
	}

	public Integer getUin() {
		return uin;
	}

	public void setUin(Integer uin) {
		this.uin = uin;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
	}

	public String getDeviceId() {
		return "e" + FunctionUtil.getFixLenthString(10);
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPass_ticket() {
		return pass_ticket;
	}

	public void setPass_ticket(String pass_ticket) {
		this.pass_ticket = pass_ticket;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public int getMedia_count() {
		return media_count;
	}
	
	public String getWebwx_data_ticket() {
		return webwx_data_ticket;
	}

	public void setWebwx_data_ticket(String webwx_data_ticket) {
		this.webwx_data_ticket = webwx_data_ticket;
	}

	public String getWebwx_auth_ticket() {
		return webwx_auth_ticket;
	}

	public void setWebwx_auth_ticket(String webwx_auth_ticket) {
		this.webwx_auth_ticket = webwx_auth_ticket;
	}

	public void addMedia_count() {
		this.media_count++;
	}

	public Map<String, Object> getBaseRequest() {
		Map<String, Object> params = new HashMap<>();
		params.put("Uin", getUin());
		params.put("Sid", getSid());
		params.put("Skey", getSkey());
		params.put("DeviceID", getDeviceId());
		return params;
	}
	
	public Map<String, Object> getMsgRequest(String word) {
		Map<String, Object> params = new HashMap<>();
		String clientMsgId = System.currentTimeMillis() * 1000 + FunctionUtil.getFixLenthString(5);
		params.put("Type", 1);
		params.put("Content", word);
		params.put("FromUserName", getId());
		params.put("ToUserName", getGroupId());
		params.put("LocalID", clientMsgId);
		params.put("ClientMsgId", clientMsgId);
		return params;
	}

	public Object getMediaRequest(String mediaId) {
		Map<String, Object> params = new HashMap<>();
		String clientMsgId = System.currentTimeMillis() * 1000 + FunctionUtil.getFixLenthString(5);
		params.put("Type", 3);
		params.put("MediaId", mediaId);
		params.put("FromUserName", getId());
		params.put("ToUserName", getGroupId());
		params.put("LocalID", clientMsgId);
		params.put("ClientMsgId", clientMsgId);
		return params;
	}

}
