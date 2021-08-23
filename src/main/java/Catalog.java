
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Catalog {

    Map<String, CatalogEntry> entries = new HashMap<>();

    public Catalog (String pricesFile, String toolsFile) throws IOException {
        Reader pricesReader = null;
        Reader toolsReader = null;
        try {

            pricesReader = new InputStreamReader(getClass().getResourceAsStream(pricesFile));
            toolsReader = new InputStreamReader(getClass().getResourceAsStream(toolsFile));
            List<PriceEntry> prices = Arrays.asList(new ObjectMapper().readValue(pricesReader, PriceEntry[].class));
            List<Tool> tools = Arrays.asList(new ObjectMapper().readValue(toolsReader, Tool[].class));

            // This is a temporary data structure to help map the prices to tools
            Map<String, PriceEntry> typeToPriceMap = new HashMap<>();
            for (PriceEntry entry : prices) {
                typeToPriceMap.put(entry.type(), entry);
            }

            // For each tool, get the corresponding price and create a catalog entry that knows about the tool and the price
            // catalog entries stored by tool code for easy retrieval
            for (Tool tool : tools) {
                if (typeToPriceMap.containsKey(tool.type())) {
                    entries.put(tool.id(), new CatalogEntry(typeToPriceMap.get(tool.type()), tool));
                }
            }
        } finally {
            if (pricesReader != null) {
                pricesReader.close();
            }
            if (toolsReader != null) {
                toolsReader.close();
            }
        }
    }

    public CatalogEntry getCatalogEntry(String toolCode) {
        return entries.get(toolCode);
    }

    static class CatalogEntry {

        private final PriceEntry price;
        private final Tool tool;

        public CatalogEntry(PriceEntry priceEntry, Tool tool) {
            this.price = priceEntry;
            this.tool = tool;
        }

        public PriceEntry getPricing() {
            return price;
        }

        public Tool getTool() {
            return tool;
        }
    }
}
