package net.valiantwolf.aoc23.util;

public abstract class MathUtil
{
    public static int clamp( int value, int min, int max )
    {
        return value < min
               ? min
               : Math.min( value, max );
    }
}
