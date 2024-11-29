package data.util;

@FunctionalInterface
public interface ParityChecker<T> {
    boolean isEven(T obj);
}
