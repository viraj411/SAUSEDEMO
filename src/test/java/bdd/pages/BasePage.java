package bdd.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public abstract class BasePage {

    protected final Page page;

    protected BasePage(Page page) {
        this.page = page;
    }

    protected void click(String selector) {
        page.locator(selector).first().click();
    }

    protected void fill(String selector, String value) {
        page.locator(selector).fill(value == null ? "" : value);
    }

    protected String textOf(String selector) {
        return page.locator(selector).first().innerText().trim();
    }

    protected List<String> textsOf(String selector) {
        page.locator(selector).first().waitFor();
        return page.locator(selector).allInnerTexts().stream()
                .map(String::trim)
                .toList();
    }

    /** Waits up to the page timeout. Returns false instead of throwing when the element never shows. */
    protected boolean isVisible(String selector) {
        try {
            page.locator(selector).first().waitFor();
            return true;
        } catch (TimeoutError e) {
            return false;
        }
    }

    /** Checks right now with no waiting, for counting or absence checks. */
    protected boolean isDisplayedNow(String selector) {
        return page.locator(selector).count() > 0 && page.locator(selector).first().isVisible();
    }

    protected void waitForUrl(String fragment) {
        page.waitForURL(Pattern.compile(".*" + Pattern.quote(fragment) + ".*"));
    }

    /** Clicks the first match until no matches remain, as the Selenium helper did. */
    protected void clickUntilGone(String selector) {
        while (page.locator(selector).count() > 0) {
            int before = page.locator(selector).count();
            page.locator(selector).first().click();
            assertThat(page.locator(selector)).not().hasCount(before);
        }
    }
}
