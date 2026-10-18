package com.qa.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EmployeeJobPage {

    private WebDriverWait wait;
    private By jobTab = By.xpath("//a[normalize-space()='Job']");
    private By jobDetailsHeading = By.xpath("//h6[normalize-space()='Job Details']");
    private By jobTitleDropdown = By.xpath("//label[normalize-space()='Job Title']"+ "/../following-sibling::div//div[contains(@class,'oxd-select-text-input')]");
    private By employmentStatusDropdown = By.xpath("//label[normalize-space()='Employment Status']"+ "/../following-sibling::div//div[contains(@class,'oxd-select-text-input')]");
    private By saveButton =By.xpath("//button[normalize-space()='Save']");
    private By successMessage =By.xpath("//div[contains(@class,'oxd-toast--success')]");
    private By formLoader = By.cssSelector(".oxd-form-loader");

    public EmployeeJobPage(WebDriver driver, int waitSeconds) {

        wait = new WebDriverWait(driver, Duration.ofSeconds(waitSeconds));
    }


	public void openJobTab() {
    	wait.until(ExpectedConditions.elementToBeClickable(jobTab)).click();
    }

    public boolean isJobDetailsVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(jobDetailsHeading)).isDisplayed();
       
    }
    
    public void selectJobTitle(String jobTitle) {
    	wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
    	wait.until(ExpectedConditions.visibilityOfElementLocated(jobTitleDropdown)).click();
    	By option = By.xpath("//div[@role='option']"+ "[normalize-space()='" + jobTitle + "']");
    	 wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }
    
    public void selectEmploymentStatus(String status) {
    	wait.until(ExpectedConditions.invisibilityOfElementLocated(formLoader));
        wait.until(ExpectedConditions.elementToBeClickable(employmentStatusDropdown)).click();
        By option = By.xpath("//div[@role='option']" + "[normalize-space()='" + status + "']");
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }

    public void saveJobDetails() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        
    }
    

    public String getSelectedJobTitle() {
        return wait.until(ExpectedConditions .visibilityOfElementLocated(jobTitleDropdown)).getText();
    }

    public String getSelectedEmploymentStatus() {
        return wait.until(ExpectedConditions .visibilityOfElementLocated(employmentStatusDropdown)).getText();
    }
    
    public void waitForSavedJobDetails( String jobTitle, String employmentStatus) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(jobTitleDropdown, jobTitle));
        wait.until(ExpectedConditions.textToBePresentInElementLocated( employmentStatusDropdown, employmentStatus));
    }
    

}