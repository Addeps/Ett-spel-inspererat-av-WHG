/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WHG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Andreas Eriksson
 */
public class Figure {

    protected int width, height; //Dimension på figuren
    int x, y; //Kordinaterna på figuren
    Color color; //Bakgrundsfärg till figuren

    /**
     * Konstruktorn får reda på hur objektet ser ut och var det befinner sig.
     *
     * @param x = X-koordinaten objektet befinner sig på.
     * @param y = Y-koordinaten objektet befinner sig på.
     * @param width = Hur många pixlar brett objektet är.
     * @param height = Hur många pixlar högt objektet är.
     * @param color = Vilken färg objektet har.
     */
    public Figure(int x, int y, int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void tick() {
    }

    /**
     * Metoden ritar upp objektet med det utséende och på de koordinater
     * objektet fått.
     *
     * @param g
     */
    public void render(Graphics g) {
        g.setColor(getColor());
        g.fillRect(getX(), getY(), width, height);
    }

    /**
     * Metoden skickar tillbaka var objektet befinner sig och vilka dimensioner
     * det har.
     *
     * @return skickar tillbaka figurens gränsområde i form av en rektangel
     */
    public Rectangle boundArea() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Skickar tillbaka om objektet kolliderar med ett annat objekt
     *
     * @param rect tar emot en rektangel och kollar om objektet kolliderar med
     * rektangeln.
     * @return
     */
    public boolean collision(Rectangle rect) {
        return false;
    }

    public void reverseTick() {
    }
}
