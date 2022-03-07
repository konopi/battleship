package konopi.battleship.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OceanGridTest {
    private OceanGrid og;

    @BeforeEach
    void setUp() {
        og = new OceanGrid(10, 10);
    }

    @Test
    void testAddShip() {
        /* should be added successfully */
        Ship inBounds = new Ship(new Coordinates("B5"), 4,
                Ship.Orientation.VERTICAL, "Battleship");
        /* is out of bounds for specified grid, should fit in the locally defined grid */
        Ship outOfBounds = new Ship(new Coordinates("G14"), 6,
                Ship.Orientation.HORIZONTAL, "Carrier");
        /* overlaps with inBounds, so it shouldn't get added, should fit in the locally defined grid */
        Ship overlap = new Ship(new Coordinates("B4"), 2,
                Ship.Orientation.HORIZONTAL, "Destroyer");

        OceanGrid localGrid = new OceanGrid(24, 24);

        assertTrue(og.addShip(inBounds));
        assertFalse(og.addShip(outOfBounds));
        assertFalse(og.addShip(overlap));

        assertTrue(localGrid.addShip(outOfBounds));
        assertTrue(localGrid.addShip(overlap));
    }

    @Test
    void testShoot() {
        Ship s = new Ship(new Coordinates("A3"), 2, Ship.Orientation.VERTICAL, "Cruiser");
        og.addShip(s);

        OceanGrid.ShotResult z15 = og.shoot(new Coordinates("Z15")); // out of bounds

        OceanGrid.ShotResult a4 = og.shoot(new Coordinates("A4")); // miss
        OceanGrid.ShotResult b3 = og.shoot(new Coordinates("B3")); // hit
        OceanGrid.ShotResult a3 = og.shoot(new Coordinates("A3")); // sink

        assertNull(z15);

        assertEquals(Ship.HitDesignation.MISS, a4.hitDesignation());
        assertEquals(Ship.HitDesignation.HIT, b3.hitDesignation());
        assertEquals(Ship.HitDesignation.SINK, a3.hitDesignation());

        assertNull(a4.shipName());
        assertEquals("Cruiser", b3.shipName());
        assertEquals("Cruiser", a3.shipName());
    }

    @Test
    void testGetActiveShipAmount() {
        assertEquals(0, og.getActiveShipAmount());

        Ship s = new Ship(new Coordinates("A3"), 2, Ship.Orientation.VERTICAL, "Cruiser");
        og.addShip(s);
        assertEquals(1, og.getActiveShipAmount());

        og.shoot(new Coordinates("A3")); // hit
        assertEquals(1, og.getActiveShipAmount());

        og.shoot(new Coordinates("B3")); // sink
        assertEquals(0, og.getActiveShipAmount());
    }
    
    @Test
    void testGetHitMap() {
        Ship s = new Ship(new Coordinates("A3"), 2, Ship.Orientation.VERTICAL, "Cruiser");
        og.addShip(s);

        OceanGrid.ShotResult z15 = og.shoot(new Coordinates("Z15")); // out of bounds

        OceanGrid.ShotResult a4 = og.shoot(new Coordinates("A4")); // miss
        OceanGrid.ShotResult b3 = og.shoot(new Coordinates("B3")); // hit
        OceanGrid.ShotResult a3 = og.shoot(new Coordinates("A3")); // sink

        assertNull(og.getHitMap().get(new Coordinates("Z15")));

        assertEquals(3, og.getHitMap().size());

        assertEquals(Ship.HitDesignation.MISS, og.getHitMap().get(new Coordinates("A4")));
        assertEquals(Ship.HitDesignation.HIT, og.getHitMap().get(new Coordinates("B3")));
        assertEquals(Ship.HitDesignation.SINK, og.getHitMap().get(new Coordinates("A3")));
    }
}
