package Pages;

import dev.failsafe.internal.util.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Homepage {
    WebDriver driver;

    public Homepage(WebDriver driver) {
        this.driver = driver;
    }

    //Locators
    private By threedot = By.id("react-burger-menu-btn");
    private By menuItems = By.className("bm-item-list");
    private By closemenu = By.id("react-burger-cross-btn");
    private By sortingmenu = By.className("product_sort_container");
    private By socialmediaicons = By.className("social");
    private By footer = By.className("footer_copy");


    public void printMenuItemsAndCheckClickable() {
        List<WebElement> items = driver.findElements(menuItems); // Get all menu items

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        for (WebElement item : items) {
            String text = item.getText().trim(); // Get the text and trim spaces

            // Check if item is displayed and enabled
            boolean isDisplayed = item.isDisplayed();
            boolean isEnabled = item.isEnabled();

            // Check if item is clickable using WebDriverWait
            boolean isClickable = false;
            try {
                wait.until(ExpectedConditions.elementToBeClickable(item));
                isClickable = true;
            } catch (TimeoutException e) {
                isClickable = false;
            }

            // Print results
            System.out.println("Menu Item: " + text);
            //System.out.println(" - Displayed: " + isDisplayed);
            System.out.println(" - Enabled: " + isEnabled);
            System.out.println(" - Clickable: " + isClickable);
            System.out.println("---------------------------------");
        }
    }

    public void clickMenu() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(threedot)).isDisplayed();
        driver.findElement(threedot).click();
    }

    public void clickclose() {
        driver.findElement(closemenu).click();
    }

    public boolean issortingmenuDisplayed() {
        return driver.findElement(sortingmenu).isDisplayed();
    }

    public void printSocialMediaIconsAndCheckClickable() {
        List<WebElement> icons = driver.findElements(socialmediaicons);// Get all social media icons

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        for (WebElement icon : icons) {
            String text = icon.getText().trim(); // Get text if available
            if (text.isEmpty()) {
                text = icon.getAttribute("aria-label"); // Use aria-label if text is missing
            }
            if (text == null || text.isEmpty()) {
                text = icon.getAttribute("title"); // Use title attribute if available
            }
            if (text == null || text.isEmpty()) {
                text = "Unknown Icon"; // Default text if nothing is found
            }

            // Check if icon is displayed and enabled
            boolean isDisplayed = icon.isDisplayed();
            boolean isEnabled = icon.isEnabled();

            // Check if icon is clickable using WebDriverWait
            boolean isClickable = false;
            try {
                wait.until(ExpectedConditions.elementToBeClickable(icon));
                isClickable = true;
            } catch (TimeoutException e) {
                isClickable = false;
            }

            // Print results
            System.out.println("Social Media Icon:" + text);
            System.out.println(" - Displayed: " + isDisplayed);
            System.out.println(" - Enabled: " + isEnabled);
            System.out.println(" - Clickable: " + isClickable);
            System.out.println("---------------------------------");
        }
    }

    public String isFooterDisplayed() {
        return driver.findElement(footer).getText();
    }


    public void checksortingByAlphabets() {
        // Wait for the sorting dropdown to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement sortingDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(sortingmenu));

        Select sc = new Select(sortingDropdown);
        sc.selectByIndex(0);

        // Wait for the dropdown to update the selected option
        WebElement selectedOption = wait.until(ExpectedConditions.visibilityOf(sc.getFirstSelectedOption()));
        String selectedTextAtoZ = selectedOption.getText();

        System.out.println("Selected sorting option: " + selectedTextAtoZ);

        // Wait for the inventory list to be visible
        List<WebElement> inventoryListItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_name")));

        System.out.println("Total inventory items: " + inventoryListItems.size());

        boolean isSorted = true;

        for (int i = 1; i < inventoryListItems.size(); i++) {
            String currentName = inventoryListItems.get(i).getText().trim();
            String previousName = inventoryListItems.get(i - 1).getText().trim();

            char currentFirstLetter = Character.toLowerCase(currentName.charAt(0));
            char previousFirstLetter = Character.toLowerCase(previousName.charAt(0));

            if (currentFirstLetter < previousFirstLetter) {
                isSorted = false;
                break; // Stop checking if the order is violated
            }
        }

        if (isSorted) {
            System.out.println("The inventory list is sorted in A to Z order.");
        } else {
            System.out.println("The inventory list is NOT sorted in A to Z order.");
        }
    }

    public void checkSortingByZtoA() {
        // Wait for the sorting dropdown to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement sortingDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(sortingmenu));

        Select sc = new Select(sortingDropdown);
        sc.selectByIndex(1);  // Select the second option for Z to A sorting

        // Re-locate the dropdown to get a fresh reference after the selection
        sortingDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(sortingmenu)); // Re-fetching sortingDropdown after selecting

        // Re-create the Select object with the fresh reference
        sc = new Select(sortingDropdown);

        // Wait for the updated first selected option to be visible
        WebElement selectedOption = wait.until(ExpectedConditions.visibilityOf(sc.getFirstSelectedOption()));

        String selectedTextZtoA = selectedOption.getText();
        System.out.println("Selected sorting option: " + selectedTextZtoA);

        // Wait for the inventory list to be visible
        List<WebElement> inventoryListItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_name")));

        System.out.println("Total inventory items: " + inventoryListItems.size());

        boolean isSorted = true;

        // Check if the inventory list is sorted in reverse (Z to A) order
        for (int i = 1; i < inventoryListItems.size(); i++) {
            String currentName = inventoryListItems.get(i).getText().trim();
            String previousName = inventoryListItems.get(i - 1).getText().trim();

            char currentFirstLetter = Character.toLowerCase(currentName.charAt(0));
            char previousFirstLetter = Character.toLowerCase(previousName.charAt(0));

            if (currentFirstLetter > previousFirstLetter) {
                isSorted = false;
                break; // Stop checking if the order is violated
            }
        }

        if (isSorted) {
            System.out.println("The inventory list is sorted in Z to A order.");
        } else {
            System.out.println("The inventory list is NOT sorted in Z to A order.");
        }
    }
    public void checkPriceSortingLowToHigh() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for the sorting dropdown to be visible
        WebElement sortingDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(sortingmenu));

        Select sc = new Select(sortingDropdown);
        sc.selectByIndex(2);  // Selecting the "Low to High" option

        // Re-fetch the sorting dropdown to avoid stale element exceptions
        sortingDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(sortingmenu));
        sc = new Select(sortingDropdown);

        // Wait for the selected option to be visible
        WebElement selectedOption = wait.until(ExpectedConditions.visibilityOf(sc.getFirstSelectedOption()));
        String selectedPriceLowToHigh = selectedOption.getText();
        System.out.println("Selected sorting price: " + selectedPriceLowToHigh);

        // Wait for the inventory prices to be visible
        List<WebElement> inventoryListItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_price")));
        System.out.println("Total inventory items: " + inventoryListItems.size());

        // Extract and convert price values to a list of doubles
        List<Double> prices = new ArrayList<>();
        for (WebElement item : inventoryListItems) {
            String priceText = item.getText().replace("$", "").trim();  // Remove "$" sign
            prices.add(Double.parseDouble(priceText)); // Convert to double
            System.out.println("Low to High price "+ priceText);
        }

        // Validate that prices are sorted in ascending order
        boolean isSorted = true;
        for (int i = 1; i < prices.size(); i++) {
            if (prices.get(i) < prices.get(i - 1)) {  // If current price < previous price, it's incorrect
                isSorted = false;
                break;
            }
        }

        if (isSorted) {
            System.out.println("The inventory list is correctly sorted from Low to High.");
        } else {
            System.out.println("The inventory list is NOT sorted correctly from Low to High.");
        }
    }

    public void checkPriceSortingHighToLow() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait for the sorting dropdown to be visible
        WebElement sortingDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(sortingmenu));

        Select sc = new Select(sortingDropdown);
        sc.selectByIndex(3);  // Selecting the "High to Low" option

        // Re-fetch the sorting dropdown to avoid stale element exceptions
        sortingDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(sortingmenu));
        sc = new Select(sortingDropdown);

        // Wait for the selected option to be visible
        WebElement selectedOption = wait.until(ExpectedConditions.visibilityOf(sc.getFirstSelectedOption()));
        String selectedPriceHighToLow = selectedOption.getText();
        System.out.println("Selected sorting price: " + selectedPriceHighToLow);

        // Wait for the inventory prices to be visible
        List<WebElement> inventoryListItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("inventory_item_price")));
        System.out.println("Total inventory items: " + inventoryListItems.size());

        // Extract and convert price values to a list of doubles
        List<Double> prices = new ArrayList<>();
        for (WebElement item : inventoryListItems) {
            String priceText = item.getText().replace("$", "").trim();  // Remove "$" sign
            prices.add(Double.parseDouble(priceText));  // Convert to double
            System.out.println("High to low price " + priceText);
        }

        // Validate that prices are sorted in descending order (High to Low)
        boolean isSorted = true;
        for (int i = 1; i < prices.size(); i++) {
            if (prices.get(i) > prices.get(i - 1)) {  // If current price > previous price, it's incorrect
                isSorted = false;
                break;
            }
        }

        if (isSorted) {
            System.out.println("The inventory list is correctly sorted from High to Low.");
        } else {
            System.out.println("The inventory list is NOT sorted correctly from High to Low.");
        }
    }

}









