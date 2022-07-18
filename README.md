# job4j_chat

Проект REST чат

Приложение чат, основанный на REST архитектуре.
Используемые технологии Spring boot, Spring Security, Spring Data, Hibernate Validator.
Авторизация JWT (JSON Web Token)

Предоставляет следующий REST API:
## 1. Зарегистрировать пользователя. ##
![Регистрация пользователя](https://github.com/Koregin/job4j_chat/blob/master/images/user_register.jpg)

## 2. Получить токен JWT для дальнейшей авторизации ##
Для выполнения запросов необходимо использовать полученный токен.  
![Получение токена](https://github.com/Koregin/job4j_chat/blob/master/images/user_get_token.jpg)

## 3. Создать комнату ##
![Создать комнату](https://github.com/Koregin/job4j_chat/blob/master/images/create_room.jpg)

## 4. Получить комнату со всеми сообщениями ##
![Получить комнату](https://github.com/Koregin/job4j_chat/blob/master/images/get_room.jpg)

## 5. Создать сообщение ##
![Создать сообщение](https://github.com/Koregin/job4j_chat/blob/master/images/create_message.jpg)

## 6. Обновить сообщение ##
![Обновить сообщение](https://github.com/Koregin/job4j_chat/blob/master/images/update_message.jpg)

## 7. Удалить сообщение ##
![Удалить сообщение](https://github.com/Koregin/job4j_chat/blob/master/images/delete_message.jpg)
 
Сообщения может редактировать и удалять только пользователь, который их создал.  
Удалять комнаты может только пользователь с правами ROLE_ADMIN

## Контакты ##
Если есть вопросы по работе приложения прошу обращаться. telegram: @KoreginE