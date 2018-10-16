package com.computor;


import java.util.ArrayList;
import java.util.HashMap;
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

    static void solveForX(double a, double b, double c)
    {

        ArrayList<Double> answers = new ArrayList<>();
        if (a == 0 && b == 0 && c != 0)
        {
            System.out.println("There exit no solutions for this polynomial.");
            return;
        }

        if (a == 0 && b == 0 && c == 0)
        {
            System.out.println("The set of real numbers is the solution.");
            return;
        }
        if (a == 0)
        {
            System.out.println("There's one solution.\n" + (-(c / b)));
            return;
        }
        double discriminant = (b * b) - (4 * a * c);
        if (discriminant == 0)
        {
            System.out.println("The disciminant is: " + discriminant + "\nThere is one solution.");
            double ans = (-b + sqrt(discriminant, 0.00000001)) / (2 * a);
            System.out.println(ans);
        }
        else if (discriminant > 0)
        {
            System.out.println("The disciminant is: " + discriminant + "\nThere are two solutions.");
            double ans1 = (-b + sqrt(discriminant, 0.00000001)) / (2 * a);
            double ans2 = (-b - sqrt(discriminant, 0.00000001)) / (2 * a);
            System.out.println(ans1);
            System.out.println(ans2);
        }
        else
        {
            System.out.println("The disciminant is: " + discriminant + "\nThere two complex solutions.");
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
        if (split.length != 2)
        {
            System.out.print("Invalid equation.");
            System.exit(0);
        }
        String sideString  = side.equals("LHS") ? split[0].trim(): split[1].trim();
        ArrayList<Integer> signs = getSigns(sideString);
        String[] tempTerms = sideString.split("\\+|-");
        boolean firstTermSign = tempTerms.length == signs.size();
        if (sideString.charAt(sideString.length() - 1) == '+' || sideString.charAt(sideString.length() - 1) =='-')
        {
            System.out.println("Cant solve equation. Too many Signs detected");
            System.exit(0);
        }
        for (int index = 0; index < tempTerms.length; index++)
        {
            if (index == 0 && tempTerms[index].equals(""))
                continue;
            if (isValidTerm(tempTerms[index]))
            {
                String[] factors = tempTerms[index].split("\\*");
                double coefficient = Double.parseDouble(factors[0].trim());
                int power = Integer.parseInt(factors[1].trim().split("\\^")[1].trim());
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
        if (factors.length != 2 || !factors[0].trim().matches("\\d+(\\.\\d+)?") || !factors[1].trim().matches("X\\s?\\^\\s?[012]"))
        {
            System.out.println("Can't solve equation. Invalid term detected: " + termString);
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
        ArrayList<Term> reducedForm = new ArrayList<>();
        for (int power = 0; power < 3; power++)
        {

            double new_coefficient = 0;
            for (Term term : lhsTerms)
            {
                if (term.getPower() == power)
                {
                    new_coefficient += term.getSign() * term.getCo_efficient();
                }

            }
            for (Term term : rhsTerms)
            {
                if (term.getPower() == power)
                {
                    new_coefficient += -1 * (term.getSign() * term.getCo_efficient());
                }
            }

            if (new_coefficient != 0)
            {
                reducedForm.add(new Term(dabs(new_coefficient), power, new_coefficient > 0? 1 : -1));
            }
        }
        return (reducedForm);
    }

    private static  void printReducedForm(ArrayList<Term> reducedForm)
    {
        System.out.print("Reduced form: ");
        for (int i = 0; i< reducedForm.size(); i++)
        {
            if (i == 0)
            {
                if (reducedForm.get(i).getSign()  < 0)
                    System.out.print("-" + reducedForm.get(i).getCo_efficient() + " * X^" + reducedForm.get(i).getPower() + " ");
                else
                    System.out.print(reducedForm.get(i).getCo_efficient() + " * X^" + reducedForm.get(i).getPower() + " ");
            }
            else
            {
                if (reducedForm.get(i).getSign()  < 0)
                    System.out.print("- " + reducedForm.get(i).getCo_efficient() + " * X^" + reducedForm.get(i).getPower() + " ");
                else
                    System.out.print("+ " + reducedForm.get(i).getCo_efficient() + " * X^" + reducedForm.get(i).getPower() + " ");
            }
        }
        System.out.println("= 0\nPolynomial degree: " + (reducedForm.size() > 0 ? reducedForm.size() - 1 : "0"));
    }

    private static  Map<String, Double> getABC(ArrayList<Term> reducedForm)
    {
        Map<String, Double> abc = new HashMap<>();

        for(Term term : reducedForm)
        {
            if (term.getPower() == 0)
                abc.put("c", term.getSign() * term.getCo_efficient());
            else if (term.getPower() == 1)
                abc.put("b", term.getSign() * term.getCo_efficient());
            else //if (term.getPower() == 2)
                abc.put("a", term.getSign() * term.getCo_efficient());
        }
        if (!abc.containsKey("a"))
            abc.put("a", 0.0);
        if (!abc.containsKey("b"))
            abc.put("b", 0.0);
        if (!abc.containsKey("c"))
            abc.put("c", 0.0);

        return (abc);
    }



    public static void main(String[] args)
    {
        ArrayList<Term> lhsTerms = getTerms(args[0], "LHS");
        ArrayList<Term> rhsTerms = getTerms(args[0], "RHS");
        ArrayList<Term> reducedForm = getReducedForm(lhsTerms, rhsTerms);
        printReducedForm(reducedForm);
        Map<String, Double> abc = getABC(reducedForm);
        solveForX(abc.get("a"), abc.get("b"), abc.get("c"));
    }
}