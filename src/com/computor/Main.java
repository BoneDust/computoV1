package com.computor;

import org.omg.CORBA.SystemException;

import javax.print.DocFlavor;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Map;

public class Main
{
    static double dabs(double num)
    {
        if (num < 0)
            return (-num);
        return (num);
    }

    static double sqrt(double num, final double accuracy)
    {

        double start = (int)(num/2);
        while (start *start > num)
            start -= 1;
        start++;
        double ans = start;
        while (true)
        {
           if (ans * ans == num || dabs(ans * ans - num) <= accuracy)
               return (ans);
           ans = (ans + num / ans) / 2;
        }

    }

    static void solveForX(int a, int b, int c)
    {

        ArrayList<Double> answers = new ArrayList<>();
        if (a == 0 && b == 0 && c != 0)
        {
            System.out.println("There exit no solutions for this polynomial");
            return;
        }

        if (a == 0 && b == 0 && c == 0)
        {
            System.out.println("The set of real numbers is the solution");
            return;
        }
        
        double discriminant = (b * b) - (4 * a * c);
        if (discriminant == 0)
        {
            System.out.println("The disciminant is: " + discriminant + "There is one solution");
            double ans = (-b + sqrt(discriminant, 0.00000001)) / (2 * a);
            System.out.println(ans);
        }
        else if (discriminant > 0)
        {
            System.out.println("The disciminant is: " + discriminant + "There are two solutions");
            double ans1 = (-b + sqrt(discriminant, 0.00000001)) / (2 * a);
            double ans2 = (-b - sqrt(discriminant, 0.00000001)) / (2 * a);
            System.out.println(ans1);
            System.out.println(ans2);
        }
        else
        {
            System.out.println("The disciminant is: " + discriminant + "There two complex solutions");
            double firstPart = (-b / (2 * a)), secondPart = sqrt(discriminant, 0.00000001)/ (2 * a);
            String complex1 = firstPart + " + " + secondPart + "i";
            String complex2 = firstPart + " - " + secondPart + "i";
            System.out.println(complex1);
            System.out.println(complex2);
        }
    }

    private static ArrayList<Term> getTerms(String equation, String side)
    {
        ArrayList<Term> terms = new ArrayList<>();

        String[] split = equation.split("=");
        String sideString  = side.equals("LHS") ? split[0] : split[1];
        ArrayList<Integer> signs = getSigns(sideString);
        String[] tempTerms = sideString.split("\\+|-");
        boolean firstTermSign = tempTerms.length == signs.size();
        if (tempTerms.length < signs.size())
        {
            System.out.println("To many Signs detected");
            System.exit(0);
        }
        for (int index = 0; index < tempTerms.length; index++)
        {
            if (isValidTerm(tempTerms[index]))
            {
                String[] factors = tempTerms[index].split("\\*");
                double coefficient = Double.parseDouble(factors[0].trim());
                int power = Integer.parseInt(factors[1].trim().split("^")[1]);//todo not sure about ^
                int sign;
                if (index == 0)
                {
                    if (firstTermSign)
                        sign = signs.get(0);
                    else
                        sign = 1;
                }
                else
                {
                    if (firstTermSign)
                        sign = signs.get(index);
                    else
                        sign = signs.get(index - 1);
                }
                terms.add(new Term(coefficient, power, sign));
            }
        }
        return (terms);
    }

    private static boolean isValidTerm(String termString)
    {
        String[] factors = termString.split("\\*");
        if (factors.length != 2 || !factors[0].trim().matches("^\\d+(\\.\\d+)?$") ||
           !factors[1].trim().matches("X^\\d+$"))//todo not so sure here
        {
            System.out.println("Invalid term detected: " + termString);
            System.exit(0);
        }
        return (true);
    }

    private static ArrayList<Integer> getSigns(String line)
    {
        ArrayList<Integer> signs = new ArrayList<>();
        for(char c: line.toCharArray())
        {
            if (c == '+')
                signs.add(1);
            if (c == '-')
                signs.add(-1);
        }
        return (signs);
    }

    private static ArrayList<Term> getReducedForm( ArrayList<Term> lhsTerms, ArrayList<Term> rhsTerms)
    {
        ArrayList<Term> reducedForm = new ArrayList<>()
        {


        }
    }



    public static void main(String[] args)
    {
        ArrayList<Term> lhsTerms = getTerms(args[1], "LHS");
        ArrayList<Term> rhsTerms = getTerms(args[1], "RHS");
        ArrayList<Term> reducedForm = getReducedForm(lhsTerms, rhsTerms);
        printReducedForm(reducedForm);
        Map<String, Integer> abc = getABC(reducedForm);
        solveForX(abc.get("a"), abc.get("b"), abc.get("c"));
    }
}