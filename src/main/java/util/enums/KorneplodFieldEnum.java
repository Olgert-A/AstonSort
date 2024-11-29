package util.enums;

import util.ViewRepresentation;

public enum KorneplodFieldEnum implements ViewRepresentation {
    TYPE("Тип корнеплода"),
    WEIGHT("Вес"),
    COLOR("Цвет"),
    ALL("Все поля");

    private final String localeName;

    KorneplodFieldEnum(String localeName) {
        this.localeName = localeName;
    }

    @Override
    public String getLocaleName() {
        return localeName;
    }

    @Override
    public String getOrdinalLocaleName() {
        return ViewOrdinalUtil.getViewOrdinal(this.ordinal()) +
                " - " +
                this.localeName;
    }
}
