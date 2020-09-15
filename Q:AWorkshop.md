# Stemmers

<img src="/Users/evan/Desktop/StemmersExercise.png" style="zoom:50%;" />

* inputs 

  * Document, words, piece of text

* outputs

  * word which was removed suffix, **stem**

* goal

  * conflating different variations to one unique stem

* Type of stemming approaches

  * Algorithmic approach

    procedures  is a series of steps

  * Dictionary approach 

* Porter stemmer 

  * Algorithmic approach

  * Output:

    Stem(not a word) 

* Krovetz stemmer

  * hybird 

  * Dictionary -> stemming

  * this dictionary not as the same in dictionary

  * Output

    word

  * reduce false positive

## Zipf's Law

![](/Users/evan/Desktop/Zip'sLawExercise.png)

* definiation

  * the rank of a word multiple a term frequency is a constant

  * order the words in decreasing order of frequency

* indexing 

  * discar the high frequency words like stoplist

  * Lower weght for high frequency words

* stoplist

# Stoplist

![](/Users/evan/Desktop/StopListExercise.png)

remove the words which don;t carry meaning

* ways to produce this stoplist
  * domain experise
  * statistical perspective(distribution)
* which steps of IR pipline
  * query time
  * indexing time
    * improve efficiency (samller index)
      * less staff we need to keep in memory
      * less staff we need to iterate
    * not improve effectiveness especially for web sclae
      * small data may not very reliable in aspective of zipf's law
      * web whcih has large mounts of data, the common words has small weight so the result will be the same
* effectiveness
  * imporve the quality of the results
* effeiency 
  * improve the speed of the results

* alernatives
  * some special words may have meaning like IT, but will be removed because of the same form of it
  * cannot match the new definition and words

# Text processing

![](/Users/evan/Desktop/TextProcessingExercise.png)

* Stemming may increase the number of relevant documents retrieved(recall)
  * True
  * improve recall
  * could decrease the quality of the results  (precision) because the system retrieves more documents
* using a stopword  list increases the amount of space required by the index
  * False
  * remove posting list for all documents incollection
* using a N-gram  list increases the amount of space required by the index
  * True
  * slide the window to the sequence , so it will create more words
  * the bigger N, the bigger for requirement
* The Krovertz stmmer is a statistical stemmer: it uses the statistical of word occurrence to decide on stemming
  * False
* Stemming impacts retrieval effectiveness to different levels, depending upon(among others): language of text, stemming algorithm
  * True
* The use of a stopword list slows down indexing and retrieval
  * Flase
  * remove iteration to the inverted list, which is taking a lot of time
  * Reduce the time and space in indexing
* The use of stemming increases indexing and retrieval times
  * True
  * match more documents to the stem(index word)

# Set recall and set precision

![](/Users/evan/Desktop/APRecallExercise.png)

* set recall
* set precision
* P@5, P@10
* RR
* AP
  * Sum(P@i)/# relevant documents

One system for two queries

* RR
  * 1/the rank of first relevant docmunt
* MRR
  * Sum(RR for queries)/# queries

![](/Users/evan/Desktop/P@10Exercise.png)

two systems

* RR

  1/1

  1/2

* P@5

  4/5

  2/5

* AP

  (P@1 + P@3 + p@4 + P@5 + P@6 +P@10)/# relevant

  (P@2 + P@5 + P@6  + P@7 + P@9 + P@10)/# relevant

![](/Users/evan/Desktop/APExercise.png)

both decreas

![](/Users/evan/Desktop/RBPExercise.png)

D RBP can bu used for evaluatng systems when graded relevance is used

* not only 0 or 1 but can be 1 2 3...

B p is the probability, in other words, p is the patient.

how to set p?

* look at query log(stopping and clicking)
* analysis query logs and user study tpye of procdure

* Gain = 1, relevant; gain = 0, non-relevant

# VSM

![](/Users/evan/Desktop/VSMExercise.png)

How do I present the document in the VSM?

How do I present a query in the VSM?

A 

* the length of document
* the length of vector

B values in vector

* Term frequency
* IDF

![](/Users/evan/Desktop/TFIDFExercise.png)

TFIDFD in doc 6

IDF log(# N/# d(contains item) + 1) 

GERMAN 2/5 * log(6/(3+ 1))

MAN 1/5 * log(6/(2 + 1))

VW 1/5 * log(6/(3 + 1))

SPY 1/5 * log(6/(2 + 1))

![](/Users/evan/Desktop/BM25Exercise.png)

b = 0 dont care docment length so it will prefer longer documents

b = 1 very care about document length, so it will prefer shorter documents



![](/Users/evan/Desktop/RelevenceFeedbackExercise.png)

* query drift
  * after modifing the query expension or relevance feed back, query moved away from that original intent

![](/Users/evan/Desktop/QueryExpansionExercise.png)

word embedding natural languaga model

Query expension mechanism is a mechanism that takes the query and identify the terms

improce recall and precision

suggest terms that not in original query

set a threshold on the minmum value of  similarity between two words

translation language model, avoid a query drift by actually sticking to the translation

there is a maximum and the order is particualr observation from experiments

so there is a big gap between documents and documents with queries