
// --== CS400 File Header Information ==--
// Name: Gabriela Setyawan
// Email: gsetyawan@wisc.edu
// Team: FB blue
// Role: Front end developer
// TA: Daniel Finer
// Lecturer: Gary Dahl
// Notes to Grader:
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

/**
 * Frontend interface for the user to interact with the MovieMapper app by prompting them to enter
 * inputs. There are three modes in this App (Base, Genre, Rating) where contents are displayed
 * based on user input.
 * 
 * @author Gabriela Setyawan
 *
 */
public class Frontend {
  private static Backend backEnd;
  private static Scanner scnr;

  /**
   * Main method that calls the run method to start the MovieMapper
   * 
   * @param args arguments passed from the command line
   * @throws DataFormatException
   * @throws IOException
   */
  public static void main(String[] args) throws IOException, DataFormatException {
    Frontend.run(backEnd);
  }

  /**
   * Key path to run the Movie Mapper app, when it starts it should print out a welcome message and
   * guides the user to determine how they can enter a file path, either by file path or by manually
   * enter the movies.
   * 
   * @param args String array from the command prompt
   * @throws DataFormatException
   * @throws IOException
   */
  public static void run(Backend backEnd) throws IOException, DataFormatException {
    String filePath; // String object that contains the filePath to the csvFile
    String csvAsAString; // String object that contains the csv inputted as a string
    String userChoice; // Defines the userChoice given a prompt
    Reader filePathInput; // ReaderObject to read the filePathInput
    int startingIndexForList; // Defines the startingIndexForList to display three movies from
                              // backEnd
    String[] csvArray = new String[1];
    scnr = new Scanner(System.in);
    String welcomeMessage = "Welcome to the Movie Mapper! You are now in the base mode." + "\n"
        + "Enter 'x' to quit \nEnter 'f' to enter a filepath \nEnter 's' to enter a string";
    String movingModes = "There are three modes in this application: base, genre, and selection."
        + "\n"
        + "To move between modes, enter 'g' to go the genre mode, enter 'r' to go to the rating mode."
        + "\n" + "You can always hop back to the base mode by entering 'x', "
        + "only if you are in the base mode you can exit by entering 'x' once again.";
    String promptChar = "Enter a char to explore this app: ";

    while (true) {
      System.out.println(welcomeMessage); // print welcome Message
      System.out.print("Enter your selection: ");
      userChoice = scnr.next();
      if (userChoice.equals("x")) { // quits the program if input is x
        break;
      } else if (userChoice.equals("f")) {
        System.out.println("Now, please enter the filepath to the movie database csv file: "); // prompt
                                                                                               // the
                                                                                               // user
                                                                                               // to
                                                                                               // enter
                                                                                               // the
                                                                                               // filepath
                                                                                               // to
                                                                                               // the
                                                                                               // csv
                                                                                               // file
        filePath = scnr.next();
        try {
          filePathInput = new FileReader(filePath);
          backEnd = new Backend(filePathInput); // initialize a backend object with a reader object
                                                // as the
                                                // argument

        } catch (FileNotFoundException e) {
          e.printStackTrace();// handle FileNotFoundException if file is not found
        }
      } else if (userChoice.equals("s")) { // initialize a backend object with a string as the
                                           // argument
        csvAsAString = scnr.next();
        csvArray[0] = csvAsAString;
        backEnd = new Backend(csvArray);
      }
      if (backEnd == null) { // check to see if the backEnd object is already created
        continue;
      }
      // display top three movies based on AverageRating
      System.out.println("Here are the number of movies based on the selected genres and ratings: "
          + backEnd.getNumberOfMovies());
      if (backEnd.getNumberOfMovies() != 0) {
        System.out.println("Based on your previous selections for genre and ratings, "
            + "please choose the list of 3 movies you want to see by inputting an integer [0 - (numberOfMoviesinList-3): ");
        startingIndexForList = scnr.nextInt();
        printThreeMovies(startingIndexForList);
      }

      while (true) {
        System.out.println(movingModes); // explain how to move between modes
        System.out.println(promptChar); // prompt the user to enter a character
        userChoice = scnr.next();
        if (userChoice == null || userChoice.equals("x")) // if user didn't input anything or if
                                                          // they choose to
                                                          // quit, then break this while loop
          break;
        if (!userChoice.equals("g") && !userChoice.equals("r")) { // check if it is a valid input
          System.out.print(
              "You can only enter 'x' to exit, 'g' to go to the genre mode, 'r' to enter the rating mode");
          break;
        }
        if (userChoice.equals("g")) {
          genreMode(backEnd);
        } else if (userChoice.equals("r")) {
          ratingMode(backEnd);
        }
      }
      System.out.println(
          "Do you want to continue to enter a new file path or do you want to exit the application?");
      System.out.println("(Enter 'y' for a new csv path, Enter 'x' for quit)");
      userChoice = scnr.next();

      if (userChoice.equals("x")) {
        System.out.println("Thank you for using the MovieMapper! Bye!");
        break;
      } else if (userChoice.equals("y")) {
        System.out.print("You have selected to enter a new filepath for the csv file"); // goes back
                                                                                        // to the
                                                                                        // baseMode
                                                                                        // if user
                                                                                        // chooses
                                                                                        // to enter
                                                                                        // a
                                                                                        // new csv
                                                                                        // file
      }

    }
    System.out.println("Thank you for using the MovieMapper! Bye!");
  }

