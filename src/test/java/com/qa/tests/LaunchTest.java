package com.qa.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.qa.base.BaseClass;
import com.qa.pages.LoginPage;
import com.qa.utils.ConfigReader;

public class LaunchTest extends BaseClass {

	
	@Test
	public void verifyLandinPage() {
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(ConfigReader.get("explicitwaitseconds"))));
		boolean UsernameVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username"))).isDisplayed();
		Assert.assertTrue(UsernameVisible,"username filed should be visible on the loginPage");
	}
	
//	public void verifyLoginPage() {
//		
//		int wait = Integer.parseInt(ConfigReader.get("explicitwaitseconds"));
//		
//		LoginPage loginPage = new LoginPage(driver, wait);
//		loginPage.login(ConfigReader.get("username"),ConfigReader.get("password"));
//		Assert.assertTrue(loginPage.isDashboardVisible(), "dashboard should be visible after the login");
//		
//	}
	
	

}
