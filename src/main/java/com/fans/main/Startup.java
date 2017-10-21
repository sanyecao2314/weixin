package com.fans.main;

import java.io.File;

import com.fans.model.User;
import com.fans.services.LoginServices;
import com.fans.services.SendMsgServices;

public class Startup {

	public static void main(String[] args) throws Exception {
		System.setProperty("jsse.enableSNIExtension", "false");
		User user = new User();
		LoginServices loginServices = new LoginServices();
		loginServices.getUUID(user);
		loginServices.genQRCode(user);
		Thread.sleep(30_000L);
		loginServices.waitForLogin(user);
		loginServices.login(user);
		loginServices.webwxinit(user);
		
		SendMsgServices sendMsgServices = new SendMsgServices();
		sendMsgServices.sendImg(user, new File("E:\\alimama\\temp\\d2.jpg").toPath());
	}

}
