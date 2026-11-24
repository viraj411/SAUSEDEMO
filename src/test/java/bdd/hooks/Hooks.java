package bdd.hooks;

import bdd.support.SauceDemoWorld;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    private final SauceDemoWorld world;

    public Hooks(SauceDemoWorld world) {
        this.world = world;
    }

    @Before
    public void openBrowser() {
        world.start();
    }

    @After
    public void closeBrowser(Scenario scenario) {
        if (scenario.isFailed() && world.isStarted()) {
            scenario.attach(world.page().screenshot(), "image/png", "failure-screenshot");
        }
        world.stop();
    }
}
