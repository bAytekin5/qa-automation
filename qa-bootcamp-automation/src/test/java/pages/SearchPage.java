package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SearchPage {

    private WebDriver driver;

    private By searchInput = By.id("search");
    private By searchButton = By.cssSelector("button.action.search");

    public SearchPage(WebDriver driver) {
        this.driver = driver;
    }

    public void search(String keyword) {
        try {
            WebElement input = driver.findElement(searchInput);
            input.clear();
            input.sendKeys(keyword);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(searchButton));

            try {
                button.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            }

        } catch (Exception e) {
            throw new RuntimeException("Arama işlemi sırasında hata oluştu: " + e.getMessage());
        }
    }
}
