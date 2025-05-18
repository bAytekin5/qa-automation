package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SearchPage;
import utils.BaseTest;
import utils.Screenshot;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class ProductCardTest extends BaseTest {

    @Test
    public void productCardsAppearAfterSearch() {
        try {
            SearchPage searchPage = new SearchPage(driver);
            searchPage.search("jacket");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-items")));

            List<WebElement> productCards = driver.findElements(By.cssSelector(".product-item"));

            Assert.assertTrue(productCards.size() > 0, "Hiç ürün kartı bulunamadı!");

            for (WebElement card : productCards) {
                WebElement name = card.findElement(By.cssSelector(".product-item-link"));
                WebElement price = card.findElement(By.cssSelector(".price"));

                Assert.assertTrue(name.isDisplayed(), "Ürün adı görünmüyor!");
                Assert.assertTrue(price.isDisplayed(), "Ürün fiyatı görünmüyor!");
            }

            test.pass("Ürün kartları başarıyla listelendi ve doğrulandı.");
            test.addScreenCaptureFromPath(Screenshot.capture(driver, "productCards-success"));

        } catch (Exception e) {
            test.fail("Ürün kartları testinde hata: " + e.getMessage());
            try {
                test.addScreenCaptureFromPath(Screenshot.capture(driver, "productCards-failure"));
            } catch (IOException io) {
                System.out.println("Ekran görüntüsü alınamadı: " + io.getMessage());
            }
            throw new RuntimeException(e);
        }
    }
}
