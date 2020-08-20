package infs7410.week3;

import org.apache.commons.math3.stat.inference.TTest;

public class Week3 {
    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            throw new IndexOutOfBoundsException("expecting two arguments");
        }

        // Read the arguments from the command line.
        String filename1 = args[0];
        String filename2 = args[1];

        // Open the two files and extract the topics and measures from the eval files.
        TrecEvaluation file1 = new TrecEvaluation(filename1);
        TrecEvaluation file2 = new TrecEvaluation(filename2);

        // Create two arrays that contain the scores from each of the eval files for a particular measure.

        List<String> all_measures = file1.getMeasures();

        // Create a new TTest object to perform significance testing.
        TTest tTest = new TTest();
        double[] scores1 = 0;
        double[] scores2 = 0
        double pvalue = 0;

        for (int i = 0; i<= all_measures.length; i++){
            scores1 = file1.getScoresForMeasure(all_measures[i]);
            scores2 = file2.getScoresForMeasure(all_measures[i]);

            // Obtain the p-value for the two runs.
            // If you want to see all of the methods available, the Javadoc is located here:
            // https://commons.apache.org/proper/commons-math/javadocs/api-3.6/org/apache/commons/math3/stat/inference/TTest.html
            pvalue = tTest.pairedTTest(scores1, scores2);
            // Output the result so we can see it on the screen later.
            System.out.printf("p-value for files %s and %s given measure %s: %f\n", filename1, filename2, all_measures[i], pvalue);

        }








        /*
        TODO: for you to complete:
        Modify this file so that it can output the p-values for all of the measures in an eval file.

        HINT:
        Inspect the TrecEvaluation.java file. There is a method called getMeasures().
         */
    }
}
