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
public class ShopButton extends Button {

    private final int COST;

    /**
     * Konstruktor tar emot x, y koordinater, knappens bredd och höjd, knappens
     * färg, text till knappen, om knappen är markerad eller ej och hur mycket
     * pengar knappen kostar.
     *
     * @param x = Knappens x-koordinat
     * @param y = Knappens y-koordinat
     * @param width = Knappens bredd
     * @param height = Knappens höjd
     * @param color = Knappens färg
     * @param buttonText = Knappens text
     * @param marked = Om knappen är markerad
     * @param cost = Knappens kostnad
     */
    public ShopButton(int x, int y, int width, int height, Color color, String buttonText, Boolean marked, int cost) {
        super(x, y, width, height, color, buttonText, marked);
        this.COST = cost;
    }

    public int getCost() {
        return COST;
    }
}
