package data.validate;

@FunctionalInterface
public interface Validate<T> {
    boolean isValid(T obj);
}
