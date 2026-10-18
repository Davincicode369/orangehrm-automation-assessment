package com.qa.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.qa.utils.ConfigReader;

public class BaseClass {
	
	protected WebDriver driver ;
	
	@BeforeMethod
	public void setup() {
		driver = BrowserFactory.CreateDriver(ConfigReader.get("browser"));
		driver.get(ConfigReader.get("url"));
	}
	
	@AfterMethod
		public void teardown() {
		driver.quit();
			
		driver=null;
	}

}
