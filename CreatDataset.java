package infs7410.project1.ranking;

import org.terrier.matching.models.WeightingModel;
import org.terrier.querying.*;
import org.terrier.structures.*;
import org.terrier.structures.postings.IterablePosting;
import org.terrier.terms.PorterStemmer;

import java.io.IOException;
import java.io.*;

public class CreatDataset extends WeightingModel {

    private double k1 = 0.6;
    private double b = 0.5;
    private int R = 5;
    private int ri = 0;
    private boolean newTerm = true;
    private static String currentQuery = "";
    private static int termPos = 0;
    private static Index index = IndexFactory.of(IndexRef.of("./var/index/porter.properties"));
    private static Manager queryManager = ManagerFactory.from(IndexRef.of("./var/index/porter.properties"));
    private static ScoredDocList resultsList;

    public CreatDataset() {
        super();
    }

    @Override
    public String getInfo() {
        return "PRBM25";
    }

    @Override
    public double score(double tf, double docLength) {

        //double scoreTFIDF = 0;
        //double scoreBM25 = 0;
        //double scoreJelinekMercer = 0;
        //double scoreDirichletLM  = 0;
        boolean scanedScoreTFIDF = false;
        boolean scanedScoreBM25 = false;
        boolean scanedScoreJelinekMercer = false;
        boolean scanedScoreDirichletLM = false;

        if (newTerm) {

            //System.out.println("currentQuery: " + currentQuery);
            //System.out.println("currentQueryId: " + rq.getQueryID());

            newTerm = false;
            if (currentQuery.equals(rq.getOriginalQuery())) {
                termPos++;
                System.out.println("termPos: " + termPos);
            } else {
                ri = 0;
                termPos = 0;
                currentQuery = rq.getOriginalQuery();
                //System.out.println("Get in else currentQuery: " + currentQuery);
                //currentQueryID = rq.getQueryID();
                System.out.println("currentQueryId: " + rq.getQueryID());
                SearchRequest srq = queryManager.newSearchRequestFromQuery(currentQuery);
                srq.setControl(SearchRequest.CONTROL_WMODEL, "BM25");
                queryManager.runSearchRequest(srq);
                resultsList = srq.getResults();
                //System.out.println("Finish BM25");
                System.out.println("resultsList: 0" +  resultsList.get(0).getDocid());
                System.out.println("resultsList: 0" +  resultsList.get(999).getDocid());
            }
            try {
                PorterStemmer porter = new PorterStemmer();
                Lexicon<String> lexicon = index.getLexicon();
                PostingIndex direct = index.getDirectIndex();
                DocumentIndex document = index.getDocumentIndex();
                for (int i = 0; i < resultsList.size() && i < R; i++) {
                    ScoredDoc doc = resultsList.get(i);

                    DocumentIndexEntry entry = document.getDocumentEntry(doc.getDocid());
                    IterablePosting ip = direct.getPostings(entry);

                    //int docID = doc.getDocid();
                    System.out.println("DocumentId: " + doc.getMetadata("docno"));
                    //System.out.println("DocumentIndexEntry: " + entry);
                    //System.out.println("IterablePosting: " + ip);


                    if (!scanedScoreTFIDF) {
                        BufferedReader buffReadSmallTFIDF = new BufferedReader(new FileReader("./var/results/SmallTrainSet/TF_IDF.res"));
                        String contentLinebuffReadSmallTFIDF = buffReadSmallTFIDF.readLine();

                        //System.out.println("contentLinebuffReadSmallTFIDF" + contentLinebuffReadSmallTFIDF);
                        while (contentLinebuffReadSmallTFIDF != null) {
                            String[] splitedQuerySmallSetTFIDFres = contentLinebuffReadSmallTFIDF.split("\\s");
                            //System.out.println("splitedQuerySmallSetTFIDFres" + splitedQuerySmallSetTFIDFres);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 0  " + splitedQuerySmallSetTFIDFres[0]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 1  " + splitedQuerySmallSetTFIDFres[1]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 2  " + splitedQuerySmallSetTFIDFres[2]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 3  " + splitedQuerySmallSetTFIDFres[3]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 4  " + splitedQuerySmallSetTFIDFres[4]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 5  " + splitedQuerySmallSetTFIDFres[5]);
                            if (rq.getQueryID().equals(splitedQuerySmallSetTFIDFres[0])) {
                                //System.out.println("Get in first if");
                                if (Integer.toString(doc.getDocid()).equals(splitedQuerySmallSetTFIDFres[2])) {
                                    double scoreTFIDF = Double.valueOf(splitedQuerySmallSetTFIDFres[4]);
                                    System.out.println("Changed scoreTFIDF ----------------------------" + scoreTFIDF);
                                    contentLinebuffReadSmallTFIDF = null;
                                }
                            }
                            contentLinebuffReadSmallTFIDF = buffReadSmallTFIDF.readLine();
                        }
                        scanedScoreTFIDF = true;
                    }
                    //System.out.println("scoreTFIDF: " + scoreTFIDF);


                    if (!scanedScoreBM25) {
                        BufferedReader buffReadSmallBM25 = new BufferedReader(new FileReader("./var/results/SmallTrainSet/BM25.res"));
                        String contentLinebuffReadSmallBM25 = buffReadSmallBM25.readLine();

                        //System.out.println("contentLinebuffReadSmallTFIDF" + contentLinebuffReadSmallTFIDF);
                        while (contentLinebuffReadSmallBM25 != null) {
                            String[] splitedQuerySmallSetBM25res = contentLinebuffReadSmallBM25.split("\\s");
                            //System.out.println("splitedQuerySmallSetTFIDFres" + splitedQuerySmallSetTFIDFres);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 0  " + splitedQuerySmallSetTFIDFres[0]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 1  " + splitedQuerySmallSetTFIDFres[1]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 2  " + splitedQuerySmallSetTFIDFres[2]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 3  " + splitedQuerySmallSetTFIDFres[3]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 4  " + splitedQuerySmallSetTFIDFres[4]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 5  " + splitedQuerySmallSetTFIDFres[5]);
                            if (rq.getQueryID().equals(splitedQuerySmallSetBM25res[0])) {
                                //System.out.println("Get in first if");
                                if (Integer.toString(doc.getDocid()).equals(splitedQuerySmallSetBM25res[2])) {
                                    double scoreBM25 = Double.valueOf(splitedQuerySmallSetBM25res[4]);
                                    System.out.println("Changed scoreBM25 ----------------------------" + scoreBM25);
                                    contentLinebuffReadSmallBM25 = null;
                                }
                            }
                            contentLinebuffReadSmallBM25 = buffReadSmallBM25.readLine();
                        }

                        scanedScoreBM25 =  true;
                    }
                    //System.out.println("scoreBM25: " + scoreBM25);


                    if (!scanedScoreJelinekMercer) {
                        BufferedReader buffReadSmallJelinekMercer = new BufferedReader(new FileReader("./var/results/SmallTrainSet/JelinekMercer.res"));
                        String contentLinebuffReadSmallJelinekMercer = buffReadSmallJelinekMercer.readLine();

                        //System.out.println("contentLinebuffReadSmallTFIDF" + contentLinebuffReadSmallTFIDF);
                        while (contentLinebuffReadSmallJelinekMercer != null) {
                            String[] splitedQuerySmallSetJelinekMercerres = contentLinebuffReadSmallJelinekMercer.split("\\s");
                            //System.out.println("splitedQuerySmallSetTFIDFres" + splitedQuerySmallSetTFIDFres);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 0  " + splitedQuerySmallSetTFIDFres[0]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 1  " + splitedQuerySmallSetTFIDFres[1]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 2  " + splitedQuerySmallSetTFIDFres[2]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 3  " + splitedQuerySmallSetTFIDFres[3]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 4  " + splitedQuerySmallSetTFIDFres[4]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 5  " + splitedQuerySmallSetTFIDFres[5]);
                            if (rq.getQueryID().equals(splitedQuerySmallSetJelinekMercerres[0])) {
                                //System.out.println("Get in first if");
                                if (Integer.toString(doc.getDocid()).equals(splitedQuerySmallSetJelinekMercerres[2])) {
                                    double scoreJelinekMercer = Double.valueOf(splitedQuerySmallSetJelinekMercerres[4]);
                                    System.out.println("Changed scoreJelinekMercer ----------------------------" + scoreJelinekMercer);
                                    contentLinebuffReadSmallJelinekMercer = null;
                                }
                            }
                            contentLinebuffReadSmallJelinekMercer = buffReadSmallJelinekMercer.readLine();
                        }
                        scanedScoreJelinekMercer =true;
                    }
                    //System.out.println("scoreJelinekMercer: " + scoreJelinekMercer);


                    if (!scanedScoreDirichletLM) {
                        BufferedReader buffReadSmallDirichletLM = new BufferedReader(new FileReader("./var/results/SmallTrainSet/DirichletLM.res"));
                        String contentLinebuffReadSmallDirichletLM = buffReadSmallDirichletLM.readLine();

                        //System.out.println("contentLinebuffReadSmallTFIDF" + contentLinebuffReadSmallTFIDF);
                        while (contentLinebuffReadSmallDirichletLM != null) {
                            String[] splitedQuerySmallSetDirichletLMres = contentLinebuffReadSmallDirichletLM.split("\\s");
                            //System.out.println("splitedQuerySmallSetTFIDFres" + splitedQuerySmallSetTFIDFres);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 0  " + splitedQuerySmallSetTFIDFres[0]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 1  " + splitedQuerySmallSetTFIDFres[1]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 2  " + splitedQuerySmallSetTFIDFres[2]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 3  " + splitedQuerySmallSetTFIDFres[3]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 4  " + splitedQuerySmallSetTFIDFres[4]);
                            //System.out.println("splitedQuerySmallSetTFIDFres: 5  " + splitedQuerySmallSetTFIDFres[5]);
                            if (rq.getQueryID().equals(splitedQuerySmallSetDirichletLMres[0])) {
                                //System.out.println("Get in first if");
                                if (Integer.toString(doc.getDocid()).equals(splitedQuerySmallSetDirichletLMres[2])) {
                                    double scoreDirichletLM = Double.valueOf(splitedQuerySmallSetDirichletLMres[4]);
                                    System.out.println("Changed scoreDirichletLM  ----------------------------" + scoreDirichletLM );
                                    contentLinebuffReadSmallDirichletLM = null;
                                }
                            }
                            contentLinebuffReadSmallDirichletLM = buffReadSmallDirichletLM.readLine();
                        }
                        scanedScoreDirichletLM = true;
                    }
                    //System.out.println("scoreDirichletLM : " + scoreDirichletLM );



                    while (ip.next() != IterablePosting.EOL) {
                        //System.out.println("Get in while loop: ");
                        if (lexicon.getLexiconEntry(ip.getId()).getKey().equals(porter.stem(currentQuery.split("\\s+")[termPos]))) {
                            ri++;
                            System.out.println("ri: " +  ri);
                            //System.out.println("Get in if: ");
                            //System.out.println("Get in while loop: ");
                            break;
                        }

                    }
                }
            } catch (Exception e){
                System.out.println("Exception");
            }
        }
        double rsj = Math.log(((ri + 0.5)/(R - ri + 0.5))/((documentFrequency - ri + 0.5)/
                (numberOfDocuments - documentFrequency - R + ri + 0.5)));
        return rsj * ((tf * (k1 + 1))/(tf + k1 * (1 - b + b * (docLength/averageDocumentLength))));
    }
}
