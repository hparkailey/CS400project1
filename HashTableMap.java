// --== CS400 File Header Information ==--
// Name: Hailey Park
// Email: hpark353@wisc.edu
// Team: Blue
// Group: FB
// TA: Daniel Finer
// Lecturer: Gary
// Notes to Grader: <optional extra notes>

import java.util.NoSuchElementException;
import java.util.LinkedList;


/**
 * This class represents a hash table that stores key-value pairs of KeyType and ValueType
 * respectively. It also contains operations that can be done on the hash table.
 * 
 * @author Hailey
 *
 * @param <KeyType>   the type of key to map to values in the hashtable
 * @param <ValueType> the type of value stored in the hashtable
 */
public class HashTableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

  /**
   * This class represents an Object that stores a pair of KeyType and ValueType. It also includes
   * getter methods for accessing keys and values.
   * 
   * @author Hailey
   *
   * @param <KeyType>   the key to map to the hash table
   * @param <ValueType> the value to store in the hash table
   */
  private class Entry<KeyType, ValueType> {
    KeyType key;
    ValueType value;

    /**
     * A constructor to initialize the Entry object with a key and a value.
     * 
     * @param k: the key to map to the hashtable
     * @param v: the value to store in the hashtable
     */
    private Entry(KeyType k, ValueType v) {
      this.key = k;
      this.value = v;

    }

    /**
     * A getter method to aceess the key in the Entry object.
     * 
     * @return key: the key contained inside the Entry object
     */
    private KeyType getKey() {
      return this.key;
    }

    /**
     * A getter method to aceess the value in the Entry object.
     * 
     * @return value: the value contained inside the Entry object
     */
    private ValueType getValue() {
      return this.value;
    }


    /**
     * Compares whether the given Object's key is equal to this Object's key if the other Object is
     * an instace of Entry class.
     * 
     * @return true if they both are Entry Objects with the same key, false otherwise
     */
    @Override
    public boolean equals(Object other) {
      if (other instanceof Entry) {
        if (((Entry) other).key.equals(this.key))
          return true;
      }
      return false;
    }
  }

  // fields
  private LinkedList<Entry>[] table;
  KeyType[] keys;
  int capacity;
  int size;

  // constructors

  /**
   * A one-argument constructor to initialize the HashTableMap object.
   * 
   * @param capacity: the largest number of elements that can be stored in the hashtable.
   */
  public HashTableMap(int capacity) {
    this.capacity = capacity;
    this.table = (LinkedList<Entry>[]) new LinkedList[this.capacity];
    this.size = 0;
  }

  /**
   * A no-argument constructor to initialize the HashTableMap instance. Initializes the hash table
   * capacity the default size of 10.
   */
  public HashTableMap() {
    this.capacity = 10;
    this.table = (LinkedList<Entry>[]) new LinkedList[this.capacity];
    this.size = 0;
  }

  // resize helper
  /**
   * A helper method to resize the hash table when its load capacity becomes greater than or equal
   * to 85%. Doubles the current capacity, rehashes contents inside the original table, and moves
   * them to the new table.
   */
  private void resizeHelper() {
    this.capacity *= 2; // double the capacity

    LinkedList<Entry>[] resized = (LinkedList<Entry>[]) new LinkedList[this.capacity];

    for (int i = 0; i < this.table.length; i++) {
      LinkedList<Entry> entryList = this.table[i];
      if (entryList != null) {
        for (Entry e : entryList) {
          int index = Math.abs(e.getKey().hashCode()) % this.capacity;

          Entry<KeyType, ValueType> elist =
              new Entry<KeyType, ValueType>((KeyType) e.getKey(), (ValueType) e.getValue());

          if (resized[index] != null) {
            resized[index].add(elist);
          } else {
            LinkedList<Entry> entry = new LinkedList<Entry>();
            entry.add(elist);
            resized[index] = entry;
          }
        }
      }
    }

    this.table = resized;

  }

  /**
   * Puts a pair of specified key and value in the hashtable.
   * When the load capacity exceeds 85%, call resizeHelper() method to double the initial capacity 
   * as well as to rehash and move the table contents.
   * 
   * @param key:   a KeyType to map to the hashtable
   * @param value: a ValueType to store in the hashtable
   * @return true if the pair was successfuly inserted in the hashtable, false otherwise
   *         or null if the key provided is null
   */
  @Override
  public boolean put(KeyType key, ValueType value) {
    if (key == null) {
      return false; // return f when key is null
    }


    for (LinkedList<Entry> prevEntry : table) {
      if (prevEntry != null) {
        for (Entry e : prevEntry) {
          if (e != null && e.getKey().equals(key)) {
            return false; // is in the hashtable, return f
          }
        }
      }
    }
    Entry<KeyType, ValueType> temp = new Entry<KeyType, ValueType>(key, value);

    int index = Math.abs(key.hashCode()) % this.capacity;
    if (this.table[index] != null) { // hash collision
      this.table[index].add(temp); // handle by chaining
    } else {
      LinkedList<Entry> entry = new LinkedList<Entry>();
      entry.add(temp);
      this.table[index] = entry;
    }

    this.size++;

    if (this.size >= this.capacity * 0.85) {
      resizeHelper(); // resize
    }


    return true;

  }

  /**
   * Gets the value corresponding to the given key in a hashtable.
   * 
   * @param key: the given key to look for in the hashtable
   * @throws NoSuchElementException if the given key is not found in the hashtable
   * @return ValueType corresponding to the given key if found in the hashtable
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    for (LinkedList<Entry> entry : this.table) { // for each LinkedList of <Entry>
      if (entry != null) {
        for (Entry e : entry) { // take each <Entry>
          if (e != null && e.getKey().equals(key)) {
            return (ValueType) e.getValue();
          }
        }
      }
    }
    throw new NoSuchElementException("The given key is not in the hashtable");
  }

  /**
   * A getter method for the size of the hashtable.
   * 
   * @param size: the number of elements stored in the hashtable
   */
  @Override
  public int size() {
    return this.size;
  }

  /**
   * Loops through each contents in the hashtable and determines whether the specified key is
   * included or not.
   * 
   * @param key: the key we're looking for
   * @return true if the specified key was successfully found, false otherwise
   */
  @Override
  public boolean containsKey(KeyType key) {
    for (LinkedList<Entry> entryList : table) {
      if (entryList != null) {
        for (Entry entry : entryList) {
          if (entry != null && entry.getKey().equals(key))
            return true;
        }
      }
    }
    return false;
  }

  /**
   * Removes a KeyType-ValueType pair with the given key and returns a reference to the value
   * associated with the key that is being removed.
   * 
   * @param key: the key to remove in this hashtable
   * @return ValueType of the key being removed or null if the key given doesn't exist
   */
  @Override
  public ValueType remove(KeyType key) {

    for (LinkedList<Entry> entryList : table) {
      if (entryList != null) {
        for (int i = 0; i < entryList.size(); i++) {
          Entry entry = entryList.get(i);
          if (entry != null && entry.getKey().equals(key)) {
            ValueType value = (ValueType) entry.getValue();
            entryList.set(i, null);
            this.size--;
            return value;
          }
        }


      }
    }
    return null;
  }

  /**
   * Removes all the elements contained inside the hash table and resets its size to zero.
   */
  @Override
  public void clear() {
    this.table = (LinkedList<Entry>[]) new LinkedList[this.capacity];;
    size = 0;

  }


}
