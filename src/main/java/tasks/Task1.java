package tasks;

import common.Person;
import common.PersonService;

import java.util.*;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
public class Task1 {

  private final PersonService personService;
  /*
    Асимптотическая сложность O(n*m), где n - размер листа с индексами,
    а m - размер множества person.
    Если бы была другая структура данных, вместо set, к примеру map,
    то можно было по ключу искать без перебора за O(1),
    где ключ id person, доставать элементы, но так как у нас только set и list даны,
    то пользуемся тем, что есть))
   */
  /*
  Update!!!
  После разговора с Вами, я понял что можно делать что хочешь, поэтому, решил добавить Map для быстрого доступа
  к элементам по id, в силу hash tables, получается Асимптотическая сложность теперь O(n+m) за O(n) заполнили мапу
  за O(1) получаем person по id, за O(m) обходим лист
   */
  public Task1(PersonService personService) {
    this.personService = personService;
  }
  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);
    Map<Integer,Person> personMap = persons.stream()
            .collect(Collectors.toMap((Person::id), person -> person));
    return personIds.stream().map(personMap::get).collect(Collectors.toList());
  }
}
