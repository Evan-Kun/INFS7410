package infs7410.project1;

import infs7410.project1.configuration.PorterStemmerPipeline;
import infs7410.project1.configuration.TextProcessingPipeline;
import infs7410.project1.ranking.BM25;
import infs7410.project1.ranking.PseudoRelevanceBM25;
import infs7410.project1.ranking.CreatDataset;
import infs7410.project1.ranking.TermFrequency;
import infs7410.project1.ranking.DirichletLM;
import infs7410.project1.ranking.JelinekMercer;
import infs7410.project1.ranking.TF_IDF;
import infs7410.project1.fusion.CombMNZ;
import infs7410.project1.fusion.CombSUM;
import infs7410.project1.fusion.Borda;
import infs7410.project1.fusion.Fusion;
import infs7410.project1.normalisation.MinMax;
import infs7410.project1.normalisation.Normaliser;



import org.apache.log4j.Logger;
import org.terrier.applications.batchquerying.TRECQuerying;
import org.terrier.indexing.Collection;
import org.terrier.indexing.TRECCollection;
import org.terrier.matching.models.WeightingModel;
import org.terrier.querying.IndexRef;
import org.terrier.structures.Index;
import org.terrier.structures.IndexFactory;
import org.terrier.structures.indexing.Indexer;
import org.terrier.structures.indexing.classical.BasicIndexer;
import org.terrier.utility.ApplicationSetup;

import java.io.File;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.time.LocalDateTime;


