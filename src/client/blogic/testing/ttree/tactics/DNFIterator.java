package client.blogic.testing.ttree.tactics;

import common.z.SpecUtils;
import common.z.czt.visitors.OrPredClausesExtractor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.czt.z.ast.Pred;


/**
 * This class was made to calculate the DNF of a big predicate in an iterative
 * way. An instance of DNFIterator takes a list [p1, p2, ..., pN] where each 
 * pI is in DNF and where the list represents the predicate 
 * p1 /\ p2 /\ ... /\ pN. Then, each call to the next() method will return the 
 * next disjunct of the DNF of p1 /\ p2 /\ ... /\ pN.
 * @author Pablo Rodriguez Monetti
 */
public class DNFIterator {
    // The list of lists of conjunctions of atomic predicates taken from the
    // list of atomic predicates in DNF that was used to construct the iterator
    private List<List<Pred>> listOfPredLists = new ArrayList<List<Pred>>();
    // The number of disjuncts that this iterator could return
    private BigInteger size;
    // The index of the current disjunct
    private BigInteger index;
    // The list of predicates that conform the conjunction of predicates that
    // will be returned in the next called to the next() method
    private List<Pred> nextPredList = new ArrayList<Pred>();
    // The sizes of the lists contained in listOfPredLists
    private int[] listSizes;
    // The current indexes of the lists contained in listOfPredLists, that are used
    // to build the nextPredList in each call to the next() method 
    private int[] listIndexes;

    /**
     * Creates a new instance of DNFIterator.
     * @param dnfPredList the list of predicates in DNF that represent the 
     * conjunction of those predicates
     */
    public DNFIterator(List<Pred> dnfPredList){
        size = BigInteger.valueOf(1);

        int numberOfDNFPreds = dnfPredList.size();
        listSizes = new int[numberOfDNFPreds];
        listIndexes = new int[numberOfDNFPreds];
        // The disjuncts of each predicate in DNF from dnfPredList are extracted 
        // and saved in a different list, with a list (of conjunctions of 
        // atomic predicates) associated to each predicate in DNF
        for(int i=0; i< numberOfDNFPreds; i++){

            Pred dnfPred = dnfPredList.get(i);
            List<Pred> listOfConjunctions =
                    dnfPred.accept(new OrPredClausesExtractor());

            List<Pred> predList = new ArrayList<Pred>();
            int numberOfConjuncts = listOfConjunctions.size();

            for(int j=0; j<numberOfConjuncts;j++){
                Pred pred = listOfConjunctions.get(j);
                predList.add(pred);
                // nextPredList is filled with the first conjunction of each
                // dnfPred, i.e. with the first predicate of the
                // listOfConjunctions for each for iteration
                if(j==0)
                    nextPredList.add(pred);
            }
            
            size = size.multiply(BigInteger.valueOf(numberOfConjuncts));

            listIndexes[i] = 0;
            listSizes[i] = numberOfConjuncts;
            listOfPredLists.add(predList);
        }
        index = BigInteger.valueOf(0);

    }

    /**
     * Determines if there is a next disjunct of the DNF of the conjunctions 
     * of predicates
     * @return true if there is a next disjunct of the DNF of the conjunctions; 
     * false, otherwise
     */
    public boolean hasNext(){
        return(index.compareTo(size) == -1);
    }

    /**
     * Returns the next disjunct of the DNF of the conjunctions of predicates
     * @return the next disjunct of the DNF of the conjunctions of predicates
     */
    public Pred next(){
        // The disjunct to be returned is calculated from the nextPredList
        Pred pred = SpecUtils.createAndPred(nextPredList);                
        // nextPredList is modified to define the predicate that will be returned
        // in the next (not in this!) call to this method
        boolean hasInc = false;
        nextPredList.clear();
        for(int i=0; i< listOfPredLists.size(); i++){
            List<Pred> predList = listOfPredLists.get(i);
            if(!hasInc){
                if(listIndexes[i] < listSizes[i] - 1){
                    hasInc = true;
                    listIndexes[i]++;
                }
                else{
                    listIndexes[i] = 0;
                }
            }
            nextPredList.add(predList.get(listIndexes[i]));
        }

        index = index.add(BigInteger.valueOf(1));

        return pred;
    }


    /**
     * Returns the number of disjuncts that this iterator could give
     * @return the number of disjuncts that this iterator could give
     */
    public BigInteger getSize(){
        return size;
    }
}
