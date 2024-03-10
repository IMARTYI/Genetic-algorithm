import java.util.Arrays;
import java.util.Random;

public class chromosome {

    String validCharacters = "abcdefghijklmnopqrstuvwxyz-"; // each character for the chromosome
    int chromosome_length;
    char [] chromosome;
    double fitness;
    Random random = new Random();


    public chromosome(){

       // chromosome = new char[chromosome_length];
    }

    public chromosome(double fitness, int chromosome_length, char [] chromosome){

        this. fitness = fitness;
        this.chromosome_length = chromosome_length;
        this.chromosome= chromosome;

    }

    public chromosome(int chromosome_length) {

        this.chromosome_length = chromosome_length;
        generateChromosome(chromosome_length);

    }

     void generateChromosome(int size){

        chromosome = new char[size];

        for( int i =0 ; i<size;i++){
             int rand_index = random.nextInt(validCharacters.length());
             this.chromosome[i]=validCharacters.charAt(rand_index);
        }
     }
    /**
     *  Function to display the contents of the chromosome
     */
    public void printChromosome(int length){

        for(int i=0; i< length;i++){
            System.out.print(this.chromosome[i]);
        }
     }

    /**
     * Setter for the fitness level
     */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /**
     * Getter to get access to the chromosome
     */
    public char[] getChromosome() {
        return this.chromosome;
    }

    @Override
    public String toString() {
        return new String(chromosome);
    }


    public void setChromosome(char[] chromosome) {
        this.chromosome = chromosome;
    }

    public int getChromosome_length(){
        return this.chromosome_length;
    }


    /**
     * This function will be an inversion mutation method
     */
    public char[] mutate(double mutation_rate, Random rand){

        double rand_val = rand.nextDouble();

        if(rand_val <= mutation_rate){

            int start_index = random.nextInt(chromosome_length);
            int end_index = random.nextInt(chromosome_length);

            if(start_index > end_index){ // Ensure that the start index of inversion is less than the end index

                int temp = start_index;
                start_index = end_index;
                end_index = temp;

            }

            while(start_index < end_index){ // Invert each gene between the start index and end index
                char temp = chromosome[start_index];
                chromosome[start_index] = chromosome[end_index];
                chromosome[end_index] = temp;
                start_index++;
                end_index--;

            }

        }else{

            return this.chromosome;

        }

        return this.chromosome;
    }

    /**
     * Getter for the fitness
     */
    public double getFitness() {
        return this.fitness;
    }
}
