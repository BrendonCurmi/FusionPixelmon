package me.fusiondev.fusionpixelmon.forge.impl.inventory;

import org.junit.Test;

import static me.fusiondev.fusionpixelmon.forge.impl.inventory.ForgeInvInventory.getUICoords;
import static org.junit.Assert.assertEquals;

public class ForgeInvInventoryTest {

    @Test
    public void testUICoords() {
        {
            int[] i = getUICoords(0);
            assertEquals(i[0] + "," + i[1], "0,0");
        }
        {
            int[] i = getUICoords(1);
            assertEquals(i[0] + "," + i[1], "0,1");
        }
        {
            int[] i = getUICoords(8);
            assertEquals(i[0] + "," + i[1], "0,8");
        }
        {
            int[] i = getUICoords(9);
            assertEquals(i[0] + "," + i[1], "1,0");
        }
        {
            int[] i = getUICoords(10);
            assertEquals(i[0] + "," + i[1], "1,1");
        }
        {
            int[] i = getUICoords(17);
            assertEquals(i[0] + "," + i[1], "1,8");
        }
        {
            int[] i = getUICoords(18);
            assertEquals(i[0] + "," + i[1], "2,0");
        }
        {
            int[] i = getUICoords(44);
            assertEquals(i[0] + "," + i[1], "4,8");
        }
    }
}
