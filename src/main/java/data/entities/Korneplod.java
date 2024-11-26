package data.entities;

import java.util.Objects;

public class Korneplod {
    private final String type;
    private final double weight;
    private final String color;

    public Korneplod(KorneplodBuilder builder) {
        this.type = builder.type;
        this.weight = builder.weight;
        this.color = builder.color;
    }

    public String getType() {
        return type;
    }

    public double getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Korneplod korneplod)) return false;
        return Double.compare(weight, korneplod.weight) == 0 && Objects.equals(type, korneplod.type) && Objects.equals(color, korneplod.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, weight, color);
    }

    @Override
    public String toString() {
        return "Korneplod{" +
                "type='" + type + '\'' +
                ", weight=" + weight +
                ", color='" + color + '\'' +
                '}';
    }

    public static class KorneplodBuilder {
        private String type;
        private double weight;
        private String color;

        public KorneplodBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public KorneplodBuilder setWeight(double weight) {
            this.weight = weight;
            return this;
        }

        public KorneplodBuilder setColor(String color) {
            this.color = color;
            return this;
        }

        public Korneplod build() {
            return new Korneplod(this);
        }
    }
}
