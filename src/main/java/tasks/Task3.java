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
    Comparator<Person> comparator = Comparator.comparing(Person::firstName)
            .thenComparing(Person::secondName)
            .thenComparing(Person::createdAt);
    return persons.stream().
            sorted(comparator).
            collect(Collectors.toList());
  }
}
