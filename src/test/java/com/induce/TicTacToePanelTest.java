package com.induce;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

public class TicTacToePanelTest {

    static final class CapturingPanel extends TicTacToePanel {
        State lastFinish;

        CapturingPanel() {
            super(new GridLayout(3, 3));
        }

    }

    @Before
    public void setUp() {
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    public void panelConstructs() {
        TicTacToePanel p = new TicTacToePanel(new GridLayout(3, 3));
        Assert.assertNotNull(p);
    }

    @Test
    public void firstHumanMoveDoesNotFinish() throws Exception {
        CapturingPanel panel = new CapturingPanel();
        java.lang.reflect.Field f = TicTacToePanel.class.getDeclaredField("cells");
        f.setAccessible(true);
        TicTacToeCell[] cells = (TicTacToeCell[]) f.get(panel);
        panel.actionPerformed(new ActionEvent(cells[0], ActionEvent.ACTION_PERFORMED, ""));
        Assert.assertNull(panel.lastFinish);
    }
}
