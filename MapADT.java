// --== CS400 File Header Information ==--
// Name: Hailey Park
// Email: hpark353@wisc.edu
// Team: Blue
// Group: FB
// TA: Daniel Finer
// Lecturer: Gary
// Notes to Grader: <optional extra notes>

import java.util.NoSuchElementException;

public interface MapADT<KeyType, ValueType> {

    public boolean put(KeyType key, ValueType value);
    public ValueType get(KeyType key) throws NoSuchElementException;
    public int size();
    public boolean containsKey(KeyType key);
    public ValueType remove(KeyType key);
    public void clear();
    
}