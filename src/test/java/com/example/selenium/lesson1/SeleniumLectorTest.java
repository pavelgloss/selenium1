package com.example.selenium.lesson1;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.assertj.core.api.Assertions.assertThat;


public class SeleniumLectorTest {
    private WebDriver driver;

    @Before
    public void init() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "src/test/resources/drivers/IEDriverServer.exe");
        //System.setProperty("webdriver.ie.driver", "src/test/resources/drivers/IEDriverServer32bit.exe");
        System.setProperty("webdriver.opera.driver", "src/test/resources/drivers/operadriver.exe");
        System.setProperty("phantomjs.binary.path", "src/test/resources/drivers/phantomjs.exe");

        driver = new ChromeDriver();
//        driver = new InternetExplorerDriver();

        driver.manage().window().maximize();
    }

    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void failed(Throwable e, Description description) {
            saveScreenshot(driver);
        }

        @Override
        protected void succeeded(Description description) {
        }
    };

    private void saveScreenshot(WebDriver driver) {

    }

    @Test
    public void testGoogleSearch() {
        driver.get("http://www.google.com");
        WebElement searchInput = driver.findElement(By.name("q"));
        searchInput.sendKeys("selenium tutorial");
        searchInput.sendKeys(Keys.RETURN);
        driver.close();
    }

    @Test
    public void loginUsingValidCredentials() {
        // WHEN user opens login page and inputs valid credentials

        driver.get("http://digitalnizena.cz/church/");

        driver.findElement(By.cssSelector("#Login"));   // test crash if container not found

        WebElement usernameInput = driver.findElement(By.id("UserBox"));

        // finding element by value of attribute name
        // WebElement usernameInput = driver.findElement(By.name("User"));

        WebElement passwordInput = driver.findElement(By.id("PasswordBox"));
        WebElement loginButton = driver.findElement(By.cssSelector("form button.btn-primary"));

        usernameInput.sendKeys("church");
        passwordInput.sendKeys("church12345");
        loginButton.click();

        // THEN user is logged in (and see dashboard)
        assertThat(driver.getCurrentUrl()).isEqualTo("http://digitalnizena.cz/church/Menu.php");
        assertThat(driver.findElements(By.cssSelector(".login-box"))).isEmpty();

        assertThat(driver.getTitle()).contains("ChurchCRM: Welcome to");
        driver.findElement(By.cssSelector("aside.main-sidebar"));
    }

    @Test
    public void loginUsingInvalidCredentials() {
        // WHEN user opens login page and inputs invalid credentials
        driver.get("http://digitalnizena.cz/church/");

        WebElement usernameInput = driver.findElement(By.id("UserBox"));
        WebElement passwordInput = driver.findElement(By.id("PasswordBox"));
        WebElement loginButton = driver.findElement(By.className("btn-primary"));

        usernameInput.sendKeys("invalidUsername");
        passwordInput.sendKeys("invalidPassword");
        loginButton.click();

        // THEN user is not logged in and error message is shown
        assertThat(driver.getCurrentUrl()).isEqualTo("http://digitalnizena.cz/church/Login.php");
        assertThat(driver.findElements(By.cssSelector(".login-box"))).isNotEmpty();
        WebElement errorDiv = driver.findElement(By.cssSelector("#Login .alert.alert-error"));    // there are in fact two .alert .alert-error boxes in page, we want for login div
        assertThat(errorDiv.getText()).isEqualTo("Invalid login or password");
    }


}