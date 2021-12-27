package simulation;

import java.util.Objects;

public class Vector2d {
    public final int x;
    public final int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2d add(Vector2d toAdd) {
        return new Vector2d(this.x + toAdd.x, this.y + toAdd.y);
    }

    public Vector2d getBackwardFacing() {
        return new Vector2d(-this.x, -this.y);
    }
}
