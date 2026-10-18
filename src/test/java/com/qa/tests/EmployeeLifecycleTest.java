package com.qa.tests;

import java.io.IOException;
import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.qa.api.EmployeeApi;
import com.qa.base.BaseClass;
import com.qa.pages.AddEmployee;
import com.qa.pages.EmployeeJobPage;
import com.qa.pages.LoginPage;
import com.qa.pages.LogoutPage;
import com.qa.pages.PIMPage;
import com.qa.utils.ConfigReader;
import com.qa.utils.JsonReader;
import org.testng.annotations.Listeners;
import com.qa.listeners.TestListener;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@Listeners(TestListener.class)
public class EmployeeLifecycleTest extends BaseClass {
	
	
	int waitSeconds = Integer.parseInt(ConfigReader.get("explicitwaitseconds"));
	
	@Test(priority = 1)
	public void verifyEmployeeLifecycle() throws IOException {

		
		LoginPage loginpage = new LoginPage(driver, waitSeconds);
		loginpage.login(ConfigReader.get("username"), ConfigReader.get("password"));
		Assert.assertTrue(loginpage.isDashboardVisible(), "dashbord should display after login");
		
		PIMPage pimPage = new PIMPage(driver, waitSeconds);
		pimPage.openAddEmployee();
		pimPage.isAddEmployeePageVisible();
		pimPage.verifyAddEmployeeUrl();
		
		JsonObject employee = JsonReader.read("testdata/employee.json");
		
		String Firstname =	employee.get("firstName").getAsString();
		String Middlename =		employee.get("middleName").getAsString();
		String Lastname = employee.get("lastName").getAsString();
		String EmployeeId = 		employee.get("employeeId").getAsString();
		String EmployeeStatus = employee.get("employmentStatus").getAsString();
		String jobtitle = employee.get("jobTitle").getAsString();
		System.out.println("First name: " + Firstname);
		System.out.println("Middle name: " + Middlename);
		System.out.println("Last name: " + Lastname);
		System.out.println("Employee ID: " + EmployeeId);
		System.out.println("EmployementStatus"+EmployeeStatus);
		
		AddEmployee addemployee = new AddEmployee(driver, waitSeconds);
		addemployee.FillForm(Firstname, Middlename, Lastname);
		
		addemployee.EnteremployeeId(EmployeeId);
		String photoPath = java.nio.file.Paths.get("src/test/resources/images/employee.jpg").toAbsolutePath().toString();
		addemployee.uploadPhoto(photoPath);
		addemployee.saveEmployee();
		
		
		pimPage.searchEmployeeId(EmployeeId);
		Assert.assertEquals(addemployee.getSavedEmployeeId(), EmployeeId, "Employee id should match");
				
		
		Assert.assertTrue(pimPage.isEmployeeIdDisplayed(EmployeeId), "Created emplyee id should match in the search result");
		pimPage.openEmployeeEditPage(EmployeeId);
		
		EmployeeJobPage employeejobPage = new EmployeeJobPage(driver, waitSeconds);
		employeejobPage.openJobTab();
		Assert.assertTrue(employeejobPage.isJobDetailsVisible(),"Job Details should be visible.");
		
		employeejobPage.selectEmploymentStatus(EmployeeStatus);
		employeejobPage.selectJobTitle(jobtitle);
		employeejobPage.saveJobDetails();
		
		// Reopen the employee to verify the changes were saved.
		pimPage.searchEmployeeId(EmployeeId);
		pimPage.openEmployeeEditPage(EmployeeId);
		employeejobPage.openJobTab();
		
		employeejobPage.waitForSavedJobDetails(jobtitle, EmployeeStatus);

		Assert.assertEquals(employeejobPage.getSelectedEmploymentStatus(),EmployeeStatus,"Saved employment status should match");
		Assert.assertEquals(employeejobPage.getSelectedJobTitle(),jobtitle,"Saved job title should match");
		Assert.assertTrue(employeejobPage.isJobDetailsVisible(),"Job Details should be visible.");
		Assert.assertEquals(employeejobPage.getSelectedEmploymentStatus(),EmployeeStatus,"Saved employment status should match");
		Assert.assertEquals( employeejobPage.getSelectedJobTitle(),jobtitle,"Saved job title should match");

		// API verification starts here.
		Matcher matcher = Pattern.compile("/empNumber/(\\d+)").matcher(driver.getCurrentUrl());
		Assert.assertTrue( matcher.find(),"Employee page URL should contain the internal employee number");
		String empNumber = matcher.group(1);
		String apiUrl = ConfigReader.get("url").replaceAll("/+$", "")+ "/web/index.php/api/v2/pim/employees/"+ empNumber+ "/personal-details";
		EmployeeApi employeeApi = new EmployeeApi(driver);
		Response response = employeeApi.get(apiUrl);
		Assert.assertEquals(response.statusCode(), 200,"Employee API should return 200");

		Assert.assertEquals(response.jsonPath().getString("data.employeeId"),EmployeeId,"API Employee ID should match the employee created through UI");
		
		
		Assert.assertEquals(response.jsonPath().getString("data.firstName"),Firstname,"API first name should match");

		Assert.assertEquals(response.jsonPath().getString("data.middleName"),Middlename,"API middle name should match");

		Assert.assertEquals(response.jsonPath().getString("data.lastName"),Lastname,"API last name should match");
		String jobApiUrl = ConfigReader.get("url").replaceAll("/+$", "")+ "/web/index.php/api/v2/pim/employees/"+ empNumber + "/job-details";
		Response jobResponse = employeeApi.get(jobApiUrl);
		Assert.assertEquals(jobResponse.statusCode(),200,"Job Details API should return 200");
		Assert.assertEquals(jobResponse.jsonPath().getString("data.jobTitle.title"),employeejobPage.getSelectedJobTitle(),"API Job Title should match the UI");

		Assert.assertEquals(jobResponse.jsonPath().getString("data.empStatus.name"),employeejobPage.getSelectedEmploymentStatus(),"API Employment Status should match the UI");
		
		// Find and delete the employee.
		pimPage.searchEmployeeId(EmployeeId);
		Assert.assertTrue(pimPage.isEmployeeIdDisplayed(EmployeeId), "Employee should exist before deletion");
		pimPage.deleteEmployee(EmployeeId);
		// Search again to verify deletion.
		pimPage.searchEmployeeId(EmployeeId);
		Assert.assertTrue( pimPage.isNoRecordsFound(),"Deleted employee should not appear in search results");
		
		Response deletedResponse = employeeApi.get(apiUrl);
		System.out.println("API status after deletion: "+ deletedResponse.statusCode());
		System.out.println(deletedResponse.asPrettyString());
		
		Assert.assertEquals(deletedResponse.statusCode(),422,"Deleted employee number should be rejected");
		Assert.assertEquals(deletedResponse.jsonPath().getString("error.message"),"Invalid Parameter","API should report an invalid parameter");
		Assert.assertTrue(deletedResponse.jsonPath().getList("error.data.invalidParamKeys", String.class).contains("empNumber"),"API should reject the deleted employee number");
		
		LogoutPage logoutPage = new LogoutPage(driver, waitSeconds);
		
		Map<String, String> oldCookies = new HashMap<>();

		for (Cookie cookie : driver.manage().getCookies()) {
		    oldCookies.put(cookie.getName(), cookie.getValue());
		}
		
		logoutPage.logout();
		
		Assert.assertTrue(logoutPage.isLoginPageVisible(),"Login page should appear after logout");
		// Try accessing a protected page after logout.
		String dashboardUrl = ConfigReader.get("url").replaceAll("/+$", "")+ "/web/index.php/dashboard/index";
		
		
		Response oldSessionResponse = RestAssured.given().cookies(oldCookies).redirects().follow(false).get(dashboardUrl);
		System.out.println("Old session Dashboard status: "+ oldSessionResponse.statusCode());
		System.out.println("Redirect location: "+ oldSessionResponse.getHeader("Location"));
		
		Assert.assertEquals(oldSessionResponse.statusCode(), 302, "Old session should redirect after logout");
		String expectedLoginUrl = ConfigReader.get("url").replaceAll("/+$", "") + "/web/index.php/auth/login";
		Assert.assertEquals(oldSessionResponse.getHeader("Location"), expectedLoginUrl, "Old session should redirect to login");
		
		driver.get(dashboardUrl);
		Assert.assertTrue( logoutPage.isLoginPageVisible(),"Dashboard should require login after logout");
	}
	


}
