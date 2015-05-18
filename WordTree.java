import java.util.LinkedList;
import java.util.HashSet;

/**
 * Represents a tree of words, where each parent has children that are valid
 * words with one letter changed. Each word must be the same length.
 * TODO: Speed up using a trie ;)
 */
public class WordTree {

  // The characters in our alphabet
  private static final char[] chars = {'a','b','c','d','e','f',
                                       'g','h','i','j','k','l',
                                       'm','n','o','p','q','r',
                                       's','t','u','v','w','x',
                                       'y','z'};

  // The dictionary we're going to use
  private final HashSet<String> dict;
  
  // We need a pointer to the parent
  private final WordTree parent;
  // Keep a LinkedList (since we'll want to iterate a lot) of the children
  private LinkedList<WordTree> children;
  // The word this node represents
  public final String value;

  /**
   * Constructs a new WordTree based on a word
   * @param word The word to base the tree off
   * @param dict The dictionary to use
   */
  public WordTree(String word, HashSet<String> dict) {
    this.value = word;
    this.parent = null;
    this.dict = dict;
  }

  /**
   * An internal constructor for non-parent nodes
   */
  private WordTree(String word, WordTree parent) {
    this.value = word;
    this.parent = parent;
    this.dict = parent.dict;
  }


  /**
   * Generates the children of this node
   */
  private void generateChildren() {
    // Instantiate the children list
    this.children = new LinkedList<WordTree>();
    // Create a StringBuilder that we can use to mutate the value of this node
    StringBuilder childBuilder = new StringBuilder(value);
    // Get the words we've already used in the tree so far
    HashSet<String> used = getUsed();
    // For each index of the string
    for(int i = 0; i < childBuilder.length(); i++) {
      // Remember the cahracter it was
      char initialChar = childBuilder.charAt(i);
      // For each character we could replace it with
      for(int j = 0; j < chars.length; j++) {
        // Replace the character
        childBuilder.setCharAt(i, chars[j]);
        // Build the new String
        String newWord = childBuilder.toString();
        // If the string is a valid word and we've not already used it...
        if(dict.contains(newWord) && !used.contains(newWord)) {
          // Add it to the tree
          children.add(new WordTree(newWord, this));
        }
      }
      // Reset the character we've been changing
      childBuilder.setCharAt(i, initialChar);
    }
  }

  /**
   * Gets the children of this node
   */
  public LinkedList<WordTree> getChildren() {
    if(children == null) {
      generateChildren();
    }
    return children;
  }

  /**
   * Gets the previously used words in the tree
   */
  private HashSet<String> getUsed() {
    HashSet<String> used;
    if(parent == null) {
      used = new HashSet<String>();
    } else {
      used = parent.getUsed();
    }
    used.add(value);
    return used;
  }
}