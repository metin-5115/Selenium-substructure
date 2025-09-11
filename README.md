# Selenium-substructure
# 🧪 Selenium Web Test Automation Framework

## 📌 Project Purpose
This repository provides a **modular and reusable framework** for a **Selenium + Cucumber + TestNG** based web test automation project.  
The goal is to enable **local** execution as well as **remote parallel execution via Docker Selenium Grid**, while also offering professional reporting support.

### 🚗 Driver Management
- **`DriverFactory`**
    - Holds `ThreadLocal<WebDriver>`. Assign with `setDriver()`, access with `getDriver()`, close with `quit()`.
    - Used in: `TestRunner`, `Hook`, `BasePage`, `BrowserUtility`.
- **`TargetFactory`**
    - Creates `WebDriver` based on `target` value.
    - `LOCAL` → Local driver via `BrowserFactory`.
    - `REMOTE` → `RemoteWebDriver(gridURL, options)` (Docker Grid).
    - Used in: `TestRunner`.

### 🧪 Test Framework
- **`TestRunner`**
    - Runs Cucumber scenarios with TestNG.
    - `@BeforeMethod` → Driver is created via `DriverFactory.setDriver()`.
    - `@DataProvider(parallel = true)` → Scenarios can run in parallel.
    - `@AfterClass` → Runner is finalized.
- **`Hook`**
    - `@Before` → Scenario information is logged/prepared.
    - `@After` → If scenario fails/skips, screenshot is captured and attached to the report. Afterwards, `DriverFactory.quit()` is called.

### 🌐 Page Layer
- **`BasePage`**
    - Common functions: `waitVisibility`, `click`, `sendKeys`, `scroll`, `navigate()`, `switchTab`, `switchIframe`, etc.
    - Operates using `DriverFactory.getDriver()`.
- **`BrowserUtility`**
    - Retrieves `Capabilities` from the active driver → browser name, version, OS.
- **`LogUtility`**
    - Uses `ThreadContext.ROUTINGKEY` so each thread writes logs into its own file.

### ✅ Assertion
- **`Assertion`**
    - Wraps TestNG `Assert` methods; on failure throws `ScenarioInfoException`.

### 🐳 Docker / Grid
- **`docker-compose.grid.yml`**
    - Starts Selenium Hub + Chrome/Firefox nodes.
    - Example: `--scale chrome=3` → 3 parallel Chrome nodes.
    - Alternative: `SE_NODE_MAX_SESSIONS=3` for multiple sessions in a single container.
- **Usage**
    - Connect to the Grid using `-Dtarget=remote -Dgrid="your docker hub URL/port"`.

---




## ✅ Java Classes

### 1) `Assertion`
- **Purpose:** Wrapper around TestNG `Assert` that throws `ScenarioInfoException` on failure to enrich error messages (browser, URL, original cause).
- **Key methods:** `assertEquals(...)`, `assertNotEquals(...)`, `assertTrue(...)`, `assertTrueWithContains(...)`
- **Used by:** Step definitions / page assertions.
- **Gotchas:** Prefer these over raw `Assert` to keep rich failure context consistent.

---

### 2) `DriverFactory`
- **Purpose:** Manages `WebDriver` instances with `ThreadLocal` for safe parallel execution.
- **Key methods:** `setDriver()`, `getDriver()`, `quit()`, `getInfo()`
- **Used by:** `TestRunner`, `Hook`, `BasePage`, `BrowserUtility`.
- **Gotchas:** Always call `quit()` (e.g., in `@After`) to avoid leaks.

---

### 3) `TargetFactory`
- **Purpose:** Creates driver by **target**: `LOCAL` (via `BrowserFactory`) or `REMOTE` (Selenium Grid via `RemoteWebDriver`).
- **Key methods:** `createInstance(String browser)`, `createRemoteInstance()`
- **Config keys:** `target=local|remote`, `grid=http://.../wd/hub`
- **Gotchas:** Ensure remote capabilities match the requested browser; grid URL must be reachable.

---

### 4) `ThreadFactory`
- **Purpose:** Per-thread metadata via `ThreadLocal` (e.g., `browserInfo`, `scenarioInfo`) for logging/labeling.
- **Key methods:** `setBrowserInfo()`, `getBrowserInfo()`, `setScenarioInfo()`, `getScenarioInfo()`
- **Gotchas:** Consider `remove()` or cleanup at the end of tests if you add more fields.

---

### 5) `Helper`
- **Purpose:** Utility helpers (currently date formatting in Turkish locale). Extends `BasePage`.
- **Key methods:** `getDate(int nextDay, String format)`
- **Gotchas:** Extends `BasePage`, so it can access driver utilities if needed.

---

### 6) `BrowserUtility`
- **Purpose:** Reads `Capabilities` from the current driver.
- **Key methods:** `getBrowserName()`, `getBrowserOs()`, `getBrowserVersion()`
- **Used by:** `LogUtility` and any reporting hooks.

---

### 7) `LogUtility`
- **Purpose:** Builds per-thread log folder and sets Log4j2 `ThreadContext` (`ROUTINGKEY`) for **RoutingAppender**.
- **Output path:** `test-logs/<os>/<environment>/<browser>/<version>/<threadName>/...`
- **Gotchas:** Clear `ThreadContext` in teardown to avoid context leakage with thread reuse.

---

### 8) `BasePage`
- **Purpose:** Common Selenium utilities for Page Objects.
- **Capabilities:** Navigation (`navigate`, `navigateSpecific`, `refresh`), waits (`waitVisibility`, `waitPresence`, `waitClickable`, `waitForLoad`), actions (`click`, `clickJS`, `sendKeys`, `clear`), reading (`getElementText`, `getAllElementText`), existence checks.
- **Dependencies:** `DriverFactory`, `PropertiesReader`, `LogUtility`.
- **Gotchas:** Prefer explicit waits instead of `Thread.sleep` where possible.

