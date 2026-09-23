package Tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.WebDriverManager;

public abstract class BaseTest {

    protected WebDriver driver;

    /**
     * Browser is resolved in priority order:
     *   1. -Dbrowser=<name> system property (command-line / CI matrix)
     *   2. <parameter name="browser" value="..."/> in the TestNG XML suite
     *   3. @Optional default → chrome
     */
    @Parameters("browser")
    @BeforeClass
    public void startBrowser(@Optional("chrome") String browser) {
        String resolvedBrowser = System.getProperty("browser", browser);
        driver = WebDriverManager.getDriver(resolvedBrowser);
    }

    @AfterClass(alwaysRun = true)
    public void stopBrowser() {
        WebDriverManager.quitDriver();
    }
}
