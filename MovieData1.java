// Name: DaeOck Kim
// Email: dkim627@wisc.edu
// Team: Red
// Group: FB
// TA: Daniel Finer
// Lecturer: Florian Heimerl
// Notes to Grader: <optional extra notes>

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

public class MovieData1 implements MovieInterface, MovieDataReaderInterface {

    private String title;
    private Integer year;
    private List<String> genre;
    private String director;
    private String description;
    private Float avgVote;

    public MovieData1(){

    }

    public MovieData1(String title, int year, List<String> genre, String director, String description, Float avgVote) {
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.director = director;
        this.description = description;
        this.avgVote = avgVote;
    }

    @Override
    public String toString() {
        return "MovieData1{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", genre=" + genre +
                ", director='" + director + '\'' +
                ", description='" + description + '\'' +
                ", avgVote=" + avgVote +
                '}';
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Integer getYear() {
        return year;
    }

    @Override
    public List<String> getGenres() {
        return genre;
    }

    @Override
    public String getDirector() {
        return director;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Float getAvgVote() {
        return avgVote;
    }

    @Override
    public int compareTo(MovieInterface otherMovie) {
        return otherMovie.getAvgVote().compareTo(this.avgVote);
    }

    public List<MovieInterface> readDataSet(Reader reader) throws IOException, DataFormatException {

        ArrayList<MovieInterface> arrayList = new ArrayList<>();
        String title;
        Integer year;
        String rawGenre;
        List<String> genre = new ArrayList<>();
        String director;
        String description;
        Float avg_vote;

        //extract scv data into one dataString
        String dataString = "";
        int data = 0;
        while (data != -1) {
            dataString+=(char)data+"";
            data = reader.read();
        }
        //split by lines in array
        String[] lines = dataString.split("\n");
        for (int i = 0; i < lines.length; i++) {

            //remove \r in case it exists
            lines[i] = lines[i].replace("\r", "");
        }
        String[] tmplines= new String[lines.length-1];
        for(int i=1;i<lines.length;i++){
            tmplines[i-1]=lines[i];
        }
        //parse csv by lines adaptively
        for (String line : tmplines) {
            ArrayList<String> col = new ArrayList<>();
            String element = "";
            char[] lineArray = line.toCharArray();
            boolean group = false;
            //traverse through char array of string in each line
            for (int i = 0; i < lineArray.length; i++) {
                //split by commas
                if (lineArray[i] == ',' && !group) {
                    col.add(element);
                    element = "";
                    continue;
                }
                //if there's multiple elements in single row
                if (lineArray[i] == '"') {
                    group = !group;
                    continue;
                }
                element += (lineArray[i] + "");
            }
            col.add(element);
            element = "";

            //correct set of data should have only 13 columns
            if (col.size() != 13) {
                throw new DataFormatException("data format isn't correct");
            }

            //save data we need accordingly
            title = col.get(0);
            year = Integer.valueOf(col.get(2));
            if (year / 1000 >= 10) {
                throw new DataFormatException("incorrect data format(year)");
            }
            rawGenre = col.get(3);
            director = col.get(7);
            description = col.get(11);
            avg_vote = Float.valueOf(col.get(12));
            if (avg_vote < 0.0 || avg_vote > 10.0) {
                throw new DataFormatException("incorrect data format(avg_vote)");
            }
            String[] tmp = rawGenre.split(",");
            for (String string : tmp) {
                genre.add(string);
            }
            MovieData1 movieData = new MovieData1(title, year, genre, director, description, avg_vote);
            arrayList.add(movieData);
        }
        return arrayList;
    }
}