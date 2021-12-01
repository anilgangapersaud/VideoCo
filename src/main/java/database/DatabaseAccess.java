package database;

/**
 * Any new repository class should implement these methods to mimic database
 */
public interface DatabaseAccess {

    /**
     * update the csv data in database
     */
    void update();

    /**
     * load the csv data into app
     */
    void load();

}
