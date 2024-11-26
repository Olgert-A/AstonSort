package view;

public enum ClassReprEnum implements ViewRepresentationEnum {
    CAR("Машина"),
    BOOK("Книга"),
    KORNEPLOD("Корнеплод");

    private final String localeName;

    ClassReprEnum(String localeName) {
        this.localeName = localeName;
    }

    @Override
    public String getLocaleName() {
        return this.localeName;
    }

    @Override
    public String getRepresentation() {
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
