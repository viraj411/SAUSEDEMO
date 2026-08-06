package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TestData;

import java.time.Duration;

public class LoginPage {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

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
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    public void validLogin(String username, String password) {
        submitCredentials(username, password);
    }

    public void logout() {
        click(burgerMenuButton);
        click(logoutLink);
        System.out.println("Logged out successfully");
    }

    public boolean invalidLogin(String username, String password) {
        return errorAfterLogin(username, password).equals(TestData.INVALID_CREDENTIALS_MESSAGE);
    }

    public boolean validateLoginWithEmptyPassword() {
        return mentionsPassword(errorAfterLogin(TestData.VALID_USERNAME, ""));
    }

    public boolean validateLoginWithEmptyUsername() {
        return mentionsUsername(errorAfterLogin("", TestData.VALID_PASSWORD));
    }

    public boolean validateLoginWithEmptyUsernameAndPassword() {
        String error = errorAfterLogin("", "");
        return mentionsUsername(error) || mentionsPassword(error);
    }

    /**
     * Submits the given credentials, returns the resulting error text and leaves
     * the form empty so the next attempt starts from a clean state.
     */
    private String errorAfterLogin(String username, String password) {
        submitCredentials(username, password);
        String errorMessageText = getErrorMessageText();
        clearLoginFields();
        return errorMessageText;
    }

    private void submitCredentials(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }

    private String getErrorMessageText() {
        String errorMessageText = visible(errorMessage).getText();
        System.out.println(errorMessageText);
        return errorMessageText;
    }

    private void clearLoginFields() {
        visible(usernameField).clear();
        visible(passwordField).clear();
    }

    private void type(By locator, String text) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(text);
    }

    private void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    private WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private boolean mentionsUsername(String errorMessageText) {
        return errorMessageText.contains("Username");
    }

    private boolean mentionsPassword(String errorMessageText) {
        return errorMessageText.toLowerCase().contains("password");
    }
}
