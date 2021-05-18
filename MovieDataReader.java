// --== CS400 File Header Information ==--
// Name:Hailey Park
// Email:hpark353@wisc.edu
// Team: blue
// Role: Data Wrangler
// TA: Daniel Finer
// Lecturer: Gary
// Notes to Grader: <optional extra notes>

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;
import java.io.BufferedReader;


/**
 * A class implementing MovieDataReaderInterface to read in the data and store them as Movie
 * objects.
 * 
 * @author Hailey
 *
 */
public class MovieDataReader implements MovieDataReaderInterface {

  /**
   * A class to represent a movie along with its attributes like title, year, genres, director,
   * description, and its average rating.
   * 
   * @author Hailey
   *
   */
  class Movie implements MovieInterface {
    String title;
    Integer year;
    List<String> genres;
    String director;
    String description;
    Float avgVote;

    /**
     * A constructor to instantiate a Movie object, which represents a movie and its attributes.
     * 
     * @param title:       the tile of the movie
     * @param year:        the year the movie was released
     * @param genres:      genres of the movie
     * @param director:    the director of the movie
     * @param description: brief description of the movie
     * @param avgVote:     the average rating of the movie
     */
    public Movie(String title, Integer year, List<String> genres, String director,
        String description, Float avgVote) {
      this.title = title;
      this.year = year;
      this.genres = genres;
      this.director = director;
      this.description = description;
      this.avgVote = avgVote;
    }

    /**
     * A getter method to return the title of the Movie object.
     * 
     * @return title of this Movie object
     */
    @Override
    public String getTitle() {
      return this.title;
    }

    /**
     * A getter method to return the year of the Movie object.
     * 
     * @return year of this Movie object
     */
    @Override
    public Integer getYear() {
      return this.year;
    }

    /**
     * A getter method to return genres of the Movie object.
     * 
     * @return genres of this Movie object
     */
    @Override
    public List<String> getGenres() {
      return this.genres;
    }

    /**
     * A getter method to return the director of the Movie object.
     * 
     * @return director of this Movie object
     */
    @Override
    public String getDirector() {
      return this.director;
    }

    /**
     * A getter method to return the description of the Movie object.
     * 
     * @return description of this Movie object
     */
    @Override
    public String getDescription() {
      return this.description;
    }

    /**
     * A getter method to return the average rating of the Movie object.
     * 
     * @return avgVote of this Movie object
     */
    @Override
    public Float getAvgVote() {
      return this.avgVote;
    }

    /**
     * Order Movie objects that implement MovieInterface by their average rating in descending
     * order.
     * 
     * @return 1 if this Movie object has lower rating, 0 if both Movie obejcts have the same
     *         rating,and -1 otherwise
     */
    @Override
    public int compareTo(MovieInterface otherMovie) {
      if (this.avgVote < otherMovie.getAvgVote())
        return 1;
      else if (this.avgVote == otherMovie.getAvgVote())
        return 0;
      else {
        return -1;
      }
    }

    /**
     * Returns a string representation of the Movie object.
     * 
     * @return a string of structured version of the movie containing all the attributes in the
     *         Movie object
     */
    @Override
    public String toString() {
      return this.title + " (" + this.year + ")\ngenres: " + this.genres + "\ndescription: "
          + this.description + "\ndirector: " + this.director + "\navgVote: " + this.avgVote;
    }

  }

