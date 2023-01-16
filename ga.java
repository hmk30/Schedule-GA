public class ga {
    // command line args: <n: int> <maxGenerations: int> <pc: double> <pm: double>
    // <selection: int {1=tournament, else=elitist}>
    // n = population size
    public static void main(String[] args) {
        int populationSize, maxGenerations, selectionMethod;
        double crossoverProbability, mutationProbability;

        try {
            populationSize = Integer.parseInt(args[0]);
            maxGenerations = Integer.parseInt(args[1]);
            crossoverProbability = Double.parseDouble(args[2]);
            mutationProbability = Double.parseDouble(args[3]);
            selectionMethod = Integer.parseInt(args[4]);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            // System.out.println("Invalid arguments provided.");
            // System.out.println("required format: `java ga (int) (int) (double)
            // (double) (int)`");
            // return;
            System.out.println("Using default args...");
            populationSize = 250;
            maxGenerations = 100;
            crossoverProbability = 0.5;
            mutationProbability = 0.05;
            selectionMethod = 1;
        }

        GeneticAlgorithm ga = new GeneticAlgorithm(populationSize, maxGenerations,
                crossoverProbability,
                mutationProbability, selectionMethod);

        System.out.println("");

        ga.run();
    }
}
