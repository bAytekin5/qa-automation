package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SearchPage;
import utils.BaseTest;
import utils.UserStorage;
import utils.Screenshot;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class CartTest extends BaseTest {

    @Test
    public void userCanAddProductToCart() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            String email = UserStorage.readEmail();
            if (email == null) throw new IllegalStateException("Sepet testi için e-posta bulunamadı!");

            LoginPage login = new LoginPage(driver);
            login.clickSignInLink();
            login.enterEmail(email);
            login.enterPassword("Test1234");
            login.clickLoginButton();

            SearchPage searchPage = new SearchPage(driver);
            searchPage.search("jacket");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".product-item")));
            List<WebElement> productLinks = driver.findElements(By.cssSelector(".product-item-link"));
            Assert.assertFalse(productLinks.isEmpty(), "Ürün listesi boş!");
            productLinks.get(0).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".product-info-main")));

            selectOption(".swatch-attribute.size .swatch-option", "Beden seçildi.", "Beden seçeneği bulunamadı!");
            selectOption(".swatch-attribute.color .swatch-option", "Renk seçildi.", " Renk seçeneği bulunamadı!");

            WebElement addToCartBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("product-addtocart-button"))
            );
            addToCartBtn.click();

            WebElement successMessage = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".message-success.success.message"))
            );

            Assert.assertTrue(successMessage.getText().toLowerCase().contains("added") ||
                            successMessage.getText().toLowerCase().contains("sepete"),
                    "Sepete ekleme sonrası başarı mesajı çıkmadı!");

            driver.findElement(By.cssSelector(".action.showcart")).click();
            WebElement viewCart = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.viewcart")));
            viewCart.click();

            WebElement cartTable = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart.table"))
            );
            List<WebElement> cartItems = cartTable.findElements(By.cssSelector(".product-item-details"));
            Assert.assertFalse(cartItems.isEmpty(), "Sepet boş görünüyor!");

            test.pass("Ürün sepete başarıyla eklendi ve doğrulandı.");
            test.addScreenCaptureFromPath(Screenshot.capture(driver, "cart-success"));

        } catch (Exception e) {
            test.fail("Sepet testi hatası: " + e.getMessage());
            try {
                test.addScreenCaptureFromPath(Screenshot.capture(driver, "cart-failure"));
            } catch (IOException io) {
                System.out.println("Ekran görüntüsü alınamadı: " + io.getMessage());
            }
            throw new AssertionError("Cart test hatası: " + e.getMessage());
        }
    }

    private void selectOption(String cssSelector, String successMsg, String errorMsg) {
        List<WebElement> options = driver.findElements(By.cssSelector(cssSelector));
        if (!options.isEmpty()) {
            options.get(0).click();
            System.out.println(successMsg);
        } else {
            throw new AssertionError(errorMsg);
        }
    }
}
