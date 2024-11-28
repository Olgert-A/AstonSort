package util.enums;

import util.ViewRepresentation;

public enum SortTypeEnum implements ViewRepresentation {
    SORT("Обычная"),
    SORTEVENVALUES("Сортировка четных полей");


    private final String localeName;

    SortTypeEnum(String localeName) {
        this.localeName = localeName;
    }

    @Override
    public String getLocaleName() {
        return this.localeName;

    }

    @Override
    public String getOrdinalLocaleName() {
        return this.ordinal() +
                " - " +
                this.localeName;
    }


    @Override
    public String toString() {
        return "SearchTypeEnum{" +
                "localeName='" + localeName + '\'' +
                '}';
    }
}
