/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WHG;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 *
 * @author Andreas Eriksson
 */
public class Player extends Figure {

    private int speedX = 0, speedY = 0;
    private int speed;

    /**
     * Konstruktorn får reda på hur objektet ser ut och var det befinner sig.
     *
     * @param x = X-koordinaten objektet befinner sig på.
     * @param y = Y-koordinaten objektet befinner sig på.
     * @param width = Hur många pixlar brett objektet är.
     * @param height = Hur många pixlar högt objektet är.
     * @param color = Vilken färg objektet har.
     * @param speed = Spelarens hastighet.
     */
    public Player(int x, int y, int width, int height, Color color, int speed) {
        super(x, y, width, height, color);
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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
     * Kollar om programmet får flytta spelaren i antingen X eller Y riktning
     * och isåfall flyttar programmet spelaren i angiven riktning.
     */
    @Override
    public void tick() {
        if (!(getX() < 0 - getSpeedX()) && !(getX() + width + getSpeedX() > WHG.GAME_WIDTH + 10)) {
            setX(getX() + getSpeedX());
        }
        if (!(getY() < 0 - getSpeedY()) && !(getY() + height + getSpeedY() > WHG.GAME_HEIGHT + 10)) {
            setY(getY() + getSpeedY());
        }
    }

    /**
     * Metoden flyttar spelaren 1 pixel från objektet wall om spelaren skulle
     * kollidera med ett objekt av typen wall.
     *
     * @param wall = objektet wall som spelaren kolliderar med
     */
    public void reverseTick(Block wall) {
        if (getSpeedX() > 0) {//Höger
            setX(wall.getX() - (1 + width));
            setSpeedX(0);
        } else if (getSpeedX() < 0) {//Vänster
            setX(wall.getX() + (wall.getWidth() + 1));
            setSpeedX(0);
        } else if (getSpeedY() > 0) {//Ner
            setY(wall.getY() - (1 + height));
            setSpeedY(0);
        } else if (getSpeedY() < 0) {//Upp
            setY(wall.getY() + (wall.getHeight() + 1));
            setSpeedY(0);
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
     * Metoden flyttar objektet i en riktning om en av de 
     * fyra piltangenterna är nertryckta.
     *
     * @param ke = en knapptryckning på tangentbordet
     */
    public void move(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                setSpeedX(-speed);
                break;
            case KeyEvent.VK_RIGHT:
                setSpeedX(speed);
                break;
            case KeyEvent.VK_UP:
                setSpeedY(-speed);
                break;
            case KeyEvent.VK_DOWN:
                setSpeedY(speed);
                break;
            case KeyEvent.VK_A:
                setSpeedX(-speed);
                break;
            case KeyEvent.VK_D:
                setSpeedX(speed);
                break;
            case KeyEvent.VK_W:
                setSpeedY(-speed);
                break;
            case KeyEvent.VK_S:
                setSpeedY(speed);
                break;
            default:
                break;
        }
    }

    /**
     * Metoden stannar objektet när en av de fyra piltangenterna släppts upp.
     *
     * @param ke = en knapptryckning på tangentbordet
     */
    public void stop(KeyEvent ke) {
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                setSpeedX(0);
                break;
            case KeyEvent.VK_RIGHT:
                setSpeedX(0);
                break;
            case KeyEvent.VK_UP:
                setSpeedY(0);
                break;
            case KeyEvent.VK_DOWN:
                setSpeedY(0);
                break;
            case KeyEvent.VK_A:
                setSpeedX(0);
                break;
            case KeyEvent.VK_D:
                setSpeedX(0);
                break;
            case KeyEvent.VK_W:
                setSpeedY(0);
                break;
            case KeyEvent.VK_S:
                setSpeedY(0);
                break;
            default:
                break;
        }
    }
}
