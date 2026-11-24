package bdd.steps;

import bdd.pages.LoginPage;
import bdd.support.SauceDemoWorld;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.TestData;

public class LoginSteps {

    private final SauceDemoWorld world;
    private final LoginPage loginPage;

    public LoginSteps(SauceDemoWorld world) {
        this.world = world;
        this.loginPage = new LoginPage(world.page());
    }

    @Then("the login form is displayed")
    public void theLoginFormIsDisplayed() {
        Assert.assertTrue(loginPage.isLoginFormDisplayed());
    }

    @Then("the password field is masked")
    public void thePasswordFieldIsMasked() {
        Assert.assertEquals(loginPage.getPasswordFieldType(), "password");
    }

    @When("I log in with valid credentials")
    public void iLogInWithValidCredentials() {
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        loginPage.waitForInventoryPage();
    }

    @When("I log in as {string} with password {string}")
    public void iLogInAs(String username, String password) {
        loginPage.login(username, password);
    }

    @When("I refresh the page")
    public void iRefreshThePage() {
        world.page().reload();
    }

    @When("I log out")
    public void iLogOut() {
        loginPage.logout();
    }

    @When("I open the inventory page directly")
    public void iOpenTheInventoryPageDirectly() {
        world.page().navigate(TestData.INVENTORY_URL);
    }

    @Then("I see the login error {string}")
    public void iSeeTheLoginError(String expected) {
        Assert.assertEquals(loginPage.getErrorText(), expected);
    }

    @When("I dismiss the login error")
    public void iDismissTheLoginError() {
        loginPage.dismissError();
    }

    @Then("the login error is not displayed")
    public void theLoginErrorIsNotDisplayed() {
        Assert.assertFalse(loginPage.isErrorDisplayed());
    }
}
