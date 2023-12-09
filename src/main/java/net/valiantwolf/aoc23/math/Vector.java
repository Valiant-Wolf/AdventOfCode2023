package net.valiantwolf.aoc23.math;

import net.valiantwolf.aoc23.util.MathUtil;

public record Vector(int x, int y)
{
    public static final Vector IDENTITY = new Vector( 1, 1 );
    public static final Vector UNIT_X = new Vector( 1, 0 );
    public static final Vector UNIT_Y = new Vector( 0, 1 );
    public static final Vector ORIGIN = new Vector( 0, 0 );

    public Vector add( Vector that )
    {
        return new Vector( this.x + that.x, this.y + that.y );
    }

    public Vector subtract( Vector that )
    {
        return new Vector( this.x - that.x, this.y - that.y );
    }

    public Vector multiply( int scalar )
    {
        return new Vector( this.x * scalar, this.y * scalar );
    }

    public int chebyshev()
    {
        return Math.max( Math.abs( x ), Math.abs( y ) );
    }

    public int manhattan()
    {
        return Math.abs( x ) + Math.abs( y );
    }

    public Vector clamp( int max )
    {
        int abs = Math.abs( max );
        return clamp( -abs, abs );
    }

    public Vector clamp( int min, int max )
    {
        return new Vector( MathUtil.clamp( x, min, max ), MathUtil.clamp( y, min, max ) );
    }
}
