package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Locale;

public class BaseTest {

    protected WebDriver driver;
    protected static ExtentReports extent;
    protected static ExtentTest test;

    @BeforeMethod
    public void setup(Method method) {
        Locale.setDefault(Locale.ENGLISH);
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://magento.softwaretestingboard.com/");

        extent = ExtentReportManager.getInstance();
        test = extent.createTest(method.getDeclaringClass().getSimpleName() + " - " + method.getName());
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (result.getStatus() == ITestResult.FAILURE) {
                test.fail("Test başarısız: " + result.getThrowable());
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.pass("Test başarıyla tamamlandı.");
            }
        } catch (Exception e) {
            System.out.println("Raporlama sırasında hata oluştu: " + e.getMessage());
        }

        if (driver != null) {
            driver.quit();
        }

        extent.flush();
    }
}
