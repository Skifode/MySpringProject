INSERT INTO `post`
(`is_active`, `moderation_status`, `moderator_id`, `text`, `time`, `title`, `user_id`, `view_count`)
VALUES
('1', 'ACCEPTED', '1',
  'За 2009 на Ozon.ru можно найти 8 книг по Java (всего за последние 3 года их вышло 22 — примерно по 7 в год, на books.ru набор примерно такой же, так что наша выборка довольно репрезентативна). Посмотрим, что это за книги?'
, '2004-05-23', 'Книги', '1', '0'),
('1', 'ACCEPTED', '1',
'«Программирование web-приложений на языке Java» от Буди Курняван, «Технологии программирования на Java 2. Распределенные приложения» от Х. М. Дейтел, П. Дж. Дейтел, С. И. Сантри и «Java сервлеты и JSP. Сборник рецептов» Брюс Перри. Выпущены они были в 2002, 2001 и 2004 годах и современные Java EE(5 версия вышла в 2006, к концу 2009 выходит 6) технологии и фреймворки(EJB 3, JPA, JSF, Spring, Hibernate, Dependency Injection) никак не покрывают, поэтому говорить что-то о них смысла попросту нет.',
'2005-05-23', 'Java EE', '2', '0'),
('1', 'ACCEPTED', '1',
'Здесь авторы куда более именитые. \nВсе 4 книги в принципе можно рекомендовать как начальные, хотя я бы посоветовал Хорстманна. \n«Философия Java» Брюс Эккель. По праву считается одной из лучших книг по Java(русское издание даже идёт с логотипом Luxoft), но к сожалению перевод страдает не только качеством(судя по отзывам на books.ru), а и урезанным содержанием: 640 страниц против 1150 в оригинале. \n«Java 2. Библиотека профессионала. Том 2. Тонкости программирования» Кей Хорстманн, Гари Корнелл. Это второй том книги, которую можно назвать учебником от создателя(Sun Press). Первый том книги вышел в прошлом году, а задержка перевода составляет всего год. Наверное, это самый полный и актуальный(отражающий 6 версию) учебник по Java SE. \n«Java. Руководство для начинающих» Герберт Шилдт. Треть всех книг по Java за последние 3 года именно от этого автора. Так же у этой книги самый большой тираж — 3000 экземпляров. Популярны и его книги по C++ & C#(уж не знаю, как ему удаётся писать по 3-4 книге в год по разным технологиям), поэтому наверное много программистов, переходящих с этих технологий на Java, выбирают именно его: знакомая фамилия как-никак. \n«Язык программирования Java и среда NetBeans» В. Монахов. Курс лекций СПбГУ, содержит обзор практически всех технологий компании Sun: Java, Netbeans, Java ME/SE/EE. Так что если вы хотите познакомится с платформой в целом — это ваш выбор. Правда в свете недавнего приобретения Sun компанией Oracle, будущее инструментов, используемых в книге, неизвестно.',
'2003-05-23', 'Java SE', '3', '0'),
('1', 'ACCEPTED', '1', 'Тут одни книги? В 21 веке не читают книги. \n \n бред какой..', '2020-09-24', 'Что это за блог?', '4', '0'),
('1', 'ACCEPTED', '1',
'Единственная книжка по Java ME за этот год — Майкл Моррисон «Java 2 Me. Программирование игр для мобильных телефонов». К сожалению сказать ничего не могу, но рейтинг оригинала на Amazon = 4,5 звезды(максимум 5). Надеюсь, что это так, потому что все другие книги по этой технологии за прошедшие года(а их набралось всего 4) не нашли ни одного положительного комментария на books.ru & Ozon.',
'2006-05-23','Java ME', '5', '0'),
('1', 'ACCEPTED', '1',
'Откроем рейтинг с самой противоречивой книги. У неё очень много негативных отзывов. Причина — поверхностный взгляд как на сам язык, так и на указанную в названии специфику. И даже несмотря на то, что третье издание значительно переработано и улучшено, книга вряд ли будет интересна специалистам. \nА вот новичков вполне может заинтересовать. Но не в качестве учебного пособия, а как «лёгкое чтиво» для мотивации к обучению. Знаете, это как с художественной литературой, основанной на исторических событиях — хорошее изложение способствует дальнейшему погружению в реальные факты. \nКнига Михаила Фленова написана приятным языком, не требуют глубоких познаний в PHP, а соотношение цена/объём весьма неплохое.',
'2009-05-23', 'PHP глазами хакера, Михаил Фленов', '6', '0'),
('1', 'ACCEPTED', '1',
'Главным достоинством этой книги является тот факт, что у авторов получилось запихнуть в неё максимум информации, которая может понадобиться новичку. Кроме того, здесь вы найдёте много примеров по каждой теме, рассмотрение вопросов, не касающихся непосредственно языка (например, стиля кода). Ну и, конечно, цена для такого объёма и содержания очень привлекательная.',
'2011-05-23', 'PHP 7, Дмитрий Котеров, Игорь Симдянов', '7', '0'),
('1', 'ACCEPTED', '1',
'Дэвид Скляр является одним из тех авторов в мире PHP, чьи книги принято ставить в пример. Данный случай — не исключение, вы получаете почти идеальное руководство для изучения с самого нуля (потребуется лишь базовое знание HTML). В конце книги приведены примеры, чтобы закрепить всё прочитанное, а содержимое дополнено качественными иллюстрациями.',
'2019-05-23', 'Изучаем PHP 7. Руководство по созданию интерактивных веб-сайтов, Дэвид Скляр', '6', '0'),
('1', 'ACCEPTED', '1',
'Если же средств в обрез, а изучить PHP очень хочется, воспользуйтесь этой замечательной книгой Максима Кузнецова и Игоря Симдянова. По сравнению со Скляром издание немного проигрывает по качеству (мягкая обложка) и количеству полезных примеров. \n \nЧто касается теории, то её подача поможет понять материал без использования интернет-ресурсов и вообще без компьютера под боком, что бывает полезно, если вы изучаете PHP в свободное время (например, в дороге или отпуске). Важным плюсом является и то, что книга свежая, все нововведения языка и требования к современной разработке отражены в полной мере.',
'2019-05-23', 'Самоучитель PHP 7, Максим Кузнецов, Игорь Симдянов', '5', '0'),
('1', 'ACCEPTED', '5',
'С помощью данного руководства дизайнеры более уверенно сделают свои первые шаги на ниве фриланса. Автор книги шаг за шагом описывает, с чего начать работу фрилансером, как вести поиск новых клиентов и сохранить постоянных, как сделать успешными свои проекты, а также какие рынки доступны для фрилансеров.',
'2017-05-23', 'Web Designer’s Success Guide', '5', '0'),
('1', 'ACCEPTED', '5',
'Пособие, рассчитанное на дизайнеров, разработчиков и менеджеров. В книге изложены особенности создания сайтов, программного обеспечения, аппаратных средств и потребительских товаров для пожилых людей и людей, находящихся на реабилитации. Изучив предоставленную информацию, можно создавать полезные и доступные для всех пользователей проекты.',
'2018-05-23', 'Just Ask — Integrating Accessibility Throughout Design', '5', '0'),
('1', 'ACCEPTED', '5',
'Книга для начинающих и состоявшихся специалистов, работающих с веб-технологией JavaScript. В книге рассказывается об основах JavaScript и шаблонах проектирования – их особенностях, применении. Приводятся реальные практические примеры решения многих проблем, часто возникающих при создании программных продуктов.',
'2020-05-23', 'Essential JavaScript And jQuery Design Patterns', '5', '0');
