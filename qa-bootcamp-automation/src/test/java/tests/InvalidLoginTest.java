package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;
import utils.Screenshot;

import java.io.IOException;

public class InvalidLoginTest extends BaseTest {

    @Test
    public void invalidLoginShowsError() {
        try {
            LoginPage login = new LoginPage(driver);
            login.clickSignInLink();
            login.enterEmail("wronguser@mail.com");
            login.enterPassword("WrongPass123");
            login.clickLoginButton();

            WebElement errorMsg = driver.findElement(By.cssSelector(".message-error"));
            Assert.assertTrue(errorMsg.isDisplayed(), "Hata mesajı görünmüyor!");

            test.pass("Hatalı giriş denemesi başarıyla engellendi.");
            test.addScreenCaptureFromPath(Screenshot.capture(driver, "invalid-login-success"));

        } catch (Exception e) {
            test.fail("Hatalı login testi başarısız: " + e.getMessage());
            try {
                test.addScreenCaptureFromPath(Screenshot.capture(driver, "invalid-login-failure"));
            } catch (IOException io) {
                System.out.println("Ekran görüntüsü alınamadı: " + io.getMessage());
            }
            throw new AssertionError(e);
        }
    }
}
