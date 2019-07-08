Микросервис для заказа книг
============================
База данных состоит из 3-х таблиц, а именно: 
1) Пользователи (Users) - идентификатор (id) и состояние счета пользователя (balance)
2) Книги (Books) - идентификатор (id), поля описания товара (название книги, авторы, краткое описание книги и др.), 
стоимость книги, количество в наличии (доступных для заказа) и число проданных
3) Заказы (Orders) -  идентификатор, список: товар/количество, статус, идентификатор пользователя, сумма заказа,  
дата заказа

Для работы с микросервиом: 
--------------------------
1 вариант:
----------
Перейти по ссылке http://176.119.158.72:8080/swagger-ui.html#

2 вариант:
----------
Запустить микросервис на локальной машине. 
Для этого: 
1) скопировать каталог jar;
2) выполнить скрипт run.bat из скопированного каталога;
3) дождаться сообщения: "Started Application in X seconds", где X - число затраченного времени на запуск микросервиса (обычно от 10 до 50 секунд).
4) Перейти по ссылке http://127.0.0.1:8080/swagger-ui.html#

Содержимое каталогов:
---------------------
1) jar - исполняемый файл. Запускает микросервис на локальной машине;
2) javadoc - javadoc документация;
3) src/main - исходный код проекта.

Замечания:
--------------------------------
1) Список книг и их количество для заказа храниться в одной ячейке таблицы orders в бинарном виде, что сводит на нет простые запросы к данному столбцу. Если существует необходимость в таких запросах, то более лучшем решением будет хранение таких данных в отдельной таблице.
2) Таблица с книгами содержит столбец, в котором фиксируется число проданных книг. Проданной называем книгу, заказ на которую был оплачен. При удалении книги осуществляется проверка данного поля (из ТЗ - купленная книга не может быть удалена). Такая реализация позволяет (например) выбрать самые продаваемые книги.
3) liquibase для создания таблиц не использовался. Вместо этого была создана БД на удаленном сервере.
4) При создании пользователей, книг и заказов - поле id заполнять не нужно. Идентификатор генерируется автоматически.
5) При создании заказа необходимо заполнить поля userId и список пар bookId/number. Остальные поля генерируются автоматически. Пример создания заказа для пользователя с ID=24:

{
  "books": [
    {
      "bookId": 38,
      "number": 2
    },
    {
      "bookId": 41,
      "number": 1
    }
  ],
  "userId": 24
}

6) После создания заказа для его оформления необходимо вызвать метод api/orders/pay, который произведет списание средств с баланса пользователя, изменит число доступных книг и статус заказа. Также метод pay осуществляет ряд проверок (наличие и изменение цены товара) для созданного заказа.
