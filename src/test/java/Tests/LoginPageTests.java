package Tests;

import Pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.WebDriverManager;

public class LoginPageTests {

    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeClass
    public void setup() {
        driver = WebDriverManager.getDriver();
        driver.get(WebDriverManager.BASE_URL);
        loginPage = new LoginPage(driver);
    }

    @BeforeMethod
    public void navigateToLoginPage() {
        driver.get(WebDriverManager.BASE_URL);
    }

    @Test(priority = 1)
    public void testValidLogin() {
        loginPage.validLogin("standard_user", "secret_sauce");
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
        System.out.println("Login successful");
        loginPage.logout();
    }

    @Test(priority = 2)
    public void testInvalidLogin() {
        Assert.assertTrue(loginPage.invalidLogin("Admin@123", "Wigzo@123"));
        System.out.println("Invalid login validated");
    }

    @Test(priority = 3)
    public void testLoginWithBlankPassword() {
        Assert.assertTrue(loginPage.validateLoginWithEmptyPassword());
    }

    @Test(priority = 4)
    public void testLoginWithBlankUsername() {
        Assert.assertTrue(loginPage.validateLoginWithEmptyUsername());
    }

    @Test(priority = 5)
    public void testLoginWithBlankUsernameAndPassword() {
        Assert.assertTrue(loginPage.validateLoginWithEmptyUsernameAndPassword());
    }

    @AfterClass
    public void teardown() {
        WebDriverManager.quitDriver();
    }
}
