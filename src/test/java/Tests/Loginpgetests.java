package Tests;
import Pages.*;
import browserstack.shaded.jackson.databind.deser.Deserializers;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.WebDriverManager;

public class Loginpgetests  {
    WebDriver driver;
    LoginPage loginPage;


    @BeforeClass
    public void setup() {
        driver = WebDriverManager.getDriver();
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
    }

    @Test(priority = 2)
    public void testvalidLogin() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
        System.out.println("Login successful");
    }
    @Test(priority = 1)
    public void testInvalidLogin() {
       Assert.assertTrue(loginPage.invalidlogin());
        System.out.println( "Invalid Login");
    }
    @AfterClass
    public void teardown() {
        WebDriverManager.quitDriver();
    }

}
