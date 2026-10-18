package com.qa.pages;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.bidi.module.Browser;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.JsonObject;
import com.qa.utils.JsonReader;

public class AddEmployee {

	private WebDriver driver;
	private WebDriverWait wait;
	
	private By PIMPageMenu = By.xpath("//a//span[normalize-space()='PIM']");
	private By AddEmployee = By.xpath("//li//a[normalize-space()='Add Employee']");
	public By FirstName = By.name("firstName");
	private By Middlename = By.name("middleName");
	private By lastname = By.name("lastName");
	private By EmployeeId = By.xpath("//label[normalize-space()='Employee Id']"+ "/parent::div/following-sibling::div/input");;
	private By PhotoUpload = By.xpath("//input[@type='file']");
	private By SaveBtn = By.xpath("//button[normalize-space()='Save']");
	private By personalDetailsHeading = By.xpath("//h6[normalize-space()='Personal Details']");
	private By employeeCreatedMessage = By.xpath( "//div[contains(@class,'oxd-toast--success')]"+ "//*[normalize-space()='Successfully Saved']");
	
public AddEmployee(WebDriver driver, int WaitSeconds) {
	this.driver = driver;
	wait = new WebDriverWait(driver, Duration.ofSeconds(WaitSeconds));
}

	
	public void FillForm(String Firstname, String MiddleNmae, String Lastname) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(FirstName)).sendKeys(Firstname);
		wait.until(ExpectedConditions.visibilityOfElementLocated(Middlename)).sendKeys(MiddleNmae);
		wait.until(ExpectedConditions.visibilityOfElementLocated(lastname)).sendKeys(Lastname);
		
	}
	
	public void EnteremployeeId(String employeeId) {
		
		WebElement EmployeeIdFiled =  wait.until(ExpectedConditions.visibilityOfElementLocated(EmployeeId));
	
		EmployeeIdFiled.click();
		EmployeeIdFiled.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		EmployeeIdFiled.sendKeys(Keys.BACK_SPACE);
		EmployeeIdFiled.sendKeys(employeeId);
		
	}
	
	public void uploadPhoto(String photoPath) {
		driver.findElement(PhotoUpload).sendKeys(photoPath);
	}

	public void saveEmployee() {
wait.until(ExpectedConditions.elementToBeClickable(SaveBtn)).click();
wait.until(ExpectedConditions.visibilityOfElementLocated(employeeCreatedMessage));
	}
	
	

	public boolean isPersonalDetailsVisible() {
	    return wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeading)).isDisplayed();
	}
	
	
	public String getSavedEmployeeId() {
		wait.until(ExpectedConditions.attributeToBeNotEmpty(driver.findElement(EmployeeId), "value"));
		
		String fetchedEmployeeId = driver.findElement(EmployeeId).getDomProperty("value");
		return fetchedEmployeeId;
	}

}
