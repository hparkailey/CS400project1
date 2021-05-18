import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

// --== CS400 File Header Information ==--
// Name: Ethan Knifsend
// Email: Knifsend@wisc.edu
// Team: FB blue
// Role: Backend Developer
// TA: Daniel Finer
// Lecturer: Gary Dahl
// Notes to Grader: n/a

/**
 * Backend class implements the BackendInterface as defined for the role of the Backend developer.
 *
 * <p>
 * Bugs: n/a
 *
 * @author Ethan Knifsend
 */
public class Backend implements BackendInterface {

  private List<String> allGenres;
  private List<String> genres;
  private List<String> avgRatings;

  private HashTableMap<String, List<MovieInterface>> htGenres;
  private HashTableMap<String, List<MovieInterface>> htRatings;

  /**
   * Constructor which takes in an array of Strings as a command line argument to obtain the set of
   * movies. The Hash Tables and lists are initialized when the constructor is called.
   * 
   * @param args [] String command line argument to obtain set of movies
   * @see initializeTables()
   */
  public Backend(String[] args) {
    try {
      File file = new File(args[0]);
      Reader inputFileReader = new FileReader(file);

      MovieDataReaderInterface reader = new MovieDataReader();

      List<MovieInterface> movies = reader.readDataSet(inputFileReader);

      allGenres = new ArrayList<String>();
      genres = new ArrayList<String>();
      avgRatings = new ArrayList<String>();

      htGenres = new HashTableMap<String, List<MovieInterface>>();
      htRatings = new HashTableMap<String, List<MovieInterface>>();

      initializeTables(movies);
    } catch (FileNotFoundException fnfe) {
      System.out
          .println("File read unsuccessful. The file was not found. Threw FileNotFoundException.");
    } catch (DataFormatException dfe) {
      System.out.println("File read unsuccessful. Threw DataFormatException.");
    } catch (IOException ioe) {
      System.out.println("File read unsuccessful. Threw IOException.");
    }
  }

  /**
   * Constructor which takes in a reader to read a file containing a set of movies. The Hash Tables
   * and lists are initialized when the constructor is called.
   * 
   * @param inputFileReader Reader which reads file containing a set of movies
   * @see initializeTables()
   */
  public Backend(Reader inputFileReader) {
    MovieDataReaderInterface reader = new MovieDataReader();

    try {
      List<MovieInterface> movies = reader.readDataSet(inputFileReader);

      allGenres = new ArrayList<String>();
      genres = new ArrayList<String>();
      avgRatings = new ArrayList<String>();

      htGenres = new HashTableMap<String, List<MovieInterface>>();
      htRatings = new HashTableMap<String, List<MovieInterface>>();

      initializeTables(movies);
    } catch (FileNotFoundException fnfe) {
      System.out
          .println("File read unsuccessful. The file was not found. Threw FileNotFoundException.");
    } catch (DataFormatException dfe) {
      System.out.println("File read unsuccessful. Threw DataFormatException.");
    } catch (IOException ioe) {
      System.out.println("File read unsuccessful. Threw IOException.");
    }
  }

  /**
   * Private helper methods which fills the HashTables by mapping the movies genre/avgRating to a
   * list of associate movies
   * 
   * @param movies List<MovieInterface> containing the movies which are mapped into the HashTable
   *               with their genre/avgRagting as the key
   * @see mapGenre()
   * @see mapAvgRating()
   */
  private void initializeTables(List<MovieInterface> movies) {
    for (int i = 0; i < movies.size(); i++) {
      MovieInterface m = movies.get(i);

      List<MovieInterface> newList = new ArrayList<MovieInterface>();
      newList.add(m);

      mapGenre(m, newList);
      mapAvgRating(m, newList);
    }
  }

  /**
   * Private helper method which adds the inputed movie into the HashTable mapped by genre
   * 
   * @param m       MovieInterface that will be mapped to HashTable based on its genre(s)
   * @param newList List<MovieInterface> which will be added as value to the HashTable or appended
   *                to current List if the key is already existing
   */
  private void mapGenre(MovieInterface m, List<MovieInterface> newList) {
    List<String> genresOfMovie = m.getGenres();

    for (int j = 0; j < genresOfMovie.size(); j++) {
      if (!htGenres.containsKey(genresOfMovie.get(j))) {
        allGenres.add(genresOfMovie.get(j));
        htGenres.put(genresOfMovie.get(j), newList);
      } else {
        htGenres.get(genresOfMovie.get(j)).addAll(newList);
      }
    }
  }

  /**
   * Private helper method which adds the inputed movie into the HashTable mapped by avgRating
   * 
   * @param m       MovieInterface that will be mapped to HashTable based on its avgRating
   * @param newList List<MovieInterface> which will be added as value to the HashTable or appended
   *                to current List if the key is already existing
   */
  private void mapAvgRating(MovieInterface m, List<MovieInterface> newList) {
    if (!htRatings.containsKey(String.valueOf(m.getAvgVote()))) {
      htRatings.put(String.valueOf(m.getAvgVote()), newList);
    } else {
      htRatings.get(String.valueOf(m.getAvgVote())).addAll(newList);
    }
  }

  /**
   * Mutator method which adds an inputed genre to the list of valid genres
   * 
   * @param genre String which is added to the genres list
   */
  @Override
  public void addGenre(String genre) {
    // TODO Auto-generated method stub
    if (!genres.contains(genre)) {
      genres.add(genre);
    }
  }