  /**
   * Method to move the app to the genreMode. Allows user to select and deselect genres
   */
  static void genreMode(Backend backEnd) {
    int selectGenre;
    ArrayList<String> displaySelectedGenres;
    String selectionGuideForGenre =
        "Welcome to the Genre mode! Here are the list of genres in the movie mapper. "
            + "To select, enter the integer of the genre "
            + "which will be displayed on the left side of the genre." + "\n"
            + "To go back to the base mode, press 'x' \n" + "To go to the rating mode, press 'r'"
            + "\n" + "Genres that are selected are indicated with a '*'";
    System.out.println(selectionGuideForGenre);
    if (backEnd == null) {
      System.out.println("BACKEND IS NULL");
    }
    ArrayList<String> displayList = (ArrayList<String>) backEnd.getAllGenres(); // to display all
                                                                                // genres
    for (int i = 0; i < displayList.size(); i++) {
      System.out.println(i + 1 + "." + displayList.get(i));
    }
    boolean[] checkIfSelected = new boolean[displayList.size()]; // boolean array that corresponds
                                                                 // to the
                                                                 // displayList to see if the genre
                                                                 // is selected
                                                                 // or not,
    // if selected it is true, otherwise false

    while (true) {
      System.out.println("Enter an integer corresponding to the genre" + "\n"
          + "to display movies within that genre from the list, to deselect a previous selection enter the integer\n"
          + "from the previously selected genre, otherwise enter 'x' to exit and go back to the base mode. "
          + "Movies that are selected are indicated with an asterisk (*) \n"
          + "Enter your selection: "); // prompt user to enter selection
      String userChoice = scnr.next();
      if (userChoice.equals("x")) // enter 'x' to go back to the baseMode
        break;
      else if (userChoice.equals("r")) {
        ratingMode(backEnd);
      } else {
        try {
          selectGenre = Integer.parseInt(userChoice);
          if (selectGenre < 1 || selectGenre > displayList.size()) { // check if input is valid
            System.out.println("Please enter a valid selection. "
                + "Enter an integer corresponding to the genre "
                + "to display movies within that genre from the list, to deselect a previous selection enter the integer"
                + "from the previously selected genre, otherwise enter 'x' to exit"); // display an
                                                                                      // error
                                                                                      // message
            break;
          } else {

            displaySelectedGenres = (ArrayList<String>) backEnd.getAllGenres(); // getAllGenres as a
                                                                                // List
            // from Backend

            for (int i = 0; i < displaySelectedGenres.size(); i++) {
              System.out.print(i + 1 + "." + displaySelectedGenres.get(i)); // display the list to
                                                                            // the
                                                                            // user to start from 1
              if (i == selectGenre - 1) { // check if the current index corresponds to user's
                                          // selected
                                          // input
                checkIfSelected[selectGenre-1] = !(checkIfSelected[selectGenre - 1]); // negate
                                                                                        // the
                                                                                        // truth
                                                                                        // value
                                                                                        // at that
                                                                                        // index
              }
              if (checkIfSelected[i]) {
                System.out.print("*"); // add * to indicate that that genre is selected
                backEnd.addGenre(backEnd.getAllGenres().get(selectGenre - 1)); // pass user input of
                                                                               // the
                                                                               // selected genre to
                                                                               // backend as an
                // integer corresponding to the name of the genre
              } else {
                backEnd.removeGenre(backEnd.getAllGenres().get(selectGenre - 1));
              }
              System.out.print("\n");
            }

          }
        } catch (InputMismatchException e) {
          System.out.println("Please input an integer");
        }
      }
    }
  }

