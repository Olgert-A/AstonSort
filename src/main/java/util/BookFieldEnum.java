package util;

public enum BookFieldEnum implements ViewRepresentation {
    AUTHOR("Автор"),
    TITLE("Название книги"),
    PAGES("Количество страниц"),
    ALL("Все поля");

    private final String localeName;

    BookFieldEnum(String localeName) {
        this.localeName = localeName;
    }

    @Override
    public String getLocaleName() {
        return localeName;
    }

    @Override
    public String getOrdinalLocaleName() {
        return this.ordinal() +
                " - " +
                this.localeName;
    }


    @Override
    public String toString() {
        return "BookFieldEnum{" +
                "localeName='" + localeName + '\'' +
                '}';
    }
}
