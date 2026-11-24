package bdd.steps;

import bdd.pages.CartPage;
import bdd.pages.Homepage;
import bdd.pages.ProductPage;
import bdd.support.SauceDemoWorld;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.TestData;

import java.util.List;

public class CartSteps {

    private final Homepage homepage;
    private final ProductPage productPage;
    private final CartPage cartPage;

    public CartSteps(SauceDemoWorld world) {
        this.homepage = new Homepage(world.page());
        this.productPage = new ProductPage(world.page());
        this.cartPage = new CartPage(world.page());
    }

    @When("I open the product {string}")
    public void iOpenTheProduct(String name) {
        productPage.openProduct(name);
    }

    @Then("the product details name is {string}")
    public void theProductDetailsNameIs(String expected) {
        Assert.assertEquals(productPage.getDetailsName(), expected);
    }

    @Then("the product details description is {string}")
    public void theProductDetailsDescriptionIs(String expected) {
        Assert.assertEquals(productPage.getDetailsDescription(), expected);
    }

    @Then("the product details price is {string}")
    public void theProductDetailsPriceIs(String expected) {
        Assert.assertEquals(productPage.getDetailsPrice(), expected);
    }

    @When("I go back to products")
    public void iGoBackToProducts() {
        productPage.backToProducts();
    }

    @When("I add to cart from the product details")
    public void iAddToCartFromTheProductDetails() {
        productPage.addItemFromDetails();
    }

    @Then("the details button shows {string}")
    public void theDetailsButtonShows(String label) {
        productPage.waitForDetailsButton(label);
    }

    @When("I add the backpack to the cart")
    public void iAddTheBackpackToTheCart() {
        productPage.addItem(TestData.BACKPACK_SLUG);
    }

    @When("I add the bike light to the cart")
    public void iAddTheBikeLightToTheCart() {
        productPage.addItem(TestData.BIKE_LIGHT_SLUG);
    }

    @When("I add every product to the cart")
    public void iAddEveryProductToTheCart() {
        productPage.addAllItemsToCart();
    }

    @Then("the backpack button shows {string}")
    public void theBackpackButtonShows(String label) {
        productPage.waitForCatalogButton(TestData.BACKPACK_SLUG, label);
    }

    @When("I remove the backpack from the catalog")
    public void iRemoveTheBackpackFromTheCatalog() {
        productPage.removeItem(TestData.BACKPACK_SLUG);
    }

    @Then("the cart badge shows {int}")
    public void theCartBadgeShows(int count) {
        productPage.waitForCartBadgeCount(count);
    }

    @When("I open the cart")
    public void iOpenTheCart() {
        productPage.openCart();
    }

    @Then("the cart has {int} item(s)")
    public void theCartHasItems(int count) {
        cartPage.waitForItemCount(count);
    }

    @Then("the cart lists only {string}")
    public void theCartListsOnly(String name) {
        Assert.assertEquals(cartPage.getItemNames(), List.of(name));
    }

    @Then("the cart includes {string}")
    public void theCartIncludes(String name) {
        Assert.assertTrue(cartPage.getItemNames().contains(name), "Cart items: " + cartPage.getItemNames());
    }

    @Then("the cart item quantities are {string}")
    public void theCartItemQuantitiesAre(String expected) {
        Assert.assertEquals(cartPage.getQuantities(), List.of(expected.split(", ")));
    }

    @Then("the cart prices are {string}")
    public void theCartPricesAre(String expected) {
        Assert.assertEquals(cartPage.getItemPrices(), List.of(expected.split(", ")));
    }

    @Then("the checkout button is displayed")
    public void theCheckoutButtonIsDisplayed() {
        Assert.assertTrue(cartPage.isCheckoutDisplayed());
    }

    @When("I remove the backpack from the cart")
    public void iRemoveTheBackpackFromTheCart() {
        cartPage.removeItem(TestData.BACKPACK_SLUG);
    }

    @When("I remove all items from the cart")
    public void iRemoveAllItemsFromTheCart() {
        cartPage.removeAllItems();
    }

    @Then("the cart is empty")
    public void theCartIsEmpty() {
        cartPage.waitForItemCount(0);
    }

    @When("I continue shopping")
    public void iContinueShopping() {
        cartPage.continueShopping();
    }

    @When("I reset the app state")
    public void iResetTheAppState() {
        homepage.resetAppState();
    }
}
