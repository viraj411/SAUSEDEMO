package Pages;

import dev.failsafe.internal.util.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
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
        driver.findElement(threedot).click();
    }

    public void clickclose() {
        driver.findElement(closemenu).click();
    }
    public boolean issortingmenuDisplayed() {
        return driver.findElement(sortingmenu).isDisplayed();
    }

        public void printSocialMediaIconsAndCheckClickable() {
            List<WebElement> icons = driver.findElements(socialmediaicons); // Get all social media icons

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
    }



