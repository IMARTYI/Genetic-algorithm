
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class main {

    Evaluation e = new Evaluation(); // Evaulation object
    chromosome c = new chromosome();
    Random rand;
    int POPULATION_SIZE;
    chromosome [] CHROMOSOME_POPULATION;
    Scanner reader = null;
    String INPUT_TEXT;
    int CHROMOSOME_LENGTH;
    chromosome [] parents;
    chromosome [] next_generation;
    String data_1;
    String data_2;
    String csv_fileName  = "Results.csv";


    public main(){

        POPULATION_SIZE = 500; // This variable will act as a parameter to change the population size
        CHROMOSOME_POPULATION = new chromosome[POPULATION_SIZE]; // Initialize population array
        data_1 = readFile("src/Data1.txt"); // data_1 contains each piece of Data


        /**
         *  This main for loop runs the function Run GA for five different number seeds.
         *   Change parameters when  running the run GA function below
         */
        for( int seed = 0;seed<5;seed++){ // Generate Five Different Number Seeds
            System.out.println("Seed:" + seed);
            rand = new Random(seed);
            runGA(.20,1,5,50,POPULATION_SIZE,3,data_1,seed); // Change parameters for GA here

        }
    }

    /**
     * Main Function to be called for the entirety of the Genetic Algorithm
     */
    public void runGA(double mutation_rate,double crossover_rate, int k,int maxGA, int populationSize,int numbeOfelites,String data,int seed){

        double average_fitness=0;
        double best_fitness=0;


        String [] headers = {"Generation","AverageFitness","BestFitness"};

        String [][] output = new String[maxGA][headers.length];


        generatePopulation(CHROMOSOME_POPULATION,POPULATION_SIZE); // Generate the population
        evaluateFitness(CHROMOSOME_POPULATION,data);


        for(int i=0; i<maxGA;i++){

            System.out.println("Generation:" + i);


            average_fitness = compute_Average(CHROMOSOME_POPULATION); // Variable to hold the average of each Generation
            best_fitness = getBestChromosome(CHROMOSOME_POPULATION).getFitness(); // Variable to hold the strongest Generation

            System.out.println("Average fitness Level: "+average_fitness);
            System.out.println(Arrays.toString(getBestChromosome(CHROMOSOME_POPULATION).getChromosome()) + "Fitness:" + best_fitness);


            parents = tournamentSelection(k,CHROMOSOME_POPULATION,data);
            next_generation =TwoPointCrossover(crossover_rate,parents);// Can change the Crossover method here

            for( int z=0; z<POPULATION_SIZE;z++){ // Apply Mutation
                next_generation[z].mutate(mutation_rate,rand);

            }


            Elieteism(CHROMOSOME_POPULATION,numbeOfelites); // ensure elietesm
            evaluateFitness(next_generation,data); //evaluate fitness
            CHROMOSOME_POPULATION = next_generation; // reassign array to continue loop

            output[i][0] = String.valueOf(i); // Display Number of generations
            output[i][1] = String.valueOf(average_fitness);
            output[i][2] = String.valueOf(best_fitness);

        }

        String file_name = "Results" + seed + ".csv";

        writeTOCSV(file_name,headers,output);
    }

    /**
     * Function to write to csv file
     */
    public void writeTOCSV(String fileName, String [] headers, String[][] data){

        try(FileWriter writer = new FileWriter(fileName)){

            for(int i=0;i<headers.length;i++){ // Write CSV headers

                writer.append(headers[i]);
                if(i<headers.length -1){
                    writer.append(",");

                }else{
                    writer.append('\n');
                }

            }

           for(String[] row : data){
               for(int i=0; i< row.length;i++){
                   writer.append(row[i]);
                   if(i<row.length-1){
                       writer.append(",");
                   }else{
                       writer.append("\n");

                   }
               }

           }

        } catch (IOException ex) {

            System.out.println("Error");
            ex.printStackTrace();
        }
    }
    /**
     * Function to implement eliteism
     */
    public void Elieteism(chromosome [] arr,int numb){

        Arrays.sort(arr,(a,b) -> Double.compare(a.getFitness(),b.getFitness()));

        System.arraycopy(arr,0,next_generation,0,numb); //Copy the best Chromosome into the next genration.
    }


    /**
     * Function to take in the best chromosome array and find the best fitness chromsome.
     */
    public chromosome getBestChromosome(chromosome [] arr){

        chromosome best_chromosome = arr[0];

        for(int i =0; i<arr.length;i++){

            if(arr[i].getFitness()< best_chromosome.getFitness()){
                best_chromosome = arr[i];

            }
        }
        return  best_chromosome;
    }
    /**
     * Function to take in the array and compute the average for each generation
     */
    public double compute_Average(chromosome arr[]){

        double avg_fitness  = 0;

        double sum=0;


        for(int i=0; i< arr.length;i++){

            sum+= arr[i].getFitness();

        }
        avg_fitness = sum/ arr.length;

        return avg_fitness;
    }

    public void evaluateFitness(chromosome[] arr,String data){

        for(int i=0; i< arr.length;i++){

            arr[i].setFitness(Evaluation.fitness(String.valueOf(arr[i].getChromosome()),data));

        }
    }
    /**
     * This function will be used to generate an intial population size of 50 chromosomes
     */
    public void generatePopulation (chromosome[] arr,int size){
        for (int i=0; i<size;i++){
            chromosome c = new chromosome();
            c.chromosome_length= CHROMOSOME_LENGTH;
            c.generateChromosome(CHROMOSOME_LENGTH);
            arr [i] = c;
        }
    }

    /**
     * @param k the size of the tournament
     * @param arr the input chromosome population
     */
    public chromosome[] tournamentSelection(int k, chromosome[] arr, String data){

        chromosome [] result = new chromosome[POPULATION_SIZE];

        for (int i= 0; i< arr.length;i++){

            chromosome [] tournament = new chromosome[k]; // Create tournament size of K between 2 and 5.

            for(int j=0; j<k;j++){  // Randomly pick from the generation

                tournament[j] = arr[rand.nextInt(POPULATION_SIZE)];

            }

            for(int c =0; c< tournament.length;c++){ //Assign Fitness level to each chromosome

                tournament[c].setFitness(Evaluation.fitness(String.valueOf(tournament[c]),data));

                //System.out.println("Chromosome: " + Arrays.toString(tournament[i].getChromosome()) + "Fitness level: " + tournament[i].getFitness());
            }

            chromosome best_fitness = tournament[0];

            for(int b =0; b < tournament.length;b++){ // Find the best fitness level out of each of the
                if(tournament[b].getFitness()<best_fitness.getFitness()) {
                    best_fitness = tournament[b];
                }
            }
            result[i] = new chromosome(best_fitness.fitness,CHROMOSOME_LENGTH, best_fitness.chromosome);
        }

        return result;
    }

    /**
     * Function to implement Uniform crossover
     */
    public chromosome[] uniformCrossover(double crossoverRate, chromosome[] arr){

        chromosome [] children = new chromosome[POPULATION_SIZE];// Array to return newest

        for(int i=0; i<arr.length-1;i+=2){

            chromosome parentOne = arr[i]; //set the first parent
            chromosome parentTwo = arr[i+1]; // set the second parent

            char [] p1 =  parentOne.getChromosome(); // get the character array
            char [] p2 =  parentTwo.getChromosome(); // get the character array

            char [] c1 = new char[CHROMOSOME_LENGTH]; // Create the child gene
            char [] c2 = new char[CHROMOSOME_LENGTH]; // Create the child gene


            chromosome childOne = new chromosome(CHROMOSOME_LENGTH);
            chromosome childTwo = new chromosome(CHROMOSOME_LENGTH);

            double rand_number = rand.nextDouble();

            if(rand_number <= crossoverRate){

                int [] mask = new int[CHROMOSOME_LENGTH];

                for(int j=0; j<CHROMOSOME_LENGTH;j++){ // Generate mask array of 0s and 1s

                    mask[j] = rand.nextInt(2);
                }


                for(int k=0; k < CHROMOSOME_LENGTH;k++){

                    if (mask[k]==1){ // If 1, child keeps traits from parent

                        c1[k] = p1[k];
                        c2[k] = p2[k];

                    }else{ // If 0, child swaps traits with parents

                        c1[k] = p2[k];
                        c2[k] = p1[k];
                    }
                }

                childOne.setChromosome(c1);
                childTwo.setChromosome(c2);

            }else{

                childOne.setChromosome(p1);
                childTwo.setChromosome(p2);

                children[i] = childOne;
                children[i+1] = childTwo;
            }

            children[i] = childOne;
            children[i+1] = childTwo;
        }

        return children;
    }


    /**
     * Function to implement Two Point Crossover
     */
    public chromosome[] TwoPointCrossover(double crossover_rate, chromosome[] arr ){

        chromosome [] children = new chromosome[POPULATION_SIZE];

        for(int i=0; i< arr.length-1;i+=2){

            chromosome parentOne =  arr[i];
            chromosome parentTwo = arr[i+1];

            char [] p1 = parentOne.getChromosome();
            char[]  p2 = parentTwo.getChromosome();

            char [] c1 = new char[CHROMOSOME_LENGTH];
            char [] c2 = new char[CHROMOSOME_LENGTH];

            chromosome childOne = new chromosome(CHROMOSOME_LENGTH);
            chromosome childTwo = new chromosome(CHROMOSOME_LENGTH);

            double rand_val = rand.nextDouble();

            if( rand_val <= crossover_rate){

                int pointOne = rand.nextInt(CHROMOSOME_LENGTH);
                int pointTwo = rand.nextInt(CHROMOSOME_LENGTH);


                if (pointOne>pointTwo){ // In the event point one is bigger than point two, swap

                    int temp = pointOne;
                    pointOne = pointTwo;
                    pointTwo = temp;

                }

                for( int j=0;j<CHROMOSOME_LENGTH;j++){

                    if(j>=pointOne &&j<= pointTwo){

                        c1[j]= p2[j];
                        c2[j] = p1[j];
                    }else{

                        c1[j] = p1[j];
                        c2[j] = p2[j];

                    }
                }

                childOne.setChromosome(c1);
                childTwo.setChromosome(c2);


            }else{

                childOne.setChromosome(p1);
                childOne.setChromosome(p2);

                children[i] = childOne;
                children[i+1] = childTwo;

            }

            children[i] = childOne;
            children[i+1] =childTwo;

        }

        return children;
    }


    /**
     * Function to read the text file
     */
    public String readFile (String file_name){

        String INPUT_TEXT ="";

        try{
            this.reader = new Scanner(new FileInputStream(file_name));

        }catch (Exception e ){

            e.printStackTrace();
            System.out.println("File Not Found ! ");
        }

        CHROMOSOME_LENGTH = reader.nextInt(); //Assign length to the integer read in the txt file

        reader.nextLine();

        while(reader.hasNextLine()){

            INPUT_TEXT += reader.nextLine();

        }

        return INPUT_TEXT;
    }

    public static void main(String[] args) {
        new main();
    }
}
