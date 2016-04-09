package org.alexiwilius.ranti_app.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntDef;

import org.alexiwilius.ranti_app.util.Cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AlexiWilius on 30.9.2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    @IntDef({READABLE, WRITABLE})
    private @interface DatabaseTypes {
    }

    public static final int READABLE = 0;
    public static final int WRITABLE = 1;

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    private final String dbName, path;

    private static Map<String, SQLiteDatabase> dbMap;

    public static boolean updateIfAvailable(Context context, String dbName, String version) throws IOException {
        String path = dbName + ".version";
        if (!Cache.exists(path) || !Cache.get(path).equals(version)) {
            copyDatabase(context, dbName);
            Cache.set(path, version);
            return true;
        }

        return false;
    }

    public static SQLiteDatabase getInstance(Context context, String dbName, @DatabaseTypes int type)
            throws IOException {
        if (dbMap == null)
            dbMap = new HashMap<>();

        if (!dbMap.containsKey(dbName)) {
            DatabaseManager manager = new DatabaseManager(context, dbName);
            manager.openDataBase();
            dbMap.put(dbName + "." + type, manager.getReadableDatabase());
        }

        return dbMap.get(dbName + "." + type);
    }

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    private DatabaseManager(Context context, String dbName) {
        super(context, dbName, null, 1);
        this.myContext = context;
        this.dbName = dbName;
        this.path = myContext.getDatabasePath(dbName).getPath();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (!dbExist) {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            getReadableDatabase();

            try {

                copyDatabase(myContext, dbName);

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        try {
            SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

            checkDB.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private static void copyDatabase(Context context, String dbName) throws IOException {

        //Open your local db as the input stream
        InputStream myInput = context.getAssets().open(dbName);

        File f = context.getDatabasePath(dbName),
                parent = new File(f.getParent());
        if (parent.exists() || !parent.exists() && parent.mkdir()) {

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(f);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
    }

    public void openDataBase() throws SQLException, IOException {

        createDataBase();
        //Open the database
        myDataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}