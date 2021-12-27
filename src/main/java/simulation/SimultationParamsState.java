package simulation;

public record SimultationParamsState(int mapWidth, int mapHeight, int startEnergy, int moveEnergy, int plantEnergy,
                                     int jungleRatio, boolean isMagicBreedModeMapOne, boolean isMagicBreedModeMapTwo) { }
