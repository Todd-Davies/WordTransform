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
  
  public void addWord(String word) { 
    if(word != null) dict.add(word);
  }
  
  private final WordTree parent;
  private LinkedList<WordTree> children;
  public final String value;

  public WordTree(String word, HashSet<String> dict) {
    this.value = word;
    this.parent = null;
    this.dict = dict;
  }

  public WordTree(String word) {
    this.value = word;
    this.parent = null;
    dict = new HashSet<String>();
  }

  public WordTree(String word, WordTree parent) {
    this.value = word;
    this.parent = parent;
    this.dict = parent.dict;
  }


  /**
   * Generates or gets the children of this node
   */
  public LinkedList<WordTree> getChildren() {
    if(children == null) {
      this.children = new LinkedList<WordTree>();
      StringBuilder childBuilder = new StringBuilder(value);
      HashSet<String> used = getUsed();
      for(int i = 0; i < childBuilder.length(); i++) {
        char initialChar = childBuilder.charAt(i);
        for(int j = 0; j < chars.length; j++) {
          childBuilder.setCharAt(i, chars[j]);
          String newWord = childBuilder.toString();
          if(dict.contains(newWord) && !used.contains(newWord)) {
            children.add(new WordTree(newWord, this));
          }
        }
        childBuilder.setCharAt(i, initialChar);
      }
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