package util.enums;

import util.ViewRepresentation;

public enum InputTypeEnum implements ViewRepresentation {
    MANUAL("Ручной ввод"),
    RANDOM("Случайное заполнение"),
    FILE("Чтение из файла");

    private final String localeName;

    InputTypeEnum(String localeName) {
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
