package com.qa.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class TestListener implements ITestListener {

    private ExtentReports report;
    private ExtentTest test;

    @Override
    public void onStart(ITestContext context) {
    	System.out.println("Extent listener started");
    	System.out.println("Report path: " + new java.io.File("test-output/ExtentReport.html").getAbsolutePath());
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
          spark.config().setDocumentTitle("OrangeHRM Automation Report");
       spark.config().setReportName("Employee Lifecycle Assessment");

        report = new ExtentReports();
        report.attachReporter(spark);
        report.setSystemInfo("Framework", "Selenium Java + TestNG + REST Assured");
    }
    @Override
    public void onTestStart(ITestResult result) {
     test = report.createTest(result.getName());
    }
    @Override
    public void onTestSuccess(ITestResult result) {
  test.pass("Test completed successfully");
    }
    @Override
    public void onTestFailure(ITestResult result) {
   test.fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.skip("Test skipped");
        if (result.getThrowable() != null) {
        test.skip(result.getThrowable());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        report.flush();
        System.out.println("Extent report saved");
    }
}