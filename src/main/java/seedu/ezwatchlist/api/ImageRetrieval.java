package seedu.ezwatchlist.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.config.TmdbConfiguration;
import info.movito.themoviedbapi.tools.MovieDbException;
import seedu.ezwatchlist.api.exceptions.OnlineConnectionException;

/**
 * A class for the image retrieval.
 */
public class ImageRetrieval {
    private static final String DEFAULT_FILE_SIZE = "w500";
    private static final String ROOT = defaultDirectory();
    public static final String IMAGE_CACHE_LOCATION = ROOT + File.separator
            + "Ezwatchlist" + File.separator + "posters";
    private String imageUrl;

    /**
     * Creates an instance of a image retrieval used to download images online
     *
     * @param tmdbApi the tmdbApi object used to get the url
     * @param filePath the url online to the image
     * @throws OnlineConnectionException when not connected to the internet
     */
    public ImageRetrieval(TmdbApi tmdbApi, String filePath) throws OnlineConnectionException {
        try {
            TmdbConfiguration configuration = tmdbApi.getConfiguration();
            imageUrl = configuration.getBaseUrl() + DEFAULT_FILE_SIZE + filePath;
        } catch (MovieDbException e) {
            throw new OnlineConnectionException("Internet Connection failed at Image Retrieval");
        }
    }

    /**
     * Retrieves the default directory of the platform
     *
     * @return the string path to the root folder
     */
    //adapted from
    //https://stackoverflow.com/questions/6561172/find-directory-for-application-data-on-linux-and-macintosh
    static String defaultDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return System.getenv("APPDATA");
        } else if (os.contains("mac")) {
            return System.getProperty("user.home") + "/Library/Application Support";
        } else if (os.contains("nux")) {
            return System.getProperty("user.home");
        } else {
            return System.getProperty("user.dir");
        }
    }

    /**
     * Retrieves the image online by downloading it into the save folder
     *
     * @param fileName the name of the file saved
     * @return the string path of the file
     * @throws OnlineConnectionException when not connected to the internet
     */
    public String retrieveImage(String fileName) throws OnlineConnectionException {
        try {
            downloadImage(fileName);
        } finally {
            return fileName.replaceAll("[^A-Za-z0-9\\[\\]]", "") + ".png";
        }
    }

    /**
     * Returns the online url from this object instance.
     *
     * @return the online url
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Downloads the image from the online url. Is a helper method.
     * @param fileName the name of the file to be saved.
     * @throws OnlineConnectionException when not connected online.
     */
    private void downloadImage(String fileName) throws OnlineConnectionException {
        try (InputStream in = new URL(imageUrl).openStream()) {
            File parent = new File(IMAGE_CACHE_LOCATION);

            if (!parent.exists()) {
                parent.mkdirs();
            }

            Files.copy(in, Paths.get(IMAGE_CACHE_LOCATION + File.separator
                    + fileName.replaceAll("[^A-Za-z0-9\\[\\]]", "") + ".png"));
        } catch (FileAlreadyExistsException f) {
            System.err.println("Duplicate image");
        } catch (IOException e) {
            System.err.println(e.getCause());
            throw new OnlineConnectionException("No internet connection at downloading image");
        }
    }
}
