package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
    private final By errorCloseButton = By.className("error-button");
    private final By burgerMenuButton = By.id("react-burger-menu-btn");
    private final By logoutLink = By.id("logout_sidebar_link");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        type(usernameField, username);
        type(passwordField, password);
        click(loginButton);
    }

    public String loginAndGetError(String username, String password) {
        login(username, password);
        return textOf(errorMessage);
    }

    public void logout() {
        click(burgerMenuButton);
        click(logoutLink);
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
    }

    public void dismissError() {
        click(errorCloseButton);
    }

    public boolean isLoginFormDisplayed() {
        return isVisible(usernameField) && isVisible(passwordField) && isVisible(loginButton);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public boolean isOnInventoryPage() {
        return driver.getCurrentUrl().contains("inventory");
    }

    public String getPasswordFieldType() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).getAttribute("type");
    }

    private boolean isVisible(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return element.isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
