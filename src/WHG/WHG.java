/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WHG;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Enum används för att underlätta kodning. Fungerar som en boolean men med
 * många "val".
 *
 * @author Andreas Eriksson
 */
enum GameState {
    StartScreen, LevelScreen, ShopScreen, GameScreen, GameOverScreen, PauseScreen, HowToPlayScreen
};

public class WHG extends Canvas implements Runnable, KeyListener {

    public static final int GAME_WIDTH = 1200;//bredd 1200 px
    public static final int GAME_HEIGHT = 900;//höjd 900px
    private static GameState gameState = GameState.StartScreen;//Veta vilken bild som ska visas på skärmen

    private int updates, frames;
    private int playerCoins = 0, coins, levelCoins;
    private int playerWidth = 0, playerSpeed = 0, playerHeight = 0;
    private long startTime, timer;
    private final double AMOUNT_OF_TICKS = 60; //klockslag
    private final double NUMBER_OF_SECONDS = 1000000000 / AMOUNT_OF_TICKS; // 1miljard / 60
    private double delta;
    private boolean gameOn = false;
    private boolean dead;

    private final ArrayList<MenuButton> menuButtons = new ArrayList<>();//Meny för alla våra knappar
    private final ArrayList<LevelButton> levelButtons = new ArrayList<>();//Meny för alla våra knappar
    private final ArrayList<ShopButton> shopButtons = new ArrayList<>();//Meny för alla våra knappar
    private final ArrayList<Block> blocks = new ArrayList<>();//Meny för alla våra knappar
    private final ArrayList<Figure> figureList = new ArrayList<>();//Figurer
    private Player playerOne;
    private Thread thread;

    /**
     * Startar spelet
     *
     * @param args skapar en ny instans av programmet.
     */
    public static void main(String[] args) {
        WHG game = new WHG();
        game.init();

    }

    private void init() {
        this.setPreferredSize(new Dimension(GAME_WIDTH - 10, GAME_HEIGHT - 10));//1200x900
        this.setMaximumSize(new Dimension(GAME_WIDTH - 10, GAME_HEIGHT - 10));//1200x900
        this.setMinimumSize(new Dimension(GAME_WIDTH - 10, GAME_HEIGHT - 10));//1200x900

        JFrame boardFrame = new JFrame();
        boardFrame.add(this);//Lägger duken ihop rullad på ramen //This syftar på canvas
        boardFrame.pack();//Sträcker ut duken och spikar fast den på ramen
        boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        boardFrame.setResizable(false);//Går ej att ändra storlek på fönster
        boardFrame.setLocationRelativeTo(null);//Placera fönstret i mitten av skärmen
        boardFrame.setVisible(true);//Visa "tavlan" (duk + ram)
        this.start();//Kör start metoden
        this.setFocusable(true);//Fokus på fönstret
    }

