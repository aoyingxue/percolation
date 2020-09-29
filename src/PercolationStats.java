import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private double[] trialResults;
    private int trialCount;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        validate(n,trials);

        trialCount=trials;
        int gridSize=n;
        trialResults=new double[trialCount];

        // n times of trials
        for (int trial=0;trial<trialCount;trial++){
            Percolation p=new Percolation(gridSize);
            // repeat until the system percolates
            while(!p.percolates()){
                // choose the site uniformly at random among all sites
                int row=StdRandom.uniform(1,gridSize+1);
                int col=StdRandom.uniform(1,gridSize+1);
                // open the site
                p.open(row,col);

            }
            int openSites=p.numberOfOpenSites();
            //fraction of sites that are opened
            double result=openSites/(gridSize*gridSize);
            trialResults[trial]=result;
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(trialResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(trialResults);
    }


    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean()-((1.96*stddev())/Math.sqrt(trialCount));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean()+((1.96*stddev())/Math.sqrt(trialCount));
    }

    // test client (see below)
    public static void main(String[] args){
        // performs T independent computational experiments on an n by n grid
        int gridSize=10;
        int trialCount=10;
        if (args.length>=2){
            gridSize=Integer.parseInt(args[0]);
            trialCount=Integer.parseInt(args[1]);

        }
        PercolationStats ps=new PercolationStats(gridSize,trialCount);

        // prints the sample mean, sample standard deviation, 95% confidence interval
        String confidence=ps.confidenceLo()+", "+ps.confidenceHi();
        StdOut.println("mean                    = "+ps.mean());
        StdOut.println("stddev                  = "+ps.stddev());
        StdOut.println("95% confidence interval = ["+confidence+"]");
    }

    private void validate(int n,int trials){
        if (n<=0 || trials<=0){
            throw new IllegalArgumentException("N and Trials must be above 0.");
        }
    }
}
