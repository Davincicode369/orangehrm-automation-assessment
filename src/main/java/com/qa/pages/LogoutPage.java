package com.qa.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LogoutPage {

    private WebDriverWait wait;

    private By userMenu =By.cssSelector(".oxd-userdropdown-tab");
    private By logoutLink =By.xpath("//a[normalize-space()='Logout']");
    private By usernameField = By.name("username");

    public LogoutPage(WebDriver driver, int waitSeconds) {

        wait = new WebDriverWait( driver, Duration.ofSeconds(waitSeconds));
    }

    public void logout() {
wait.until(ExpectedConditions.elementToBeClickable(userMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    public boolean isLoginPageVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).isDisplayed();
    }
}