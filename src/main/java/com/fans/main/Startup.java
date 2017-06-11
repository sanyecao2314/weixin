package com.fans.main;

import com.fans.model.User;
import com.fans.services.LoginServices;

public class Startup {

	public static void main(String[] args) throws Exception {
		User user = new User();
		LoginServices pageFun = new LoginServices();
		pageFun.getUUID(user);
		pageFun.genQRCode(user);
		Thread.sleep(30_000L);
		pageFun.waitForLogin(user);
		pageFun.login(user);
		pageFun.webwxinit(user);
		
	}

}
