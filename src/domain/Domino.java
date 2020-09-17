package domain;

import java.util.Arrays;

public class Domino {

    private final int[] value;

    public Domino (int down, int up) {
        if (down > up) {
            int aux = down;
            down = up;
            up = aux;
        }
        value = new int[]{down, up};
    }

    public Domino (int[] value) {
        this(value[0], value[1]);
    }

    public int total () {
        return Arrays.stream(value).sum();
    }

    public int[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Domino{" +
                "value=" + Arrays.toString(value) +
                '}';
    }
}
