# Restful-Booker API Automation

> **Quick Language Switch:**  
> [🇬🇧 English Version](#-english-version) &nbsp;|&nbsp; [🇬🇷 Ελληνική Έκδοση](#-ελληνική-έκδοση)

---

## 🇬🇧 English Version

An automated **REST API test suite** for the public [Restful-Booker](https://restful-booker.herokuapp.com) service, written in **Java** with **REST Assured** and **JUnit 5**. It covers the whole booking lifecycle (health check, authentication, create, read, update, partial update, delete) with both positive and negative scenarios.

### Tech Stack
- **Java** + **Maven** – build and test execution
- **REST Assured 5.5.0** – fluent HTTP requests and response assertions
- **JUnit 5 (5.10.2)** – test framework (`@DisplayName`, `@Nested`, lifecycle hooks)
- **Jackson 2.17.0** – serialization of request models to JSON
- **Datafaker 2.4.0** – random test data (e.g. invalid credentials)
- **Maven Surefire 3.2.5** – test runner and reports

### Project Structure

```text
RestfulBookerApiAutomation
├── pom.xml
└── src/test/java/com/booker
    ├── models/                     # POJOs: Booking, BookingDates, AuthCredentials
    └── tests/
        ├── HealthCheckTest.java
        ├── CreateTokenTest.java
        ├── CreateBookingTest.java
        ├── GetBookingTest.java
        ├── UpdateBookingTest.java
        ├── PartialUpdateBookingTest.java
        └── DeleteBookingTest.java
```

### What Is Covered

| Test class | Endpoint | Scenarios |
|---|---|---|
| `HealthCheckTest` | `GET /ping` | Server is online |
| `CreateTokenTest` | `POST /auth` | Valid credentials return a token; random credentials return `Bad credentials` |
| `CreateBookingTest` | `POST /booking` | Valid booking, alternative date formats, and invalid data (missing/null fields, negative price, check-in after check-out) → `400` |
| `GetBookingTest` | `GET /booking`, `GET /booking/{id}` | List IDs, get by dynamic ID with field-type checks, filter by first name, last name, check-in and check-out |
| `UpdateBookingTest` | `PUT /booking/{id}` | Update with Basic Auth, Cookie token, or both; the result is verified afterwards |
| `PartialUpdateBookingTest` | `PATCH /booking/{id}` | Partial updates (first name only, dates only, one date), empty body, invalid dates |
| `DeleteBookingTest` | `DELETE /booking/{id}` | Delete with Cookie token or Basic Auth, then confirm `404` |

### Key Highlights
- **Dynamic test data** – booking IDs are fetched from the API at runtime instead of being hardcoded.
- **Two authentication styles** – token via `Cookie: token=...` and `Authorization: Basic`.
- **Typed request models** – POJOs serialized by Jackson, so payloads stay readable.
- **Documented API quirks** – tests marked `(Defect-masking)` assert the behaviour the service actually returns (for example `201 Created` on delete, `200` on bad credentials).
- **Shared helpers** – `CreateTokenTest.getAuthToken()` and `GetBookingTest.getFirstBookingId()` are reused by other classes.

> ℹ️ The tests run against a **live public API** shared by everyone. Data can change between runs, and the service resets itself periodically, so an occasional failure is not always a bug in the tests.

### How to Run

#### Prerequisites
- **JDK 25** (the `maven-compiler-plugin` in `pom.xml` is set to `25`)
- **Maven 3.9+**
- Internet access to `https://restful-booker.herokuapp.com`

#### Option 1: Via Terminal / CLI (Recommended)

```bash
# Run all tests
mvn clean test

# Run a single test class
mvn test -Dtest=CreateBookingTest

# Run a single test method
mvn test -Dtest=GetBookingTest#getAllBookingsTest
```

Reports are generated in `target/surefire-reports/`.

#### Option 2: Via IDE (IntelliJ IDEA / Eclipse)
1. Open the project as a **Maven project** (select `pom.xml`) and set the Project SDK to **JDK 25**.
2. Right-click `src/test/java`, a test class, or a single test method.
3. Choose **Run Tests** (IntelliJ) or **Run As → JUnit Test** (Eclipse).

<br>

---
---

<br>

## 🇬🇷 Ελληνική Έκδοση

Μια αυτοματοποιημένη **σουίτα δοκιμών REST API** για την δημόσια υπηρεσία [Restful-Booker](https://restful-booker.herokuapp.com), γραμμένη σε **Java** με **REST Assured** και **JUnit 5**. Καλύπτει ολόκληρο τον κύκλο ζωής μιας κράτησης (health check, authentication, δημιουργία, ανάγνωση, πλήρης ενημέρωση, μερική ενημέρωση, διαγραφή) με θετικά και αρνητικά σενάρια.

### Τεχνολογίες
- **Java** + **Maven** – build και εκτέλεση των tests
- **REST Assured 5.5.0** – HTTP requests και assertions στις απαντήσεις
- **JUnit 5 (5.10.2)** – test framework (`@DisplayName`, `@Nested`, lifecycle hooks)
- **Jackson 2.17.0** – μετατροπή των request models σε JSON
- **Datafaker 2.4.0** – τυχαία δεδομένα δοκιμών (π.χ. λάθος credentials)
- **Maven Surefire 3.2.5** – εκτέλεση tests και αναφορές

### Δομή Project

```text
RestfulBookerApiAutomation
├── pom.xml
└── src/test/java/com/booker
    ├── models/                     # POJOs: Booking, BookingDates, AuthCredentials
    └── tests/
        ├── HealthCheckTest.java
        ├── CreateTokenTest.java
        ├── CreateBookingTest.java
        ├── GetBookingTest.java
        ├── UpdateBookingTest.java
        ├── PartialUpdateBookingTest.java
        └── DeleteBookingTest.java
```

### Τι Καλύπτεται

| Κλάση test | Endpoint | Σενάρια |
|---|---|---|
| `HealthCheckTest` | `GET /ping` | Ο server είναι online |
| `CreateTokenTest` | `POST /auth` | Σωστά credentials επιστρέφουν token· τυχαία credentials επιστρέφουν `Bad credentials` |
| `CreateBookingTest` | `POST /booking` | Έγκυρη κράτηση, εναλλακτικές μορφές ημερομηνίας και άκυρα δεδομένα (λείπουν/null πεδία, αρνητική τιμή, check-in μετά το check-out) → `400` |
| `GetBookingTest` | `GET /booking`, `GET /booking/{id}` | Λίστα IDs, ανάκτηση με δυναμικό ID και έλεγχο τύπων πεδίων, φιλτράρισμα με όνομα, επώνυμο, check-in και check-out |
| `UpdateBookingTest` | `PUT /booking/{id}` | Ενημέρωση με Basic Auth, Cookie token ή και τα δύο· το αποτέλεσμα επαληθεύεται μετά |
| `PartialUpdateBookingTest` | `PATCH /booking/{id}` | Μερικές ενημερώσεις (μόνο όνομα, μόνο ημερομηνίες, μία ημερομηνία), κενό body, άκυρες ημερομηνίες |
| `DeleteBookingTest` | `DELETE /booking/{id}` | Διαγραφή με Cookie token ή Basic Auth και επιβεβαίωση `404` |

### Βασικά Χαρακτηριστικά
- **Δυναμικά δεδομένα** – τα IDs των κρατήσεων ανακτώνται από το API κατά την εκτέλεση, δεν είναι hardcoded.
- **Δύο τρόποι authentication** – token μέσω `Cookie: token=...` και `Authorization: Basic`.
- **Typed request models** – POJOs που σειριοποιεί ο Jackson, ώστε τα payloads να είναι ευανάγνωστα.
- **Τεκμηριωμένες ιδιαιτερότητες του API** – τα tests με την ένδειξη `(Defect-masking)` ελέγχουν τη συμπεριφορά που επιστρέφει πραγματικά η υπηρεσία (π.χ. `201 Created` στη διαγραφή, `200` σε λάθος credentials).
- **Κοινά helpers** – τα `CreateTokenTest.getAuthToken()` και `GetBookingTest.getFirstBookingId()` επαναχρησιμοποιούνται από άλλες κλάσεις.

> ℹ️ Τα tests τρέχουν σε ένα **ζωντανό δημόσιο API** που το μοιράζονται όλοι. Τα δεδομένα μπορεί να αλλάξουν μεταξύ εκτελέσεων και η υπηρεσία κάνει reset περιοδικά, οπότε μια σποραδική αποτυχία δεν σημαίνει πάντα πρόβλημα στα tests.

### Πώς να Τρέξετε

#### Προαπαιτούμενα
- **JDK 25** (το `maven-compiler-plugin` στο `pom.xml` έχει οριστεί σε `25`)
- **Maven 3.9+**
- Πρόσβαση στο internet για το `https://restful-booker.herokuapp.com`

#### Επιλογή 1: Μέσω Terminal / CLI (Προτεινόμενη)

```bash
# Εκτέλεση όλων των tests
mvn clean test

# Εκτέλεση μίας κλάσης
mvn test -Dtest=CreateBookingTest

# Εκτέλεση μίας μεθόδου
mvn test -Dtest=GetBookingTest#getAllBookingsTest
```

Οι αναφορές παράγονται στον φάκελο `target/surefire-reports/`.

#### Επιλογή 2: Μέσω IDE (IntelliJ IDEA / Eclipse)
1. Ανοίξτε το project ως **Maven project** (επιλέξτε το `pom.xml`) και ορίστε Project SDK **JDK 25**.
2. Δεξί κλικ στο `src/test/java`, σε μια κλάση test ή σε μία μέθοδο.
3. Επιλέξτε **Run Tests** (IntelliJ) ή **Run As → JUnit Test** (Eclipse).
