package tasks;

import common.Person;
import common.PersonService;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
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
  public Task1(PersonService personService) {
    this.personService = personService;
  }
  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);
    return personIds.stream().map(x->findPersonById(persons,x)).collect(Collectors.toList());
  }
  private Person findPersonById(Set<Person> persons,Integer id){
    return persons.stream()
            .filter(person->person.id().equals(id))
            .findFirst()
            .orElse(null);
  }
}
