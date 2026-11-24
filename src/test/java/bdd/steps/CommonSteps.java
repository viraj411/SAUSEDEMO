package bdd.steps;

import bdd.pages.LoginPage;
import bdd.support.SauceDemoWorld;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import utils.TestData;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CommonSteps {

    private final SauceDemoWorld world;
    private final LoginPage loginPage;

    public CommonSteps(SauceDemoWorld world) {
        this.world = world;
        this.loginPage = new LoginPage(world.page());
    }

    @Given("I am on the SauceDemo login page")
    public void iAmOnTheLoginPage() {
        loginPage.open();
    }

    @Given("I am logged in")
    public void iAmLoggedIn() {
        loginPage.open();
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        loginPage.waitForInventoryPage();
    }

    @Then("the page title is {string}")
    public void thePageTitleIs(String expected) {
        assertThat(world.page()).hasTitle(expected);
    }

    @Then("the page heading is {string}")
    public void thePageHeadingIs(String expected) {
        assertThat(world.page().locator(".title")).hasText(expected);
    }

    @Then("I am on the inventory page")
    public void iAmOnTheInventoryPage() {
        Assert.assertTrue(loginPage.isOnInventoryPage());
    }

    @Then("I am not on the inventory page")
    public void iAmNotOnTheInventoryPage() {
        Assert.assertFalse(loginPage.isOnInventoryPage());
    }
}
