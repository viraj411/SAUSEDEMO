package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {

    private static final String INVALID_CREDENTIALS_MESSAGE =
            "Epic sadface: Username and password do not match any user in this service";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
    private final By burgerMenuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void validLogin(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(burgerMenuButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        System.out.println("Logged out successfully");
    }

    public boolean invalidLogin(String username, String password) {
        submitCredentials(username, password);
        String errorMessageText = getErrorMessageText();
        clearLoginFields();
        return errorMessageText.equals(INVALID_CREDENTIALS_MESSAGE);
    }

    public boolean validateLoginWithEmptyPassword() {
        submitCredentials("standard_user", "");
        String errorMessageText = getErrorMessageText();
        clearLoginFields();
        return errorMessageText.toLowerCase().contains("password");
    }

    public boolean validateLoginWithEmptyUsername() {
        submitCredentials("", "secret_sauce");
        String errorMessageText = getErrorMessageText();
        clearLoginFields();
        return errorMessageText.contains("Username");
    }

    public boolean validateLoginWithEmptyUsernameAndPassword() {
        submitCredentials("", "");
        String errorMessageText = getErrorMessageText();
        return errorMessageText.contains("Username") || errorMessageText.toLowerCase().contains("password");
    }

    private void submitCredentials(String username, String password) {
        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        WebElement passwordElement = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        usernameElement.clear();
        passwordElement.clear();
        usernameElement.sendKeys(username);
        passwordElement.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    private String getErrorMessageText() {
        WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        String errorMessageText = errorElement.getText();
        System.out.println(errorMessageText);
        return errorMessageText;
    }

    private void clearLoginFields() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField)).clear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).clear();
    }
}
