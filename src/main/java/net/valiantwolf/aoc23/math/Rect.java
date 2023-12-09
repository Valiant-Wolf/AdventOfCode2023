package net.valiantwolf.aoc23.math;

public record Rect(Vector lowerBound, Vector upperBound) {

    public Rect(Vector lowerBound, Vector upperBound) {

        boolean x, y;

        x = lowerBound.x() > upperBound.x();
        y = lowerBound.y() > upperBound.y();

        if (x) {
            if (y) {
                this.lowerBound = upperBound;
                this.upperBound = lowerBound;
            } else {
                this.lowerBound = new Vector(upperBound.x(), lowerBound.y());
                this.upperBound = new Vector(lowerBound.x(), upperBound.y());
            }
        } else if (y) {
            this.lowerBound = new Vector(lowerBound.x(), upperBound.y());
            this.upperBound = new Vector(upperBound.x(), lowerBound.y());
        } else {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }
    }

    public boolean contains(Vector point) {
        return point.x() >= this.lowerBound.x()
                && point.y() >= this.lowerBound.y()
                && point.x() < this.upperBound.x()
                && point.y() < this.upperBound.y();
    }

    public boolean intersects(Rect that) {
        return !(that.upperBound.x() <= this.lowerBound.x()
                || that.lowerBound.x() >= this.upperBound.x()
                || that.upperBound.y() <= this.lowerBound.y()
                || that.lowerBound.y() >= this.upperBound.y());
    }

    public boolean contains(Rect that) {
        return that.lowerBound.x() >= this.lowerBound().x()
                && that.lowerBound.y() >= this.lowerBound.y()
                && that.upperBound.x() <= this.upperBound.x()
                && that.upperBound.y() <= this.upperBound.y();
    }

}
