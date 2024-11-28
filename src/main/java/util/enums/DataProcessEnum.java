package util.enums;

import util.ViewRepresentation;

public enum DataProcessEnum implements ViewRepresentation {
    SORT("Сортировка"),
    SEARCH("Поиск"),
    NOTHING("Закончить работу с данными");

    private final String localeName;

    DataProcessEnum(String localeName) {
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
