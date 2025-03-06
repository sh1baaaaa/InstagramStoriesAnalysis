# Instagram Stories Analysis

## Описание проекта
Данный проект представляет сервис, позволяющий получать информацию об Instagram сторисах по URL или ID. 

В частности локации, отметки, хэштеги, дата истечения и т.п.

Также отмечу, что в проекте используется Redis для кэширования ответов на час в целях снижения нагрузки.

Документация API представлена в Swagger. Также присутствует счётчик общих запросов и ошибок в Prometheus. 

## Технологический стэк:
- Java 17
- Spring Framework (WebFlux / Data Redis / Actuator)
- Redis
- Prometheus
- Gradle
- Docker

## Запуск
### Для локального запуска приложения потребуется следующее:
- Java 17
- Docker
- HikerAPI Token

1) Склонируйте проект
2) Зарегистрируйтесь на [HikerAPI](https://hikerapi.com/)
3) Во вкладке Tokens скопируйте Access Key (Токен для запросов)
2) Измените в application.yml поле hiker.token на ваш токен
2) Запустите Docker
3) Откройте директорию проекта в консоли
4) Введите следующие команды:
```rb
./gradlew clean
```
```rb
./gradlew build
```
```rb
docker-compose up --build
```
После запуска контейнера вам будет доступно два эндпоинита:
- http://localhost:8081/api/stories/check - POST-эндпоинт. Документацию смотреть в Swagger
- http://localhost:8081/swagger-ui/index.html#/ - Swagger
- http://localhost:9090/ - Prometheus

Для отслеживания кол-ва запросов и ошибок в Prometheus, используйте два запроса:
1) http_requests_total
2) http_requests_errors_total


## Логика сервиса
ID при запросе вводится опционально.

Для получения информации о сторисе по URL вводите ссылку в следующем виде:
***https://instagram.com/stories/username/num***

### Механизм:
1) Если id не равно null, то осуществляется запрос к HikerAPI по пути: https://api.hikerapi.com/v1/story/by/id. Иначе https://api.hikerapi.com/v1/story/by/url
2) Ответ с HikerAPI конвертируется в DTO-обьект и возвращается пользователю
3) Ответ кэшируется на час в Redis и при повторном запросе данные вытягиваются из Б/Д если данная сторис хранится.

## Статус проекта и его будущее
Проект разрабатывался в коммерческих целях
