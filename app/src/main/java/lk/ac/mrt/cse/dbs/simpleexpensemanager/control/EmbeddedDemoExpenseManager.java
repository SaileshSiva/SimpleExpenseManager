package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import...

public class EmbeddedDemoExpenseManager extends ExpenseManager{

    public EmbeddedDemoExpenseManager(){setup();}

    @override
    public void setup(){
        TransactionDAO EmbeddedTransactionDAO = new EmbeddedTransacionDAO(MainActivity.getContext());
        setTransactionDAO(EmbeddedTransactionDAO);

        AccountDAO EmbeddedAccountDAO = new EmbeddedAccountDAO(MainActivity.getContext());
        setAccountDAO (EmbeddedAccountDAO);

        //sample data
        Account sampleAcc1 = new Account('12345A',"Commercial","Anankan",10000.0);
        Account sampleAcc2 = new Account('67890B',"BOC","Thuva",80000.0);

    }
}