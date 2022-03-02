package konopi.battleship.logic;

import java.util.Objects;

import static java.lang.Math.abs;

/**
 * The Coordinates class is used to reference game coordinates.
 * It accepts coordinates up to letter 'Z'. Rightward expansion is limited by integer size.
 */
public class Coordinates {
    /**
     * Horizontal index for internal representation.
     */
    private final int x;
    /**
     * Vertical index for internal representation.
     */
    private final int y;

    /**
     * Main constructor.
     * @param letterNumber Game coordinates written as text, e.g. 'A5' or 'A-5'.
     */
    public Coordinates(String letterNumber) {
        String numberStr = letterNumber.substring(1);
        int number;
        try {
            number = abs(Integer.parseInt(numberStr));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Coordinate number must be an integer: " + numberStr);
        }

        char letter = Character.toUpperCase(letterNumber.charAt(0));

        if (number == 0) {
            throw new IllegalArgumentException("Coordinate number must be > 0: " + number);
        }
        if (letter < 'A' || letter > 'Z') {
            throw new IllegalArgumentException("Coordinate letter must be a latin character: " + letter);
        }

        x = number - 1;
        y = letter - 'A';
    }

    /**
     * Private constructor setting the internal representation directly.
     * Used in {@link #getOffset(int, int) getOffset}.
     * @param x {@link #x}
     * @param y {@link #y}
     */
    private Coordinates(int x, int y) {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Get coordinates offset by specified amount.
     * Letters increase downwards, numbers increase rightwards.
     * @param letterOffset Vertical difference, increases down.
     * @param numberOffset Horizontal difference, increases right.
     * @return New {@link Coordinates} object offset by the specified amount.
     * @throws IllegalArgumentException The offset coordinates are smaller than 'A1' on one of the axes.
     */
    public Coordinates getOffset(int letterOffset, int numberOffset) throws IllegalArgumentException {
        if (letterOffset == 0 && numberOffset == 0) return this;

        int offsetX = x + numberOffset;
        int offsetY = y + letterOffset;

        return new Coordinates(offsetX, offsetY);
    }

    @Override
    public String toString() {
        int number = x + 1;
        char letter = (char)(y + 'A');
        return letter + String.valueOf(number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