  /**
   * Displays a numbered list of ratings that users can select. When user first enters the mode,
   * they will be prompted to enter an integer to select a rating. When one of these ratings is
   * selected, all movies that have the rating will be retrieved from the database.
   * 
   */
  static void ratingMode(Backend backEnd) {
    String selectionGuideForRating =
        "Welcome to the rating mode! Here are the list of movies based on ratings. "
            + "To see a list movies based on a certain rating, enter an integer between 0 and 10, the selected ratings will have a * on the right, "
            + "to deselect a rating input the integer to indicate the rating you want to deselect"
            + "\n" + "To go back to the base mode, enter 'x'" + "\n"
            + "To go to the genre mode, enter 'g' \n";
    System.out.print(selectionGuideForRating);
    int selectedRating;

    ArrayList<String> allRatings = new ArrayList<String>();
    for (int i = 0; i < 11; i++) {
      allRatings.add(Integer.toString(i));
      System.out.println(i + ". " + allRatings.get(i));
    }

    boolean[] checkIfSelected = new boolean[allRatings.size() + 1];

    while (true) {
      System.out.println(
          "Select an integer [0-10] to get a list of movies corresponding to that rating: ");
      String userChoice = scnr.next();
      selectedRating = Integer.parseInt(userChoice);
      if (userChoice.equals("x")) // enter 'x' to go back to the baseMode
        break;
      else if (userChoice.equals("g")) {
        genreMode(backEnd);
      } else {
        if (selectedRating < 0 || selectedRating > 10) {
          System.out.println("Please enter a valid selection");
          break;
        } else {
          for (int i = 0; i < allRatings.size(); i++) {
            if (i == selectedRating) { // check if the current index corresponds to user's selected
                                       // input
              checkIfSelected[selectedRating] = !(checkIfSelected[selectedRating]); // negate the
              // truth value
              // at that index

              if (checkIfSelected[i]) {
                backEnd.addAvgRating(String.valueOf(selectedRating));// pass user input of the
                                                                     // selected
                                                                     // rating to backend to add
                // to the list of selected Ratings
                System.out.println(
                    "------------------Here are the number of movies in the selected rating: "
                        + backEnd.getNumberOfMovies());
                System.out.println("Here are the movies that you have selected based on rating: ");
                for (int j = 0; j < backEnd.getNumberOfMovies(); j += 3) { // prints all movies in
                                                                           // the
                                                                           // selected rating
                  printThreeMovies(j);
                }
              } // else {
              backEnd.removeAvgRating(String.valueOf(selectedRating));// pass user input of the
                                                                      // selected
                                                                      // rating to backend to
              // remove from the list of selected Ratings
            }
            System.out.print("\n");
          }

        }

      }
    }
  }


  /**
   * Helper to print three movies from the Backend class
   */
  private static void printThreeMovies(int index) {
    for (int i = 0; i < 3; i++) {
      try {
        System.out.println(backEnd.getThreeMovies(index));
      } catch (NullPointerException error) {
        System.out.print("You've reached the end of the list!");
        return;
      }
    }

  }

}
