package view;

public enum CarFieldEnum implements ViewRepresentationEnum {
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
    public String getRepresentation() {
        return this.ordinal() +
                " - " +
                this.localeName;
    }


    @Override
    public String toString() {
        return "CarFieldEnumerator{" +
                "localeName='" + localeName + '\'' +
                '}';
    }


}