# ЛР №7–9. Типізований CRUD-сервіс: узагальнення, колекції, сортування

**Тема проєкту №14 — система бронювання квитків на автобус.**

## Сутності

Дві сутності, обидві успадковують `BaseEntity`:

| Сутність | `name` у `BaseEntity` | Власні поля |
|---|---|---|
| `Trip` (ієрархія з ЛР6: `SuburbanTrip`, `IntercityTrip`, `InternationalTrip`) | маршрут | `departureTime`, `price` + поля підкласів |
| `Booking` | ім'я пасажира | `trip`, `seats`, `status` |

## Нові класи

```
src/bus/
├── model/BaseEntity.java                  id, name, equals/hashCode за id
├── exception/EntityNotFoundException.java
├── service/
│   ├── CRUDService.java                   CRUDService<T extends BaseEntity>
│   ├── InMemoryCrudService.java           реалізація на LinkedHashMap
│   ├── TripService.java                   findAllSortedByNameAndByDepartureTime()
│   ├── InMemoryTripService.java
│   └── EntityComparators.java             компаратори за id та іменем
├── util/EntityPrinter.java                printAll(Collection<? extends BaseEntity>)
└── demo/CrudDemo.java                     пункт меню 6
```

## Рівень 1

**BaseEntity.** Абстрактний клас з `long id` і `String name`, обидва `final`: id не змінюється після створення. `Trip` і `Booking` успадковують його й передають id та назву через `super(...)`.

**EntityNotFoundException — неперевірений (`RuntimeException`).** Контракт `CRUDService` обов'язковий і має сигнатуру `T findById(long id)` без `throws`, тому перевірений виняток туди не вписався б. Крім того, пошук неіснуючого id — це помилка того, хто викликає (передав id рейсу, якого немає), а не ситуація, яку кожен виклик зобов'язаний обробляти. Хто хоче — перехоплює його, як у `CrudDemo`. Виняток несе поле `id`.

З тієї ж причини `DomainException` (ЛР3) тепер успадковується від `RuntimeException`: інакше кожне створення рейсу чи бронювання в сервісах і демо обросло б `throws`/`try-catch`. Ієрархія `DomainException → InvalidPriceException / InvalidQuantityException`, повторне збудження і перехоплення в `BookingInputDemo` працюють як раніше.

**Навіщо `T extends BaseEntity`.** Без обмеження `T` був би просто `Object`, і всередині сервісу не можна було б викликати `entity.getId()` (ключ у `save()`) і `getName()` (сортування за іменем). Обмеження гарантує, що будь-яке `T` має id і назву, а `CRUDService<String>` просто не скомпілюється.

**InMemoryCrudService\<T\>.** Один клас без змін працює з обома сутностями: `InMemoryTripService extends InMemoryCrudService<Trip>` і `new InMemoryCrudService<Booking>("Бронювання")`.

**Демо (пункт меню 6):** додавання 5 рейсів; `save()` з наявним id = 2 оновлює рейс (новий час і ціна), кількість лишається 5; `delete(5)` → `true`, повторно → `false`; `findById(999)` → `EntityNotFoundException`.

## Рівень 2

**findAllSortedByName() — через Comparator.** Сутностей дві, а порядків сортування кілька (за маршрутом, за маршрутом і часом, за id, за сумою бронювання). `Comparable` дає лише один «природний» порядок, вшитий у клас, і для `Trip` його довелося б оголосити в абстрактному класі або в `BaseEntity`. `Comparator` описується окремо, комбінується (`thenComparing`) і може бути спільним для різних типів. Порівняння назв іде через `Collator` з українською локаллю: звичайний `String.compareTo()` порівнює коди символів і ставить «Є», «І», «Ї», «Ґ» не на свої місця за абеткою.

**TripService.** `findAllSortedByNameAndByDepartureTime()` залежить від поля `departureTime`, яке є лише в `Trip`, тому метод не в загальному `CRUDService<T>`, а в окремому `TripService extends CRUDService<Trip>`. Реалізація — `InMemoryTripService extends InMemoryCrudService<Trip> implements TripService`: спочатку за маршрутом, при збігу — за часом відправлення. Тут теж `Comparator`, бо це другий, додатковий порядок для рейсів.

