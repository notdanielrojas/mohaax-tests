import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Clase base para todas las pruebas de Selenium.
 * Se encarga de la configuración inicial del WebDriver (ChromeDriver) y la gestión de variables de entorno (dotenv) para asegurar un entorno de prueba consistente.
 **/
public class BaseTest {

    /** WebDriver para la automatización de la interacción con el navegador. */
    protected WebDriver driver;
    /** Objeto para cargar y acceder a las variables de entorno del archivo .env. */
    protected Dotenv dotenv;

    /**
     * Configura el entorno de prueba antes de cada método de prueba.
     * 1. Carga las variables de entorno.
     * 2. Configura y crea una instancia de ChromeDriver.
     * 3. Maximiza la ventana del navegador.
     * 4. Establece una espera implícita global de 10 segundos.
     */
    @BeforeMethod
    public void setup() {
        // Lee la propiedad 'browser' del sistema (Maven/Terminal) o usa 'chrome' por defecto
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        dotenv = Dotenv.load();

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            case "safari":
                driver = new SafariDriver();
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    /**
     * Cierra el navegador y finaliza la sesión de WebDriver después de cada método de prueba.
     * Esto libera recursos y asegura que el navegador se cierre correctamente.
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}