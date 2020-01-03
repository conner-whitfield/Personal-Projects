import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class de
{
    private int iterations;
    private double beta;
    private Scanner KEYBOARD = new Scanner(System.in);
    private data set;
    private int selection;
    private String fx;
    private int cutoff;

    public de(int iters, int cut, double b, data s, int choice)
    {
        this.cutoff = cut;
        this.iterations = iters;
        this.beta = b;
        this.set = s;
        this.selection = choice;
        algorithm(this.iterations, this.cutoff);
    }

    public void algorithm(int hardstop, int cut)
    {
        Random RNG = new Random();
        int stop = 0;
        int beginconverge = 0;
        for(int i = 0; i < hardstop; i++)
        {
            boolean updatemade = false;
            System.out.println("DE iteration number " + i);
            double[][] temp = set.getTable();
            //for each pop member
            for(int popmembers = 0; popmembers < this.set.getTable().length; popmembers++)
            {
                double[] agent = fetchvector(popmembers);
                //select X other vectors
                ArrayList<Integer> selectedindexes = selectXvectors(popmembers, 3);
                //compute potential new position
                double[] newvector = calcpotential(selectedindexes, popmembers);
                //get fitness of this new vector and the agent (popmembers value)
                float newvectorfitness = getfitness(newvector, this.selection);
                //System.out.println("new vector fitness " + newvectorfitness);
                float agentfitness = getfitness(agent, this.selection);
                //System.out.println("agent vector fitness " + agentfitness);
                if(newvectorfitness < agentfitness)
                {
                    //System.out.println("     Replacing agent " + popmembers);
                    double[][] newtable = replaceagent(popmembers, newvector);
                    this.set.setTable(newtable);
                    //this.set.printvalues();
                    updatemade = true;
                }
            }
            if(updatemade)
            {
                stop = 0;
            }
            else
            {
                if(stop == 0) beginconverge = i;
                stop++;
                System.out.println("Iteration " + i + " made no changes. Stop value: " + stop + " of " + cut);
                if(stop == cut)
                {
                    System.out.println("Reached " + cut + " iterations without change, ending DE algorithm run." +
                            "\nBonus: This convergence began at DE iteration " + i);
                    i = hardstop;
                    break;
                }
            }
        }
        double mostfitvalue = Double.MAX_VALUE;
        double[] mostfitvector = new double[this.set.getTable()[0].length];
        ArrayList<double[]> vectors = new ArrayList<double[]>();
        for(int row = 0; row < this.set.getTable().length; row++)
        {
            double[] fetch = fetchvector(row);
            float fitness = (float) getfitness(fetch, this.selection);
            if(fitness < mostfitvalue)
            {
                vectors.clear();
                vectors.add(fetch);
                mostfitvalue = fitness;
                mostfitvector = fetch;
            }
            else if(fitness == mostfitvalue)
            {
                vectors.add(fetch);
                mostfitvalue = fitness;
                mostfitvector = fetch;
            }
        }
        System.out.println("========== CUT OFF REACHED ==========" +
                "\nRESULTS:" +
                "\n  -   Iterations: " + this.iterations +
                "\n  -   Entries: " + this.set.getTable().length +
                "\n  -   Num Parameters: " + this.set.getTable()[0].length +
                "\n  -   Selected function: " + this.fx);
        System.out.print(
                "\n  -   Minimized parameter lists beginning with X and ending with W (not rounded): ");
        for(int i = 0; i < vectors.size(); i++)
        {
            System.out.println("        -   " + Arrays.toString(vectors.get(i)));
        }
        System.out.print("\n  -   Bounds (one pair per parameter): " + this.set.getBounds().toString() +
                "\n  -   Minimized output (approx): " + mostfitvalue);
    }

    private double[] fetchvector(int row)
    {
        double[] fill = new double[this.set.getTable()[row].length];
        for(int i = 0; i < this.set.getTable()[row].length; i++)
        {
            fill[i] = this.set.getTable()[row][i];
        }
        return fill;
    }

    private ArrayList<Integer> selectXvectors(int current, int quantity)
    {
        Random RNG = new Random();
        ArrayList<Integer> selectedvectorindexes = new ArrayList<Integer>();
        while(selectedvectorindexes.size() < quantity)
        {
            int select = RNG.nextInt(set.getTable().length);
            if(select == current || selectedvectorindexes.contains(select))
            {

            }
            else
            {
                selectedvectorindexes.add(select);
            }
        }

        return selectedvectorindexes;
    }

    private double[] calcpotential(ArrayList<Integer> selectedindexes, int agent)
    {
        Random RNG = new Random();
        ArrayList<ArrayList> fullindexes = new ArrayList<ArrayList>();
        for(int i = 0; i < selectedindexes.size(); i++)
        {
            ArrayList<Double> tobeadded = new ArrayList<>();
            for(int j = 0; j < this.set.getTable()[selectedindexes.get(i)].length; j++)
            {
                tobeadded.add(this.set.getTable()[selectedindexes.get(i)][j]);
            }
            fullindexes.add(tobeadded);
        }
        ArrayList<Double> potentialvector = new ArrayList<Double>();
        int boundindex = 0;
        for(int index = 0; index < fullindexes.get(0).size(); index++)
        {
            if(RNG.nextBoolean()) //perform crossover on index
            {
                // y_i = a_i + F(b_i - c_i)
                double ai = (double) fullindexes.get(0).get(index);
                double bi = (double) fullindexes.get(1).get(index);
                double ci = (double) fullindexes.get(2).get(index);
                double value = ai + (this.beta * (bi - ci));
                if(value < this.set.getBounds().get(boundindex) && value > this.set.getBounds().get(boundindex + 1))
                {
                    potentialvector.add(value);
                }
                //TODO DONE?: IF VALUE IS IN RESPECTIVE BOUNDS, ADD
                else
                {
                    potentialvector.add(this.set.getTable()[agent][index]);
                }
                boundindex += 2;
            }
            else    // do not perform crossover on index, use index from agent
            {
                potentialvector.add(this.set.getTable()[agent][index]);
            }

        }
        double[] potential = new double[potentialvector.size()];
        for(int i = 0; i < potentialvector.size(); i++)
        {
            potential[i] = potentialvector.get(i);
        }
        return potential;
    }

    private float getfitness(double[] vector, int function)
    {
        double total = 0;
        if(function == 1)
        {
            //    f(x) = x^(2) + 4
            this.fx = "f(x) = x^(2) + 4";
            total = (vector[0] * vector[0]) + 4;
        }
        else if(function == 2)
        {
            //    f(x) = 3x^(2) - 5x + 9
            this.fx = "f(x) = 3x^(2) - 5x + 9";
            total = (3 * (vector[0] * vector[0])) - (5 * vector[0]) + 9;
        }
        else if(function == 3)
        {
            //    f(x) = sin(3 + x^(3))
            this.fx = "f(x) = sin(3 + x^(3))";
            total = Math.sin(3 + (vector[0] * vector[0] * vector[0]));
        }
        else if(function == 4)
        {
            //    Beale Function: f(x,y) = (1.5 - x + xy)^(2) + (2.25 - x + xy^(2))^(2) + (2.625 - x + xy^(3))^(2)
            this.fx = "Beale Function: f(x,y) = (1.5 - x + xy)^(2) + (2.25 - x + xy^(2))^(2) + (2.625 - x + xy^(3))^(2)";
            total = (1.5  - vector[0] + (vector[0] * vector[1])) * (1.5  - vector[0] + (vector[0] * vector[1]));
            total += ((2.25 - vector[0] + (vector[0] * vector[1] * vector[1])) * (2.25 - vector[0] + (vector[0] * vector[1] * vector[1])));
            total += (2.625 - vector[0] + (vector[0] * vector[1] * vector[1] * vector[1])) * (2.625 - vector[0] + (vector[0] * vector[1] * vector[1] * vector[1]));
        }
        else if(function == 5)
        {
            //    Booth Function: f(x,y) = (x + 2y - 7)^(2) + (2x + y - 5)^(2)
            this.fx = "Booth Function: f(x,y) = (x + 2y - 7)^(2) + (2x + y - 5)^(2)";
            total = (vector[0] + (2 * vector[1]) - 7) * (vector[0] + (2 * vector[1]) - 7);
            total += ((2 * vector[0]) + vector[1] - 5) * ((2 * vector[0]) + vector[1] - 5);
        }
        return (float) total;
    }

    private double[][] replaceagent(int agentrow, double[] newvector)
    {
        double[][] newtable = this.set.getTable();
        for(int col = 0; col < newtable[agentrow].length; col++)
        {
            newtable[agentrow][col] = newvector[col];
        }
        return newtable;
    }
}