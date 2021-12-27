package simulation;

public class JungleGenerationStrategy {

    int jungleRatioPercentage;

    Vector2d mapSize;

    public JungleGenerationStrategy(int jungleRatioPercentage, Vector2d mapSize) {
        this.jungleRatioPercentage = jungleRatioPercentage;
        this.mapSize = mapSize;
    }

    JungleCoords getJungleBoundaries() {
        int jungleSizeX = mapSize.x * jungleRatioPercentage / 100;
        int jungleSizeY = mapSize.y * jungleRatioPercentage / 100;
        int middleX = mapSize.x / 2;
        int middleY = mapSize.y / 2;


        return new JungleCoords(
                new Vector2d(middleX - jungleSizeX / 2, middleY - jungleSizeY / 2),
                new Vector2d(middleX + jungleSizeX / 2, middleY + jungleSizeY / 2)
        );
    }
}
