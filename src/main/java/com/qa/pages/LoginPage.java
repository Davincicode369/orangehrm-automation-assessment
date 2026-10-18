package com.qa.pages;

import static org.testng.Assert.expectThrows;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.utils.ConfigReader;

public class LoginPage {
	
	//private WebDriver driver;
	private WebDriverWait wait;
	
	private By Username = By.name("username");
	private By Password = By.name("password");
	private By LoginButton = By.cssSelector("button[type='submit']");
	private By dashboardHeading = By.xpath("//h6[normalize-space()='Dashboard']");
	
	public LoginPage(WebDriver driver, int waitSecond) {
		//this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(waitSecond));
	}
	
	public void login(String username, String password) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(Username)).sendKeys(username);
		wait.until(ExpectedConditions.visibilityOfElementLocated(Password)).sendKeys(password);
		wait.until(ExpectedConditions.elementToBeClickable(LoginButton)).click();
	}
	
	public boolean isDashboardVisible() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardHeading)).isDisplayed();
	}

}
