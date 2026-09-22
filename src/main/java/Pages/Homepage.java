package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Homepage extends BasePage {

    private static final By BURGER_MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By CLOSE_MENU_BUTTON = By.id("react-burger-cross-btn");
    private static final By MENU_WRAP = By.className("bm-menu-wrap");
    private static final By MENU_LINKS = By.cssSelector("a.bm-item");
    private static final By ALL_ITEMS_LINK = By.id("inventory_sidebar_link");
    private static final By RESET_APP_STATE_LINK = By.id("reset_sidebar_link");
    private static final By SORTING_MENU = By.className("product_sort_container");
    private static final By INVENTORY_ITEMS = By.className("inventory_item");
    private static final By INVENTORY_ITEM_NAMES = By.className("inventory_item_name");
    private static final By INVENTORY_ITEM_PRICES = By.className("inventory_item_price");
    private static final By SOCIAL_CONTAINER = By.className("social");
    private static final By SOCIAL_LINKS = By.cssSelector(".social a");
    private static final By FOOTER = By.className("footer_copy");
    private static final By PAGE_TITLE = By.className("title");
    private static final By CART_ICON = By.id("shopping_cart_container");

    public Homepage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(INVENTORY_ITEMS));
    }

    public String getPageTitle() {
        return stableText(PAGE_TITLE);
    }

    public int getProductCount() {
        return driver.findElements(INVENTORY_ITEMS).size();
    }

    public boolean eachProductShowsNameAndPrice() {
        List<WebElement> items = driver.findElements(INVENTORY_ITEMS);
        if (items.isEmpty()) {
            return false;
        }
        for (WebElement item : items) {
            String name = item.findElement(INVENTORY_ITEM_NAMES).getText().trim();
            String price = item.findElement(INVENTORY_ITEM_PRICES).getText().trim();
            if (name.isEmpty() || !price.matches("\\$\\d+\\.\\d{2}")) {
                return false;
            }
        }
        return true;
    }

    public List<String> getProductNames() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(INVENTORY_ITEM_NAMES))
                .stream()
                .map(item -> item.getText().trim())
                .toList();
    }

    public String getFirstProductName() {
        return getProductNames().get(0);
    }

    public List<Double> getProductPrices() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(INVENTORY_ITEM_PRICES))
                .stream()
                .map(item -> Double.parseDouble(item.getText().replace("$", "").trim()))
                .toList();
    }

    public String sortByValue(String value) {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(SORTING_MENU));
        new Select(dropdown).selectByValue(value);
        wait.until(driver -> value.equals(new Select(driver.findElement(SORTING_MENU))
                .getFirstSelectedOption()
                .getAttribute("value")));
        return new Select(driver.findElement(SORTING_MENU)).getFirstSelectedOption().getText().trim();
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
        wait.until(driver -> isMenuOpen());
    }

    public boolean isMenuOpen() {
        List<WebElement> wraps = driver.findElements(MENU_WRAP);
        return !wraps.isEmpty() && "false".equals(wraps.get(0).getAttribute("aria-hidden"));
    }

    public void closeMenuIfOpen() {
        if (!isMenuOpen()) {
            return;
        }
        click(CLOSE_MENU_BUTTON);
        wait.until(driver -> !isMenuOpen());
    }

    public List<String> getMenuItemLabels() {
        clickMenu();
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(MENU_LINKS))
                .stream()
                .map(item -> item.getText().replaceAll("\\s+", " ").trim())
                .filter(label -> !label.isEmpty())
                .toList();
    }

    public boolean areMenuItemsEnabled() {
        List<WebElement> items = driver.findElements(MENU_LINKS);
        return !items.isEmpty() && items.stream().allMatch(WebElement::isEnabled);
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
        scrollIntoView(SOCIAL_CONTAINER);
        return driver.findElements(SOCIAL_LINKS).stream()
                .map(link -> link.getAttribute("href"))
                .toList();
    }

    public String getFooterText() {
        scrollIntoView(FOOTER);
        return textOf(FOOTER);
    }

    public boolean isCartIconDisplayed() {
        return isDisplayed(CART_ICON);
    }
}
