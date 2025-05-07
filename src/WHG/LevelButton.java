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
public class LevelButton extends Button {

    /**
     * Konstruktor tar emot x, y koordinater, knappens bredd och höjd, knappens
     * färg, text till knappen och om knappen är markerad eller ej.
     *
     * @param x = Knappens x-koordinat
     * @param y = Knappens y-koordinat
     * @param width = Knappens bredd
     * @param height = Knappens höjd
     * @param color = Knappens färg
     * @param buttonText = Knappens text
     * @param marked = Om knappen är markerad
     */
    public LevelButton(int x, int y, int width, int height, Color color, String buttonText, boolean marked) {
        super(x, y, width, height, color, buttonText, marked);
    }

}
