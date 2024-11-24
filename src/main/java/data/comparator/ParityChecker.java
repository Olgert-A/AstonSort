package data.comparator;

@FunctionalInterface
public interface ParityChecker<T> {
    boolean isEven(T obj);
}
