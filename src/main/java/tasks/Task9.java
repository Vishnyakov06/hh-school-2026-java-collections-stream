package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  /*
      Проверка на null, вдруг ничего не передали, а мы как-то с этим объектом работаем.
      Заменили прямую проверку на отсутствие элементов вызовом специализированного под эту операцию метод,
      что выглядит более лаконично что ли (ну это кому как)
      Сначала думал, как-то может изменить входной список, вообще задавался вопросом, что в данном случае костыль нормальная практика,
      так как не наша проблема, что пришел не корректный список, но потом подумал что можно не удалять первый элемент,
      что вредит производительности, так как придется весь список смещать назад, это O(n), вариант просто пропускать в итерации
      этот первый некорректный элемент.
   */
  public List<String> getNames(List<Person> persons){
    return getNamesStream(persons).collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  /*
      Приступив к этому методу, было понятно что лучше надо убрать distinict, так как и так в set записывали
      была проблема с тем что мы обращаемся к getNames получаем список с пропущенной первой строчкой, то есть прошлись за O(n)
      Где n размер листа, а затем пройтись еще раз же по нему, но добавляя в set, O(n-1), ну в биг о нотации имеем O(2n)
      я решил переписать то что было в getNames в метод getDifferentNames, чтобы после прохода и скипа 1 строки сразу бы преобразовывали
      в set, но тогда был в тупую повторенный код, решил придержаться чистоте кода и не повторится тем самым DRY соблюли))
      и того O(n)
   */
  public Set<String> getDifferentNames(List<Person> persons) {
    return getNamesStream(persons).collect(Collectors.toSet());
  }
  private Stream<String> getNamesStream(List<Person> persons){
    if(persons==null || persons.isEmpty()){
      return Stream.empty();
    }
    return persons.stream()
            .skip(1)
            .map(Person::firstName);
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  /*
      Была некрасивая цепочка условных операторов, заменили на проверку через Objects.nonNull
      добавили терминальную операцию joining для преобразования строки к нужному формату
   */
  public String convertPersonToString(Person person) {
    return Stream.of(person.secondName(),
                    person.firstName(),
                    person.middleName()
            )
            .filter(field->Objects.nonNull(field))
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  /*
  с помощью перегрузки метода toMap, который принимает 3 параметром стратегию выбора элемента при повторном ключе,
  получаем аналогичный способ избегания добавления дубликата.
   */
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream()
            .collect(Collectors.toMap(Person::id,this::convertPersonToString,
                    (oldKey,newKey ) -> oldKey));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  /*
  первое что пришло в голову пересечь коллекции с помощью retainsAll
  но если бы это были бы ArrayList, то это те же O(n^2)
  подумал что можно бы одну коллекцию преобразовать в множество, для быстрого поиска по ключу
  тем самым создание множества O(n), доступ по ключу для проверки O(1) и проход по второй коллекции O(m)
  с помощью anyMatch выходим сразу же при обнаружении тем самым сокращаем обход второй коллекции, но в худшем случае все так же O(n)
   */
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    Set<Person> persons1ToSet = new HashSet<>(persons1.size());
    persons1ToSet.stream().collect(Collectors.toSet());
    return persons2.stream().anyMatch(persons1ToSet::contains);
  }

  // Посчитать число четных чисел
  /*
  подсчет в одном потоке + добавил битовый сдвиг, по идеи должно быть быстрее, чем %
   */
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> (num & 1) == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
  /*
  вроде бы если ключом у нас является integer, то hashCode вернет само число,
  hashSet под капотом hashMap а значит использует бакеты, номер бакеты вычисляется как hachCode%кол-во бакетов,
  у нас бакетов будет больше 10к в силу расширения 2^14 вроде, а значит любое число меньшее 2^14 при mod даст само себя
  значит элементы будут сидеть ровно в тех бакетах по индексу, чему равны сами ключи элементов, поэтому когда мы пойдем обходить
  через toString который определен в AbstractCollection как обычный перебор подряд коллекции, то мы просто будет итерироваться
  от бакета к бакету, то есть от 1-10к
   */
}
