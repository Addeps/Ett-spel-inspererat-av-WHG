/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WHG;

import java.awt.Color;
import java.awt.Rectangle;

/**
 *
 * @author Andreas Eriksson
 */
public class Enemy extends Figure {

    private int speedX, speedY;

    /**
     * Konstruktorn får reda på hur objektet ser ut och var det befinner sig.
     *
     * @param x = X-koordinaten objektet befinner sig på.
     * @param y = Y-koordinaten objektet befinner sig på.
     * @param width = Hur många pixlar brett objektet är.
     * @param height = Hur många pixlar högt objektet är.
     * @param color = Vilken färg objektet har.
     * @param speedX = Vilken hastighet i x-led objektet har.
     * @param speedY = Vilken hastighet i y-led objektet har.
     */
    public Enemy(int x, int y, int width, int height, Color color, int speedX, int speedY) {
        super(x, y, width, height, color);
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    /**
     * Flyttar fienden i x och y led.
     */
    @Override
    public void tick() {
        if (!(getX() < 0 - getSpeedX()) && !(getX() + width + getSpeedX() > WHG.GAME_WIDTH + 10)) {
            setX(getX() + getSpeedX());
        } else {
            setSpeedX(-speedX);
        }
        if (!(getY() < 0 - getSpeedY()) && !(getY() + height + getSpeedY() > WHG.GAME_HEIGHT + 10)) {
            setY(getY() + getSpeedY());
        } else {
            setSpeedY(-speedY);
        }
    }

    /**
     * Skickar tillbaka om objektet kolliderar med ett objekt
     *
     * @param rect tar emot en rektangel och kollar om objektet kolliderar med
     * rektangeln.
     * @return
     */
    @Override
    public boolean collision(Rectangle rect) {
        return rect.intersects(new Rectangle(x, y, width, height));
    }

    /**
     * Flyttar tillbaka spelaren till dess föregående position.
     */
    @Override
    public void reverseTick() {
        setSpeedX(-speedX);
        setSpeedY(-speedY);
    }
}
