package SourceCode;

import java.io.BufferedReader;
import java.util.Scanner;

/**
 * Main class that allows User to interact with a program
 */

public class BibtexSystem {

    public static void main(String []args){
        BibtexReader reader = new BibtexReader();
        try{
            String path = "xampl.bib";
            BufferedReader bufferedReader = reader.prepareFile(path);
            reader.performFileOperation(bufferedReader);
            Scanner s = new Scanner(System.in);
            System.out.println("For printing all publications type \"all\" \nTo search publications enter the searching value \nRemember this is case sensitive");
            String key = s.nextLine().trim();
            System.out.println("Enter a field name to be searched in. \n \"all\" stands for every field");
            String field = s.nextLine().trim();
            System.out.println("Enter a sign to make a table with:");
            String sign = s.next().trim();
            System.out.println(reader.toString(key,sign,field));
            /*
            Example of searching:
            we want every Donald E. Knuth's book:
            -> Donald E. Knuth (the key)
            -> author (field to search)
            -> * (a sign)
             */

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

/*
Wzgledem kodu zaprezentowanego na zajeciach dodano:
-lepszy mechanizm wyszukiwania
-format imie-nazwisko ( Aman, Marcin zostanie przekonwertowane na Marcin Aman)
 */
