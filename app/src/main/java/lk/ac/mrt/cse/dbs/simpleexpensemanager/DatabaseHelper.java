package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String ACCOUNT = "Account";
    public static final String ACCOUNT_NUMBER = "Account_number";
    public static final String BANK = "Bank";
    public static final String ACCOUNT_HOLDER = "Account_Holder";
    public static final String INITIAL_BALANCE = "Initial_Balance";
    public static final String TRANSACTIONS = "Transactions";

    public static final String EXPENSE_TYPE = "expenseType";
    public static final String AMOUNT = "amount";
    public static final String DATE_VALUE = "date_value";
    private static DatabaseHelper instance = null;
    private static final String DATABASE_NAME = "200327L.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + ACCOUNT + " (" + ACCOUNT_NUMBER + " PRIMARY KEY," + BANK + " TEXT," + ACCOUNT_HOLDER + " TEXT, " + INITIAL_BALANCE + " REAL check("+INITIAL_BALANCE+">= 0));";
        sqLiteDatabase.execSQL(createTableStatement);
        String sqlQuery = ("CREATE TABLE IF NOT EXISTS " + TRANSACTIONS + "(Id INTEGER PRIMARY KEY AUTOINCREMENT," + ACCOUNT_NUMBER + " TEXT," + EXPENSE_TYPE + " TEXT," + AMOUNT + " REAL check("+AMOUNT+">= 0), " + DATE_VALUE + " Date);");
        sqLiteDatabase.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static SQLiteDatabase getDatabaseWrite() throws Exception {
        if (instance != null) {
            return instance.getWritableDatabase();
        } else {
            throw new Exception("Database not initialized");
        }
    }

    public SQLiteDatabase getDatabaseRead() {
        return instance.getReadableDatabase();
    }

}
