package Tests;

import Pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.TestData;

public class LoginPageTests extends BaseTest {

    private LoginPage loginPage;

    @BeforeClass
    public void initPage() {
        loginPage = new LoginPage(driver);
    }

    @BeforeMethod
    public void navigateToLoginPage() {
        driver.get(TestData.BASE_URL);
        if (loginPage.isOnInventoryPage()) {
            loginPage.logout();
        }
    }

    @Test(description = "Login form shows username, password, and the login button")
    public void testLoginFormIsDisplayed() {
        Assert.assertTrue(loginPage.isLoginFormDisplayed());
        Assert.assertEquals(driver.getTitle(), TestData.EXPECTED_HOME_TITLE);
    }

    @Test(description = "Password characters are masked")
    public void testPasswordFieldIsMasked() {
        Assert.assertEquals(loginPage.getPasswordFieldType(), "password");
    }

    @Test(description = "Valid credentials land on the inventory page")
    public void testValidLogin() {
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        Assert.assertEquals(driver.getTitle(), TestData.EXPECTED_HOME_TITLE);
        Assert.assertTrue(loginPage.isOnInventoryPage());
    }

    @Test(description = "Refreshing the inventory page keeps the logged-in session")
    public void testSessionSurvivesRefresh() {
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        driver.navigate().refresh();
        Assert.assertTrue(loginPage.isOnInventoryPage());
    }

    @Test(description = "Logout returns the user to the login form")
    public void testLogoutReturnsToLoginForm() {
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        loginPage.logout();
        Assert.assertTrue(loginPage.isLoginFormDisplayed());
        Assert.assertFalse(loginPage.isOnInventoryPage());
    }

    @Test(description = "Inventory cannot be opened without a session")
    public void testInventoryRedirectsWhenLoggedOut() {
        driver.get(TestData.INVENTORY_URL);
        Assert.assertTrue(loginPage.isLoginFormDisplayed());
        Assert.assertFalse(loginPage.isOnInventoryPage());
    }

    @Test(description = "Unknown username and password show the mismatch error")
    public void testInvalidLogin() {
        Assert.assertEquals(
                loginPage.loginAndGetError(TestData.INVALID_USERNAME, TestData.INVALID_PASSWORD),
                TestData.INVALID_CREDENTIALS_MESSAGE);
        Assert.assertFalse(loginPage.isOnInventoryPage());
    }

    @Test(description = "A valid username with the wrong password is rejected")
    public void testWrongPasswordOnly() {
        Assert.assertEquals(
                loginPage.loginAndGetError(TestData.VALID_USERNAME, TestData.WRONG_PASSWORD),
                TestData.INVALID_CREDENTIALS_MESSAGE);
    }

    @Test(description = "A wrong username with the valid password is rejected")
    public void testWrongUsernameOnly() {
        Assert.assertEquals(
                loginPage.loginAndGetError(TestData.INVALID_USERNAME, TestData.VALID_PASSWORD),
                TestData.INVALID_CREDENTIALS_MESSAGE);
    }

    @Test(description = "Whitespace around the username is not trimmed into a valid login")
    public void testUsernameWithSurroundingSpaces() {
        Assert.assertEquals(
                loginPage.loginAndGetError(" " + TestData.VALID_USERNAME + " ", TestData.VALID_PASSWORD),
                TestData.INVALID_CREDENTIALS_MESSAGE);
    }

    @Test(description = "A locked account shows the locked-out error")
    public void testLockedOutUser() {
        Assert.assertEquals(
                loginPage.loginAndGetError(TestData.LOCKED_OUT_USERNAME, TestData.VALID_PASSWORD),
                TestData.LOCKED_OUT_MESSAGE);
        Assert.assertFalse(loginPage.isOnInventoryPage());
    }

    @Test(description = "A blank password asks for the password")
    public void testLoginWithBlankPassword() {
        Assert.assertEquals(
                loginPage.loginAndGetError(TestData.VALID_USERNAME, ""),
                TestData.PASSWORD_REQUIRED_MESSAGE);
    }

    @Test(description = "A blank username asks for the username")
    public void testLoginWithBlankUsername() {
        Assert.assertEquals(
                loginPage.loginAndGetError("", TestData.VALID_PASSWORD),
                TestData.USERNAME_REQUIRED_MESSAGE);
    }

    @Test(description = "Blank username and password report the username first")
    public void testLoginWithBlankUsernameAndPassword() {
        Assert.assertEquals(
                loginPage.loginAndGetError("", ""),
                TestData.USERNAME_REQUIRED_MESSAGE);
    }

    @Test(description = "Closing the error hides the message and stays on login")
    public void testDismissLoginError() {
        loginPage.loginAndGetError(TestData.INVALID_USERNAME, TestData.INVALID_PASSWORD);
        Assert.assertTrue(loginPage.isErrorDisplayed());
        loginPage.dismissError();
        Assert.assertFalse(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.isLoginFormDisplayed());
    }
}
