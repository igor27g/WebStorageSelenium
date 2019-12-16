import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WebStorageExamples {

   ChromeDriver driver;
   WebDriverWait wait;

   String airly = "https://airly.eu/map/pl/#50.06237,19.93898";

    @BeforeEach
    public void driverSetup(){

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
        driver.manage().window().maximize();


        wait = new WebDriverWait(driver,10);
    }

    @AfterEach
    public void driverClose()
    {
        driver.close();
        driver.quit();
    }

    @Test
    public void localStorageExample() {
        driver.navigate().to(airly);
        LocalStorage local = driver.getLocalStorage();
        String value = local.getItem("persist:map");
        int size = local.size();
        Set<String> keys = local.keySet();
        String removedValue = local.removeItem("persist:map");
        local.setItem("spell", "aohomora!");
        local.clear();
    }

    @Test
    public void localStorageMyExample() {
        driver.navigate().to(airly);
        LocalStorage local = driver.getLocalStorage();
        String value = local.getItem("persist:user");
        System.out.println(value);
        int size = local.size();
        Set<String> keys = local.keySet();
        local.setItem("test1", "tet1");
        local.clear();
        int sizeAfterClear = local.size();

    }

    @Test
    public void sessionStorageExample() {
        driver.navigate().to("https://www.youtube.com/watch?v=RrOM1QI6v84");
        final SessionStorage session = driver.getSessionStorage();
        String value = session.getItem("yt-remote-session-app");
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(a->session.size()==5);
        int size = session.size();
        Set<String> keys = session.keySet();
        String removedValue = session.removeItem("yt-remote-session-app");
        session.setItem("spell", "aohomora!");
        session.clear();
    }


    @Test
    public void localStorageMyExampleJS() {
        driver.navigate().to(airly);
        String key = "persist:map";

        String value = (String) ((JavascriptExecutor)driver).executeScript("return localStorage.getItem(arguments[0]);",key);
        ((JavascriptExecutor)driver).executeScript("localStorage.setItem(arguments[0],arguments[1]);", "spell", "Value1");
        ((JavascriptExecutor)driver).executeScript("localStorage.removeItem(arguments[0]);",key);
        String indexValue = (String) ((JavascriptExecutor)driver).executeScript("return localStorage.key(arguments[0]);",2);
        long size = (long) ((JavascriptExecutor)driver).executeScript("return localStorage.length;");
        ((JavascriptExecutor)driver).executeScript("localStorage.clear();");
    }
    @Test
    public void sessionStorageMyExample() {
        driver.navigate().to("https://www.youtube.com/watch?v=RrOM1QI6v84");
        final SessionStorage session = driver.getSessionStorage();
        String value = session.getItem("yt-remote-session-app");
        System.out.println(value);
        session.setItem("Key1", "Value");
        String value2 = session.getItem("Key1");
        System.out.println(value2);
        WebDriverWait wait = new WebDriverWait(driver, 10);  //trik, aby zmusić drivera do czekania
        wait.until(a->session.size()==6);
        int sizeAfterAdd2Items = session.size();
    }


    //1.Test sprawdzający czy na starcie mamy jeden klucz w Local Storage.
    @Test
    public void CheckKeysInLocalStorage() {
        driver.navigate().to("https://fakestore.testelka.pl/product/fuerteventura-sotavento/");
        LocalStorage local = driver.getLocalStorage();
        int size = local.size();
        Assertions.assertEquals(1, size, "Wrong numbers of keys");
    }

    //2.Test sprawdzający czy na starcie mamy dwa klucze w Session Storage.
    @Test
    public void CheckKeysInSessionStorage() {
        driver.navigate().to("https://fakestore.testelka.pl/product/fuerteventura-sotavento/");
        SessionStorage session = driver.getSessionStorage();
        int size = session.size();
        Assertions.assertEquals(2, size , "Wrong number of keys");
    }

    //3.Test sprawdzający, czy po dodaniu produktu do koszyka w Session Storage pojawia się klucz wc_cart_created.
    @Test
    public void AddProductToCart() {
        driver.navigate().to("https://fakestore.testelka.pl/product/fuerteventura-sotavento/");
        driver.findElement(By.cssSelector("a[class='woocommerce-store-notice__dismiss-link']")).click();
        driver.findElement(By.cssSelector("button[name='add-to-cart']")).click();
        SessionStorage session = driver.getSessionStorage();
        Assertions.assertTrue(session.keySet().contains("wc_cart_created"), "Session storage contains wc_cart_created");
    }

    //4.Test sprawdzający czy po usunięciu klucza z Session Storage zawierającego w nazwie wc_fragments ten klucz rzeczywiście się usuwa.
    @Test
    public void wcFragmentsKeyRemovedTest() {
        driver.navigate().to("https://fakestore.testelka.pl/product/fuerteventura-sotavento/");
        driver.findElement(By.cssSelector("a[class='woocommerce-store-notice__dismiss-link']")).click();
        driver.findElement(By.cssSelector("button[name='add-to-cart']")).click();
        SessionStorage session = driver.getSessionStorage();
        Set<String> keys = session.keySet();
        session.removeItem("wc_cart_created");
        Set<String> keys2 = session.keySet();
//        String cartKey = (String) ((JavascriptExecutor)driver).executeScript("return localStorage.key(arguments[0]);",2);
//        Assertions.assertEquals("wc_cart_created", cartKey, "Item didn't add");
    }



//    @Test
//    public void localStorageMyExample() {
//        driver.navigate().to(airly);
//        LocalStorage local = driver.getLocalStorage();
//        String value = local.getItem("persist:user");
//        System.out.println(value);
//        int size = local.size();
//        Set<String> keys = local.keySet();
//        local.setItem("test1", "tet1");
//        local.clear();
//        int sizeAfterClear = local.size();
//
//    }
}
