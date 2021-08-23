import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolTest {

    @Test
    public void testCreate() {
        Tool tool = new Tool("Ladder", "Ridgid", "LADR");
        assertEquals("LADR", tool.id());
        assertEquals("Ladder", tool.type());
        assertEquals("Ridgid", tool.brand());
    }

}