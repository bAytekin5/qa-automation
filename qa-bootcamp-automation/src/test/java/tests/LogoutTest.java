package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.BaseTest;
import utils.UserStorage;
import utils.Screenshot;

import java.io.IOException;
import java.time.Duration;

public class LogoutTest extends BaseTest {

    private final Duration TIMEOUT = Duration.ofSeconds(15);

    @Test
    public void userCanLogoutSuccessfully() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        try {
            String email = UserStorage.readEmail();
            if (email == null) throw new IllegalStateException("Logout için e-posta bulunamadı.");

            performLogin(email, "Test1234");

            openUserMenu(wait);
            clickLogoutLink(wait);
            verifyLogout(wait);

            test.pass("Kullanıcı başarıyla çıkış yaptı.");
            test.addScreenCaptureFromPath(Screenshot.capture(driver, "logout-success"));

        } catch (Exception e) {
            test.fail("Logout testi başarısız: " + e.getMessage());
            try {
                test.addScreenCaptureFromPath(Screenshot.capture(driver, "logout-failure"));
            } catch (IOException io) {
                System.out.println("Ekran görüntüsü alınamadı: " + io.getMessage());
            }
            throw new AssertionError("Logout testi başarısız oldu: " + e.getMessage());
        }
    }

    private void performLogin(String email, String password) {
        LoginPage login = new LoginPage(driver);
        login.clickSignInLink();
        login.enterEmail(email);
        login.enterPassword(password);
        login.clickLoginButton();
    }

    private void openUserMenu(WebDriverWait wait) {
        By menuSelector = By.cssSelector(".header.links .customer-welcome");
        wait.until(ExpectedConditions.visibilityOfElementLocated(menuSelector)).click();
    }

    private void clickLogoutLink(WebDriverWait wait) {
        By logoutSelector = By.cssSelector(".authorization-link a");
        WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(logoutSelector));
        logout.click();
    }

    private void verifyLogout(WebDriverWait wait) {
        WebElement signInLink = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.linkText("Sign In"))
        );
        Assert.assertTrue(signInLink.isDisplayed(), "Logout sonrası 'Sign In' linki görünmedi!");
    }
}
