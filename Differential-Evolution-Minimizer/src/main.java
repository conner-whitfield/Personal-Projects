import java.util.*;

//'main' used to handle menus, user input, and instantiating 'de' and 'data' objects for algorithm runs.

public class main
{
    public static void main(String[] args)
    {
        Scanner KEYBOARD = new Scanner(System.in);
        boolean run = true;
        while(run)
        {
            int choice = -1;
            ArrayList<Double> boundlist = new ArrayList<Double>();
            boundlist.clear();
            int testcases = -1;
            int iterations = -1;
            int nochanges = -1;
            while (choice < 1 || choice > 6)
            {
                System.out.println("\n========== MAIN MENU ==========" +
                        "\nSelect a function to minimize with DE:" +
                        "\n1    f(x) = x^(2) + 4" +
                        "\n2    f(x) = 3x^(2) - 5x + 9" +
                        "\n3    f(x) = sin(3 + x^(3))" +
                        "\n4    Beale Function: f(x,y) = (1.5 - x + xy)^(2) + (2.25 - x + xy^(2))^(2) + (2.625 - x + xy^(3))^(2)" +
                        "\n5    Booth Function: f(x,y) = (x + 2y - 7)^(2) + (2x + y - 5)^(2)" +
                        "\n6    QUIT" +
                        "\nPlease enter one of the following values: 1  2  3  4  5  6");
                try
                {
                    choice = KEYBOARD.nextInt();
                }
                catch(InputMismatchException e)
                {
                    KEYBOARD.next();
                    System.out.println("\nBad input, please enter an integer from the list 1, 2, 3, 4, 5, 6");
                }
            }
            if(choice == 6)
            {
                run = false;
                break;
            }
            ArrayList<String> parameters = new ArrayList<String>();
            if(choice >= 1 && choice <= 3)
            {
                parameters.add("X");
            }
            else if(choice >= 4 && choice <= 5)
            {
                parameters.add("X");
                parameters.add("Y");
            }
            for(int index = 0; index < parameters.size(); index++)
            {
                boolean wrong = true;
                while(wrong)
                {
                    try
                    {
                        System.out.println("\nEnter bounds for each parameter as instructed." +
                                "\nBounds of each parameter will not be exceeded when looking for minimum" +
                                "\nPlease enter an upper bound for parameter " + parameters.get(index));
                        double upper = KEYBOARD.nextDouble();
                        System.out.println("\nPlease enter a lower bound for parameter " + parameters.get(index));
                        double lower = KEYBOARD.nextDouble();
                        if (upper > lower)
                        {
                            boundlist.add(upper);
                            boundlist.add(lower);
                            wrong = false;
                        }
                        else {
                            System.out.println("Please enter proper values for upper and lower bounds." +
                                    "\nUpper bound must be greater than lower bound, and they can be doubles.");
                        }
                    }
                    catch(InputMismatchException e)
                    {
                        KEYBOARD.next();
                        System.out.println("\nBad input. Upper bound must be greater than lower bound, and both bound values must be numbers.");
                    }
                }
            }
            while (iterations < 50)
            {
                System.out.println("Please enter a number of iterations for DE." +
                        "\nDE will hard cutoff after this many iterations and display results." +
                        "\nPlease enter a value greater than 0. Minimum value: 50");
                try
                {
                    iterations = KEYBOARD.nextInt();
                }
                catch(InputMismatchException e)
                {
                    KEYBOARD.next();
                    System.out.println("\nBad input, please enter an integer greater than 50");
                }
            }
            while (nochanges >= iterations || nochanges < 0)
            {
                int approx = (int) Math.round(.4 * iterations);
                System.out.println("Please enter a whole number for number of iterations to assume convergence." +
                        "\nThis means that if this many DE iterations occur in a row and no table changes occur," +
                        "\nthe table will be considered converged and results will be displayed." +
                        "\nRecommended minimum value: " + approx);
                try {
                    nochanges = KEYBOARD.nextInt();
                }
                catch(InputMismatchException e)
                {
                    KEYBOARD.next();
                    System.out.println("\nBad input, please enter an integer greater than 0 and less than " + iterations);
                }
            }
            while (testcases < 50)
            {
                System.out.println("Please enter a number of test cases (AKA entries or population size) to use." +
                        "\nMinimum value: 50");
                try
                {
                    testcases = KEYBOARD.nextInt();
                }
                catch(InputMismatchException e)
                {
                    KEYBOARD.next();
                    System.out.println("\nBad input, please enter an integer greater than 50");
                }
            }

            // all parameters have been acquired, initialize the data set then run differential evolution to print output results
            data set = new data(testcases, parameters.size(), boundlist);
            de algorithm = new de(iterations, nochanges, 1, set, choice);
            /*TODO: define distance value, in order to eliminate duplicate parameter approximations
                and acquire more specific parameter values.a
            */
        }
    }

}
