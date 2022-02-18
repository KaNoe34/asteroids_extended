import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.JOptionPane;
/**
 * The frame for the MenuPanel
 * The program's main GUI
 * Crates a new Game and has the MenuPanel
 * 
 * @author Oskar Czarnecki
 * @version 02/18/22
 */
public class MainFrame extends JFrame implements ActionListener {
    private static MenuPanel menu = new MenuPanel();
    private static GamePanel game = new GamePanel();
    private static JMenuBar bar = new JMenuBar();
    private static JMenu help = new JMenu("?");
    private static JMenuItem controls = 
        new JMenuItem("Control Information");
    private static JMenuItem info = new JMenuItem("Game Information");
    //private static JMenuItem extra = new JMenuItem("...");
    
    //to keep track of where the window was 
    //before the game started
    private static Point loc;

    public MainFrame () {
        
        //let Global know this is the game
        //panel
        Global.setGamePanel(game);
        
        setResizable(true);
        
        //exit program upon closing this window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //set dimension variables for
        //fullscreen window now
        Global.updateDimensions();
        
        //use card layout for this frame
        //to switch between panels
        getContentPane().setLayout(new CardLayout());
        
        //add the menu panel
        getContentPane().add(menu);
        
        //add the game panel
        getContentPane().add(game);
        
        //add action listeners to JMenu buttons
        controls.addActionListener(this);
        info.addActionListener(this);
        //extra.addActionListener(this);
        
        //put buttons in the 
        //JMenu
        help.add(controls);
        help.add(info);
        //help.add(extra);
        
        //but the JMenu on the 
        //JMenuBar
        bar.add(help);
        
        //put the bar at the top of the frame
        setJMenuBar(bar);
        
        //save window location
        loc = getLocation();
        
        switchToMenu();
        
        //let Global know this is the main frame
        Global.setMainFrame(this);
        
        //make sure the frame doesn't keep
        //taking focus away from the game
        //panel
        setAutoRequestFocus(false);
        
        setVisible(true);
    }

    /*
     * A call to this method seamlessly 
     * switches from the game to the menu.
     */
    public void switchToMenu () {
        //show the menu bar
        bar.setVisible(true);
        
        //set to saved location
        setLocation(loc);

        setResizable(false);
        setSize(800, 600);
        
        //get the LayoutManager
        CardLayout l = (CardLayout)getContentPane().getLayout();
        
        //switch to the first added component
        l.first(getContentPane());
        
        setTitle("Asteroids Extended: Main Menu");
    }

    /*
     * A call to this method seamlessly
     * switches from the menu to the game.
     */
    public void switchToGame () {
        
        //hide the bar
        bar.setVisible(false);
        
        //save window location
        loc = getLocation();
        
        //set upper left corner to be aligned with
        //screen
        setLocation(0, 0);
        
        //set largest possible screen size,
        //hide taskbar
        setResizable(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        //get the LayoutManager
        CardLayout l = (CardLayout)getContentPane().getLayout();
        
        //switch to last component added
        l.last(getContentPane());
        
        setTitle("Asteroids Extended");
        
        //reset gameOver status if previously
        //changed
        Global.setGameOver(false);
        
        //start new Thread to run the game
        Thread t1 = new Thread() {
                public void run() {
                    Game g = new Game();
                }
            };
        t1.start();
    }

    public void actionPerformed (ActionEvent e) {
        
        //set command to the name of the pressed button
        String command = e.getActionCommand();
        if (command.equals("Control Information")) {
            JOptionPane.showMessageDialog(this, "Okay, Listen up\n\n"
                + "You use your keyboard to control the game.\n"
                + "To fly forward -> Press the Up Arrow key\n"
                + "To fly backward -> Press the Down Arrow key\n"
                + "To turn clockwise -> Press the Right Arrow key\n"
                + "To turn counterclockwise -> press the Left Arrow key"
                + "\nTo shoot -> Press the Space Bar\n"
                + "To engage hyperspace -> Press the Shift key\n"
                + "To pause the game -> Press P\n"
                + "To to the menu -> Press the Escape key", "Controls",
                1);
        } else if (command.equals("Game Information")) {
            JOptionPane.showMessageDialog(this, 
                "Control your spaceship using the keyboard.\n"
                + "Shooting asteroids gets you points and helps you "
                + "survive.\n"
                + "Larger asteroids will split into two until they reach"
                + " the smallest size.\n"
                + "The smallest ones are worth the most points.\n"
                + "Once you clear the screen of asteroids, a new round "
                + "begins.\n"
                + "The rounds get progressively harder.\n"
                + "You have three lifes to begin with and can earn more "
                + "every 100,000 points.\n"
                + "If you die you lose points depending on what hit you."
                + "\nThere's a hyperspace button. Make use of it.", 
                "Instructions", 1);
        } else if (command.equals("...")) {
            JOptionPane.showMessageDialog(this, 
                "This program was made by Oskar Czarnecki.\n"
                + "TableLayout utilized by the main menu was made by Daniel E. "
                + "Barbalace.\n"
                + "Things that didn't make it into the game:\n"
                + "Alien ships\nMultiplayer mode :(\n"
                + "Harder difficulty\n"
                + "Sound effects improvised by my sister\n"
                + "Any sound effects\nPink spaceships\n"
                + "Any colored spaceships\nCustomizeable settings\n"
                + "Easter egg mode with pictures of everyone in this "
                + "class instead of asteroids\nBetter GUI\n"
                + "Autopilot mode\n"
                + "A high score system\n"
                + "High score instantly posted on Facebook\n\n"
                + "This Easter Egg",
                "", 1);
        }
    }
}
