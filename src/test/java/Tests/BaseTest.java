package Tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import utils.WebDriverManager;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeClass
    public void startBrowser() {
        driver = WebDriverManager.getDriver();
    }

    @AfterClass(alwaysRun = true)
    public void stopBrowser() {
        WebDriverManager.quitDriver();
    }
}
