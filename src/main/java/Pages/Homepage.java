package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TestData;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Homepage {

    private static final By BURGER_MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By MENU_ITEMS = By.className("bm-item-list");
    private static final By CLOSE_MENU_BUTTON = By.id("react-burger-cross-btn");
    private static final By SORTING_MENU = By.className("product_sort_container");
    private static final By SOCIAL_MEDIA_ICONS = By.className("social");
    private static final By FOOTER = By.className("footer_copy");
    private static final By INVENTORY_ITEM_NAMES = By.className("inventory_item_name");
    private static final By INVENTORY_ITEM_PRICES = By.className("inventory_item_price");

    private final WebDriver driver;

    public Homepage(WebDriver driver) {
        this.driver = driver;
    }

    private WebDriverWait createWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void printMenuItemsAndCheckClickable() {
        List<WebElement> items = driver.findElements(MENU_ITEMS);
        WebDriverWait wait = createWait();

        for (WebElement item : items) {
            String text = item.getText().trim();
            boolean isEnabled = item.isEnabled();
            boolean isClickable = isElementClickable(wait, item);

            System.out.println("Menu Item: " + text);
            System.out.println(" - Enabled: " + isEnabled);
            System.out.println(" - Clickable: " + isClickable);
            System.out.println("---------------------------------");
        }
    }

    public void clickMenu() {
        WebDriverWait wait = createWait();
        wait.until(ExpectedConditions.elementToBeClickable(BURGER_MENU_BUTTON)).click();
    }

    public void clickCloseMenu() {
        driver.findElement(CLOSE_MENU_BUTTON).click();
    }

    public boolean isSortingMenuDisplayed() {
        return driver.findElement(SORTING_MENU).isDisplayed();
    }

    public void printSocialMediaIconsAndCheckClickable() {
        List<WebElement> icons = driver.findElements(SOCIAL_MEDIA_ICONS);
        WebDriverWait wait = createWait();

        for (WebElement icon : icons) {
            String text = resolveIconLabel(icon);
            boolean isDisplayed = icon.isDisplayed();
            boolean isEnabled = icon.isEnabled();
            boolean isClickable = isElementClickable(wait, icon);

            System.out.println("Social Media Icon: " + text);
            System.out.println(" - Displayed: " + isDisplayed);
            System.out.println(" - Enabled: " + isEnabled);
            System.out.println(" - Clickable: " + isClickable);
            System.out.println("---------------------------------");
        }
    }

    public boolean isFooterDisplayed() {
        WebDriverWait wait = createWait();
        WebElement footerElement = wait.until(ExpectedConditions.visibilityOfElementLocated(FOOTER));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", footerElement);
        String footerText = footerElement.getText().trim();
        return footerElement.isDisplayed()
                && footerText.matches(TestData.FOOTER_TEXT_PATTERN);
    }

    public void checkSortingByAlphabets() {
        WebDriverWait wait = createWait();
        Select sortDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(SORTING_MENU)));
        sortDropdown.selectByIndex(0);

        WebElement selectedOption = wait.until(ExpectedConditions.visibilityOf(sortDropdown.getFirstSelectedOption()));
        System.out.println("Selected sorting option: " + selectedOption.getText());

        List<WebElement> inventoryListItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(INVENTORY_ITEM_NAMES));
        System.out.println("Total inventory items: " + inventoryListItems.size());
        System.out.println(isSortedByFirstLetter(inventoryListItems, true)
                ? "The inventory list is sorted in A to Z order."
                : "The inventory list is NOT sorted in A to Z order.");
    }

    public void checkSortingByZtoA() {
        WebDriverWait wait = createWait();
        Select sortDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(SORTING_MENU)));
        sortDropdown.selectByIndex(1);

        WebElement sortingDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(SORTING_MENU));
        sortDropdown = new Select(sortingDropdown);
        WebElement selectedOption = wait.until(ExpectedConditions.visibilityOf(sortDropdown.getFirstSelectedOption()));
        System.out.println("Selected sorting option: " + selectedOption.getText());

        List<WebElement> inventoryListItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(INVENTORY_ITEM_NAMES));
        System.out.println("Total inventory items: " + inventoryListItems.size());
        System.out.println(isSortedByFirstLetter(inventoryListItems, false)
                ? "The inventory list is sorted in Z to A order."
                : "The inventory list is NOT sorted in Z to A order.");
    }

    public void checkPriceSortingLowToHigh() {
        validatePriceSorting(2, true);
    }

    public void checkPriceSortingHighToLow() {
        validatePriceSorting(3, false);
    }

    public boolean isBurgerMenuPresent() {
        try {
            WebElement menu = driver.findElement(BURGER_MENU_BUTTON);
            return menu.isDisplayed() && menu.isEnabled();
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void validatePriceSorting(int optionIndex, boolean ascending) {
        WebDriverWait wait = createWait();
        WebElement sortingDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(SORTING_MENU));

        Select sortDropdown = new Select(sortingDropdown);
        sortDropdown.selectByIndex(optionIndex);

        sortingDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(SORTING_MENU));
        sortDropdown = new Select(sortingDropdown);

        WebElement selectedOption = wait.until(ExpectedConditions.visibilityOf(sortDropdown.getFirstSelectedOption()));
        System.out.println("Selected sorting price: " + selectedOption.getText());

        List<WebElement> inventoryListItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(INVENTORY_ITEM_PRICES));
        System.out.println("Total inventory items: " + inventoryListItems.size());

        List<Double> prices = new ArrayList<>();
        for (WebElement item : inventoryListItems) {
            String priceText = item.getText().replace("$", "").trim();
            prices.add(Double.parseDouble(priceText));
            System.out.println((ascending ? "Low to High" : "High to Low") + " price " + priceText);
        }

        boolean isSorted = true;
        for (int i = 1; i < prices.size(); i++) {
            if (ascending ? prices.get(i) < prices.get(i - 1) : prices.get(i) > prices.get(i - 1)) {
                isSorted = false;
                break;
            }
        }

        System.out.println(isSorted
                ? "The inventory list is correctly sorted from " + (ascending ? "Low to High." : "High to Low.")
                : "The inventory list is NOT sorted correctly from " + (ascending ? "Low to High." : "High to Low."));
    }

    private boolean isSortedByFirstLetter(List<WebElement> inventoryListItems, boolean ascending) {
        for (int i = 1; i < inventoryListItems.size(); i++) {
            char currentFirstLetter = Character.toLowerCase(inventoryListItems.get(i).getText().trim().charAt(0));
            char previousFirstLetter = Character.toLowerCase(inventoryListItems.get(i - 1).getText().trim().charAt(0));

            if (ascending ? currentFirstLetter < previousFirstLetter : currentFirstLetter > previousFirstLetter) {
                return false;
            }
        }
        return true;
    }

    private boolean isElementClickable(WebDriverWait wait, WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private String resolveIconLabel(WebElement icon) {
        String text = icon.getText().trim();
        if (!text.isEmpty()) {
            return text;
        }

        text = icon.getAttribute("aria-label");
        if (text != null && !text.isEmpty()) {
            return text;
        }

        text = icon.getAttribute("title");
        return (text == null || text.isEmpty()) ? "Unknown Icon" : text;
    }
}
