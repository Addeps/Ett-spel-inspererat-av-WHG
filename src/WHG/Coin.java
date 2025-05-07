/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WHG;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Andreas Eriksson
 */
public class Coin extends Block {

    private final int COINX = x + 20;
    private final int COINY = y + 20;
    private final int COINWIDTH = 10;
    private final int COINHEIGT = 10;
    private final Color COINCOLOR = Color.YELLOW;

    /**
     * Konstruktorn får reda på hur objektet ser ut och var det befinner sig.
     *
     * @param x = X-koordinaten objektet befinner sig på.
     * @param y = Y-koordinaten objektet befinner sig på.
     * @param width = Hur många pixlar brett objektet är.
     * @param height = Hur många pixlar högt objektet är.
     * @param color = Vilken färg objektet har.
     */
    public Coin(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }

    /**
     * Metoden ritar upp objektet med det utséende och på de koordinater
     * objektet fått.
     *
     * @param g
     */
    @Override
    public void render(Graphics g) {
        g.setColor(getColor());
        g.fillRect(getX(), getY(), width, height);

        g.setColor(COINCOLOR);
        g.fillOval(COINX, COINY, COINWIDTH, COINHEIGT);
    }
}
