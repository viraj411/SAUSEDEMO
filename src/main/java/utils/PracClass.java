package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class PracClass {
    
    public static void main(String[] args) {
        WebDriver driver = null;
        
        try {
            // Setup Chrome with options
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            options.addArguments("--disable-notifications");
            
            // Create WebDriver instance
            driver = new ChromeDriver(options);
            System.out.println("✅ Chrome browser opened successfully!");
            
            // Navigate to Google
            driver.get("https://www.google.com");
            System.out.println("✅ Navigated to Google");
            System.out.println("📄 Page Title: " + driver.getTitle());
            
            // Find search box and enter search term
            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("Selenium WebDriver");
            System.out.println("✅ Entered search term: Selenium WebDriver");
            
            // Press Enter to search instead of clicking button
            searchBox.sendKeys("\n");
            System.out.println("✅ Pressed Enter to search");
            
            // Wait for results and get page title
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.titleContains("Selenium WebDriver"));
            System.out.println("✅ Search results loaded");
            System.out.println("📄 Results Page Title: " + driver.getTitle());
            
            // Take screenshot
            String screenshotPath = WebDriverManager.takeScreenshot("GoogleSearch");
            if (screenshotPath != null) {
                System.out.println("📸 Screenshot saved: " + screenshotPath);
            }
            
            // Navigate to SauceDemo
            driver.get("https://www.saucedemo.com");
            System.out.println("✅ Navigated to SauceDemo");
            System.out.println("📄 Page Title: " + driver.getTitle());
            
            // Find username field and enter username
            WebElement usernameField = driver.findElement(By.id("user-name"));
            usernameField.sendKeys("standard_user");
            System.out.println("✅ Entered username: standard_user");
            
            // Find password field and enter password
            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.sendKeys("secret_sauce");
            System.out.println("✅ Entered password");
            
            // Take screenshot before login
            WebDriverManager.takeScreenshot("BeforeLogin");
            System.out.println("📸 Screenshot taken before login");
            
            // Click login button
            WebElement loginButton = driver.findElement(By.id("login-button"));
            loginButton.click();
            System.out.println("✅ Clicked login button");
            
            // Wait for login to complete
            wait.until(ExpectedConditions.urlContains("inventory"));
            System.out.println("✅ Login successful!");
            System.out.println("📄 Current URL: " + driver.getCurrentUrl());
            
            // Take screenshot after login
            WebDriverManager.takeScreenshot("AfterLogin");
            System.out.println("📸 Screenshot taken after login");
            
            // Find and click on first product
            WebElement firstProduct = driver.findElement(By.className("inventory_item_name"));
            System.out.println("🛍️ First product: " + firstProduct.getText());
            firstProduct.click();
            System.out.println("✅ Clicked on first product");
            
            // Take screenshot of product details
            WebDriverManager.takeScreenshot("ProductDetails");
            System.out.println("📸 Screenshot of product details taken");
            
            // Go back to inventory
            driver.navigate().back();
            System.out.println("✅ Navigated back to inventory");
            
            // Final screenshot
            WebDriverManager.takeScreenshot("FinalInventory");
            System.out.println("📸 Final screenshot taken");
            
            System.out.println("\n🎉 All operations completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error occurred: " + e.getMessage());
            e.printStackTrace();
            
        } finally {
            // Close the browser
            if (driver != null) {
                try {
                    Thread.sleep(2000); // Wait 2 seconds to see the final result
                    driver.quit();
                    System.out.println("✅ Browser closed successfully!");
                } catch (Exception e) {
                    System.err.println("❌ Error closing browser: " + e.getMessage());
                }
            }
        }
    }
    
    // Helper method to print separator
    public static void printSeparator() {
        System.out.println("=".repeat(50));
    }
    
    // Helper method to wait
    public static void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
} 