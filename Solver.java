import java.util.HashSet;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Solver {

  // Keep track of the start word
  private final String startWord;
  // The dictionary of words
  private final HashSet<String> dict;
  // The root of the word tree
  private final WordTree root;

  /**
   * Constructs a new Solver object, mainly just reads in the dictionary.
   */
  public Solver(String filename, String start)
      throws IOException {
    dict = new HashSet<String>();
    // Read in the dictionary
    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String word;
      do {
        dict.add(word = br.readLine());
      } while(word != null);
    }
    if(!dict.contains(start)) {
      throw new RuntimeException("Start word not in dictionary!");
    }
    // Set the final variables
    startWord = start;
    // Create the initial tree
    root = new WordTree(startWord, dict);
  }

  /**
   * Runs the pathfinding heuristic
   * @return A LinkedList of String if a path was found, null if not
   */
  public LinkedList<String> solve(final String targetWord) {
    if(!dict.contains(targetWord)) {
      throw new RuntimeException("Target word " + targetWord +
                                 " not in dictionary!");
    }
    // Create the comperator for this run
    Comparator<WordTree> comperator = new Comparator<WordTree>() {
      public int compare(WordTree w1, WordTree w2) {
        return distance(w1.value, targetWord) - distance(w2.value, targetWord);
      }
    };
    // Create a LinkedList for the answer
    LinkedList<WordTree> answer = new LinkedList<WordTree>();
    // Run the algorithm
    answer = solve(answer, root, targetWord, comperator);
    if(answer == null) {
      // If the answer wasn't found, return null
      return null;
    } else {
      // Otherwise, convert the list to a String and return that
      LinkedList<String> out = new LinkedList<String>();
      while(!answer.isEmpty()) out.push(answer.pop().value);
      return out;
    }
  }

  /**
   * Uses a heuristic to find a path between the current tree word and the end
   * word. The heuristic is the next closest to the end word from any mutation
   * of one character of the current word.
   */
  private LinkedList<WordTree> solve(LinkedList<WordTree> result, WordTree tree,
      String word, Comparator<WordTree> comperator) {
    // Add this word
    result.push(tree);
    // If we're at the destination, return the result
    if(tree.value.equals(word)) return result;
    else {
      // Otherwise, get the children for the tree
      LinkedList<WordTree> children = tree.getChildren();
      // If there are children (if there aren't, we've run out of words)
      if(children != null && children.size() > 0) {
        // Create a priority queue for them
        PriorityQueue<WordTree> childQueue =
          new PriorityQueue<WordTree>(children.size(), comperator);
        // Add each child
        for(WordTree child : children) childQueue.add(child);
        // While we've got words in the queue
        while(!childQueue.isEmpty()) {
          // Try and solve using the next word in the queue as the next step
          LinkedList<WordTree> answer = solve(result, childQueue.poll(), word,
                                              comperator);
          // If the answer was not null, return it
          if(answer != null) return answer;
        }
      }
      // This wasn't the correct path
      result.pop();
      return null;
    }
  }

  /**
   * Computes the distance between two words
   * Assumes both words are the same length
   */
  private int distance(String word1, String word2) {
    char[] w1 = word1.toCharArray();
    char[] w2 = word2.toCharArray();
    int distance = 0;
    for(int i = 0; i < w1.length; i++) if(w1[i] != w2[i]) distance++;
    return distance;
  }

  public static void main(String[] args) throws IOException {
    Solver solver = new Solver(args[0], args[1]);
    for(int i = 2; i < args.length; i++) {
      LinkedList<String> answer = solver.solve(args[i]);
      if(answer != null) {
        for(String word : answer) System.out.println(word);
      } else {
        System.out.println("No solution!");
      }
    }
  }

}