    private synchronized void start() {
        if (gameOn) {
            return;
        }
        gameOn = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        if (gameOn) {
            return;
        }

        try {
            thread.join(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }

    /**
     * Läser in knapp objekten och startar timern.
     */
    @Override
    public void run() {
        loader();
        startTime = System.nanoTime();//Startar timer i systemets tid i nanosek
        delta = 0; //medelvärdet
        updates = 0;
        frames = 0;
        timer = System.currentTimeMillis();//systemets tiden i millisek
        while (gameOn) {
            speed();
        }
        stop();
    }

    private void speed() {
        long lapTime = System.nanoTime();//Systemets tid i nanosek 
        delta += (lapTime - startTime) / NUMBER_OF_SECONDS;
        startTime = lapTime;
        if (delta >= 1) {
            tick();
            updates++;
            delta--;
        }

        render();
        frames++;
        if (System.currentTimeMillis() - timer > 1000) {
            timer += 1000;
            System.out.println(updates + " Ticks, FPS " + frames);
            updates = 0;
            frames = 0;
        }
    }

    private void loader() {
        addKeyListener(this);//Läser av tangenterna man trycker på
        int xCenter = (GAME_WIDTH + 10) / 2;//Räknar ut hälften av skärmen
        //MenuButtons
        menuButtons.add(new MenuButton(xCenter - 75, 50, 150, 70, Color.red, "PLAY", true));//To level select
        menuButtons.add(new MenuButton(xCenter - 75, 200, 150, 70, Color.red, "SHOP", false));//Shows you the shop screen
        menuButtons.add(new MenuButton(xCenter - 100, 350, 210, 70, Color.red, "HOW TO PLAY", false));//Shows you how to play
        menuButtons.add(new MenuButton(xCenter - 75, 500, 150, 70, Color.red, "QUIT", false));//Exit game

        //LevelButtons
        levelButtons.add(new LevelButton(100, 300, 150, 70, Color.yellow, "Level1", true));
        levelButtons.add(new LevelButton(300, 300, 150, 70, Color.yellow, "Level2", false));
        levelButtons.add(new LevelButton(500, 300, 150, 70, Color.yellow, "Level3", false));
        levelButtons.add(new LevelButton(700, 300, 150, 70, Color.yellow, "Level4", false));
        levelButtons.add(new LevelButton(900, 300, 150, 70, Color.yellow, "Back", false));

        //ShopButtons
        shopButtons.add(new ShopButton(150, 400, 150, 70, Color.GREEN, "+1 Speed", true, 10));
        shopButtons.add(new ShopButton(350, 400, 150, 70, Color.GREEN, "-1 Width", false, 10));
        shopButtons.add(new ShopButton(550, 400, 150, 70, Color.GREEN, "-1 Height", false, 10));
        shopButtons.add(new ShopButton(750, 400, 150, 70, Color.GREEN, "Reset Player", false, 10));//Ger spelaren dess ursprungliga storlek och hastighet
        shopButtons.add(new ShopButton(950, 400, 150, 70, Color.GREEN, "Back", false, 0));

        try {//Läser in spelarens statistik
            File file = new File("PlayerStat.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String row;
            while ((row = br.readLine()) != null) {
                //Need help!!! playerWidth, playerHeight, playerSpeed
                String[] info = row.split("I");
                coins = Integer.parseInt(info[0]);
                playerSpeed = Integer.parseInt(info[1]);
                playerWidth = Integer.parseInt(info[2]);
                playerHeight = Integer.parseInt(info[3]);
            }
            br.close();

        } catch (IOException e) {
            System.out.println("ERROR: COULD NOT READ FILE");
        }
    }

    private void tick() {
        if (gameState == GameState.GameScreen) {
            //Kollision i spelet
        
            for (Figure f : figureList) {
                f.tick();
            }
            for (Block obj : blocks) {
                for (Figure f : figureList) {
                    if (f instanceof Enemy) {
                        if (f.collision(obj.boundArea()) && obj instanceof Wall) {
                            f.reverseTick();
                        } else if (f.collision(playerOne.boundArea())) {
                            dead = true;
                            levelCoins = 0;
                            gameState = GameState.GameOverScreen;
                        }
                    }
                }
                
                if (playerOne.collision(obj.boundArea()) && obj instanceof Wall) {
                    playerOne.reverseTick(obj);
                } else if (playerOne.collision(obj.boundArea()) && obj instanceof Coin) {
                    blocks.remove(obj);
                    playerCoins++;
                    break;
                } else if (playerOne.collision(obj.boundArea()) && obj instanceof Finish) {
                    if (playerCoins == levelCoins) {
                        dead = false;
                        coins = coins + playerCoins;
                        saver();
                        gameState = GameState.GameOverScreen;
                    }
                }
            }
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, GAME_WIDTH + 10, GAME_HEIGHT + 10);
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 20));

        switch (gameState) {//För att rita ut rätt skärm till användaren.
            case StartScreen://Visar meny så man vet vad man kan välja
                for (MenuButton menuButton : menuButtons) {//Ritar ut alla knapparna genom loopen
                    menuButton.render(g);//Anropar knapp för knapps metod "render" och skickar med g (grafiken)
                }

                break;
            case LevelScreen://Visar en meny med vilka banor man kan spela
                for (LevelButton levelButton : levelButtons) {//Ritar ut alla knapparna genom loopen
                    levelButton.render(g);//Anropar knapp för knappens metod "render" och skickar med g (grafiken)
                }
                break;
            case GameScreen://Spelskärmen
                for (Block level : blocks) {
                    level.render(g);
                }
                for (Figure obj : figureList) {
                    obj.render(g);
                }
                g.setColor(Color.RED);
                // g.drawString("Game is playing", GAME_WIDTH / 2 - 80, 350);
                g.drawString("Coins collected: " + playerCoins, 50, 30);
                break;
            case ShopScreen://shop skärmen
                g.drawString("You Have: " + coins + " Coins", 50, 50);
                g.drawString("Shop: ", 50, 100);
                g.drawString("All upgrades cost 10 coins", 110, 100);
                g.drawString("Current speed: " + playerSpeed, 50, 150);
                g.drawString("Current Width: " + playerWidth, 50, 200);
                g.drawString("Current Height: " + playerHeight, 50, 250);

                for (ShopButton shopButton : shopButtons) {
                    shopButton.render(g);
                }
                break;
            case PauseScreen://Skärmen för att pausa
                g.drawString("Game paused \"Press p to resume\"", GAME_WIDTH / 2 - 150, 350);
                g.drawString("Press delete to return to menu", GAME_WIDTH / 2 - 150, 400);
                break;
            case GameOverScreen://När spelet är slut
                g.drawString("Game Over", GAME_WIDTH / 2 - 150, 300);
                if (dead == false) {
                    g.drawString("Level Complete", GAME_WIDTH / 2 - 150, 350);
                    g.drawString("Gained: " + playerCoins + " coins", GAME_WIDTH / 2 - 150, 400);
                    g.drawString("Press \"Enter\" to return to menu", GAME_WIDTH / 2 - 150, 450);
                } else {
                    g.drawString("Missoin failed, well get 'em next time.", GAME_WIDTH / 2 - 150, 350);
                    g.drawString("Press \"Enter\" to return to menu", GAME_WIDTH / 2 - 150, 400);
                }
                break;
            case HowToPlayScreen://Förklarar hur man ska spela spelet
                g.drawString("Use the Arrowkeys to move your Character.", GAME_WIDTH / 2 - 200, 250);
                g.drawString("Avoid the red squares and pick up coins.", GAME_WIDTH / 2 - 200, 300);
                g.drawString("Collect all coins and get to the finish (green square) to win the game.", GAME_WIDTH / 2 - 200, 350);
                g.drawString("You can pause the game at any time by pressing esc.", GAME_WIDTH / 2 - 200, 400);
                g.drawString("Press \"Enter\" to return to menu", GAME_WIDTH / 2 - 200, 450);
                break;
            default:
                break;
        }
        g.dispose();
        bs.show();
    }

    private void saver() {//Spara ner pengar samt köpta variabler.
        File file = new File("PlayerStat.txt");
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            String saveStats = "" + coins + "I" + playerSpeed + "I" + playerWidth + "I" + playerHeight;
            bw.write(saveStats);
            bw.flush();
            bw.close();
            fw.close();

        } catch (IOException e) {
            System.out.println("ERROR: FILE NOT FOUND");
        }
    }

