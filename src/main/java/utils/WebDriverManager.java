package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.util.HashMap;
import java.util.Map;

public final class WebDriverManager {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private WebDriverManager() {
    }

    public static WebDriver getDriver(String browser) {
        if (driverThread.get() == null) {
            WebDriver driver = createDriver(browser);
            if (!isHeadless() && !browser.equalsIgnoreCase("safari")) {
                driver.manage().window().maximize();
            }
            driverThread.set(driver);
        }
        return driverThread.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
    }

    private static WebDriver createDriver(String browser) {
        return switch (browser.toLowerCase()) {
            case "firefox" -> new FirefoxDriver(firefoxOptions());
            case "safari"  -> new SafariDriver(safariOptions());
            default        -> new ChromeDriver(chromeOptions());
        };
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", blockedContentPrefs());
        options.addArguments("--incognito");
        if (isHeadless()) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("dom.webnotifications.enabled", false);
        options.addPreference("geo.enabled", false);
        if (isHeadless()) {
            options.addArguments("--headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }
        return options;
    }

    /** Safari does not support headless mode or most custom prefs. */
    private static SafariOptions safariOptions() {
        return new SafariOptions();
    }

    private static Map<String, Object> blockedContentPrefs() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_setting_values.geolocation", 2);
        prefs.put("profile.default_content_setting_values.media_stream", 2);
        prefs.put("profile.default_content_setting_values.popups", 2);
        return prefs;
    }

    /** GitHub Actions sets CI=true. Local runs stay headed. */
    private static boolean isHeadless() {
        return Boolean.parseBoolean(System.getenv("CI"));
    }
}
