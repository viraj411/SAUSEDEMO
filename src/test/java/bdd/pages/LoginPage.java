package bdd.pages;

import com.microsoft.playwright.Page;
import utils.TestData;

public class LoginPage extends BasePage {

    private static final String USERNAME_FIELD = "#user-name";
    private static final String PASSWORD_FIELD = "#password";
    private static final String LOGIN_BUTTON = "#login-button";
    private static final String ERROR_MESSAGE = "h3[data-test='error']";
    private static final String ERROR_CLOSE_BUTTON = ".error-button";
    private static final String BURGER_MENU_BUTTON = "#react-burger-menu-btn";
    private static final String LOGOUT_LINK = "#logout_sidebar_link";

    public LoginPage(Page page) {
        super(page);
    }

    public void open() {
        page.navigate(TestData.BASE_URL);
    }

    public void login(String username, String password) {
        fill(USERNAME_FIELD, username);
        fill(PASSWORD_FIELD, password);
        click(LOGIN_BUTTON);
    }

    public void waitForInventoryPage() {
        waitForUrl("inventory.html");
        page.locator(".inventory_item").first().waitFor();
    }

    public String getErrorText() {
        page.locator(ERROR_MESSAGE).waitFor();
        return textOf(ERROR_MESSAGE);
    }

    public void logout() {
        click(BURGER_MENU_BUTTON);
        click(LOGOUT_LINK);
        isVisible(USERNAME_FIELD);
    }

    public void dismissError() {
        click(ERROR_CLOSE_BUTTON);
    }

    public boolean isLoginFormDisplayed() {
        return isVisible(USERNAME_FIELD) && isVisible(PASSWORD_FIELD) && isVisible(LOGIN_BUTTON);
    }

    public boolean isErrorDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }

    public boolean isOnInventoryPage() {
        return page.url().contains("inventory");
    }

    public String getPasswordFieldType() {
        page.locator(PASSWORD_FIELD).waitFor();
        return page.locator(PASSWORD_FIELD).getAttribute("type");
    }
}