    /**
     * Skapar objektet playerOne Lägger till objektet playerOne i figureList
     * Skapar fileLocation Letar igenom levelButtons efter en markerad knapp
     * Sparar namnet på den knappen + strängen ".txt" i fileLocation Anropar
     * metoden loadLevel och skickar med fileLocation
     */
    private void gameLoader() {
        playerCoins = 0;
        String fileLocation = "";

        for (int i = 0; i < levelButtons.size(); i++) {
            if (levelButtons.get(i).getMarked() == true) {
                fileLocation = levelButtons.get(i).getButtonText() + ".txt";
                System.out.println(fileLocation);
            }
        }

        try {
            loadLevel(fileLocation);
        } catch (IOException iOException) {
            System.out.println("ERROR: COULD NOT READ FILE");
        }
    }

    private void loadLevel(String fileLocation) throws FileNotFoundException, IOException {
        File file = new File(fileLocation);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String row;
        int y = 0;
        figureList.clear();
        blocks.clear();
        while ((row = br.readLine()) != null) {
            int x = 0;
            String[] symbol = row.split("");
            for (String number : symbol) {
                switch (number) {
                    case "1":
                        blocks.add(new Wall(x * 50, y * 50, 50, 50, Color.BLUE));
                        break;
                    case "2":
                        blocks.add(new Coin(x * 50, y * 50, 50, 50, Color.DARK_GRAY));
                        levelCoins++;
                        break;
                    case "3":
                        blocks.add(new Finish(x * 50, y * 50, 50, 50, Color.GREEN));
                        break;
                    case "4":
                        figureList.add(new Enemy(x * 50, y * 50, 50, 50, Color.RED, 6, 0));//fiende åt höger
                        break;
                    case "5":
                        figureList.add(new Enemy(x * 50, y * 50, 50, 50, Color.RED, -6, 0));//fiende åt vänster
                        break;
                    case "6":
                        figureList.add(new Enemy(x * 50, y * 50, 50, 50, Color.RED, 0, 6));//fiende ner
                        break;
                    case "7":
                        figureList.add(new Enemy(x * 50, y * 50, 50, 50, Color.RED, 0, -6));//fiende upp
                        break;
                    case "P":
                        playerOne = new Player(x * 50, y * 50, playerWidth, playerHeight, Color.WHITE, playerSpeed);
                        figureList.add(playerOne);
                    default:
                        break;
                }
                x++;
            }
            y++;
        }
        br.close();
    }

