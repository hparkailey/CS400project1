// --== CS400 File Header Information ==--
// Name: Hailey Park
// Email: hpark353@wisc.edu
// Team: Blue
// Group: FB
// TA: Daniel Finer
// Lecturer: Gary
// Notes to Grader: <optional extra notes>

import java.util.NoSuchElementException;

/**
 * This class checks whether methods implemented in HashTableMap class function as expected.
 * 
 * @author Hailey
 *
 */
public class TestHashTable {

  public static void main(String[] args) {
    System.out.println("test1:" + test1());
    System.out.println("test2:" + test2());
    System.out.println("test3:" + test3());
    System.out.println("test4:" + test4());
    System.out.println("test5:" + test5());
  }

  /**
   * Checks whether constructors, put(), and size() methods are correctly implemented in a
   * HashTableMap class. The first scenario checks whether the constructors are correctly
   * implemented with an initial size of 0. The second scenario checks if the hash table size
   * increases correctly based on the number of items inserted in the HashTableMap as well as if the
   * resizing operation is correclty implemented if the number of items inserted exceeds its default
   * load capacity. The third scenario checks whether invalid inputs, such as null or duplicate
   * keys, are handled correctly so that the put() method returns false.
   * 
   * @return true if methods are performing as expected, false otherwise
   */
  public static boolean test1() {
    // case 1 : initialized correctly
    HashTableMap<String, Integer> groceries = new HashTableMap<String, Integer>();
    if (groceries.size() != 0)
      return false;

    HashTableMap<String, Integer> largerGroceries = new HashTableMap<String, Integer>(1);
    if (largerGroceries.size() != 0)
      return false;

    // case 2: resize helper implemented correctly
    groceries.put("Apple", 1);
    if (groceries.size() != 1)
      return false;
    groceries.put("Pear", 2);
    if (groceries.size() != 2)
      return false;
    groceries.put("Steak", 30);
    groceries.put("Broccoli", 3);
    groceries.put("Milk", 3);
    groceries.put("Cheese", 11);
    groceries.put("Oatmeal", 4);
    if (!groceries.put("Rice", 8))
      return false;
    if (!groceries.put("Paprika", 2))
      return false;
    if (groceries.size() != 9)
      return false;
    if (!groceries.put("Icecream", 6))
      return false;
    if (groceries.size() != 10)
      return false;
    if (!groceries.put("Chocolate bar", 2))
      return false;

    // case 3: invalid inputs handled correctly
    if (groceries.put("Apple", 1))
      return false;
    if (groceries.put("Icecream", 7))
      return false;
    if (groceries.put(null, 10))
      return false;


    return true;
  }

  /**
   * Checks whether get() method is correctly implemented in the HashTableMap class. The first
   * scenario checks if the get() method returns correct values corresponding to the given keys. The
   * second scenario checks whether the method throws NoSuchElementException as expected when
   * invalid keys are provided as an input. The third scenario checks whether the method is
   * returning correct values corresponding to chained keys.
   * 
   * @return true if the method functions as expected, false otherwise
   */
  public static boolean test2() {
    HashTableMap<String, String> grades = new HashTableMap<String, String>();
    grades.put("CALC1", "AB");
    grades.put("CALC2", "BC");
    grades.put("CALC3", "A");
    grades.put("ECON101", "CD");
    grades.put("CS400", "NA");

    // case 1: valid keys
    if (!grades.get("CALC1").equals("AB"))
      return false;
    if (!grades.get("ECON101").equals("CD"))
      return false;
    if (!grades.get("CS400").equals("NA"))
      return false;

    // case 2: invalid keys
    try {
      grades.get("CS540");
      return false;
    } catch (NoSuchElementException e) {
      // expected behavior
    }

    // case 3: access chained keys
    HashTableMap<Integer, String> collision = new HashTableMap<Integer, String>();
    collision.put(5, "five1");
    collision.put(15, "five2");
    if (!collision.get(5).equals("five1"))
      return false;
    if (!collision.get(15).equals("five2"))
      return false;

    return true;
  }

  /**
   * Tests whether containsKey() method was successfully implemented in the HashTableMap class. The
   * first scenario checks whether the containsKey() method returns correct boolean value whether or
   * not the key is present in the hashtable. The second scenario checks whether the method can
   * correctly identify if chained keys are present in the hashtable. The third scenario checks
   * whether the method can return correct output when the key is not contained in the table.
   * 
   * @return true if the method functions as expected, false otherwise
   */
  public static boolean test3() {
    HashTableMap<Integer, Integer> intMap = new HashTableMap();
    // case 1: single value
    intMap.put(1, 1);
    if (!intMap.containsKey(1))
      return false;

    // case 2: chained value
    intMap.put(11, 11);
    if (!intMap.containsKey(11))
      return false;
    if (!intMap.containsKey(1))
      return false;


    // case 3: key not contained
    if (intMap.containsKey(21))
      return false;


    return true;
  }

  /**
   * Tests whether remove() method was successfully implemented in the HashTableMap class. The first
   * scenario checks if the method is returning null when invalid keys are provided as an input. The
   * second scenario checks whether the method is returning removed values and whether the removed
   * values are no longer present in the table by calling size() and containsKey() methods.
   * 
   * 
   * @return true if the method functions as expected, false otherwise
   */
  public static boolean test4() {
    HashTableMap<Integer, Integer> intMap = new HashTableMap();
    // case 1: invalid keys
    if (intMap.remove(null) != null)
      return false;
    if (intMap.remove(3) != null)
      return false;

    // case 2: remove valid keys
    intMap.put(1, 1);
    intMap.put(11, 11);
    intMap.put(3, 3);

    if (intMap.remove(1) != 1)
      return false;
    if (intMap.size != 2)
      return false;
    if (intMap.containsKey(1))
      return false;

    intMap.put(1, 1);
    if (intMap.remove(11) != 11)
      return false;
    if (intMap.size != 2)
      return false;
    if (!intMap.containsKey(3))
      return false;

    return true;
  }

  /**
   * Tests whether clear() method was successfully implemented in the HashTableMap class.
   * This method tests whether all key-value pairs are removed from the table by calling size(), 
   * containsKey(), remove(), and put() methods. 
   * 
   * @return true if the method functions as expected, false otherwise.
   */
  public static boolean test5() {
    HashTableMap<String, String> strMap = new HashTableMap();
    strMap.put("a", "alphabet");
    strMap.put("b", "banana");
    strMap.put("c", "candy");
    strMap.put("d", "dolphin");
    strMap.put("e", "elpehant");
    strMap.clear();
    if (strMap.size() != 0)
      return false;
    if (strMap.containsKey("c"))
      return false;
    if (strMap.remove("e") != null)
      return false;
    if (!strMap.put("a", "apple"))
      return false;

    return true;
  }

}
