package bdd.steps;

import bdd.pages.CartPage;
import bdd.pages.CheckoutPage;
import bdd.pages.ProductPage;
import bdd.support.SauceDemoWorld;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.TestData;

import java.util.List;

public class CheckoutSteps {

    private final ProductPage productPage;
    private final CartPage cartPage;
    private final CheckoutPage checkoutPage;

    public CheckoutSteps(SauceDemoWorld world) {
        this.productPage = new ProductPage(world.page());
        this.cartPage = new CartPage(world.page());
        this.checkoutPage = new CheckoutPage(world.page());
    }

    @Given("I am at the checkout information with the backpack in my cart")
    public void iAmAtTheCheckoutInformationWithTheBackpack() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.openCart();
        cartPage.clickCheckout();
    }

    @Given("I am at the checkout overview with the backpack in my cart")
    public void iAmAtTheCheckoutOverviewWithTheBackpack() {
        iAmAtTheCheckoutInformationWithTheBackpack();
        enterValidShippingDetails();
    }

    @Given("the backpack and the bike light are in my cart")
    public void theBackpackAndTheBikeLightAreInMyCart() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.addItem(TestData.BIKE_LIGHT_SLUG);
    }

    @When("I start checkout")
    public void iStartCheckout() {
        cartPage.clickCheckout();
    }

    @When("I enter shipping details {string}, {string} and {string}")
    public void iEnterShippingDetails(String firstName, String lastName, String zip) {
        checkoutPage.enterShippingDetails(firstName, lastName, zip);
    }

    @When("I enter valid shipping details")
    public void iEnterValidShippingDetails() {
        enterValidShippingDetails();
    }

    @Then("the checkout error is {string}")
    public void theCheckoutErrorIs(String expected) {
        Assert.assertEquals(checkoutPage.getErrorText(), expected);
    }

    @Then("the checkout error is not displayed")
    public void theCheckoutErrorIsNotDisplayed() {
        Assert.assertFalse(checkoutPage.isErrorDisplayed());
    }

    @When("I dismiss the checkout error")
    public void iDismissTheCheckoutError() {
        checkoutPage.dismissError();
    }

    @Then("the shipping fields are displayed")
    public void theShippingFieldsAreDisplayed() {
        Assert.assertTrue(checkoutPage.areShippingFieldsDisplayed());
    }

    @Then("the finish button is displayed")
    public void theFinishButtonIsDisplayed() {
        Assert.assertTrue(checkoutPage.isFinishButtonDisplayed());
    }

    @Then("the finish button is not displayed")
    public void theFinishButtonIsNotDisplayed() {
        Assert.assertFalse(checkoutPage.isFinishButtonDisplayed());
    }

    @Then("the first name is {string}")
    public void theFirstNameIs(String expected) {
        Assert.assertEquals(checkoutPage.getFirstNameValue(), expected);
    }

    @Then("the last name is {string}")
    public void theLastNameIs(String expected) {
        Assert.assertEquals(checkoutPage.getLastNameValue(), expected);
    }

    @Then("the zip code is {string}")
    public void theZipCodeIs(String expected) {
        Assert.assertEquals(checkoutPage.getZipCodeValue(), expected);
    }

    @Then("the overview shows the item {string}")
    public void theOverviewShowsTheItem(String name) {
        Assert.assertEquals(checkoutPage.getOverviewItemNames(), List.of(name));
    }

    @Then("the summary shows {string} and {string}")
    public void theSummaryShows(String payment, String shipping) {
        Assert.assertEquals(checkoutPage.getSummaryValues(), List.of(payment, shipping));
    }

    @Then("the subtotal matches the item prices")
    public void theSubtotalMatchesTheItemPrices() {
        Assert.assertTrue(checkoutPage.doesSubtotalMatchItems());
    }

    @Then("the total is consistent")
    public void theTotalIsConsistent() {
        Assert.assertTrue(checkoutPage.isTotalConsistent());
    }

    @Then("the tax is greater than zero")
    public void theTaxIsGreaterThanZero() {
        Assert.assertTrue(checkoutPage.getTax() > 0);
    }

    @Then("the subtotal is {double}")
    public void theSubtotalIs(double expected) {
        Assert.assertEquals(checkoutPage.getSubtotal(), expected);
    }

    @When("I cancel checkout")
    public void iCancelCheckout() {
        checkoutPage.clickCancel();
    }

    @When("I finish the order")
    public void iFinishTheOrder() {
        checkoutPage.completeOrder();
    }

    @Then("the success message is {string}")
    public void theSuccessMessageIs(String expected) {
        Assert.assertEquals(checkoutPage.getSuccessMessage(), expected);
    }

    @Then("the dispatch message is {string}")
    public void theDispatchMessageIs(String expected) {
        Assert.assertEquals(checkoutPage.getDispatchMessage(), expected);
    }

    @When("I click back home")
    public void iClickBackHome() {
        checkoutPage.clickBackToHome();
    }

    private void enterValidShippingDetails() {
        checkoutPage.enterShippingDetails(
                TestData.SHIPPING_FIRST_NAME,
                TestData.SHIPPING_LAST_NAME,
                TestData.SHIPPING_ZIP_CODE);
    }
}
