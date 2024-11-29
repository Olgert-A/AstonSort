package util.enums;

import util.ViewRepresentation;

public enum YesNoEnum implements ViewRepresentation {
    YES("Да"),
    NO("Нет");

    private final String localeName;

    YesNoEnum(String localeName) {
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
