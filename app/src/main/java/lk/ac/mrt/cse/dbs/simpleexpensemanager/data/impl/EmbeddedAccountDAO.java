package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import ...

public class EmbeddedAccountDAO extends SQLiteOpenHelper implements AccountDAO {

    public static final String DATABASE_NAME = "170531T";
    public static final String CONTACTS_COLUMN_ACC = "accountno";
    public static final String CONTACTS_COLUMN_BANK = "bankname";
    public static final String CONTACTS_COLUMN_USER_NAME = "username";
    public static final String CONTACTS_COLUMN_BALANCE = "balance";

    public EmbeddedTransactionDAO(context context){super (context, DATABASE_NAME , null,1);}

    @override
    public void onCreate(SQLiteDatabase database){
        database.execSQL("create table accountTable"+ ("accountno text primary key, bankname text, username text, balance double"));

        database.execSQL("create table transactionTable"+ ("accountno text,type text, data BLOB, amount double"));

    }

    @override
    public void onUpgrade(SQLiteDatabase databse, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS accountTable");
        onCreate(database);
    }

    @override
    public List<String> getAccNoLists(){
        ArrayList<String> no_list = new ArrayList<String>();

        SQLiteDatabase database = this.getReadableDatabase();
        cursor point = database.rawQuery("Select * from accountTable",null);
        point.movetoFirst();

        while (point.isAfterlast() == false){
            no_list_list.add(point.getString(point.getColumnIndex(CONTACTS_COLUMN_ACC)));
            point.moveTONext();
        }
        return no_list_list;
    }

    @override
    public List<Account> getAccList(){
        ArrayList<Account> acc_list = new ArrayList<Account>();

        SQLiteDatabasse databasse = this.getReadableDatabase();
        cursor point = database.rawquery("select * from accountTable", null);
        point.moveToFirst();

        while(point.isAfterLast() == false){
            String accountNO= res.getString(res.getColumnIndex(CONTACTS_COLUMN_ACC));
            String bankName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_BANK_NAME));
            String accountUserName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_USER_NAME));
            Double balance = res.getDouble(res.getColumnIndex(CONTACTS_COLUMN_BALANCE));

            array_list.add(new Account(accountNO,bankName,accountUserName,balance));
            point.moveToNext();
        }
        return acc_list;
    }

    @Override
    public Account getAccount(String accountNO) throws InvalidAccountException {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor point =  database.rawQuery( "select * from accountTable where id="+accountNO+"", null );

        String accountNO = res.getString(res.getColumnIndex(CONTACTS_COLUMN_ACC));
        String bankName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_BANK_NAME));
        String accountUserName = res.getString(res.getColumnIndex(CONTACTS_COLUMN_USER_NAME));
        Double balance = res.getDouble(res.getColumnIndex(CONTACTS_COLUMN_BALANCE));

        return  new Account(accountNO,bankName,accountUserName,balance);
    }
    @Override
    public void addAccount(Account account) {
        String accountNo = account.getAccountNo();
        String bankName = account.getBankName();
        String userName = account.getAccountUserName();
        Double balance = account.getBalance();


        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountno", accountNo);
        contentValues.put("bankname", bankName);
        contentValues.put("username", userName);
        contentValues.put("balance", balance);

        database.insert("accountTable", null, contentValues);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("accountTable",
                "accountno = ? ",
                new String[] { accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
    }
}