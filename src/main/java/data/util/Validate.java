package data.util;

@FunctionalInterface
public interface Validate<T> {
    boolean isValid(T obj);
}
