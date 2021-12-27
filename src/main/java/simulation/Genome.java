package simulation;

import java.util.*;

public class Genome {

    public static int MAX_GENE_VALUE = 7;
    public static int MIN_GENE_VALUE = 0;
    public static int GENOME_SIZE = 32;

    private final ArrayList<Integer> genomeValue;

    Genome() {

        this.genomeValue = new ArrayList<>(Genome.GENOME_SIZE);

        Random random = new Random();

        for (int i = 0; i < Genome.GENOME_SIZE; i++) {
            this.genomeValue.add(i, random.nextInt(Genome.MAX_GENE_VALUE) + Genome.MIN_GENE_VALUE);
        }
        Collections.sort(this.genomeValue);
    }

    Genome(ArrayList<Integer> genomeValue) {
        this.genomeValue = genomeValue;
    }


    public int getRandomGene() {
       Random random = new Random();

       return this.genomeValue.get(random.nextInt(Genome.GENOME_SIZE));
    }

    public void sortGenome() {
        Collections.sort(this.genomeValue);
    }


    Genome mixGenomes(Genome secondGenome, int secondGenomeParticipationPercentage) {
        ArrayList<Integer> newGenome = new ArrayList<>(Genome.GENOME_SIZE);

        this.sortGenome();
        secondGenome.sortGenome();

        int secondGenomeParticipation = (Genome.GENOME_SIZE * secondGenomeParticipationPercentage) / 100;
        for (int i = 0; i < secondGenomeParticipation; i++) {
            newGenome.add(i, secondGenome.getRandomGene());
        }

        for (int i = 0; i < Genome.GENOME_SIZE - secondGenomeParticipation; i++) {
            newGenome.add(i + secondGenomeParticipation, this.getRandomGene());
        }

        Genome mixedGenome = new Genome(newGenome);

        mixedGenome.sortGenome();

        return mixedGenome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genome genome = (Genome) o;
        ArrayList<Integer> listOne = new ArrayList<>(this.genomeValue);
        ArrayList<Integer> listTwo = new ArrayList<>(genome.genomeValue);
        Collections.sort(listOne);
        Collections.sort(listTwo);
        return listOne.equals(listTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genomeValue);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder((this.genomeValue.stream().map(Object::toString).reduce((str, acc) -> acc + str).get()));
        return builder.reverse().toString();
    }
}
