import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {

     @Test
    public void testGetCatalogEntry() throws IOException {
         Catalog catalog = new Catalog("prices.json", "tools.json");
         Catalog.CatalogEntry entry = catalog.getCatalogEntry("LADW");
         assertEquals("Ladder", entry.getPricing().type());
         assertEquals(1.99, entry.getPricing().dailyCharge());
         assertEquals("Ladder", entry.getTool().type());
     }
}