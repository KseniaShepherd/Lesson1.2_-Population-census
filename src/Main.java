import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Jack", "Connor", "Harry", "George", "Samuel", "John");
        List<String> families = Arrays.asList("Evans", "Young", "Harris", "Wilson", "Davies", "Adamson", "Brown");
        Collection<Person> persons = new ArrayList<>();

        for (int i = 0; i < 10_000_000; i++) {
            persons.add(new Person(
                    names.get(new Random().nextInt(names.size())),
                    families.get(new Random().nextInt(families.size())),
                    new Random().nextInt(100),
                    Sex.values()[new Random().nextInt(Sex.values().length)],
                    Education.values()[new Random().nextInt(Education.values().length)])
            );
        }
        calculate(persons);
        calculateParallel(persons);
    }

    public static void calculate(Collection<Person> persons) {
        long startTime = System.nanoTime();

        long countUnderage = persons.stream()
                .filter(person -> person.getAge() < 18)
                .count();
        List<String> conscripts = persons.stream()
                .filter(person -> person.getAge() > 18 && person.getAge() < 27)
                .map(Person::getFamily).collect(Collectors.toList());
        List<Person> workers = persons.stream()
                .filter(person -> person.getEducation()
                        .equals(Education.HIGHER))
                .filter(person -> person.getSex().equals(Sex.WOMEN) && person.getAge() > 18 && person.getAge() < 60
                        || person.getSex().equals(Sex.MAN) && person.getAge() > 18 && person.getAge() < 65)
                .sorted(Comparator.comparing(Person::getFamily))
                .collect(Collectors.toList());
        long stopTime = System.nanoTime();
        double processTime = (double) (stopTime - startTime) / 1_000_000_000.0;
        System.out.println("Process time: " + processTime + " s");

    }

    public static void calculateParallel(Collection<Person> persons) {
        long startTime = System.nanoTime();
        long countUnderage = persons.parallelStream()
                .filter(person -> person.getAge() < 18)
                .count();
        List<String> conscripts = persons.parallelStream()
                .filter(person -> person.getAge() > 18 && person.getAge() < 27)
                .map(Person::getFamily)
                .collect(Collectors.toList());

        List<Person> workers = persons.parallelStream()
                .filter(person -> person.getEducation().equals(Education.HIGHER))
                .filter(person -> person.getSex().equals(Sex.WOMEN) && person.getAge() > 18 && person.getAge() < 60
                        || person.getSex().equals(Sex.MAN) && person.getAge() > 18 && person.getAge() < 65)
                .sorted(Comparator.comparing(Person::getFamily))
                .collect(Collectors.toList());
        long stopTime = System.nanoTime();
        double processTime = (double) (stopTime - startTime) / 1_000_000_000.0;
        System.out.println("Process time: " + processTime + " s");
    }
}
// время выполнения программы для первого случая - Process time: 0.559359673 s
// время выполнения программы для второго случая - Process time: 0.402623733 s