import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GeneticAlgorithm {
    private final Random random = new Random();

    private int generation;

    final private int populationSize, maxGenerations, selectionMethod;
    final private double crossoverProbability, mutationProbability;

    private List<Schedule> population;
    private Schedule bestSchedule;
    private int bestFitness = -9999; // must be lower than random valid population's lowest possible fitness

    public GeneticAlgorithm(int populationSize, int maxGenerations, double crossoverProbability,
            double mutationProbability, int selectionMethod) {

        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.selectionMethod = selectionMethod;
        this.population = new ArrayList<Schedule>();
    }

    private void generateStartPopulation() {
        for (int i = 0; i < populationSize; i++) {
            Schedule s = createValidSchedule();
            population.add(s);
        }
    }

    // returns a new, randomly generated, and validated Schedule
    private Schedule createValidSchedule() {
        Schedule s;

        // loop until schedule is valid
        do {
            s = new Schedule();
            Map<Integer, List<Integer>> usedCombinations = new HashMap<Integer, List<Integer>>();
            for (Course c : s.courses) {
                int rid = -1, tpid = -1;

                // loop until a course is valid
                int count = 0; // used to abort attempting a schedule that is impossible to make
                while (count < 100) {
                    // pick a random room
                    rid = random.nextInt(Room.roomList().size());

                    // pick a random time
                    tpid = random.nextInt(TimePeriod.timeList().size());

                    c.rid = rid;
                    c.tpid = tpid;
                    // ensure seating and multimedia requirements are satisfied
                    if (!c.isValid()) {
                        count++;
                        continue;
                    }

                    // if combination has not been used
                    if (usedCombinations.get(rid) == null) {
                        List<Integer> list = new ArrayList<Integer>();
                        list.add(tpid);
                        usedCombinations.put(rid, list);
                        break;
                    }
                    // also if combination has not been used, but rid key exists
                    else if (!usedCombinations.get(rid).contains(tpid)) {
                        List<Integer> list = usedCombinations.get(rid);
                        list.add(tpid);
                        usedCombinations.put(rid, list);
                        break;
                    }
                    // if combination has been used before, try again
                    else {
                        count++;
                        continue;
                    }
                }
            }
        } while (!s.isValid());

        return s;
    }

    // select a Schedule from the population using the tournament method
    private Schedule tournamentSelection() {

        int rn = random.nextInt(populationSize);
        Schedule schedule1 = population.get(rn);

        rn = random.nextInt(populationSize);
        Schedule schedule2 = population.get(rn);

        return (schedule1.getFitness() > schedule2.getFitness()) ? schedule1 : schedule2;
    }

    // select a Schedule from the top 50% of the population (sorted by fitness)
    private Schedule elitistSelection() {

        int cutoff = (int) (0.5 * populationSize);

        int rn = random.nextInt(cutoff) + cutoff;

        return population.get(rn);
    }

    // crossover 2 schedules and return one child
    private Schedule crossover(Schedule first, Schedule second) {
        Schedule child;

        int abort = 100; // decrement var for each crossover attempt made. If exceeding this amount,
        // crossover likely impossible, so abort

        int crossoverPoint;
        do {
            child = first.clone();
            crossoverPoint = random.nextInt(Course.count - 2) + 1; // cannot be 0 or courselist size
            for (int i = 0; i < Course.count; i++) {
                if (i < crossoverPoint)
                    child.courses.set(i, first.courses.get(i));
                else
                    child.courses.set(i, second.courses.get(i));
            }
            abort--;

        } while (abort > 0 && !child.isValid());
        if (abort == 0) {
            // if crossover fails, just return better child
            return (first.getFitness() > second.getFitness()) ? first : second;
        } else {
            return child;
        }
    }

    // return a clone of the provided schedule with 1 gene mutated
    // mutate the gene at the given index, or randomly if index == -1
    private Schedule mutate(Schedule toMutate) {
        Schedule s;

        int abort = 100; // try to mutate 100 times, if all fail, abort
        do {
            s = toMutate.clone();
            int rn = random.nextInt(Course.count);

            // mutate s.courses[index]
            // pick random room and time to mutate to
            int rid = random.nextInt(Room.count);
            int tpid = random.nextInt(TimePeriod.count);

            // make mutations
            Course oldCourse = s.courses.get(rn);
            Course newCourse = new Course(oldCourse.ID, oldCourse.name, oldCourse.professorID, oldCourse.size,
                    oldCourse.multimedia);
            newCourse.rid = rid;
            newCourse.tpid = tpid;

            s.courses.set(rn, newCourse);

            abort--;

            // check if the resulting schedule is valid, if so, return it, else try again
        } while (!s.isValid() && abort > 0);

        if (abort == 0)
            return toMutate.clone();
        else
            return s.clone();
    }

    private void generateNewPopulation() {

        List<Schedule> newPopulation = new ArrayList<Schedule>();

        // population must be sorted for elitist selection
        // doing it here ensures sort only happens once, instead of many many times
        // huge performance increase
        if (selectionMethod == 2)
            population.sort((o1, o2) -> o1.getFitness() - o2.getFitness());

        // while new population is not yet completed
        while (newPopulation.size() < populationSize) {
            Schedule parent1 = null;
            Schedule parent2 = null;
            Schedule child = null;

            // select parents
            if (selectionMethod == 2) {
                parent1 = elitistSelection();
                parent2 = elitistSelection();
            } else {
                parent1 = tournamentSelection();
                parent2 = tournamentSelection();
            }

            // set child equal to one of the parents, in case no crossover happens
            // since parent1 and parent2 are already random schedules, just use the first
            // one
            child = parent1;

            // if crossover chance hits,
            // perform crossover on the child and its parent that it isnt a clone of
            double rn = random.nextDouble();
            if (rn < crossoverProbability) {
                child = crossover(parent1, parent2);
            }

            // if mutation chance hits
            // mutate the child
            double rn2 = random.nextDouble();
            if (rn2 < mutationProbability) {
                child = mutate(child);
            }

            child.generation = generation;

            newPopulation.add(child);
        }

        // apply new population
        population.clear();
        population = newPopulation;
    }

    // if a new schedule is found with the same fitness, keep the first one.
    private void findBestSchedule() {
        for (Schedule s : population) {
            if (s.getFitness() > bestFitness) {
                bestSchedule = s.clone();
                bestFitness = bestSchedule.getFitness();
                System.out.println("New best found in generation " + generation + " with fitness: " + bestFitness);
                // bestSchedule.print();
            }
        }
    }

    // calculate average fitness for the current population and write it to a file
    // (with generation)
    private void recordFitness() {
        int sum, min, max;
        double avg;

        int fitness = population.get(0).getFitness();
        min = fitness;
        max = fitness;
        sum = fitness;

        for (int i = 0; i < populationSize; i++) {
            fitness = population.get(i).getFitness();
            sum += fitness;

            if (fitness < min)
                min = fitness;
            else if (fitness > max)
                max = fitness;
        }

        avg = (double) sum / (double) populationSize;

        try {
            FileWriter writer = new FileWriter("data.csv", true);

            // data in csv form like gen,min,avg,max
            // where {gen,min,max} are ints
            // and {avg} is a float with 3 decimal places and 2 digits before decimal
            // (including leading 0 if necessary)
            // ex. 0,6,09.600,13
            // ex. 0,7,10.300,15
            String data = String.format("%d,%d,%06.3f,%d\n", generation, min, avg, max);

            writer.write(data);

            writer.close();
        } catch (IOException e) {
            System.out.println("ERROR WRITING FITNESS VALUES TO FILE");
            e.printStackTrace();
            System.exit(1);
        }
    }

    // print each schedule in the population
    @SuppressWarnings("unused")
    private void printPopulation() {
        for (Schedule s : population) {
            s.print();
            System.out.println("");
        }
    }

    // runs the genetic algorithm
    public void run() {
        generateStartPopulation();
        findBestSchedule();
        // printPopulation();
        recordFitness();

        generation = 1;
        while (generation < maxGenerations) {
            System.out.println("generation: " + generation);
            generateNewPopulation();
            findBestSchedule();
            recordFitness();
            generation++;
        }

        System.out.println("************* BEST *************");
        bestSchedule.testing = true;
        bestSchedule.print();
    }

}