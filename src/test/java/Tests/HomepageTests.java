package Tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestData;

public class HomepageTests extends InventoryTest {

    @Test(description = "Inventory heading is Products")
    public void testProductsHeading() {
        Assert.assertEquals(homepage.getPageTitle(), TestData.EXPECTED_PRODUCTS_TITLE);
    }

    @Test(description = "The catalog lists every standard product")
    public void testProductCount() {
        Assert.assertEquals(homepage.getProductCount(), TestData.EXPECTED_PRODUCT_COUNT);
    }

    @Test(description = "Each catalog card shows a name and a dollar price")
    public void testEachProductShowsNameAndPrice() {
        Assert.assertTrue(homepage.eachProductShowsNameAndPrice());
    }

    @Test(description = "The cart icon is visible and empty before anything is added")
    public void testCartIconStartsEmpty() {
        Assert.assertTrue(homepage.isCartIconDisplayed());
        Assert.assertEquals(productPage.getCartBadgeCount(), 0);
    }

    @Test(description = "The menu lists All Items, About, Logout, and Reset App State")
    public void testMenuItemsArePresentAndEnabled() {
        Assert.assertEquals(homepage.getMenuItemLabels(), TestData.EXPECTED_MENU_ITEMS);
        Assert.assertTrue(homepage.areMenuItemsEnabled());
        homepage.closeMenuIfOpen();
        Assert.assertFalse(homepage.isMenuOpen());
    }

    @Test(description = "All Items keeps the shopper on the catalog")
    public void testAllItemsStaysOnInventory() {
        homepage.clickMenu();
        homepage.clickAllItems();
        homepage.closeMenuIfOpen();
        Assert.assertEquals(homepage.getPageTitle(), TestData.EXPECTED_PRODUCTS_TITLE);
        Assert.assertTrue(loginPage.isOnInventoryPage());
    }

    @Test(description = "Name A to Z sorts the catalog ascending")
    public void testSortByNameAscending() {
        Assert.assertEquals(homepage.sortByValue(TestData.SORT_A_TO_Z), TestData.SORT_A_TO_Z_LABEL);
        Assert.assertEquals(homepage.getFirstProductName(), TestData.SORT_A_TO_Z_FIRST_PRODUCT);
        Assert.assertTrue(homepage.isSortedByName(true));
    }

    @Test(description = "Name Z to A sorts the catalog descending")
    public void testSortByNameDescending() {
        Assert.assertEquals(homepage.sortByValue(TestData.SORT_Z_TO_A), TestData.SORT_Z_TO_A_LABEL);
        Assert.assertEquals(homepage.getFirstProductName(), TestData.SORT_Z_TO_A_FIRST_PRODUCT);
        Assert.assertTrue(homepage.isSortedByName(false));
    }

    @Test(description = "Price low to high sorts the catalog by ascending price")
    public void testSortByPriceLowToHigh() {
        Assert.assertEquals(
                homepage.sortByValue(TestData.SORT_PRICE_LOW_TO_HIGH),
                TestData.SORT_PRICE_LOW_TO_HIGH_LABEL);
        Assert.assertEquals(homepage.getFirstProductName(), TestData.SORT_PRICE_LOW_FIRST_PRODUCT);
        Assert.assertTrue(homepage.isSortedByPrice(true));
    }

    @Test(description = "Price high to low sorts the catalog by descending price")
    public void testSortByPriceHighToLow() {
        Assert.assertEquals(
                homepage.sortByValue(TestData.SORT_PRICE_HIGH_TO_LOW),
                TestData.SORT_PRICE_HIGH_TO_LOW_LABEL);
        Assert.assertEquals(homepage.getFirstProductName(), TestData.SORT_PRICE_HIGH_FIRST_PRODUCT);
        Assert.assertTrue(homepage.isSortedByPrice(false));
    }

    @Test(description = "The footer shows the Sauce Labs copyright line")
    public void testFooterIsDisplayed() {
        Assert.assertTrue(homepage.getFooterText().matches(TestData.FOOTER_TEXT_PATTERN));
    }

    @Test(description = "Social links point at Twitter, Facebook, and LinkedIn")
    public void testSocialLinks() {
        var hrefs = homepage.getSocialLinkHrefs();
        Assert.assertEquals(hrefs.size(), 3, "Social links were: " + hrefs);
        Assert.assertTrue(hrefs.get(0).contains("twitter.com") || hrefs.get(0).contains("x.com"), hrefs.toString());
        Assert.assertTrue(hrefs.get(1).contains("facebook.com"), hrefs.toString());
        Assert.assertTrue(hrefs.get(2).contains("linkedin.com"), hrefs.toString());
    }
}
