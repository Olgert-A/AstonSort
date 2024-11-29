package util.enums;

import util.ViewRepresentation;

public enum EntityEnum implements ViewRepresentation {
    CAR("Машина"),
    BOOK("Книга"),
    KORNEPLOD("Корнеплод");

    private final String localeName;

    EntityEnum(String localeName) {
        this.localeName = localeName;
    }

    @Override
    public String getLocaleName() {
        return this.localeName;
    }

    @Override
    public String getOrdinalLocaleName() {
        return ViewOrdinalUtil.getViewOrdinal(this.ordinal()) +
                " - " +
                this.localeName;
    }
}
