package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class WebDriverManager {
    private static WebDriver driver;
    private static final String SCREENSHOT_DIR = "scrennshots";

    public static WebDriver getDriver() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();

            // Block popups, notifications, and other unwanted prompts
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.default_content_setting_values.notifications", 2); // Block notifications
            prefs.put("profile.default_content_setting_values.geolocation", 2);  // Block location
            prefs.put("profile.default_content_setting_values.media_stream", 2); // Block mic/camera
            prefs.put("profile.default_content_setting_values.popups", 2);       // Block popups

            options.setExperimentalOption("prefs", prefs);

            // Open Chrome in incognito mode
            options.addArguments("--incognito");

            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public static String takeScreenshot(String testName) {
        if (driver == null) {
            System.out.println("WebDriver is not initialized. Cannot take screenshot.");
            return null;
        }

        try {
            // Create screenshots directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate timestamp for unique filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + File.separator + fileName;

            // Take screenshot
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File sourceFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(filePath);

            // Copy file to destination
            FileUtils.copyFile(sourceFile, destinationFile);

            System.out.println("Screenshot saved: " + filePath);
            return filePath;
        } catch (IOException e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
            return null;
        }
    }
}



