package bdd.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Homepage extends BasePage {

    private static final String BURGER_MENU_BUTTON = "#react-burger-menu-btn";
    private static final String CLOSE_MENU_BUTTON = "#react-burger-cross-btn";
    private static final String MENU_WRAP = ".bm-menu-wrap";
    private static final String MENU_LINKS = "a.bm-item";
    private static final String ALL_ITEMS_LINK = "#inventory_sidebar_link";
    private static final String RESET_APP_STATE_LINK = "#reset_sidebar_link";
    private static final String SORTING_MENU = ".product_sort_container";
    private static final String INVENTORY_ITEMS = ".inventory_item";
    private static final String INVENTORY_ITEM_NAMES = ".inventory_item_name";
    private static final String INVENTORY_ITEM_PRICES = ".inventory_item_price";
    private static final String SOCIAL_CONTAINER = ".social";
    private static final String SOCIAL_LINKS = ".social a";
    private static final String FOOTER = ".footer_copy";
    private static final String PAGE_TITLE = ".title";
    private static final String CART_ICON = "#shopping_cart_container";

    public Homepage(Page page) {
        super(page);
    }

    public void waitUntilLoaded() {
        page.locator(INVENTORY_ITEMS).first().waitFor();
    }

    public String getPageTitle() {
        return textOf(PAGE_TITLE);
    }

    public int getProductCount() {
        return page.locator(INVENTORY_ITEMS).count();
    }

    public boolean eachProductShowsNameAndPrice() {
        Locator items = page.locator(INVENTORY_ITEMS);
        int count = items.count();
        if (count == 0) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            String name = items.nth(i).locator(INVENTORY_ITEM_NAMES).innerText().trim();
            String price = items.nth(i).locator(INVENTORY_ITEM_PRICES).innerText().trim();
            if (name.isEmpty() || !price.matches("\\$\\d+\\.\\d{2}")) {
                return false;
            }
        }
        return true;
    }

    public List<String> getProductNames() {
        return textsOf(INVENTORY_ITEM_NAMES);
    }

    public String getFirstProductName() {
        return getProductNames().get(0);
    }

    public List<Double> getProductPrices() {
        return textsOf(INVENTORY_ITEM_PRICES).stream()
                .map(price -> Double.parseDouble(price.replace("$", "").trim()))
                .toList();
    }

    public String sortByValue(String value) {
        page.locator(SORTING_MENU).selectOption(value);
        assertThat(page.locator(SORTING_MENU)).hasValue(value);
        return page.locator(SORTING_MENU).locator("option:checked").innerText().trim();
    }

    public boolean isSortedByName(boolean ascending) {
        List<String> actual = getProductNames();
        List<String> expected = new ArrayList<>(actual);
        expected.sort(String.CASE_INSENSITIVE_ORDER);
        if (!ascending) {
            Collections.reverse(expected);
        }
        return actual.equals(expected);
    }

    public boolean isSortedByPrice(boolean ascending) {
        List<Double> actual = getProductPrices();
        List<Double> expected = new ArrayList<>(actual);
        expected.sort(ascending ? Comparator.naturalOrder() : Comparator.reverseOrder());
        return actual.equals(expected);
    }

    public void clickMenu() {
        if (isMenuOpen()) {
            return;
        }
        click(BURGER_MENU_BUTTON);
        assertThat(page.locator(MENU_WRAP)).hasAttribute("aria-hidden", "false");
    }

    public boolean isMenuOpen() {
        return page.locator(MENU_WRAP).count() > 0
                && "false".equals(page.locator(MENU_WRAP).first().getAttribute("aria-hidden"));
    }

    public void closeMenuIfOpen() {
        if (!isMenuOpen()) {
            return;
        }
        click(CLOSE_MENU_BUTTON);
        assertThat(page.locator(MENU_WRAP)).not().hasAttribute("aria-hidden", "false");
    }

    public List<String> getMenuItemLabels() {
        clickMenu();
        return textsOf(MENU_LINKS).stream()
                .map(label -> label.replaceAll("\\s+", " "))
                .filter(label -> !label.isEmpty())
                .toList();
    }

    public boolean areMenuItemsEnabled() {
        Locator items = page.locator(MENU_LINKS);
        int count = items.count();
        if (count == 0) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            if (!items.nth(i).isEnabled()) {
                return false;
            }
        }
        return true;
    }

    public void clickAllItems() {
        click(ALL_ITEMS_LINK);
    }

    public void resetAppState() {
        clickMenu();
        click(RESET_APP_STATE_LINK);
        closeMenuIfOpen();
    }

    public List<String> getSocialLinkHrefs() {
        page.locator(SOCIAL_CONTAINER).scrollIntoViewIfNeeded();
        Locator links = page.locator(SOCIAL_LINKS);
        List<String> hrefs = new ArrayList<>();
        for (int i = 0; i < links.count(); i++) {
            hrefs.add((String) links.nth(i).evaluate("element => element.href"));
        }
        return hrefs;
    }

    public String getFooterText() {
        page.locator(FOOTER).scrollIntoViewIfNeeded();
        return textOf(FOOTER);
    }

    public boolean isCartIconDisplayed() {
        return isVisible(CART_ICON);
    }

    public boolean isFooterMatching(String pattern) {
        return getFooterText().matches(pattern);
    }
}
