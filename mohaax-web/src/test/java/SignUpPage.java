import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object Model (POM) para la página de registro de usuarios (Sign Up).
 * Contiene los localizadores (By) y los métodos de interacción para la funcionalidad de registro.
 */
public class SignUpPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- Localizadores de Elementos ---

    /** Localizador para el botón de 'Iniciar Sesión' en la página principal, necesario para iniciar el flujo. */
    private final By loginButtonMainPage = By.xpath("//button[text()=' Iniciar Sesión']");
    /** Localizador para el enlace de 'Regístrate aquí' dentro del formulario de login. */
    private final By signUpLink = By.xpath("//a[contains(text(),'Regístrate aquí')]");
    /** Localizador para el campo de entrada del nombre de usuario (por ID). */
    private final By usernameInput = By.id("username");
    /** Localizador para el campo de entrada del email (por ID). */
    private final By emailInput = By.id("email");
    /** Localizador para el campo de entrada de 'volute' (por ID). */
    private final By voluteInput = By.id("volute");
    /** Localizador para el campo de entrada de la contraseña (por ID). */
    private final By passwordInput = By.id("password");
    /** Localizador para el campo de entrada de la repetición de contraseña (por ID). */
    private final By repeatPasswordInput = By.id("repeatPassword");
    /** Localizador para el botón de 'Registrarse' del formulario (type='submit'). */
    private final By registerButton = By.xpath("//button[@type='submit']");
    /** Localizador para el mensaje de éxito después de un registro exitoso. */
    private final By successMessage = By.xpath("//div[text()='Usuario Creado Con éxito']");
    /** Localizador para el mensaje de error cuando el campo de nombre de usuario es obligatorio. */
    private final By usernameErrorMessage = By.xpath("//span[contains(text(),'Nombre de usuario es obligatorio')]");
    /** Localizador para el mensaje de error cuando el formato del correo no es válido. */
    private final By emailErrorMessage = By.xpath("//span[contains(text(),'Correo no válido')]");
    /** Localizador para el mensaje de error cuando las contraseñas no coinciden. */
    private final By passwordMismatchError = By.xpath("//span[contains(text(),'Las contraseñas no coinciden')]");
    /** Localizador para el mensaje de error cuando el nombre de usuario ya existe. */
    private final By usernameExistsMessage = By.xpath("//div[contains(text(),'Ya hay un jugador con ese nombre')]");
    /** Localizador para el mensaje de error cuando el email ya está registrado. */
    private final By emailExistsMessage = By.xpath("//div[contains(text(),'Ya hay un jugador registrado con ese email')]");
    /** Localizador para el mensaje de error cuando todos los campos son requeridos (mensaje general). */
    private final By allFieldsRequiredMessage = By.xpath("//div[contains(text(),'Todos los campos son requeridos')]");
    /** Localizador para el mensaje de error específico para el campo 'volute' vacío. */
    private final By voluteEmptyFieldMessage = By.xpath("//div[contains(text(),'Todos los campos son requeridos')]");
    /** Localizador para el mensaje de error interno del servidor (ej. longitud de username muy larga). */
    private final By serverInternalErrorMessage = By.xpath("//div[contains(text(),'Hubo un error interno en el servidor')]");

    /**
     * Constructor para la clase SignUpPage.
     * @param driver La instancia de WebDriver a usar para la interacción con la página.
     */
    public SignUpPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Navega a la página de registro.
     * 1. Va a la URL base.
     * 2. Hace clic en el botón principal para abrir el formulario de login.
     * 3. Hace clic en el enlace de 'Regístrate aquí'.
     * 4. Espera a que el campo de nombre de usuario sea visible.
     * @param url La URL base de la aplicación.
     */
    public void navigateToSignUpPage(String url) {
        driver.get(url);
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginButtonMainPage));
        loginButton.click();
        WebElement signUpLinkElement = wait.until(ExpectedConditions.elementToBeClickable(signUpLink));
        signUpLinkElement.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
    }

    /**
     * Ingresa las credenciales y datos de registro en los campos del formulario.
     * Se asegura de limpiar cada campo antes de enviar los valores.
     * @param username El nombre de usuario.
     * @param email El correo electrónico.
     * @param volute El valor para el campo 'volute'.
     * @param password La contraseña.
     * @param repeatPassword La repetición de la contraseña.
     */
    public void enterRegisterCredentials(String username, String email, String volute, String password, String repeatPassword) {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        usernameField.clear(); // Limpia el campo de nombre de usuario
        usernameField.sendKeys(username);

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        emailField.clear(); // Limpia el campo de email
        emailField.sendKeys(email);

        WebElement voluteField = wait.until(ExpectedConditions.visibilityOfElementLocated(voluteInput));
        voluteField.clear(); // Limpia el campo de volute
        voluteField.sendKeys(volute);

        WebElement passwordInputField = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passwordInputField.clear(); // Limpia el campo de contraseña
        passwordInputField.sendKeys(password);

        WebElement repeatPasswordField = wait.until(ExpectedConditions.visibilityOfElementLocated(repeatPasswordInput));
        repeatPasswordField.clear(); // Limpia el campo de repetir contraseña
        repeatPasswordField.sendKeys(repeatPassword);
    }

    /**
     * Hace clic en el botón de 'Registrarse' del formulario.
     */
    public void clickSignUpButton() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();
    }

    /**
     * Obtiene el texto del mensaje de error de nombre de usuario obligatorio.
     * @return El texto del mensaje de error.
     */
    public String getUsernameErrorMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameErrorMessage)).getText();
    }

    /**
     * Obtiene el texto del mensaje de error de formato de email no válido.
     * @return El texto del mensaje de error.
     */
    public String getEmailErrorMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emailErrorMessage)).getText();
    }

    /**
     * Obtiene el texto del mensaje de registro exitoso.
     * @return El texto del mensaje de éxito.
     */
    public String getSuccessMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage)).getText();
    }

    /**
     * Obtiene el texto del mensaje de error de contraseñas no coincidentes.
     * @return El texto del mensaje de error.
     */
    public String getPasswordMismatchErrorText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(passwordMismatchError)).getText();
    }

    /**
     * Obtiene el texto del mensaje de error de nombre de usuario ya existente.
     * @return El texto del mensaje de error.
     */
    public String getUsernameExistsMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameExistsMessage)).getText();
    }

    /**
     * Obtiene el texto del mensaje de error de email ya registrado.
     * @return El texto del mensaje de error.
     */
    public String getEmailExistsMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emailExistsMessage)).getText();
    }

    /**
     * Obtiene el texto del mensaje de error de campos requeridos (general).
     * @return El texto del mensaje de error.
     */
    public String getAllFieldsRequiredMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(allFieldsRequiredMessage)).getText();
    }

    /**
     * Obtiene el texto del mensaje de error específico para el campo 'volute' vacío.
     * @return El texto del mensaje de error.
     */
    public String getVoluteEmptyFieldMessageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(voluteEmptyFieldMessage)).getText();
    }

    /**
     * Obtiene el texto del mensaje de error interno del servidor.
     * @return El texto del mensaje de error.
     */
    public String getServerInternalErrorMessageText(){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(serverInternalErrorMessage)).getText();
    }
}