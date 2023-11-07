# <u>Проект интернет доски объявлений</u>
## Мы команда разработчиков FourthTeam.
<hr>

Согласно техническому заданию от компании SkyPro, необходимо собрать backend-часть сайта на Java.

**Backend-часть проекта включает в себя следующую функциональность:**

- Авторизация и аутентификация пользователей.
- Распределение ролей между пользователями: пользователь и администратор*.
- CRUD для объявлений на сайте: администратор может удалять или редактировать все объявления, а пользователи — только свои.
- Пользователи могут оставлять отзывы под каждым объявлением.
- В шапке сайта можно выполнять поиск объявлений по названию.
- Показ и сохранение изображений объявлений.

<hr>

### Frontend - функциональность описана в файле [openapi.yaml](openapi.yaml).
### Команда для запуска фронтенда:
```
docker run --rm -p 3000:3000 ghcr.io/bizinmitya/front-react-avito:v1.19
```

<hr>

### Диаграмма взаимосвязи сущностей:

![schema_bd](C:\Users\Acer\Desktop\schema_bd.png)

<hr>

### У нас есть 6 недель на разработку всего проекта, после чего мы передадим готовое решение компании SkyPro для использования.
### Этапы проекта (1 этап = 1 неделя)

**Этап I:**
>Настройка проекта Spring.

**Этап II:**
>Настройка авторизации и аутентификации.

**Этап III:**
>Описание моделей объявлений и отзывов.

**Этап IV:**
>Определение прав доступа к контроллерам.

**Этап V.**
>Сохранение и получение изображений.

**Этап VI:**
>Окончательное ревью проекта и создание презентации.

<hr>

<hr>

### Команда разработчиков FourthTeam
### В разработке этого проекта принимают участие четыре junior-разработчика::

**1. [Пругло Владимир](https://github.com/Pruglo92)**

**2. [Королева Елена](https://github.com/koroliana)**

**3. [Иванов Виктор](https://github.com/Microd18)**

**4. [Волков Дмитрий](https://github.com/DmitriiVolkovIzh)**

<hr>

### Инструменты разработки:
```
* IntelliJ IDEA
* Java 17
* org.springframework.boot
* org.springframework.data
* org.springframework.security
* org.springframework.kafka
* org.springdoc
* org.liquibase
* org.junit.jupiter
* org.projectlombok
* org.testcontainers
* org.apache.commons
* io.swagger.core.v3
* Сборка с помощью org.apache.maven
```
<hr>

### FourthTeam
![4team.png](C:\Users\Acer\Desktop\4team.png)


