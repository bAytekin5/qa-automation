package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.BaseTest;
import pages.LoginPage;
import utils.Screenshot;

import java.io.IOException;

public class EmptyCartCheckoutTest extends BaseTest {

    @Test
    public void checkoutNotAllowedWithEmptyCart() {
        try {
            LoginPage login = new LoginPage(driver);
            login.clickSignInLink();
            login.enterEmail("validuser@mail.com");
            login.enterPassword("Test1234");
            login.clickLoginButton();

            driver.findElement(By.cssSelector(".showcart")).click();
            driver.findElement(By.cssSelector("a.viewcart")).click();

            driver.findElement(By.cssSelector("button.checkout")).click();

            WebElement emptyCartWarning = driver.findElement(By.cssSelector(".cart-empty"));
            Assert.assertTrue(emptyCartWarning.isDisplayed(), "Boş sepet uyarısı görünmedi!");

            test.pass("Boş sepetle checkout denemesi engellendi.");
            test.addScreenCaptureFromPath(Screenshot.capture(driver, "empty-cart-checkout"));

        } catch (Exception e) {
            test.fail("Checkout testi başarısız: " + e.getMessage());
            try {
                test.addScreenCaptureFromPath(Screenshot.capture(driver, "empty-cart-failure"));
            } catch (IOException io) {
                System.out.println("Ekran görüntüsü alınamadı: " + io.getMessage());
            }
            throw new AssertionError(e);
        }
    }
}
