// Austin Faux
// CSE 143: TA Ken Aragon
// 5/2/19

// Runs an evil game of hangman where it doesn't pick a word until the user 
// narrows down the list all the way to one remaining word. It'll display the pattern
// of the words based on your guessed characters. 

import java.util.*;

public class HangmanManager {
   private Set<String> currList; // current set of words
   private int guesses; // number of guesses user has
   private String currPatt; // current Pattern
   private Set<Character> alrGuessed; // set of characters already guessed
    
   // Pre: length must be 1 or greater and max should be 0 or greater 
   //       (throw IllegalArgumentException if not)
   // Post: Initiates a list of words of inputted length, sets max number 
   //       of guesses and constructs an initial pattern. 
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      alrGuessed = new TreeSet<Character>();
      guesses = max; // initializes max number of guesses. 
      currList = new TreeSet<String>();
      for(String word: dictionary) { // eliminates words not of size length.
         if(word.length() == length) {
            currList.add(word);
         }
      }
      currPatt = "-"; // initialized empty pattern 
      for(int i = 1; i < length; i++) {
         currPatt += " -";
      }  
   }
   
   // Post: returns the current list of words uneliminated words. 
   public Set<String> words() {
      return currList; 
   }
   
   // Post: returns the number of guesses the user has left. 
   public int guessesLeft() {
      return guesses; 
   }
   
   // Post: returns a set of already guessed characters.
   public Set<Character> guesses() {
      return alrGuessed;
   }
   
   // Pre: users current list must not be empty (throws IllegalStateException if not).
   // Post: returns the current pattern of characters with spaces inbetween. 
   public String pattern() {
      if (currList.isEmpty()) {
         throw new IllegalStateException();
      } 
      return currPatt;
   }
   
   // Pre: must have 1 or more guesses left, and the current list must not be empty
   //       (throw IllegalStateExecption or IllegalArgumentException, respectively, if not).
   // Post: adds new guessed letter to list of guessed letters. updates the pattern to be the
   //       pattern with the most words that follow it and updates the current list to that 
   //       set of words. returns the count of occurances of the guess in the new pattern. 
   public int record(char guess) {
      if (guesses < 1) {
         throw new IllegalStateException();
      } else if (currList.isEmpty()) {
         throw new IllegalArgumentException();
      }
      alrGuessed.add(guess);
      Map<String, Set<String>> pattLists = new TreeMap<String, Set<String>>();
      for(String word: currList) {
         // make a pattern for each word taking account letters guessed correctly
         String tempPat = makePattern(word, guess); // holds tempory pattern for each key
         if (!pattLists.containsKey(tempPat)) {
            pattLists.put(tempPat, new TreeSet<String>()); // maps the pattern to the
                                                           // word if it hasn't appeared before
         }
         pattLists.get(tempPat).add(word); // adds word to map
      }
      updateLists(pattLists); // updates current list
      return count(guess);
   }
   
   // Pre: sent in String must not be empty
   // Post: makes a pattern taking in account sent in guessed letter and previously guessed letter 
   private String makePattern(String word, char guess) {
      String result = "";
      String tempPat = currPatt.replace(" ", "");            
      if (word.charAt(0) == guess) { // fence post problem 
         result += guess;
      } else if (tempPat.charAt(0) != '-') {
         result += tempPat.charAt(0);
      } else {
         result += "-";
      }
      for(int i = 1; i < word.length(); i++) {
         if (word.charAt(i) == guess) {
            result += " " + guess;
         } else if (tempPat.charAt(i) != '-') {
            result += " " + tempPat.charAt(i);
         } else {
            result += " -";
         }
      }
      return result;
   }
   
   // Post: counts the number of occurances of the guessed letter.
   private int count(char guess) {
      int count = 0;
      for(int i = 0; i < currPatt.length(); i++) {
         if (currPatt.charAt(i) == guess) {
            count++;
         }
      }
      if (count == 0) {
         guesses--;
      }
      return count; 
   }
   
   // Post: takes in map of current list. updates current list and pattern to the 
   //       key/value with most words in it. 
   private void updateLists(Map<String, Set<String>> pattLists) {
      int max = 0;
      for(String key: pattLists.keySet()) {
         if (pattLists.get(key).size() > max) {
            max = pattLists.get(key).size();
            currList = pattLists.get(key);
            currPatt = key; 
         }
      }
   }
}