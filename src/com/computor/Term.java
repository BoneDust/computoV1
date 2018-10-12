package com.computor;

public class Term
{
    private double co_efficient;
    private int sign;
    private int power;

    public Term(double co_efficient, int power, int sign)
    {
        this.co_efficient = co_efficient;
        this.power = power;
        this.sign = sign;
    }

    public double getCo_efficient()
    {
        return co_efficient;
    }

    public int getSign()
    {
        return sign;
    }

    public int getPower()
    {
        return power;
    }
}
