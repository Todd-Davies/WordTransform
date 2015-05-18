import java.util.HashSet;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Solver {

  // A comperator to order words by distance from the desired word
  private final Comparator<WordTree> comp;
  // Keep track of the start and finish words
  private final String targetWord, startWord;
  private final HashSet<String> dict;

  /**
   * Constructs a new Solver object, mainly just reads in the dictionary.
   */
  public Solver(String filename, String start, final String end)
      throws IOException {
    dict = new HashSet<String>();
    // Read in the dictionary
    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String word;
      do {
        dict.add(word = br.readLine());
      } while(word != null);
    }
    // Set the final variables
    startWord = start;
    targetWord = end;
    // Create the comperator
    comp = new Comparator<WordTree>() {
      public int compare(WordTree w1, WordTree w2) {
        return distance(w1.value, targetWord) - distance(w2.value, targetWord);
      }
    };
  }

  public LinkedList<String> solve() {
    WordTree tree = new WordTree(startWord, dict);
    LinkedList<WordTree> answer = solve(new LinkedList<WordTree>(), tree,
                                        targetWord);
    if(answer == null) {
      return null;
    } else {
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
      String word) {
    // Add this word
    result.push(tree);
    // If we're at the destination, return the result
    if(tree.value.equals(word)) return result;
    else {
      // Otherwise, get the children for the tree
      LinkedList<WordTree> children = tree.getChildren();
      // If there are children
      if(children != null && children.size() > 0) {
        // Create a priority queue for them
        PriorityQueue<WordTree> childQueue =
          new PriorityQueue<WordTree>(children.size(), comp);
        // Add each child
        for(WordTree child : children) childQueue.add(child);
        // While we've got words in the queue
        while(!childQueue.isEmpty()) {
          // Try and solve using the next word in the queue as the next step
          LinkedList<WordTree> answer = solve(result, childQueue.poll(), word);
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
    Solver solver = new Solver(args[0], args[1], args[2]);
    LinkedList<String> answer = solver.solve();
    if(answer != null) {
      for(String word : answer) System.out.println(word);
    } else {
      System.out.println("No solution!");
    }
  }

}