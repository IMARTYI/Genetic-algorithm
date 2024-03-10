TO THE TA,


I did not provide a working jar file. But the source code is included and is runnable.


If you Cd into the directory with the java file "main" and run it, it should print the average of each generation with the best chromosome for that generation to the console.
All my Java Code is within the src file. This also contains the Data1.txt and the Data2.txt.There sould be the java files:

main.java
Chromosome.java
Evaluation.java




The code below is my main main constructor and you can change parameters in the runGA parametres when calling the function each time. 


for( int seed = 0;seed<5;seed++){ // Generate Five Different Number Seeds
            System.out.println("Seed:" + seed);
            rand = new Random(seed);
            runGA(.20,1,5,50,POPULATION_SIZE,3,data_1,seed); // Change parameters for GA here

        }




I did provide two Excel Files. Data_collection which contains my data from running data1.txt and Data_Collection2 which contains my data from running data2.txt]

Additonally, when the program runs, it should produce five csv files with the results. These results being, the average fitness level of each generation, as well as the chromosome
with the best fitness level. There should be five csv files. Each representing a different seed.


Additionally, I have included my excel spread sheets to display each run with each of the required parameters and each seed. These also include the t test tables.

All images and graphs should be on the report and on the excel files.

The Report pdf will also be Included.

Thank you.