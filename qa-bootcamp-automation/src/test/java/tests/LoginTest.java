package tests;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;
import utils.UserStorage;
import utils.Screenshot;

import java.io.IOException;

public class LoginTest extends BaseTest {

    @Test
    public void validLoginTest() {
        try {
            String email = UserStorage.readEmail();
            if (email == null) {
                throw new IllegalStateException("Login için e-posta bulunamadı.");
            }

            LoginPage login = new LoginPage(driver);
            login.clickSignInLink();
            login.enterEmail(email);
            login.enterPassword("Test1234");
            login.clickLoginButton();

            boolean welcomeDisplayed = driver.findElements(
                    By.cssSelector(".header.links, .greet.welcome, .logged-in")
            ).size() > 0;

            Assert.assertTrue(welcomeDisplayed, "Giriş başarısız! Welcome mesajı görünmedi.");

            test.pass("Login başarılı.");
            test.addScreenCaptureFromPath(Screenshot.capture(driver, "login-success"));

        } catch (Exception e) {
            test.fail("Login testi başarısız: " + e.getMessage());
            try {
                test.addScreenCaptureFromPath(Screenshot.capture(driver, "login-failure"));
            } catch (IOException io) {
                System.out.println("Ekran görüntüsü alınamadı: " + io.getMessage());
            }
            throw new AssertionError("Login testi başarısız oldu: " + e.getMessage());
        }
    }
}
