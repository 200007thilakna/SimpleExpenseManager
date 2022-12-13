package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper.ACCOUNT;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper.ACCOUNT_HOLDER;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper.ACCOUNT_NUMBER;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper.BANK;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper.INITIAL_BALANCE;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class InDatabaseAccountDAO implements AccountDAO {
    private SQLiteDatabase db;

    {
        try {
            db = DatabaseHelper.getDatabaseWrite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<String> getAccountNumbersList() {
        List<String> returnList = new ArrayList<>();
        String queryString = "SELECT " +ACCOUNT_NUMBER + " FROM " + ACCOUNT + "";
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // loop through the cursor(result set) and insert in to returnList
            // put them into the return list

            do {
                String accNumber = cursor.getString(0);
                returnList.add(accNumber);
            } while (cursor.moveToNext());
        }
        return returnList;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor cursor = db.rawQuery("Select * from Account;",null);
        List<Account> returnList = new ArrayList<Account>();

        if(cursor.moveToFirst()){
            do {
                returnList.add( new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2), Double.parseDouble(cursor.getString(3) ) ));

            } while (cursor.moveToNext());
        }



        return returnList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor cursor = db.rawQuery("SELECT * FROM Account WHERE  Account_number='" + accountNo+"';", null);
        cursor.moveToFirst();
        if (cursor.isAfterLast()) {
            throw new InvalidAccountException("Invalid!");
        }
        return new Account(cursor.getString(0), cursor.getString(1),cursor.getString(2), Double.parseDouble(cursor.getString(3)));

    }
    @Override
    public void addAccount(Account account) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ACCOUNT_NUMBER,account.getAccountNo());
        cv.put(BANK,account.getBankName());
        cv.put(ACCOUNT_HOLDER,account.getAccountHolderName());
        cv.put(INITIAL_BALANCE,account.getBalance());

        long insert =db.insert(ACCOUNT,null, cv);



    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
      db.execSQL("DELETE FROM Account WHERE Account_number='"+accountNo+"';");
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account = getAccount(accountNo);

        double balance = account.getBalance();
        switch (expenseType) {
            case EXPENSE:
                balance-=amount;
            case INCOME:
                balance += amount;

        }


       db.execSQL("UPDATE Account SET Initial_Balance='"+balance+"' WHERE Account_number='"+accountNo+"';");
    }


}
