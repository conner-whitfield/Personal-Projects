import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class data
{
    private int entries = 0;
    private int attributes = 0;
    private double[][] table;
    private double[][] ogtable;
    ArrayList<Double> boundlist;
    private Scanner KEYBOARD = new Scanner(System.in);

    public data(int ents, int attrs, ArrayList<Double> bounds)
    {
        this.entries = ents;
        this.attributes = attrs;
        this.table = new double[entries][attributes];
        this.boundlist = bounds;
        inittable(this.table, this.boundlist);
    }

    private void inittable(double[][] table, ArrayList<Double> bounds)
    {
        Random RNG = new Random();
        ArrayList<ArrayList> groupedbounds = new ArrayList<ArrayList>();
        ArrayList<Double> randomvector = new ArrayList<Double>();
        for(int row = 0; row < entries; row++)
        {
            for(int col = 0; col < attributes; col++)
            {
                this.table[row][col] = Double.MIN_VALUE;
            }
        }

        for(int i = 0; i < boundlist.size(); i += 2)
        {
            ArrayList<Double> tobeadded = new ArrayList<Double>();
            tobeadded.add(boundlist.get(i));
            tobeadded.add(boundlist.get(i + 1));
            groupedbounds.add(tobeadded);
        }
        for(int i = 0; i < entries; i++)
        {
            System.out.println("filling entry number " + i);
            boolean match = true;
            while(match)
            {
                for(int listindex = 0; listindex < groupedbounds.size(); listindex++)
                {
                    double upper = (double) groupedbounds.get(listindex).get(0);
                    double lower = (double) groupedbounds.get(listindex).get(1);
                    double randomvalue = lower + (upper - lower) * RNG.nextDouble();
                    //System.out.println("random value " + randomvalue);
                    randomvector.add(randomvalue);
                }
                int matchcount = 0;
                for(int row = 0; row < entries; row++)
                {
                    for (int col = 0; col < attributes; col++) {
                        if (this.table[row][col] == randomvector.get(col)) {
                            matchcount++;
                        }
                    }
                }
                if(matchcount == randomvector.size())
                {
                    match = true;
                    randomvector.clear();
                }
                else
                {
                    match = false;
                    for(int r = 0; r < entries; r++)
                    {
                        if(this.table[r][0] == Double.MIN_VALUE)
                        {
                            for(int c = 0; c < randomvector.size(); c++)
                            {
                                this.table[r][c] = randomvector.get(c);
                            }
                            r = entries;
                            randomvector.clear();
                        }
                    }
                }
            }
        }
        setTable(this.table);
    }

    public void printvalues()
    {
        for(int row = 0; row < entries; row++)
        {
            for(int col = 0; col < attributes; col++)
            {
                System.out.print(this.table[row][col] + "\t");
            }
            System.out.println();
        }
    }

    public double[][] getTable()
    {
        return this.table;
    }

    public void setTable(double[][] newtable)
    {
        this.table = newtable;
    }

    public ArrayList<Double> getBounds()
    {
        return this.boundlist;
    }
}
