package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class EmbeddedTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {
    public static final String DATABASE_NAME = "170531T";
    public static final String EXPENSE_COLUMN_ID = "id";
    public static final String EXPENSE_COLUMN_ACC = "accountno";
    public static final String EXPENSE_COLUMN_DATE = "date";
    public static final String EXPENSE_COLUMN_TYPE = "type";
    public static final String EXPENSE_COLUMN_AMOUNT = "amount";

    private List<Transaction> transactions;

    public EmbeddedTransactionDAO(Context context) {
        super(context, DATABASE_NAME , null,1);
        transactions = new LinkedList<>();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS transactiontable");
        onCreate(database);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        String accountNum= transaction.getAccountNo();
        Date date = transaction.getDate();

        byte[] byteDate = date.toString().getBytes();
        ExpenseType types = transaction.getExpenseType();
        String strType = types.toString();
        byte[] byteType = toString().getBytes();
        Double amounts = transaction.getAmount();

        Calendar c = Calendar.getInstance();

        SimpleDateFormat datef = new SimpleDateFormat("dd-MMM-yyyy");
        String formatDate = datef.format(c.getTime());
        Log.d("Date",formatDate);
        byte[] timeStamp = formatDate.getBytes();

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountno", accountNo);
        contentValues.put("amount", amounts);
        contentValues.put("type",strType);
        contentValues.put("date", byteDate);

        database.insert("transactiontable", null, contentValues);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        transactions.clear();
        Log.d("creation","starting");
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor point =  database.rawQuery( " select * from transactiontable", null );

        point.moveToFirst();

        while(point.isAfterLast() == false){

            String accountNo = res.getString(res.getColumnIndex(EXPENSE_COLUMN_NO));
            Double amount = res.getDouble(res.getColumnIndex(EXPENSE_COLUMN_AMOUNT));
            String transactionType = res.getString(res.getColumnIndex(EXPENSE_COLUMN_TYPE));

            ExpenseType transType = ExpenseType.valueOf(transactionType);
            byte[] date = point.getBlob(res.getColumnIndex(EXPENSE_COLUMN_DATE));

            String str = new String(date, StandardCharsets.UTF_8);
            Log.d("loadedDate",str);

            Date finalDate;
            try {
                SimpleDateFormat iFormat = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss 'GMT'z", Locale.ENGLISH);
                finalDate = iFormat.parse(str);
                transactions.add(new Transaction(finalDate,accountNo,transType,amount));
                Log.d("creation","success");
            }catch (java.text.ParseException e){
                Log.d("creation","failed");
                Calendar cal = Calendar.getInstance();

                finalDate = cal.getTime();
                transactions.add(new Transaction(finalDate,accountNo,transType,amount));
            }
            point.moveToNext();
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        return transactions.subList(size - limit, size);
    }
}