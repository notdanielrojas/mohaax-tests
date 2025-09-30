# 🧪 Mohaax Test Automation

Este proyecto contiene **pruebas automatizadas** para el sitio web de la comunidad Chilena de *Medal of Honor: Allied Assault*.  
Integra pruebas **API** y **UI** en un solo flujo de trabajo, utilizando herramientas modernas para asegurar la calidad de la aplicación.

## ⚙️ Tecnologías Usadas

- **Java 17+**
- **Maven** – gestión de dependencias y ejecución de tests
- **Selenium WebDriver** – automatización de navegadores (Chrome, Firefox, Safari, Edge)
- **TestNG** – framework de pruebas (parametrización, anotaciones, reportes)
- **WebDriverManager** – gestión automática de drivers
- **Bruno CLI** – ejecución de colecciones de pruebas API (`.bru`)
- **Dotenv** – variables de entorno seguras
- **ReportNG** – reportes enriquecidos para TestNG
- **Surefire Plugin** – integración de pruebas en Maven

## 📦 Instalación

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/notdanielrojas/mohaax-tests.git
   cd mohaax-tests
   ```

2. **Instalar dependencias con Maven**:
   ```bash
   mvn clean install
   ```

3. **Instalar Bruno CLI** (si aún no lo tienes):
   ```bash
   npm install -g @usebruno/cli
   ```

## 📂 Estructura del Proyecto

```
mohaax-automation/
├── pom.xml                      # Configuración de Maven
├── src/
│   ├── main/java                # Código base (si aplica)
│   └── test/java                # Pruebas automatizadas
│       ├── BaseTest.java        # Configuración común (drivers, waits, etc.)
│       ├── LoginPage.java       # Page Object Model (POM) para Login
│       ├── LoginTest.java       # Casos de prueba de Login (TestNG)
│       └── BrunoApiTest.java    # Ejecutor de colecciones Bruno
│
├── src/test/resources/
│   └── testng.xml               # Suite de pruebas TestNG
│
├── environments/                # Archivos de entorno Bruno (ignorado en Git)
│   └── mohaax.bru
├── .env                         # Variables de entorno (ignorado en Git)
└── .gitignore
```

## 🚀 Ejecución de Pruebas

### 🔹 1. Pruebas de UI (Selenium + TestNG)

Ejecuta los tests desde Maven:

```bash
mvn test
```

Por defecto se abre Chrome, pero en `BaseTest.java` puedes configurar que corra en **Firefox, Safari, Edge** o en modo **headless** (sin abrir navegador).

Ejemplo en modo *headless*:

```java
ChromeOptions options = new ChromeOptions();
options.addArguments("--headless", "--disable-gpu");
driver = new ChromeDriver(options);
```

### 🔹 2. Pruebas de API (Bruno)

Puedes correr colecciones de Bruno manualmente:

```bash
bru run . -r --env-file environments/mohaax.bru
```

O integradas con Maven (se ejecutan en la fase `test`):

```bash
mvn test
```

## 📝 Reportes

- Los reportes de **TestNG/Selenium** se generan en:
  ```
  target/surefire-reports/
  ```

- Los reportes de **Bruno API** se guardan en:
  ```
  target/bruno-results.xml
  ```

(Opcional: se pueden combinar en un solo reporte HTML unificado con el `surefire-report-plugin`).

## 🔒 Variables de Entorno

Las credenciales y URLs se manejan mediante `.env` y `environments/*.bru`.

Ejemplo de `.env`:
```env
BASE_URL=https://mohaax.example.com
USERNAME=testuser
PASSWORD=secret123
REGISTER_PATH=/register
RECOVER_PASSWORD_PATH=/recover
```

👉 El archivo real de `environments/mohaax.bru` está **ignorado en Git** por seguridad.  
Se incluye un ejemplo `mohaax.bru.example` para referencia.

## ✅ Decisiones de Diseño

- Se integran **API y UI tests en el mismo pipeline** para mayor cobertura.
    - *Ventaja*: un solo comando (`mvn test`) ejecuta ambas capas de pruebas.
    - *Desventaja*: mayor tiempo de ejecución.

- Se usa **Page Object Model (POM)** para separar lógica de negocio de la automatización UI.

- Se ejecuta Chrome en **modo headless** en CI/CD para optimizar velocidad.

- Se mantiene **seguridad** al ignorar credenciales sensibles (`.env`, `.bru`) en el repositorio.

## 📊 Ejemplo de Suite TestNG (`testng.xml`)

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd" >
<suite name="Mohaax Automation Suite">
    <test name="UI Tests">
        <classes>
            <class name="LoginTest"/>
            <class name="SignUpTest"/>
        </classes>
    </test>
    <test name="API Tests">
        <classes>
            <class name="BrunoApiTest"/>
        </classes>
    </test>
</suite>
```

## 👨‍💻 Autor

Proyecto desarrollado como parte de un **framework de pruebas automatizadas** para la comunidad Chilena *Medal of Honor: Allied Assault*.
