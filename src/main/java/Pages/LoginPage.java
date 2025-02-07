package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;  // WebDriverWait instance

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // Set explicit wait timeout
    }

    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.xpath("//*[@id='login_button_container']//h3");
    private By threedot = By.id("react-burger-menu-btn");
    private By logoutIcon = By.id("logout_sidebar_link");

    // Method for valid login and logout
    public void validLogin(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

    }
    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(threedot)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutIcon)).click();
        System.out.println("Logged out successfully");

    }

    // Method for invalid login validation
    public boolean invalidLogin(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        String errorMessageText = errorElement.getText();
        System.out.println(errorMessageText);

        return errorMessageText.equals("Epic sadface: Username and password do not match any user in this service");
    }

}

 /*  public boolean validateLoginWithEmptyPassword() {
        driver.findElement(usernameField).sendKeys("standard_user");
        driver.findElement(passwordField).sendKeys("");
        driver.findElement(loginButton).click();
        String errormassageforpassText = driver.findElement(errormassageforpass).getText();
        System.out.println(  " Sonya "+errormassageforpassText);
        return (errormassageforpassText.equals("Epic sadface: Please enter your password"));





     }






  *}

  */
