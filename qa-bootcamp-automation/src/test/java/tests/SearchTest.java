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

public class SearchTest extends BaseTest {

    @Test
    public void userCanSearchProduct() {
        try {
            SearchPage searchPage = new SearchPage(driver);
            searchPage.search("jacket");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement searchTitle = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".base"))
            );

            Assert.assertTrue(searchTitle.getText().toLowerCase().contains("jacket"),
                    "Arama sonucu başlığı beklenen kelimeyi içermiyor.");

            test.pass("Test başarılı: Arama sonucu görüntülendi.");
            test.addScreenCaptureFromPath(Screenshot.capture(driver, "search-success"));

        } catch (Exception e) {
            test.fail("Arama testi başarısız: " + e.getMessage());
            try {
                test.addScreenCaptureFromPath(Screenshot.capture(driver, "search-failure"));
            } catch (IOException io) {
                System.out.println("Ekran görüntüsü alınamadı: " + io.getMessage());
            }
            throw new RuntimeException(e);
        }
    }
}