---

### 9) `HeadlessNotSupportedException`
- **Purpose:** Thrown when `-Dheadless=true` is requested but the browser (e.g., Safari) can’t run headless.
- **Used by:** `BrowserFactory` (Safari).

---

### 10) `ScenarioInfoException`
- **Purpose:** Packs browser info (`DriverFactory.getInfo()`), current URL, and the original error into one readable message.
- **Used by:** `Assertion` and wait/action wrappers.

---

### 11) `TargetNotValidException`
- **Purpose:** Fails fast when `target` configuration is invalid (only `local` or `grid/remote` supported).

---

### 12) `BrowserFactory` (enum)
- **Purpose:** Centralized per-browser driver creation + options.
- **Values:** `CHROME`, `FIREFOX`, `EDGE`, `SAFARI`
- **Highlights:**
    - Chrome: rich options + headless flags.
    - Firefox: simple headless support.
    - Edge: maximize; headless limited depending on Selenium version.
    - Safari: no headless → throws `HeadlessNotSupportedException`.
- **Gotchas:** Keep options aligned with Selenium/Grid versions.

---

### 13) `Hook`
- **Purpose:** Cucumber hooks.
- **Flow:**
    - `@Before`: logs scenario name and tags (optionally set up driver).
    - `@After(order = 0 / 1)`: currently logs status and quits the driver.
- **Gotchas:** Screenshot capture spot is noted; capture **before** quitting if you add it.

---

### 14) `ResultListener`
- **Purpose:** TestNG lifecycle listener (skeleton).
- **Extend with:** Screenshot on failure, external logging, report flushing.

---

### 15) `RetryAnalyzer`
- **Purpose:** Retries failed tests up to `retryCount`.
- **Config key:** `retryCount`
- **Behavior:** Marks as `SKIP` to trigger retry; marks `FAILURE` after max tries.
- **Gotchas:** `count` is static (shared). For strict isolation in parallel runs, consider per-test counters.

---

### 16) `TestRunner`
- **Purpose:** Integrates Cucumber with TestNG (`TestNGCucumberRunner`).
- **Key points:** `@CucumberOptions` (features, glue, `Extent` adapter), `@DataProvider(parallel = true)`, per-method driver creation via `TargetFactory`, screenshot cleanup in `@AfterSuite`.
- **Inputs:** TestNG parameter `"browser"`; environment for reports path.
- **Gotchas:** Ensure parallel settings align with Grid capacity.

---

### 17) `AnnotationTransformer`
- **Purpose:** Automatically attaches `RetryAnalyzer` to all `@Test` methods via TestNG’s annotation transformation.
- **Benefit:** No need to annotate each test with `retryAnalyzer = ...`.

---

### 18) `PropertiesReader`
- **Purpose:** Loads `configuration.properties` and allows system property overrides (`-Dkey=value`).
- **Key methods:** `getParameter(key)`, `setParameter(key, value)`
- **Config examples:** `browser`, `target`, `environment`, `grid`, `headless`, `retryCount`, `createJiraIssue`, `jira*`
- **Gotchas:** Don’t commit secrets in `configuration.properties`. Provide a template instead.

---

## 🧩 Configuration & Infrastructure

### 19) `log4j2.xml`
- **Purpose:** Console + Routing file appender based on `ThreadContext.ROUTINGKEY`.
- **Effect:** Each test thread writes to its own log file under `test-logs/...`.
- **Safe to commit:** ✅ (no secrets)

---

### 20) `extent-config.xml`
- **Purpose:** ExtentReports UI configuration (theme, titles, encoding).
- **Used by:** Cucumber adapter plugin in `@CucumberOptions`.
- **Safe to commit:** ✅

---

### 21) Docker Compose (Grid & Standalone)
- **Variants:**
    - **Hub + Nodes:** `selenium/hub` + `selenium/node-chrome` + `selenium/node-firefox`
    - **Standalone Chrome:** `selenium/standalone-chrome`
- **Placeholders:** Ports are documented as placeholders so users can map their own (`port1:4442`, `port2:4443`, `port3:4444`, `port4:7900`).
- **Run examples:**
    - Grid: `docker compose -f docker/docker-compose.grid.yml up -d --scale chrome=3`
    - Standalone: `docker compose -f docker/docker-compose.standalone.yml up -d`
- **Safe to commit:** ✅

---

### 22) `pom.xml`
- **Purpose:** Maven coordinates & dependencies (Selenium, TestNG, Cucumber, Extent, WebDriverManager, etc.).
- **Notes:**
    - Ensure versions align with your Selenium Grid images.
    - Keep `maven-surefire-plugin`/`failsafe-plugin` settings consistent with TestNG execution (if added).


## 1️⃣ Requirements
- Java JDK 21+
- Maven 3.9+
- Docker (for parallel/remote tests)

2️⃣ Download maven dependencies

mvn clean install -DskipTests

3️⃣ Docker Selenium Grid (remote test için)

docker compose -f docker/docker-compose.grid.yml up -d --scale chrome=3

docker compose -f docker/docker-compose.grid.yml down

▶️ Run Test

mvn -Dbrowser=chrome -Dtarget=local -Dheadless=false test

mvn -Dbrowser=chrome -Dtarget=local -Dheadless=true test

mvn -Dbrowser=chrome -Dtarget=remote -Dgrid="kendi docker portunuz gelicek" -Dheadless=true -Ddataproviderthreadcount=3 test

