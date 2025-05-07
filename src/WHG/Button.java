/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WHG;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Andreas Eriksson
 */
public class Button extends Figure {

    //Inkapslade då de är privata
    private final String BUTTONTEXT;
    private Boolean marked;

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
    public Button(int x, int y, int width, int height, Color color, String buttonText, Boolean marked) {
        super(x, y, width, height, color);
        this.BUTTONTEXT = buttonText;
        this.marked = marked;
    }

    public String getButtonText() {
        return BUTTONTEXT;
    }

    public Boolean getMarked() {
        return marked;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    /**
     * Metoden ritar upp objektet med det utséende och på de koordinater
     * objektet fått.
     *
     * @param g
     */
    @Override
    public void render(Graphics g) {
        if (marked) {
            g.setColor(Color.CYAN);
            g.fillRect(x - 10, y - 10, width + 20, height + 20);
        }

        //Knapp rutan
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, width, height);
        //Knapp texten
        g.setColor(Color.RED);
        g.setFont(new Font("serif", Font.BOLD, 20));
        g.drawString(BUTTONTEXT, x + 50 - (int) (BUTTONTEXT.length() * 1.5), y + 50);

    }

    /**
     * Gränsområdet på min knapp för att kunna kolla kollisioner.
     *
     * @return Skickar tillbaka knappens gränsområdet
     *
     */
    @Override
    public Rectangle boundArea() {
        return new Rectangle(x, y, width, height);
    }

}
