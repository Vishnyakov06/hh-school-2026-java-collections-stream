package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;

/*
Задача 3
Отсортировать коллекцию сначала по фамилии, по имени (при равной фамилии), и по дате создания (при равных фамилии и имени)
 */
public class Task3 {

  public static List<Person> sort(Collection<Person> persons) {
    /*Throws: NullPointerException – if the argument is null(comment into docs)
    про null поля ничего не оговаривалось, поэтому оставил так
    вообще из бизнес логики, вряд ли можно оставить пустыми поля имени фамилии, это же основа анкеты.
    createdAt тоже не может быть null, ну конечно, если какой-то сторонний сервис по определению времени регистрации
     работает верно
     */
    return persons.stream().
            sorted(Comparator.comparing(Person::secondName)
                    .thenComparing(Person::firstName)
                    .thenComparing(Person::createdAt))
            .collect(Collectors.toList());
  }
}