public class Project1 {
    public static void main(String[] args) throws IOException {
        // ------------------------------------------------------------- //
        // The following code shows how you could use the code provided. //
        // It is not commented here because it should be fairly obvious. //
        // The classes themselves, however have detailed comments which  //
        // should provide some insights into the inner workings of what  //
        // is going on.                                                  //
        // Feel free to edit these classes to complete this project.     //
        // You are not marked on the beauty of your code, just that it   //
        // produces the expected results in a reasonable amount of time! //
        // ------------------------------------------------------------- //
        Logger logger = Logger.getLogger(infs7410.project1.Project1.class);

        final Runtime runtime = Runtime.getRuntime();
        logger.info(String.format("running with %.1fGB allocated memory", runtime.maxMemory() * 1e-9));

        TextProcessingPipeline conf = new PorterStemmerPipeline();
        conf.Configure();

        /*
        TODO: Index the collection. Note that you can do this programmatically or via the command-line.
        Refer to the tutorials for how to do this.
        */
        // Your implementation here.
        new File("./var/index").mkdirs();

        String path = Paths.get("./var/index", conf.index()).toString();
        if (!new File(path + ".properties").exists()) {
            logger.info(String.format("creating index at %s", path));
            TRECCollection collection = new TRECCollection("DOC.txt");
            Indexer indexer = new BasicIndexer("./var/index", conf.index());
            indexer.index(new Collection[]{collection});
            logger.info(String.format("index created at %s.properties", path));
            Index index = IndexFactory.of(IndexRef.of(path + ".properties"));
            // Index index = Index.createIndex("./var/index", "MyIndex");
            logger.info("We have indexed " + index.getCollectionStatistics().getNumberOfDocuments() + " documents");
        } else {
            logger.info("skipping index construction since this index already exists");
        }

        /*
        TODO: Read in queries for batch-style retrieval.
         */
        // Your implementation here.

        /*
        TODO: Perform batch-style retrieval, and rank the documents for each query.
         */
        // Your implementation here.

        /*
        TODO: Create a run file for the weighting model.
         */
        // Your implementation here.

// Intialize the list of filenames of results.
        List<String> resultFilenames = new ArrayList<>();

        List<WeightingModel> weightingModelList = new ArrayList<>();
        //WeightingModel weightingModel1 = new TF_IDF();
        //weightingModelList.add(weightingModel1);
        //WeightingModel weightingModel2 = new BM25();
        //weightingModelList.add(weightingModel2);
        WeightingModel weightingModel3 = new CreatDataset();
        weightingModelList.add(weightingModel3);
        //WeightingModel weightingModel4 = new JelinekMercer();
       // weightingModelList.add(weightingModel4);
        //WeightingModel weightingModel5 = new DirichletLM();
        //weightingModelList.add(weightingModel5);
        //WeightingModel weightingModel = new BM25();
        //weightingModelList.add(weightingModel);
        for (WeightingModel weightingModel : weightingModelList) {
            ApplicationSetup.setProperty("trec.model", weightingModel.getClass().getName());
            ApplicationSetup.setProperty("trec.results.file", weightingModel.getClass().getSimpleName() + ".res");
            // TrecResults results = new TrecResults();
            // results.setRunName("example");
            // results.write("example.res");
            TRECQuerying querying = new TRECQuerying(IndexRef.of(path + ".properties"));
            LocalDateTime currentTime = LocalDateTime.now();
            logger.info(weightingModel.getClass().getSimpleName() + "started at" + currentTime);
            querying.intialise();
            querying.processQueries();
            resultFilenames.add("./var/results/" + weightingModel.getClass().getSimpleName() + ".res");
            currentTime = LocalDateTime.now();
            logger.info(weightingModel.getClass().getSimpleName() + "finished at" + currentTime);
        }

		/*

        // Parse each of the results files and add them to a list.
        List<TrecResults> results = new ArrayList<>(resultFilenames.size());
        for (String filename : resultFilenames) {
            results.add(new TrecResults(filename));
        }



        // Set the normaliser and fusion method to use.
		Normaliser norm = new MinMax();

        List<Fusion> fusionList = new ArrayList<>();
        Fusion fusion1 = new CombSUM();
        fusionList.add(fusion1);
        Fusion fusion2 = new CombMNZ();
        fusionList.add(fusion2);
        Fusion fusion3 = new Borda();
        fusionList.add(fusion3);


        // The name of the file to oup
        List<String> outputFilenames = new ArrayList<>();
        outputFilenames.add("./var/results/CombSUM.res");
        outputFilenames.add("./var/results/CombMNZ.res");
        outputFilenames.add("./var/results/Borda.res");


        int fusionResultsIndex = 0;
        // The name of the file to oup
        for (Fusion fusion : fusionList) {

            fusion.Fuse(results);

            // Normalise the scores of each run.
            for (TrecResults trecResults : results) {
                norm.init(trecResults);
                for (int j = 0; j < trecResults.getTrecResults().size(); j++) {
                    double normScore = norm.normalise(trecResults.getTrecResults().get(j));
                    trecResults.getTrecResults().get(j).setScore(normScore);
                }
            }

            logger.info("fusing results for each topic");

            Set<String> topics = results.get(0).getTopics();
            TrecResults fusedResults = new TrecResults();
            for (String topic : topics) {
                logger.info(topic);
                List<TrecResults> topicResults = new ArrayList<>();
                for (TrecResults r : results) {
                    topicResults.add(new TrecResults(r.getTrecResults(topic)));
                }

                // Fuse the results together and write the new results list to disk.
                fusedResults.getTrecResults().addAll(fusion.Fuse(topicResults).getTrecResults());
            }

            logger.info("writing results to disk");

            fusedResults.setRunName("fused");
            fusedResults.write(outputFilenames.get(fusionResultsIndex));
            fusionResultsIndex = fusionResultsIndex + 1;

        }
        */
		
		/*
        // Create 2 file to output the features of both Dev and Test set
		logger.info("Creating output files");
		try {
		  //File devSetFeatures = new File("devSetFeatures.txt");
		  //File testSetFeatures = new File("testSetFeatures.txt");
		  File smallTrainSetFeatures = new File("smallTrainSetFeatures.txt");
		  if (smallTrainSetFeatures.createNewFile()) {
			//logger.info("File created: " + devSetFeatures.getName());
			//logger.info("File created: " + testSetFeatures.getName());
			logger.info("File created: " + smallTrainSetFeatures.getName());
		  } else {
			logger.info("File already exists.");
		  }
		} catch (IOException e) {
		  logger.info("An error occurred.");
		}
		
		//File[] files = new File("./var/results/devSet").listFiles();
		
		//Reading queryFiles
		logger.info("Buffer reading query files");
		//BufferedReader buffReadQueryDevSet = new BufferedReader(new FileReader("./query_qrels/query_qrels/dev_set.txt"));
		//BufferedReader buffReadQueryTestSet = new BufferedReader(new FileReader("./query_qrels/query_qrels/test_set.txt"));
        BufferedReader buffReadQuerySmallSet = new BufferedReader(new FileReader("./query_qrels/query_qrels/train_set.small.txt"));
		//Reading result files for Dev Set
		logger.info("Buffer reading small set result files");
		BufferedReader buffReadSmallTFIDF = new BufferedReader(new FileReader("./var/results/SmallTrainSet/TF_IDF.res"));
		BufferedReader buffReadSmallBM25 = new BufferedReader(new FileReader("./var/results/SmallTrainSet/BM25.res"));
		BufferedReader buffReadSmallPseudoRelevanceBM25 = new BufferedReader(new FileReader("./var/results/SmallTrainSet/PseudoRelevanceBM25.res"));
		BufferedReader buffReadSmallJelinekMercer = new BufferedReader(new FileReader("./var/results/SmallTrainSet/JelinekMercer.res"));
		BufferedReader buffReadSmallDirichletLM = new BufferedReader(new FileReader("./var/results/SmallTrainSet/DirichletLM.res"));
		
		logger.info("Start Reading Line of both result and query file");
		String contentLineQuerySmallSet = buffReadQuerySmallSet.readLine();
		
		String contentLinebuffReadSmallTFIDF = buffReadSmallTFIDF.readLine();
		String contentLinebuffReadSmallBM25 = buffReadSmallBM25.readLine();
		String contentLinebuffReadSmallPseudoRelevanceBM25 = buffReadSmallPseudoRelevanceBM25.readLine();
		String contentLinebuffReadSmallJelinekMercer = buffReadSmallJelinekMercer.readLine();
		String contentLinebuffReadSmallDirichletLM = buffReadSmallDirichletLM.readLine();
		
		FileWriter myWriter = new FileWriter("smallTrainSetFeatures.txt");
		
		String queryID = "";
		String scoreTFIDF = "0";
		String scoreBM25 = "0";
		String scorePseudoRelevance = "0";
		String scoreJelinekMercer = "0";
		String scoreDirichletLM = "0";
		String docID = "";
		int currentQueryLength;
		
		while (contentLineQuerySmallSet != null) {			
			String[] splitedQuerySmallSet = contentLineQuerySmallSet.split("\\s");
			
			currentQueryLength = splitedQuerySmallSet.length - 1;
			
			while (contentLinebuffReadSmallBM25 != null) {
				String[] splitedQuerySmallSetBM25res = contentLinebuffReadSmallBM25.split("\\s");
				
				if (splitedQuerySmallSet[0].equals(splitedQuerySmallSetBM25res[0])) {
					queryID = splitedQuerySmallSetBM25res[0];
					docID = splitedQuerySmallSetBM25res[2];
					scoreBM25 = splitedQuerySmallSetBM25res[4];					
					
					while (contentLinebuffReadSmallTFIDF != null) {
					String[] splitedQuerySmallSetTFIDFres = contentLinebuffReadSmallTFIDF.split("\\s");
					if (splitedQuerySmallSetBM25res[0].equals(splitedQuerySmallSetTFIDFres[0])) {
						if (splitedQuerySmallSetBM25res[2].equals(splitedQuerySmallSetTFIDFres[2])) {
							scoreTFIDF = splitedQuerySmallSetTFIDFres[4];
							break;
						}
					}

					contentLinebuffReadSmallTFIDF = buffReadSmallTFIDF.readLine();
					}
					buffReadSmallTFIDF = new BufferedReader(new FileReader("./var/results/SmallTrainSet/TF_IDF.res"));
					contentLinebuffReadSmallTFIDF = buffReadSmallTFIDF.readLine();
					
					while (contentLinebuffReadSmallPseudoRelevanceBM25 != null) {
						String[] splitedQuerySmallPseudoRelevanceres = contentLinebuffReadSmallPseudoRelevanceBM25.split("\\s");
						if (splitedQuerySmallSetBM25res[0].equals(splitedQuerySmallPseudoRelevanceres[0])) {
							if (splitedQuerySmallSetBM25res[2].equals(splitedQuerySmallPseudoRelevanceres[2])) {
								scorePseudoRelevance = splitedQuerySmallPseudoRelevanceres[4];
								break;
							}
						}
						contentLinebuffReadSmallPseudoRelevanceBM25 = buffReadSmallPseudoRelevanceBM25.readLine();
					}
					buffReadSmallPseudoRelevanceBM25 = new BufferedReader(new FileReader("./var/results/SmallTrainSet/PseudoRelevanceBM25.res"));
					contentLinebuffReadSmallPseudoRelevanceBM25 = buffReadSmallPseudoRelevanceBM25.readLine();
					
					while (contentLinebuffReadSmallJelinekMercer != null) {
						String[] splitedQuerySmallJelinekMercerres = contentLinebuffReadSmallJelinekMercer.split("\\s");
						if (splitedQuerySmallSetBM25res[0].equals(splitedQuerySmallJelinekMercerres[0])) {
							if (splitedQuerySmallSetBM25res[2].equals(splitedQuerySmallJelinekMercerres[2])) {
								scoreJelinekMercer = splitedQuerySmallJelinekMercerres[4];
								break;
							}
						}
						contentLinebuffReadSmallJelinekMercer = buffReadSmallJelinekMercer.readLine();
					}
					buffReadSmallJelinekMercer = new BufferedReader(new FileReader("./var/results/SmallTrainSet/JelinekMercer.res"));
					contentLinebuffReadSmallJelinekMercer = buffReadSmallJelinekMercer.readLine();
					
					while (contentLinebuffReadSmallDirichletLM != null) {
						String[] splitedQuerySmallDirichletLMres = contentLinebuffReadSmallDirichletLM.split("\\s");
						if (splitedQuerySmallSetBM25res[0].equals(splitedQuerySmallDirichletLMres[0])) {
							if (splitedQuerySmallSetBM25res[2].equals(splitedQuerySmallDirichletLMres[2])) {
								scoreDirichletLM = splitedQuerySmallDirichletLMres[4];
								break;
							}
						}
						contentLinebuffReadSmallDirichletLM = buffReadSmallDirichletLM.readLine();
					}
					buffReadSmallDirichletLM = new BufferedReader(new FileReader("./var/results/SmallTrainSet/DirichletLM.res"));
					contentLinebuffReadSmallDirichletLM = buffReadSmallDirichletLM.readLine();
					
					// qid then tfidf then bm25 then pseudo then jelinek then dirichlet then querylength then docid
					myWriter.write("1 qid:"+queryID+" 1:"+scoreTFIDF+" 2:"+scoreBM25+" 3:"+scorePseudoRelevance+" 4:"+scoreJelinekMercer+
					" 5:"+scoreDirichletLM+" 6:"+currentQueryLength+" #docid = "+docID+" \n");
					logger.info("Successfully wrote to the file.");
									
					queryID = "";
					scoreTFIDF = "0";
					scoreBM25 = "0";
					scorePseudoRelevance = "0";
					scoreJelinekMercer = "0";
					scoreDirichletLM = "0";
					docID = "";
				}
				contentLinebuffReadSmallBM25 = buffReadSmallBM25.readLine();
			}
			buffReadSmallBM25 = new BufferedReader(new FileReader("./var/results/SmallTrainSet/BM25.res"));
			contentLinebuffReadSmallBM25 = buffReadSmallBM25.readLine();
			contentLineQuerySmallSet = buffReadQuerySmallSet.readLine();
		}
        


		*/


    }
}
