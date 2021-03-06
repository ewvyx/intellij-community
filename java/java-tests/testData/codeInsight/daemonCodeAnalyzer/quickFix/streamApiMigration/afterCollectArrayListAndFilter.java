// "Replace with collect" "true"
import java.util.*;
import java.util.stream.Collectors;

public class Collect {
  class Person {
    String getName() {
      return "";
    }
  }

  void collectNames(List<Person> persons){
    List<String> names = persons.stream().filter(person -> person != null).map(Person::getName).collect(Collectors.toList());
  }
}
