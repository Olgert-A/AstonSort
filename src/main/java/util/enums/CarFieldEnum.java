package util.enums;

import util.ViewRepresentation;

public enum CarFieldEnum implements ViewRepresentation {
    MODEL("Модель"),
    POWER("Мощность"),
    YEAR("Год выпуска"),
    ALL("Все поля");

    private final String localeName;

    CarFieldEnum(String localeName) {
        this.localeName = localeName;
    }

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
