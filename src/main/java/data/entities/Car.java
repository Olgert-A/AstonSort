package data.entities;

import java.util.Objects;

public class Car {
    private final String model;
    private final int power;
    private final int productionYear;

    public Car(CarBuilder builder) {
        this.model = builder.model;
        this.power = builder.power;
        this.productionYear = builder.productionYear;
    }

    public String getModel() {
        return model;
    }

    public int getPower() {
        return power;
    }

    public int getProductionYear() {
        return productionYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car car)) return false;
        return power == car.power && productionYear == car.productionYear && Objects.equals(model, car.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, power, productionYear);
    }

    @Override
    public String toString() {
        return "Car{" +
                "model='" + model + '\'' +
                ", power=" + power +
                ", productionYear=" + productionYear +
                '}';
    }

    public static class CarBuilder {
        private String model;
        private int power;
        private int productionYear;

        public CarBuilder setModel(String model) {
            this.model = model;
            return this;
        }

        public CarBuilder setPower(int power) {
            this.power = power;
            return this;
        }

        public CarBuilder setProductionYear(int productionYear) {
            this.productionYear = productionYear;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}
