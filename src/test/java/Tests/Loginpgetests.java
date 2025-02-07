package Tests;
import Pages.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.WebDriverManager;

public class Loginpgetests {
    WebDriver driver;
    LoginPage loginPage;


    @BeforeClass
    public void setup() {
        driver = WebDriverManager.getDriver();
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
    }

    @Test(priority = 1)
    public void testvalidLogin() throws InterruptedException {
        loginPage.validLogin("standard_user", "secret_sauce");
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
        System.out.println("Login successful");
        loginPage.logout();

    }

    @Test(priority = 2)
    public void testnvalidLogin() {
        Assert.assertTrue(loginPage.invalidLogin("Admin@123","Wigzo@123"));
        System.out.println("Invalid Login");

    }
    @Test(priority = 3)
    public void testLoginwithblankpassword() {
        Assert.assertTrue(loginPage.validateLoginWithEmptyPassword());
    }
    @Test(priority = 4)
    public void testLoginwithblankusername() {
        Assert.assertTrue(loginPage.validateLoginWithEmptyUsername());
    }
    @Test(priority = 5)
    public void testLoginwithblankusernameandpassword() {
        Assert.assertTrue(loginPage.validateLoginWithEmptyUsernameAndPassword());
    }

    @AfterClass
    public void teardown() {
        WebDriverManager.quitDriver();
    }


}