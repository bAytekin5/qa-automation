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

public class ProductDetailTest extends BaseTest {

    @Test
    public void userCanViewProductDetail() {
        try {
            SearchPage searchPage = new SearchPage(driver);
            searchPage.search("jacket");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-item")));

            List<WebElement> productLinks = driver.findElements(By.cssSelector(".product-item-link"));
            Assert.assertFalse(productLinks.isEmpty(), "Hiç ürün bulunamadı!");

            String productName = productLinks.get(0).getText();
            productLinks.get(0).click();

            WebElement productTitle = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".page-title .base"))
            );

            Assert.assertTrue(productTitle.getText().contains(productName),
                    "Detay sayfasındaki ürün adı, karttaki adla uyuşmuyor!");

            WebElement addToCartButton = driver.findElement(By.cssSelector("#product-addtocart-button"));
            Assert.assertTrue(addToCartButton.isDisplayed(), "Add to Cart butonu görünmüyor!");

            test.pass("Ürün detay sayfası başarıyla yüklendi.");
            test.addScreenCaptureFromPath(Screenshot.capture(driver, "product-detail-success"));

        } catch (Exception e) {
            test.fail("Ürün detay testi başarısız: " + e.getMessage());
            try {
                test.addScreenCaptureFromPath(Screenshot.capture(driver, "product-detail-failure"));
            } catch (IOException io) {
                System.out.println("Ekran görüntüsü alınamadı: " + io.getMessage());
            }
            throw new RuntimeException(e);
        }
    }
}
