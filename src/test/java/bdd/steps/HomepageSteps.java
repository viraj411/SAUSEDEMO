package bdd.steps;

import bdd.pages.Homepage;
import bdd.support.SauceDemoWorld;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.TestData;

import java.util.List;

public class HomepageSteps {

    private final Homepage homepage;
    private String sortLabel;

    public HomepageSteps(SauceDemoWorld world) {
        this.homepage = new Homepage(world.page());
    }

    @Then("the catalog shows {int} products")
    public void theCatalogShowsProducts(int expected) {
        Assert.assertEquals(homepage.getProductCount(), expected);
    }

    @Then("each product shows a name and a dollar price")
    public void eachProductShowsANameAndAPrice() {
        Assert.assertTrue(homepage.eachProductShowsNameAndPrice());
    }

    @Then("the cart icon is displayed")
    public void theCartIconIsDisplayed() {
        Assert.assertTrue(homepage.isCartIconDisplayed());
    }

    @Then("the menu items are {string}")
    public void theMenuItemsAre(String expected) {
        Assert.assertEquals(homepage.getMenuItemLabels(), List.of(expected.split(", ")));
    }

    @Then("the menu items are enabled")
    public void theMenuItemsAreEnabled() {
        Assert.assertTrue(homepage.areMenuItemsEnabled());
    }

    @When("I open the menu")
    public void iOpenTheMenu() {
        homepage.clickMenu();
    }

    @When("I close the menu")
    public void iCloseTheMenu() {
        homepage.closeMenuIfOpen();
    }

    @Then("the menu is closed")
    public void theMenuIsClosed() {
        Assert.assertFalse(homepage.isMenuOpen());
    }

    @When("I click All Items")
    public void iClickAllItems() {
        homepage.clickAllItems();
    }

    @When("I sort the catalog by {string}")
    public void iSortTheCatalogBy(String value) {
        sortLabel = homepage.sortByValue(value);
    }

    @Then("the sort option shown is {string}")
    public void theSortOptionShownIs(String expected) {
        Assert.assertEquals(sortLabel, expected);
    }

    @Then("the first product is {string}")
    public void theFirstProductIs(String expected) {
        Assert.assertEquals(homepage.getFirstProductName(), expected);
    }

    @Then("the products are sorted by name ascending")
    public void theProductsAreSortedByNameAscending() {
        Assert.assertTrue(homepage.isSortedByName(true));
    }

    @Then("the products are sorted by name descending")
    public void theProductsAreSortedByNameDescending() {
        Assert.assertTrue(homepage.isSortedByName(false));
    }

    @Then("the products are sorted by price ascending")
    public void theProductsAreSortedByPriceAscending() {
        Assert.assertTrue(homepage.isSortedByPrice(true));
    }

    @Then("the products are sorted by price descending")
    public void theProductsAreSortedByPriceDescending() {
        Assert.assertTrue(homepage.isSortedByPrice(false));
    }

    @Then("the footer shows the Sauce Labs copyright line")
    public void theFooterShowsTheCopyrightLine() {
        Assert.assertTrue(homepage.isFooterMatching(TestData.FOOTER_TEXT_PATTERN));
    }

    @Then("the social links point to Twitter, Facebook and LinkedIn")
    public void theSocialLinksPointToTwitterFacebookAndLinkedIn() {
        List<String> hrefs = homepage.getSocialLinkHrefs();
        Assert.assertEquals(hrefs.size(), 3, "Social links were: " + hrefs);
        Assert.assertTrue(hrefs.get(0).contains("twitter.com") || hrefs.get(0).contains("x.com"), hrefs.toString());
        Assert.assertTrue(hrefs.get(1).contains("facebook.com"), hrefs.toString());
        Assert.assertTrue(hrefs.get(2).contains("linkedin.com"), hrefs.toString());
    }
}
