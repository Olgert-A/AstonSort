package data.validate;

public interface BookValidator {
    boolean isAuthorValid(String author);
    boolean isTitleValid(String title);
    boolean isPagesValid(int pages);
}
