import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model (POM) para la página de inicio de sesión.
 * Contiene los localizadores (By) y los métodos de interacción para la funcionalidad de login.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Localizadores de Elementos ---

    /** Localizador para el botón de 'Iniciar Sesión' en la página principal. */
    private final By loginButtonMainPage = By.xpath("//button[text()=' Iniciar Sesión']");
    /** Localizador para el campo de entrada de email o nombre de usuario. */
    private final By emailInput = By.xpath("//input[@placeholder='Ingresa tu email o Username']");
    /** Localizador para el campo de entrada de contraseña. */
    private final By passwordInput = By.xpath("//input[@placeholder='Ingresa tu contraseña']");
    /** Localizador para el botón de 'Iniciar Sesión' dentro del formulario (type='submit'). */
    private final By loginButton = By.xpath("//button[@type='submit']");
    /** Localizador para el mensaje de error de credenciales incorrectas. */
    private final By errorMessage = By.xpath("//div[contains(text(),'email o Password Incorrecto')]");
    /** Localizador para el mensaje de éxito después de un inicio de sesión exitoso. */
    private final By successMessage = By.xpath("//div[text()='Sesión iniciada correctamente']");
    /** Localizador para el mensaje de cuenta no verificada. */
    private final By unverifiedUserMessage = By.xpath("//div[text()='Debes validar tu cuenta para iniciar sesión']");
    /** Localizador para el enlace de 'Regístrate aquí'. */
    private final By signUpLink = By.xpath("//a[contains(text(),'Regístrate aquí')]");
    /** Localizador para el enlace de '¿Olvidaste Tu Contraseña?'. */
    private final By forgotPasswordLink = By.xpath("//a[contains(text(), '¿Olvidaste Tu Contraseña?')]");
    /** Localizador para el ícono SVG de alternar visibilidad de contraseña (ojo). */
    private final By passwordToggleIcon = By.xpath("//*[local-name()='svg' and @class='text-4xl fill-[#FFFFFF] cursor-pointer']");

    /**
     * Constructor para la clase LoginPage.
     * @param driver La instancia de WebDriver a usar para la interacción con la página.
     */
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navega a la página de inicio de sesión.
     * 1. Va a la URL base.
     * 2. Hace clic en el botón principal para abrir el formulario de login.
     * 3. Espera a que el campo de email sea visible.
     * @param url La URL base de la aplicación.
     */
    public void navigateToLoginPage(String url) {
        driver.get(url);
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginButtonMainPage));
        loginButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
    }

    /**
     * Ingresa el nombre de usuario/email y la contraseña en los campos del formulario.
     * Los campos se limpian antes de enviar las nuevas credenciales.
     * @param username El email o nombre de usuario a ingresar.
     * @param password La contraseña a ingresar.
     */
    public void enterLoginCredentials(String username, String password) {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        emailField.clear();
        emailField.sendKeys(username);

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passwordField.clear();
        passwordField.sendKeys(password);
    }

    /**
     * Hace clic en el botón de 'Iniciar Sesión' del formulario de login.
     */
    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    /**
     * Obtiene el texto del mensaje de error de credenciales inválidas.
     * @return El texto del mensaje de error.
     */
    public String getErrorMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    /**
     * Obtiene el texto del mensaje de inicio de sesión exitoso.
     * @return El texto del mensaje de éxito.
     */
    public String getSuccessMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
    }

    /**
     * Obtiene el texto del mensaje de error para cuentas no verificadas.
     * @return El texto del mensaje.
     */
    public String getUnverifiedUserMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(unverifiedUserMessage)).getText();
    }

    /**
     * Hace clic en el enlace de 'Regístrate aquí' para ir a la página de registro.
     */
    public void clickSignUpLink() {
        wait.until(ExpectedConditions.elementToBeClickable(signUpLink)).click();
    }

    /**
     * Hace clic en el enlace de '¿Olvidaste Tu Contraseña?' para ir a la página de recuperación.
     */
    public void clickForgotPasswordLink() {
        wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink)).click();
    }

    /**
     * Alterna la visibilidad de la contraseña haciendo clic en el ícono (ojo).
     */
    public void togglePasswordVisibility() {
        wait.until(ExpectedConditions.elementToBeClickable(passwordToggleIcon)).click();
    }

    /**
     * Obtiene el valor del atributo 'type' del campo de contraseña (e.g., "password" o "text").
     * @return El tipo de input del campo de contraseña.
     */
    public String getPasswordInputType() {
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        return passwordField.getAttribute("type");
    }

    /**
     * Obtiene el valor actual del campo de contraseña (el texto visible o no en el campo).
     * @return El texto ingresado en el campo de contraseña.
     */
    public String getPasswordInputValue() {
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        return passwordField.getAttribute("value");
    }

    /**
     * Simula la acción de pegar la contraseña en el campo usando el atajo de teclado Command + V (Mac OS).
     */
    public void pastePasswordUsingShortcut() {
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // Simula Command + V para pegar wn Windows.
            passwordField.sendKeys(Keys.chord(Keys.CONTROL, "v"));
        } else {
            // Simula Command + V para pegar wn Mac.
            passwordField.sendKeys(Keys.chord(Keys.COMMAND, "v"));
        }

    }
}