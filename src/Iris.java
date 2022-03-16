import java.util.Arrays;

public class Iris {

    private final String type;
    private final double[] params;

    private double distanceFromNewIris;

    public Iris(String name, double[] params) {
        this.type = name;
        this.params = params;
    }

    public String getType() {
        return type;
    }

    public double[] getParams() {
        return params;
    }

    public void setDistanceFromNewIris(double distanceFromNewIris) {
        this.distanceFromNewIris = distanceFromNewIris;
    }

    public double getDistanceFromNewIris() {
        return distanceFromNewIris;
    }

    public String getIrisInfoWithoutSpoilers() {
        return "Iris{params=" + Arrays.toString(params)+"}";
    }

    @Override
    public String toString() {
        return "Iris{" +
                "name='" + type + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }

}
