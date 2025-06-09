package ngrams;

import edu.princeton.cs.algs4.In;
import net.sf.saxon.expr.flwor.Tuple;

import java.io.File;
import java.sql.Time;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    // TODO: Add any necessary static/instance variables.
    HashMap<String, HashMap<Integer, Long>> wordsMap;
    HashMap<Integer, Long> countMap;


    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        // TODO: Fill in this constructor. See the "NGramMap Tips" section of the spec for help.
        In wordsIn = new In(wordsFilename);
        In countsIn = new In(countsFilename);
        this.wordsMap = new HashMap<>();
        this.countMap = new HashMap<>();
        while (wordsIn.hasNextLine()) {
            String nextLine = wordsIn.readLine();
            String[] arr = nextLine.split("\t");
            String word = arr[0];
            int year = Integer.parseInt(arr[1]);
            long times = Long.parseLong(arr[2]);
            if (this.wordsMap.containsKey(word)) {
                this.wordsMap.get(word).put(year, times);
            } else {
                this.wordsMap.put(word, new HashMap<>());
                this.wordsMap.get(word).put(year, times);
            }
        }
        while (countsIn.hasNextLine()) {
            String nextLine = countsIn.readLine();
            String[] arr =nextLine.split(",");
            int year = Integer.parseInt(arr[0]);
            long count = Long.parseLong(arr[1]);
            this.countMap.put(year, count);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries ts = new TimeSeries();
        if (this.wordsMap.containsKey(word)) {
            for (int year : this.wordsMap.get(word).keySet()) {
                if (year >= startYear && year <= endYear) {
                    ts.put(year, (double) this.wordsMap.get(word).get(year));
                }
            }
        }
        return ts;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        // TODO: Fill in this method.
        TimeSeries ts = new TimeSeries();
        if (this.wordsMap.containsKey(word)) {
            for (int year : this.wordsMap.get(word).keySet()) {
                ts.put(year, (double) this.wordsMap.get(word).get(year));
            }
        }
        return ts;
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        // TODO: Fill in this method.
        TimeSeries ts = new TimeSeries();
        for (int year : this.countMap.keySet()) {
            ts.put(year, (double) this.countMap.get(year));
        }
        return ts;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries ts = new TimeSeries();
        if (this.wordsMap.containsKey(word)) {
            for (int year : this.wordsMap.get(word).keySet()) {
                if (year >= startYear && year <= endYear) {
                    ts.put(year, (double) this.wordsMap.get(word).get(year) / this.countMap.get(year));
                }
            }
        }
        return ts;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        // TODO: Fill in this method.
        TimeSeries ts = new TimeSeries();
        if (this.wordsMap.containsKey(word)) {
            for (int year : this.wordsMap.get(word).keySet()) {
                ts.put(year, (double) this.wordsMap.get(word).get(year) / this.countMap.get(year));
            }
        }
        return ts;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        // TODO: Fill in this method.
        TimeSeries ts = new TimeSeries();
        for (String word : words) {
            ts = ts.plus(weightHistory(word, startYear, endYear));
        }
        return ts;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        // TODO: Fill in this method.
        TimeSeries ts = new TimeSeries();
        for (String word : words) {
            ts = ts.plus(weightHistory(word));
        }
        return ts;
    }

    // TODO: Add any private helper methods.
    // TODO: Remove all TODO comments before submitting.
}
