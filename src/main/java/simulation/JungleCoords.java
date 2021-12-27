package simulation;

public record JungleCoords(Vector2d leftCorner, Vector2d rightCorner) {
    public boolean isVectorWithin(Vector2d vector2d) {
        return this.leftCorner.y >= vector2d.y
                && vector2d.x >= this.leftCorner.x
                && vector2d.y >= this.rightCorner.y && vector2d.x <= this.rightCorner.x;
    }

    public int getSize() {
        return Math.abs(this.rightCorner.x - this.leftCorner.x) * Math.abs(this.leftCorner.y - this.rightCorner.y);
    }
}