  /**
   * Mutator method which adds an inputed avgRating to the list of valid avgRatings
   * 
   * @param rating String which is added to the avgRating list
   * @see orderAvgRating()
   */
  @Override
  public void addAvgRating(String rating) {
    // TODO Auto-generated method stub
    if (!avgRatings.contains(rating)) {
      avgRatings.add(rating);
    }

    orderAvgRating();
  }

  /**
   * Private helper method which orders all avgRatings inputed into class in descending order.
   * 
   * @see quickSort()
   */
  private void orderAvgRating() {
    double[] avgRatingSorted = new double[avgRatings.size()];

    for (int i = 0; i < avgRatings.size(); i++) {
      avgRatingSorted[i] = Double.parseDouble(avgRatings.get(i));
    }

    quickSort(avgRatingSorted, 0, avgRatingSorted.length - 1);

    for (int j = 0; j < avgRatings.size(); j++) {

      //System.out.println("--------------" + avgRatings.size()+"    " + j);
      avgRatings.add(j, String.valueOf(avgRatingSorted[j]));
    }
  }

  /**
   * Mutator method which removes the inputed genre from the list of valid genres
   * 
   * @param genre String which is removed from the genres list
   */
  @Override
  public void removeGenre(String genre) {
    // TODO Auto-generated method stub
    genres.remove(genre);
  }

  /**
   * Mutator method which removes the inputed avgRating to the list of valid avgRatings
   * 
   * @param rating String which is removed from the avgRatings list
   */
  @Override
  public void removeAvgRating(String rating) {
    // TODO Auto-generated method stub
    avgRatings.remove(rating);
  }

  /**
   * Accessor method which returns the list of valid genres
   * 
   * @return genres List<String> of valid genres
   */
  @Override
  public List<String> getGenres() {
    // TODO Auto-generated method stub
    return this.genres;
  }

  /**
   * Accessor method which returns the list of valid avgRatings
   * 
   * @return avgRatings List<String> of valid avgRatings
   */
  @Override
  public List<String> getAvgRatings() {
    // TODO Auto-generated method stub
    return this.avgRatings;
  }

  /**
   * Accessor method which returns the number of movies that have all of the selected genres and are
   * within the selected rating ranges
   * 
   * @return int representing the number of valid movies
   * @see listValidMovies
   */
  @Override
  public int getNumberOfMovies() {
    // TODO Auto-generated method stub
    return listValidMovies().size();
  }

  /**
   * Accessor method which returns the list of all genres in set to choose from
   * 
   * @return allGenres List<String> a list of allGenres
   */
  @Override
  public List<String> getAllGenres() {
    // TODO Auto-generated method stub
    return allGenres;
  }

  /**
   * Accessor method which returns the list of all genres in set to choose from
   * 
   * @param startingIndex int in which the list starts with regards to an order of descending
   *                      rating.
   * @return List<MovieInterface> a list of up to three movies of selected rating ranges and genres
   *         starting at the index inputed
   * @see listValidMovies()
   */
  @Override
  public List<MovieInterface> getThreeMovies(int startingIndex) {
    // TODO Auto-generated method stub
    List<MovieInterface> orderedMovies = listValidMovies();
    List<MovieInterface> threeMovies = new ArrayList<MovieInterface>();

    for (int i = startingIndex; i < startingIndex + 3; i++) {
      if (i >= orderedMovies.size()) {
        break;
      } else {
        threeMovies.add(orderedMovies.get(i));
      }
    }

    return threeMovies;
  }

  /**
   * Private helper method which returns a list of all valid movies in descending order.
   * 
   * @return List<MovieInterface> an ordered list (descending) of all valid movies
   */
  private List<MovieInterface> listValidMovies() {
    List<MovieInterface> orderedMovies = new ArrayList<MovieInterface>();

    for (int i = 0; i < avgRatings.size(); i++) {
      if (htRatings.containsKey(avgRatings.get(i))) {
        orderedMovies.addAll(htRatings.get(avgRatings.get(i)));
      }
    }

    int j = 0;

    while (j < orderedMovies.size()) {
      if (!orderedMovies.get(j).getGenres().containsAll(genres)) {
        orderedMovies.remove(j);
      } else {
        j = j + 1;
      }
    }

    return orderedMovies;
  }

  /**
   * Quicksort algorithm which orders (in descending order) the inputed array from beginning to
   * ending indexes. Implementation obtained from baeldung at
   * https://www.baeldung.com/java-quicksort.
   * 
   * @param arr   [] double to be sorted
   * @param begin int representing the starting index
   * @param end   int representing the ending index
   * @see partition
   */
  private void quickSort(double arr[], int begin, int end) {
    if (begin < end) {
      int partitionIndex = partition(arr, begin, end);

      quickSort(arr, begin, partitionIndex - 1);
      quickSort(arr, partitionIndex + 1, end);
    }
  }

  /**
   * partition() part of the Quicksort algorithm. Implementation obtained from baeldung at
   * https://www.baeldung.com/java-quicksort.
   * 
   * @param arr   [] double to be sorted
   * @param begin int representing the starting index
   * @param end   int representing the ending index
   * @return int representing partition index
   */
  private int partition(double arr[], int begin, int end) {
    double pivot = arr[end];
    int i = (begin - 1);

    for (int j = begin; j < end; j++) {
      if (arr[j] >= pivot) {
        i++;

        double swapTemp = arr[i];
        arr[i] = arr[j];
        arr[j] = swapTemp;
      }
    }

    double swapTemp = arr[i + 1];
    arr[i + 1] = arr[end];
    arr[end] = swapTemp;

    return i + 1;
  }
}