  /**
   * Cleans raw array of Strings so that elements within that array with quotation marks are
   * concateneated into one.
   * 
   * @param raw: an array of String that represents raw rows in the dataset
   * @return a new array whose cells with multiple alues in one column are concateneated into one
   */
  private String[] concatQuotations(String[] raw) {
    ArrayList<String> cleaned = new ArrayList<String>();
    String toConcat = "";
    int concatStartIndex = -1;
    
    
    for (int i = 0; i < raw.length; i++) {
      String cell = raw[i];

     // System.out.println(cell + " (" + concatStartIndex + ") :" + toConcat);
      //System.out.println(cell);
//      if(cell.contains("live-in nurse")) {
//        System.out.println(cell + " (" + concatStartIndex + ") :" + toConcat);
//      }

      
      // if cell has "
      if (cell.contains("\"")) {
        
        // " beginning
        if (concatStartIndex == -1) {
          concatStartIndex = i;
          toConcat += cell + ",";
        }  
        else if(cell.contains(".\"")){
          toConcat += cell;
          concatStartIndex = -1;
          cleaned.add(toConcat);
          toConcat = "";
        }
        else if (cell.contains("\"\"")) {
            toConcat += cell + ",";
        }
        else {
          // " ending
          toConcat += cell;
          concatStartIndex = -1;
          cleaned.add(toConcat);
          toConcat = "";
        }
        // in between " & "
      } else if (concatStartIndex != -1) {
        toConcat += cell + ",";

      } else {
        //no " marks 
        concatStartIndex = -1;
        cleaned.add(cell);
      }
    }
   // System.out.println("CLEANED: " + cleaned.toString());
    //System.out.println(cleaned.size());
    String[] toReturn = new String[cleaned.size()];
    for (int i = 0; i < toReturn.length; i++) {
      toReturn[i] = cleaned.get(i);
    }
    return toReturn;
  }



  /**
   * Loads and cleans a data file specified by the input Reader. Reads in attributes to create a
   * Movie object and returns a List of all Movie objects.
   * 
   * @param inputFileReader: Reader to read the dataset from
   * @throws FileNotFoundException: when the specified file is not found, IOException: when the file
   *                                cannot be opened for reading or there's some error while reading
   *                                the file DataFormatException: when the file does not have the
   *                                correct format
   * @return movies: List of MovieInterface that contains all the Movie objects read in and cleaned
   *         from the data file
   */
  @Override
  public List<MovieInterface> readDataSet(Reader inputFileReader)
      throws FileNotFoundException, IOException, DataFormatException {

    List<MovieInterface> movies = new ArrayList<MovieInterface>();
    BufferedReader csvReader = new BufferedReader(inputFileReader);

    String[] header = csvReader.readLine().split(",");
    String row = csvReader.readLine();

    while (row != null) {
      String[] data = row.split(",");
      String[] cleaned = concatQuotations(data);

      // a row contains more or less columns than the header line

      if (cleaned.length != header.length) {
       // System.out.println(Arrays.toString(data));
        //System.out.println(Arrays.toString(cleaned));
        // System.out.println(Arrays.toString(header));
        throw new DataFormatException("The file does not have a correct format.");
      }

      // create a Movie object and add it to the list
     // System.out.println(Arrays.toString(header));
      String title = cleaned[findIndex(header, "title")];
      Integer year = Integer.parseInt(cleaned[findIndex(header, "year")]);
      String[] genreArray = cleaned[findIndex(header, "genre")].replaceAll("\"", "").split(" ");
      List<String> genres = Arrays.asList(genreArray);
      String director = cleaned[findIndex(header, "director")].replaceAll("\"", "");
      String description = cleaned[findIndex(header, "description")].replaceAll("\"", "");
      // System.out.println(description);
      // String renewedDescription = retrieveComma(row, description);
      // System.out.println(renewedDescription);
      Float avgVote = Float.parseFloat(cleaned[findIndex(header, "avg_vote")]);

      movies.add(new Movie(title, year, genres, director, description, avgVote));

      row = csvReader.readLine();
    }
    csvReader.close();

    return movies;
  }

  /**
   * This method finds an index in the array of String for the corresponding column name
   * 
   * @param list:   an array of String to search for
   * @param column: the column name to find the index for
   * @return an index corresponding to the column name in a list
   */
  private int findIndex(String[] list, String column) {
    int i = 0;
    for (String item : list) {
      if (item.equals(column)) {
        return i;
      }
      if (!item.equals(column)) {
        i++;
      }
    }
    return i;
  }
}