**Сортування не змінює сховище.** `findAllSorted()` копіює значення в новий `ArrayList` і сортує копію. У демо є два рейси «Чернівці — Київ» (07:30 і 21:45), причому вечірній додано першим. Після `findAllSortedByName()` вони стоять разом, але в порядку додавання; після `findAllSortedByNameAndByDepartureTime()` ранковий іде перед вечірнім. Список виводиться до сортування, після обох сортувань і ще раз — щоб показати, що порядок у сховищі той самий.

**Вибір колекції — `LinkedHashMap<Long, T>`.**

| Колекція | findById | save (додати / оновити) | delete | Відсортований список |
|---|---|---|---|---|
| `ArrayList<T>` | O(n) — перебір | O(n) — спершу пошук за id | O(n) | O(n log n) |
| `HashMap<Long, T>` | O(1) | O(1) | O(1) | O(n log n) |
| `LinkedHashMap<Long, T>` | O(1) | O(1) | O(1) | O(n log n) |
| `TreeMap<Long, T>` | O(log n) | O(log n) | O(log n) | O(n log n) (сортований лише за id) |

Основні операції сервісу йдуть за id, тому потрібна мапа з ключем id. `ArrayList` на кожен `save` і `findById` перебирає список. `TreeMap` тримає порядок за id, але нам потрібні сортування за маршрутом і часом, тож це не допомагає. Між `HashMap` і `LinkedHashMap` обрано другий: та ж складність, але зберігає порядок додавання, тому вивід «до сортування» передбачуваний.

**equals()/hashCode() за id.** Перевизначені в `BaseEntity` як `final`, щоб підкласи не порушили правило «та сама сутність = той самий id». Порівняння полів з ЛР6 у `Trip` і `Booking` прибрано: рейс, у якого змінили час відправлення, лишається тим самим рейсом (демо в пункті 5). `getClass()` гарантує, що рейс і бронювання з однаковим id не рівні.

**Узгодженість з equals.** Компаратор за маршрутом не узгоджений з `equals`: два різні рейси «Чернівці — Київ» (id 1 і 3) порівнюються як 0, але `equals` дає `false`. `Comparable<Booking>` (за сумою, з ЛР4) так само неузгоджений. Для `List.sort()` це не проблема. Але в `TreeSet`/`TreeMap` з таким компаратором один із «рівних» за компаратором рейсів просто пропав би, бо ці колекції вважають об'єкти однаковими за `compare() == 0`, а не за `equals()`. Тому для сховища використано мапу за id, а сортування робиться лише в нових списках.

## Рівень 3

**`findAllSorted(Comparator<? super T>)`.** Узагальнений метод у `CRUDService`. Через нього реалізовані обидва сортування, а `findAllSortedByName()` став default-методом інтерфейсу. Параметр має тип `Comparator<? super T>`, бо компаратор, що вміє порівнювати будь-яку `BaseEntity`, вміє порівнювати й `Trip`. З `Comparator<T>` у `CRUDService<Trip>` не можна було б передати `Comparator<BaseEntity>`, хоча він підходить. У демо один `EntityComparators.BY_ID.reversed()` типу `Comparator<BaseEntity>` сортує і рейси, і бронювання.

**`printAll(Collection<? extends BaseEntity>)`.** Generics інваріантні: `List<Trip>` не є `List<BaseEntity>`. Якби це було дозволено, через посилання `List<BaseEntity>` у список рейсів можна було б додати `Booking`. Wildcard `? extends BaseEntity` дозволяє лише читати елементи як `BaseEntity` і не дозволяє додавати, тому туди безпечно передавати і `List<Trip>`, і `List<Booking>`.

## Запуск

```bash
javac -d out $(find src -name "*.java")
java -cp out bus.Main
```

Новий пункт меню: `6` — CRUD-сервіс у пам'яті.
