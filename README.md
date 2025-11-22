# 🇬🇧 Student Gradebook System 

A console application for managing student academic performance, built with **Kotlin**. The project strictly follows **Object-Oriented Principles** and multilayered Architecture**.

### Architecture
The solution is divided into four decoupled modules:
1.  **Core:** Common entities (`Student`, `Group`, `Grade`) and custom exceptions.
2.  **DAL (Data Access Layer):** Repositories handling **JSON** persistence and CRUD operations.
3.  **BLL (Business Logic Layer):** Services containing validation, calculation logic, and cascading updates.
4.  **App (Presentation Layer):** Console UI with generic menu handlers.

### Key Features
*   **Management:** Full control over Students, Groups, and Subjects.
*   **Academic Process:** Add/Update grades (0-100), auto-calculate **Average Scores**.
*   **Search & Reports:** Filter by name, group, or success rating (e.g., find failing students).
*   **Tech Stack:** Kotlin, Gradle, `kotlinx.serialization`, JUnit 5, Mockk.

### How to Run
1.  Open the project in **IntelliJ IDEA**.
2.  Run `Main.kt` in the `app` module.
3.  *Demo data loads automatically on the first launch.*

---

# 🇺🇦 Електронний журнал успішності 

Консольний додаток для обліку успішності студентів, розроблений на **Kotlin**. Проект реалізовано з дотриманням **принципів ООП** та **багатошарової архітектури**.

### Архітектура
Рішення розділене на чотири незалежні модулі:
1.  **Core:** Спільні сутності (`Student`, `Group` тощо) та власні винятки.
2.  **DAL (Шар доступу до даних):** Репозиторії, що відповідають за збереження даних у **JSON**.
3.  **BLL (Шар бізнес-логіки):** Сервіси з валідацією даних, розрахунками та каскадними операціями.
4.  **App (Шар представлення):** Консольний інтерфейс користувача.

### Основний функціонал
*   **Управління:** Повний контроль над студентами, групами та предметами.
*   **Навчальний процес:** Виставлення оцінок, автоматичний розрахунок **середнього балу**.
*   **Пошук та звіти:** Фільтрація за іменем, групою або рейтингом (наприклад, пошук заборгованостей).
*   **Технології:** Kotlin, Gradle, `kotlinx.serialization`, JUnit 5, Mockk.

### Як запустити
1.  Відкрийте проект в **IntelliJ IDEA**.
2.  Запустіть файл `Main.kt` у модулі `app`.
3.  *Тестові дані завантажаться автоматично при першому запуску.*
