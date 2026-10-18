package com.qa.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.base.BaseClass;
import com.qa.pages.LoginPage;
import com.qa.utils.ConfigReader;

public class LoginTest extends BaseClass {
	
	@Test
	public void login() {
		int wait = Integer.parseInt(ConfigReader.get("explicitwaitseconds"));
		LoginPage loginpage = new LoginPage(driver, wait);
		loginpage.login(ConfigReader.get("username"), ConfigReader.get("password"));
		Assert.assertTrue(loginpage.isDashboardVisible(),"dashboard should be visible");
		String CurrentUrl = driver.getCurrentUrl();
		System.out.println(CurrentUrl);
		String ExpUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index";
		Assert.assertEquals(ExpUrl, CurrentUrl, "Url does not matching");
		
		if(CurrentUrl.equals(ExpUrl)) {
			System.out.println("Url Matching with expected");
		}
	}

}
