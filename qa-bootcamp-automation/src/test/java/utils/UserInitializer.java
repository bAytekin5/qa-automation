package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.util.UUID;

public class UserInitializer {

    @BeforeSuite
    public void createUserOnce() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            driver.manage().window().maximize();
            driver.get("https://magento.softwaretestingboard.com/");
            driver.findElement(By.linkText("Create an Account")).click();

            String email = "testuser_" + UUID.randomUUID().toString().substring(0, 6) + "@mail.com";
            SharedData.registeredEmail = email;
            UserStorage.saveEmail(email);

            driver.findElement(By.id("firstname")).sendKeys("Test");
            driver.findElement(By.id("lastname")).sendKeys("User");
            driver.findElement(By.id("email_address")).sendKeys(email);
            driver.findElement(By.id("password")).sendKeys("Test1234");
            driver.findElement(By.id("password-confirmation")).sendKeys("Test1234");

            driver.findElement(By.cssSelector("button[title='Create an Account']")).click();

            Screenshot.capture(driver, "user-created-success");
            System.out.println("Test için kullanıcı oluşturuldu: " + email);

        } catch (Exception e) {
            System.out.println("Kullanıcı oluşturulurken hata oluştu: " + e.getMessage());
            try {
                Screenshot.capture(driver, "user-creation-failure");
            } catch (IOException ioException) {
                System.out.println("Ekran görüntüsü alınamadı: " + ioException.getMessage());
            }
        } finally {
            driver.quit();
        }
    }
}
