/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WHG;

import java.awt.Color;

/**
 *
 * @author Andreas Eriksson
 */
public class Block extends Figure {

    /**
     * Konstruktorn får reda på hur objektet ser ut och var det befinner sig.
     *
     * @param x = X-koordinaten objektet befinner sig på.
     * @param y = Y-koordinaten objektet befinner sig på.
     * @param width = Hur många pixlar brett objektet är.
     * @param height = Hur många pixlar högt objektet är.
     * @param color = Vilken färg objektet har.
     */
    public Block(int x, int y, int width, int height, Color color) {
        super(x, y, width, height, color);
    }
}
