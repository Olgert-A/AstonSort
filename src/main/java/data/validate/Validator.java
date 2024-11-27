package data.validate;

@FunctionalInterface
public interface Validator<T> {
    boolean isValid(T obj);
}
