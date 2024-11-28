package util.enums;

import util.ViewRepresentation;

public enum SortTypeEnum implements ViewRepresentation {
    SORT("Обычная"),
    SORTEVEN("Сортировка чётных значений");

    private final String localeName;

    SortTypeEnum(String localeName) {
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
}
