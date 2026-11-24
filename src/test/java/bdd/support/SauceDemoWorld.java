package bdd.support;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

/**
 * One browser per scenario. Each scenario gets a fresh context, so cookies and
 * local storage (including the cart) start empty.
 */
public class SauceDemoWorld {

    private static final int TIMEOUT_MS = 10_000;

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;

    public void start() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(isHeadless()));
        context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(1920, 1080));
        page = context.newPage();
        page.setDefaultTimeout(TIMEOUT_MS);
    }

    public Page page() {
        return page;
    }

    public boolean isStarted() {
        return page != null;
    }

    public void stop() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        page = null;
        context = null;
        browser = null;
        playwright = null;
    }

    /** GitHub Actions sets CI=true. Local runs stay headed. */
    private static boolean isHeadless() {
        return Boolean.parseBoolean(System.getenv("CI"));
    }
}
