package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PracClass {

    public static void main(String[] args) {
        WebDriver driver = null;

        try {
            driver = WebDriverManager.getDriver();
            System.out.println("Chrome browser opened successfully");

            driver.get("https://www.google.com");
            System.out.println("Navigated to Google");
            System.out.println("Page title: " + driver.getTitle());

            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("Selenium WebDriver");
            searchBox.sendKeys("\n");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.titleContains("Selenium WebDriver"));
            System.out.println("Search results loaded");

            driver.get(WebDriverManager.BASE_URL);
            System.out.println("Navigated to SauceDemo");

            driver.findElement(By.id("user-name")).sendKeys("standard_user");
            driver.findElement(By.id("password")).sendKeys("secret_sauce");

            driver.findElement(By.id("login-button")).click();
            wait.until(ExpectedConditions.urlContains("inventory"));
            System.out.println("Login successful");

            WebElement firstProduct = driver.findElement(By.className("inventory_item_name"));
            System.out.println("First product: " + firstProduct.getText());
            firstProduct.click();

            driver.navigate().back();
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            WebDriverManager.quitDriver();
            System.out.println("Browser closed");
        }
    }
}
