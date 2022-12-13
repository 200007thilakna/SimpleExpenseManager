package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class InDatabaseTransactionDAO implements TransactionDAO {

   private SQLiteDatabase db;

    {
        try {
            db = DatabaseHelper.getDatabaseWrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        db.execSQL("INSERT INTO Transactions VALUES(null,'"+accountNo+"','"+expenseType.toString()+"','"+amount+"','"+formatter.format(date)+"');");

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<>();
        Cursor mCursor = db.rawQuery("select * from Transactions", null);
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {

            String dateString = mCursor.getString(mCursor.getColumnIndex("date_value"));
            try {
                Date datetime = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);

                Transaction transaction = new Transaction(
                        datetime,
                        mCursor.getString(mCursor.getColumnIndex("Account_number")),
                        ExpenseType.valueOf(mCursor.getString(mCursor.getColumnIndex("expenseType"))),
                        mCursor.getDouble(mCursor.getColumnIndex("amount")));
                transactions.add(transaction);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mCursor.close();
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = new ArrayList<>();
        String[] args = {limit+""};
        Cursor mCursor = db.rawQuery("select * from Transactions where Id in (select Id from Transactions order by Id desc limit ?)", args);
        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {

            String dateString = mCursor.getString(mCursor.getColumnIndex("date_value"));
            try {
                Date datetime = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);

                Transaction transaction = new Transaction(
                        datetime,
                        mCursor.getString(mCursor.getColumnIndex("Account_number")),
                        ExpenseType.valueOf(mCursor.getString(mCursor.getColumnIndex("expenseType"))),
                        mCursor.getDouble(mCursor.getColumnIndex("amount")));
                transactions.add(transaction);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mCursor.close();
        return transactions;
    }
}
