package com.company;

import java.io.*;
import java.util.*;
import java.text.*;

public class Main {

    static class compare implements Comparator<bankAccount>
    {
        public int compare(bankAccount bA1, bankAccount bA2)
        {
            return (bA1.balance < bA2.balance) ? 1 : -1;
        }

    }

    static class compareDoubles implements Comparator<Double>
    {
        public int compare(Double d1, Double d2)
        {
            return (d1 < d2) ? 1 : -1;
        }
    }

    public static void main(String[] args) throws IOException{
        String s;
        bankAccount acc = new bankAccount("Bennett", 91010);

        File baFile = new File("bankAccounts.txt");
        baFile.createNewFile();

        FileWriter baFileWriter = new FileWriter(baFile);

        ArrayList<bankAccount> baList = new ArrayList();
        ArrayList<Double> baListDeposit = new ArrayList();

        NumberFormat fmt = NumberFormat.getNumberInstance();
        fmt.setMinimumFractionDigits(2);
        fmt.setMaximumFractionDigits(2);
        String name;
        ArrayList aryLst = new ArrayList();
        ListIterator iter = aryLst.listIterator();
        do {
            Scanner kbReader = new Scanner(System.in);
            System.out.print("Enter the account holders name: ");
            name = kbReader.nextLine();

            if(name.equalsIgnoreCase("debug")){
                while(true) {
                    System.out.println("Do you want to check deposits, balances, or do you want to drain another account into Bennett's bank account? ('EXIT' to leave the debug menu)");
                    String sDebug = kbReader.next();

                    if(sDebug.equalsIgnoreCase("deposits")){
                        Collections.sort(baListDeposit, new compareDoubles());

                        for (int i = 0; i < baListDeposit.size(); i++) {
                            System.out.print(baListDeposit.get(i) + ", ");
                        }

                        System.out.println(" ");
                    }

                    else if(sDebug.equalsIgnoreCase("balances")){
                        Collections.sort(baList, new compare());
                        for (int i = 0; i < baList.size(); i++) {
                            System.out.print(baList.get(i).balance + ", ");
                        }
                        System.out.println(" ");
                    }

                    else if(sDebug.equalsIgnoreCase("drain")){
                        System.out.println("What account do you want to drain into Bennett");
                        sDebug = kbReader.next();

                        for (int i = 0; i < baList.size(); i++) {
                            if(sDebug.equalsIgnoreCase(baList.get(i).name)){
                                double money = baList.get(i).balance;
                                baList.get(i).balance = 0;
                                System.out.println(baList.get(i).name + " lost " + money + ". They now have $" + fmt.format(baList.get(i).balance) + ".");

                                acc.balance += money;
                                System.out.println("Bennett now has " + fmt.format(acc.balance) + ".");
                            }
                        }
                    }
                    else if(sDebug.equalsIgnoreCase("exit")){
                        System.out.println("test" + "\n");
                        break;
                    }

                }
            }

            else if (!name.equalsIgnoreCase("EXIT")) {
                baFileWriter.write(bankAccount.getTime() + " " + name);

                System.out.print("Please enter the amount of the deposit. ");
                double amount = kbReader.nextLong();

                baListDeposit.add(amount);
                baFileWriter.write(" " + Double.toString(amount));

                System.out.println(" "); // gives an eye pleasing blank line
                bankAccount theAccount = new bankAccount(name, amount);
                baList.add(theAccount);
                iter.add(theAccount);
                baFileWriter.write("\n");

                while(true) {
                    System.out.println("Do you want to withdraw or deposit money into any account? ('deposit', 'withdraw', or 'no')");
                    s = kbReader.next();

                    if (s.equalsIgnoreCase("withdraw")) {
                        System.out.println("What account do you want to withdraw money from?");
                        s = kbReader.next();

                        for(int i = 0; i < baList.size(); i++) {

                            if ( s.equals(baList.get(i).name) ){
                                System.out.println("What the the amount you want to withdraw");
                                int amt = kbReader.nextInt();

                                baList.get(i).withdraw(amt);
                                baFileWriter.write(" " + bankAccount.getTime() + " " + baList.get(i).name + " withdrew " + amt + " dollars");
                                System.out.println(baList.get(i).name + " withdrew " + amt + " dollars");
                            }

                        }
                        System.out.println(" ");
                        break;
                    }

                    if (s.equalsIgnoreCase("deposit")) {
                        System.out.println("What account do you want to deposit money into?");
                        s = kbReader.next();

                        for(int i = 0; i < baList.size(); i++) {

                            if ( s.equals(baList.get(i).name) ){
                                System.out.println("What the the amount you want to deposit");
                                Double amt = kbReader.nextDouble();

                                baList.get(i).deposit(amt);
                                baListDeposit.add(amt);
                                baFileWriter.write( bankAccount.getTime() + " " + baList.get(i).name + " deposit " + amt + " dollars");
                                System.out.println(baList.get(i).name + " deposit " + amt + " dollars");
                            }


                        }
                        System.out.println(" ");
                        break;
                    }
                    if (s.equalsIgnoreCase("no")) {
                        System.out.println("test" + "\n");
                        break;
                    }
                    else {
                        System.out.println("Please retry");
                    }
                }
            }



        } while (!name.equalsIgnoreCase("EXIT"));
        baFileWriter.close();
        bankAccount ba = (bankAccount) iter.previous();
        double maxBalance = ba.balance;
        String maxName = ba.name;
        while (iter.hasPrevious()) {
            ba = (bankAccount) iter.previous();
            if (ba.balance > maxBalance) {
                maxBalance = ba.balance;
                maxName = ba.name;
            }
        }
        System.out.println(" ");
        System.out.println("The account with the largest balance belongs to "
                + maxName + ".");
        System.out.println("The amount is $" + fmt.format(maxBalance) + ".");

    }
}
