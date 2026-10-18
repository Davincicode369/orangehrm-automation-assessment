package com.qa.pages;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.JsonObject;
import com.qa.utils.JsonReader;



public class PIMPage {
	
	private WebDriverWait wait;
	private WebDriver driver;
	private By PIMPageMenu = By.xpath("//a//span[normalize-space()='PIM']");
	private By AddEmployee = By.xpath("//li//a[normalize-space()='Add Employee']");
	private By FirstName = By.name("firstName");
	private By Middlename = By.name("middleName");
	private By lastname = By.name("lastName");
	private By SaveBtn = By.xpath("//button[normalize-space()='Save']");
	
	private By EmployeelistTab = By.xpath("//li//a[normalize-space()='Employee List']");
	private By EmployeeIdSearhField = By.xpath("//label[text()='Employee Id']/../following-sibling::div/input");
	private By EmployeeSearchButton = By.xpath("//button[@type='submit']");
	
	public PIMPage(WebDriver driver, int waitSecond) {
		this.driver = driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(waitSecond));
	}
	
	public void openAddEmployee() {
		wait.until(ExpectedConditions.elementToBeClickable(PIMPageMenu)).click();
		wait.until(ExpectedConditions.elementToBeClickable(AddEmployee)).click();
	}

	public boolean isAddEmployeePageVisible() {
		
		return wait.until(ExpectedConditions.visibilityOfElementLocated(FirstName)).isDisplayed();
	}
	
	public void verifyAddEmployeeUrl() {
		String actualUrl = driver.getCurrentUrl();
		String expUrl = "https://opensource-demo.orangehrmlive.com/web/index.php/pim/addEmployee";
		if(actualUrl.equals(expUrl)) {
			System.out.println("url matching");
		}
		else {
			System.out.println("url not matching");
		}
	}
	
	public void searchEmployeeId(String employeeid) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(EmployeelistTab)).click();
		WebElement employeeidSerachField = wait.until(ExpectedConditions.visibilityOfElementLocated(EmployeeIdSearhField));
		employeeidSerachField.clear();
		employeeidSerachField.sendKeys(employeeid);
		wait.until(ExpectedConditions.visibilityOfElementLocated(EmployeeSearchButton)).click();
	}

	public boolean isEmployeeIdDisplayed(String EmployeeId) {
		By EmpoyeeRow = By.xpath("//div[@role='row'][.//div[normalize-space()='"+EmployeeId+"']]");
		
		return wait.until(ExpectedConditions.visibilityOfElementLocated(EmpoyeeRow)).isDisplayed();
	}
	
	public void openEmployeeEditPage(String employeeId) {

	    By editButton = By.xpath("//div[@role='row'][.//div[normalize-space()='"+ employeeId + "']]"+ "//button[.//i[contains(@class,'bi-pencil-fill')]]");

	    wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();
	}
	
	public void deleteEmployee(String employeeId) {

	    By deleteButton = By.xpath("//div[@role='row'][.//div[normalize-space()='"+ employeeId + "']]"+ "//button[.//i[contains(@class,'bi-trash')]]");
	    wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();
	    By confirmDelete = By.xpath("//button[normalize-space()='Yes, Delete']");
	    wait.until(ExpectedConditions.elementToBeClickable(confirmDelete)).click();
	    By deleteSuccess = By.xpath( "//div[contains(@class,'oxd-toast--success')]"+ "//*[normalize-space()='Successfully Deleted']");
	    wait.until(ExpectedConditions.visibilityOfElementLocated(deleteSuccess));
	    wait.until(ExpectedConditions.invisibilityOfElementLocated(deleteSuccess));
	}

	public boolean isNoRecordsFound() {
	    By noRecordsMessage = By.xpath( "//span[normalize-space()='No Records Found']");
	    return wait.until(ExpectedConditions.visibilityOfElementLocated(noRecordsMessage)).isDisplayed();
	}

}
	

