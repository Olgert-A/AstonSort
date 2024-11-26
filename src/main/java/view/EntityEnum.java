package view;

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
        return this.ordinal() +
                " - " +
                this.localeName;
    }


    @Override
    public String toString() {
        return "ClassReprEnum{" +
                "localeName='" + localeName + '\'' +
                '}';
    }
}