    /**
     * Metoden säger åt programmet att göra olika saker beroende på vilka
     * knapptryckningar som registreras på tangentbordet.
     *
     * @param ke registrerar knapptryckningar på tangentbordet.
     */
    @Override
    public void keyPressed(KeyEvent ke) {

        if (null != gameState) {
            switch (gameState) {
                case StartScreen:
                    if (ke.getKeyCode() == KeyEvent.VK_DOWN || ke.getKeyCode() == KeyEvent.VK_S) {
                        for (int i = 0; i < menuButtons.size(); i++) {//Iteration som går igenom alla menuButtons.
                            //Kollar om menuButton är markerad, om den är det så tas markeringen bort och nästa menuButton i listan markeras.
                            if (menuButtons.get(i).getMarked() == true && i + 1 < menuButtons.size()) {
                                menuButtons.get(i).setMarked(false);
                                menuButtons.get(i + 1).setMarked(true);
                                break;
                            }
                        }
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_W) {
                        for (int i = 0; i < menuButtons.size(); i++) {
                            if (menuButtons.get(i).getMarked() == true && i - 1 >= 0) {
                                menuButtons.get(i).setMarked(false);
                                menuButtons.get(i - 1).setMarked(true);
                                break;
                            }
                        }
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (menuButtons.get(0).getMarked() == true) {
                            gameState = GameState.LevelScreen;
                        } else if (menuButtons.get(1).getMarked() == true) {
                            gameState = GameState.ShopScreen;
                        } else if (menuButtons.get(2).getMarked() == true) {
                            gameState = GameState.HowToPlayScreen;
                        } else if (menuButtons.get(3).getMarked() == true) {
                            gameOn = false;
                        }
                    }
                    break;
                case LevelScreen:
                    if (ke.getKeyCode() == KeyEvent.VK_RIGHT || ke.getKeyCode() == KeyEvent.VK_D) {
                        for (int i = 0; i < levelButtons.size(); i++) {
                            if (levelButtons.get(i).getMarked() == true && i + 1 < levelButtons.size()) {
                                levelButtons.get(i).setMarked(false);
                                levelButtons.get(i + 1).setMarked(true);
                                break;
                            }
                        }
                    }

                    if (ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_A) {
                        for (int i = 0; i < levelButtons.size(); i++) {
                            if (levelButtons.get(i).getMarked() == true && i - 1 >= 0) {
                                levelButtons.get(i).setMarked(false);
                                levelButtons.get(i - 1).setMarked(true);
                                break;
                            }
                        }
                    }

                    if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (levelButtons.get(0).getMarked() == true) {
                            gameLoader();
                            gameState = GameState.GameScreen;
                        } else if (levelButtons.get(1).getMarked() == true) {
                            gameLoader();
                            gameState = GameState.GameScreen;
                        } else if (levelButtons.get(2).getMarked() == true) {
                            gameLoader();
                            gameState = GameState.GameScreen;
                        } else if (levelButtons.get(3).getMarked() == true) {
                            gameLoader();
                            gameState = GameState.GameScreen;
                        } else if (levelButtons.get(4).getMarked() == true) {
                            gameState = GameState.StartScreen;
                        }
                    }
                    break;
                case GameOverScreen:
                    if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        gameState = GameState.StartScreen;
                    }
                    break;
                case ShopScreen:
                    if (ke.getKeyCode() == KeyEvent.VK_RIGHT || ke.getKeyCode() == KeyEvent.VK_D) {
                        for (int i = 0; i < shopButtons.size(); i++) {
                            if (shopButtons.get(i).getMarked() == true && i + 1 < shopButtons.size()) {
                                shopButtons.get(i).setMarked(false);
                                shopButtons.get(i + 1).setMarked(true);
                                break;
                            }
                        }
                    }

                    if (ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_A) {
                        for (int i = 0; i < shopButtons.size(); i++) {
                            if (shopButtons.get(i).getMarked() == true && i - 1 >= 0) {
                                shopButtons.get(i).setMarked(false);
                                shopButtons.get(i - 1).setMarked(true);
                                break;
                            }
                        }
                    }

                    if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (shopButtons.get(0).getMarked() == true) {
                            if (coins >= shopButtons.get(0).getCost() && playerSpeed < 10) {
                                coins = coins - shopButtons.get(0).getCost();
                                playerSpeed = playerSpeed + 1;
                                saver();
                            } else if (coins < shopButtons.get(1).getCost()){
                                JOptionPane.showMessageDialog(null, "No coins, No speed.");

                            } else {
                                JOptionPane.showMessageDialog(null, "Player do be pretty fast.");
                            }
                        } else if (shopButtons.get(1).getMarked() == true) {
                            if (coins >= shopButtons.get(1).getCost() && playerWidth > 20) {
                                coins = coins - shopButtons.get(1).getCost();
                                playerWidth = playerWidth - 1;
                                saver();
                            } else if (coins < shopButtons.get(1).getCost()){
                                JOptionPane.showMessageDialog(null, "You Are Poor!");
                            } else {
                                JOptionPane.showMessageDialog(null, "Player slim shady.");
                            }
                        } else if (shopButtons.get(2).getMarked() == true) {
                            if (coins >= shopButtons.get(2).getCost() && playerHeight > 20) {
                                coins = coins - shopButtons.get(2).getCost();
                                playerHeight = playerHeight - 1;
                                saver();
                            } else if (coins < shopButtons.get(2).getCost()){
                                JOptionPane.showMessageDialog(null, "Not enough coins!");
                            
                            } else {
                                JOptionPane.showMessageDialog(null, "Player smol.");
                            }
                        } else if (shopButtons.get(3).getMarked() == true) {
                            if (coins >= shopButtons.get(3).getCost()){
                                playerSpeed = 5;
                                playerWidth = 49;
                                playerHeight = 49;
                                coins = coins - shopButtons.get(3).getCost();
                                saver();
                                JOptionPane.showMessageDialog(null, "Player Reset.");  
                            } else {
                                JOptionPane.showMessageDialog(null, "You can't afford to be normal, Freak!");
                            }
                            
                        } else if (shopButtons.get(4).getMarked() == true) {//BACK
                            gameState = GameState.StartScreen;
                        }
                    }
                    break;
                case HowToPlayScreen:
                    if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                        gameState = GameState.StartScreen;
                    }
                    break;
                case PauseScreen:
                    if (ke.getKeyCode() == KeyEvent.VK_P) {
                        gameState = GameState.GameScreen;
                    }
                    if (ke.getKeyCode() == KeyEvent.VK_DELETE){
                        if (JOptionPane.showConfirmDialog(null, "Do you wish to exit the level?") == 0){
                            gameState = GameState.StartScreen;
                        }
                    }

                case GameScreen:
                    if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        gameState = GameState.PauseScreen;
                    }
                    playerOne.move(ke);
                default:
                    break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    /**
     * Metoden används för att ta reda på om en nedtryckt tangent släpps upp.
     *
     * @param ke registrerar knapptryckningar på tangentbordet.
     */
    @Override
    public void keyReleased(KeyEvent ke) {
        if (gameState == GameState.GameScreen || gameState == GameState.PauseScreen) {
            playerOne.stop(ke);
        }
    }
}
