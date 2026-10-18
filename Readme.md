# OrangeHRM Employee Lifecycle Automation

Automation assessment covering employee lifecycle management through UI and API, with a JMeter performance smoke test.

## Tools

- Java 17
- Selenium WebDriver
- TestNG
- Maven
- REST Assured
- Gson
- Extent Reports
- Apache JMeter

## Automated Scenario

1. Log in and verify Dashboard.
2. Read employee data from JSON.
3. Create an employee with a profile picture.
4. Verify the saved Employee ID.
5. Search for the employee by ID.
6. Update Job Title and Employment Status.
7. Reopen the record and verify the saved changes.
8. Verify employee details through API.
9. Cross-check Job Title and Employment Status between API and UI.
10. Delete the employee and verify deletion through UI and API.
11. Log out and verify Dashboard requires login.
12. Verify the old session cookies cannot access Dashboard.

## Project Structure

| Location | Purpose |
|---|---|
| src/main/java/com/qa/base | BrowserFactory and BaseClass |
| src/main/java/com/qa/pages | Page Objects |
| src/main/java/com/qa/utils | Configuration and JSON readers |
| src/test/java/com/qa/tests | Test classes |
| src/test/java/com/qa/api | REST Assured API helper |
| src/test/java/com/qa/listeners | Extent reporting listener |
| src/test/resources/config | Application configuration |
| src/test/resources/testdata | Employee JSON data |
| src/test/resources/images | Employee profile picture |
| test-output | Functional HTML reports |
| performance | JMeter plan, results, dashboard and execution video |

## Prerequisites

- JDK 17
- Maven
- Chrome, Firefox or Edge
- Apache JMeter for performance testing
- Internet access

Verify Java and Maven:

```bat
java -version
mvn -version
```

## Configuration

Update src/test/resources/config/config.properties:

```properties
browser=chrome
url=https://opensource-demo.orangehrmlive.com/
username=Admin
password=admin123
explicitwaitseconds=20
```

Supported browser values:

- chrome
- firefox
- edge

The selected browser must be installed.

## Test Data

Employee details are stored in:

```text
src/test/resources/testdata/employee.json
```

Example:

```json
{
  "firstName": "Indrajith",
  "middleName": "K",
  "lastName": "Test",
  "employeeId": "QA1030",
  "jobTitle": "QA Engineer",
  "employmentStatus": "Full-Time Permanent"
}
```

Use an unused Employee ID. If a previous run stopped before deletion,
its employee may still exist.

Job Title and Employment Status must match available application options.

Place the profile picture at:

```text
src/test/resources/images/employee.jpg
```

## Run Functional and API Tests

From the project root, where pom.xml is located:

```bat
mvn clean test -Dtest=EmployeeLifecycleTest
```

Maven downloads the dependencies declared in pom.xml.

Alternatively, in Eclipse:

1. Import the project as an Existing Maven Project.
2. Select Maven → Update Project.
3. Right-click EmployeeLifecycleTest.java.
4. Select Run As → TestNG Test.

## Functional HTML Report

After execution, open:

```text
test-output/ExtentReport.html
```

The listener records the test result and failure details.

## API Verification

REST Assured uses cookies from the active Selenium browser session.

The internal employee number is extracted from the employee page URL.
This number is different from the Employee ID supplied in the JSON file.

Endpoints:

```text
GET /web/index.php/api/v2/pim/employees/{empNumber}/personal-details
GET /web/index.php/api/v2/pim/employees/{empNumber}/job-details
```

Checks include:

- HTTP 200 before deletion.
- Employee ID, first name, middle name and last name.
- Job Title and Employment Status against saved UI values.
- Employee unavailable after deletion.
- Old session rejected after logout.

In the observed application behavior, the Personal Details endpoint
returns HTTP 422 after deletion, with empNumber identified as invalid.
The UI also returns No Records Found for the deleted Employee ID.

For logout verification, the previous session cookies are replayed
against Dashboard with redirects disabled. The expected response is
HTTP 302 with Location pointing to the login page.

## Performance Testing

### Scope

A login-page HTTP response-time smoke test using JMeter.

| Setting | Value |
|---|---|
| Virtual users | 1 |
| Iterations | 5 |
| Ramp-up | 1 second |
| Constant Timer | 1000 milliseconds |
| Method | GET |
| Path | /web/index.php/auth/login |
| Response assertion | HTTP 200 |

### Files

```text
performance/OrangeHRM_Performance.jmx
performance/OrangeHRM_results_run1.jtl
performance/OrangeHRM_report_run1/
```

### Execution

With JMeter's bin folder on PATH, run from the project root:

```bat
jmeter.bat -n -t "performance\OrangeHRM_Performance.jmx" -l "performance\results-new.jtl" -e -o "performance\report-new"
```

Otherwise, replace jmeter.bat with its full installation path.

Use a new results filename and an empty or nonexistent output report
folder for each execution.

### Recorded Report

Open:

```text
performance/OrangeHRM_report_run1/index.html
```

Keep the complete report folder, including its supporting files.

The recorded dashboard shows 100% of requests passed the configured
response-code check.

### Limitations

This test measures login-page HTTP response time. It does not measure
browser rendering, authenticated employee workflows or system capacity.

Five samples are insufficient for reliable percentile estimates.
No business response-time SLA was supplied, so passing requests do not
establish compliance with a response-time target.

## AI Tool Usage

ChatGPT assisted with:

- Framework design and code examples.
- Page Object Model implementation.
- XPath construction and explanations.
- REST Assured assertions.
- Debugging execution failures.
- JMeter setup and documentation.

The implementation was reviewed and executed locally. API checks were
based on observed application responses.

## Execution Video

The functional test execution recording is included in the performance
folder.

## Environment Notes

The target is a shared public demo. Its data and available dropdown
options may change between executions.

The Extent listener is intended for the current sequential execution.