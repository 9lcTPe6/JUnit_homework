package ru.appline.ibs.homework;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AutoFillerTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions builder;

    @BeforeEach
    public void initializeDriver() {

        System.setProperty("webdriver.chrome.driver", "src/test/resources/webdriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driverPageLoading();

        wait = new WebDriverWait(driver, 30, 1000);

        String baseUrl = "http://www.sberbank.ru/ru/person";

        driver.get(baseUrl);
    }

    public void driverPageLoading() {
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public void clickOnElement(WebElement element) {
        builder = new Actions(driver);
        builder.moveToElement(element).click().build().perform();
    }

    @Test
    public void runTest() {

        String menuCards = "//li[4]/a/span/div";
        List<WebElement> menuCardsClick = driver.findElements(By.xpath(menuCards));
        if (!menuCardsClick.isEmpty()) {
            menuCardsClick.get(0).click();
        }

        String debCards = "//a[contains(text(),'Дебетовые карты')]";
        WebElement debCardsClick = driver.findElement(By.xpath(debCards));
        debCardsClick.click();

        String titlePos = "//h1[contains(.,'Дебетовые карты')]";
        Assertions.assertEquals("Дебетовые карты",
                driver.findElement(By.xpath(titlePos)).getText(),
                "Заголовок не соответствует заданному.");

        String forYoung = "//div[6]/a/span";
        WebElement forYoungClick = driver.findElement(By.xpath(forYoung));
        forYoungClick.click();

        String checkingHeader = "//h1[contains(.,'Молодёжная карта')]";
        Assertions.assertEquals("Молодёжная карта",
                driver.findElement(By.xpath(checkingHeader)).getText(),
                "Заголовок не соответствует действительному.");

        scrollUp();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String sendButton = "//a[contains(.,'Оформить онлайн')]";
        WebElement sendButtonClick = driver.findElement(By.xpath(sendButton));
        sendButtonClick.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scrollDown();

        fillWith("Тестов", "lastName");
        fillWith("Тест", "firstName");
        fillWith("Тестович", "middleName");
        fillWith("", "cardName");
        fillWith("07.11.2000", "birthDate");
        fillWith("test@test.ru", "email");
        fillWith("1111111111", "phone");

        String sendOfferButton = "//button[@data-step]";
        WebElement sendOfferButtonClick = driver.findElement(By.xpath(sendOfferButton));
        scrollDown();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clickOnElement(sendOfferButtonClick);

        String errorAlertXPath = "//section[2]/div[3]/descendant::div[@class=\"odcui-error__text\"]";
        WebElement errorAlert = driver.findElement(By.xpath(errorAlertXPath));
        scrollToElementJs(errorAlert);
        waitUtilElementToBeVisible(errorAlert);
        Assertions.assertEquals("Обязательное поле", errorAlert.getText(),
                "Проверка на наличие предупреждения \"Обязательное поле\" не пройдена");
    }

    private void fillWith(String value, String location) {
        String target = "//input[@data-name=\"%s\"]";
        target = String.format(target, location);
        WebElement filler = driver.findElement(By.xpath(target));
        clickOnElement(filler);
        filler.sendKeys(value);

    }

    private void scrollToElementJs(WebElement element) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void scrollUp() {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,-250)", "");
    }

    private void scrollDown() {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,250)", "");
    }

    private void waitUtilElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    private void waitUtilElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void waitUtilElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    @AfterEach
    public void after() {
        driver.quit();
    }
}
