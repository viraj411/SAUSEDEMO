package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public abstract class BasePage {

    protected static final Duration TIMEOUT = Duration.ofSeconds(10);

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TIMEOUT);
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        if (value != null && !value.isEmpty()) {
            element.sendKeys(value);
        }
    }

    protected String textOf(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
    }

    protected String stableText(By locator) {
        return wait.until(driver -> {
            try {
                String text = driver.findElement(locator).getText().trim();
                return text.isEmpty() ? null : text;
            } catch (StaleElementReferenceException e) {
                return null;
            }
        });
    }

    protected List<String> visibleTexts(By locator) {
        return wait.until(driver -> {
            try {
                List<String> texts = new ArrayList<>();
                for (WebElement element : driver.findElements(locator)) {
                    String text = element.getText().trim();
                    if (text.isEmpty()) {
                        return null;
                    }
                    texts.add(text);
                }
                return texts;
            } catch (StaleElementReferenceException e) {
                return null;
            }
        });
    }

    protected boolean isDisplayed(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    protected void scrollIntoView(By locator) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void waitForUrlToContain(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    protected int countDecreased(By locator, int previousCount) {
        return wait.until(driver -> {
            int count = driver.findElements(locator).size();
            return count < previousCount ? count : null;
        });
    }
}
