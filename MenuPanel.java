import javax.swing.*;
import java.awt.*;
import layout.*;
import java.awt.event.*;
/**
 * Displays the first menu upon starting
 * the program
 * From here the user can begin
 * the game or use the MenuBar
 * 
 * @author Oskar Czarnecki
 * @version 06/05/12
 */

public class MenuPanel extends JPanel implements ActionListener {
    private static JButton start = new JButton("Start Game");
    private static JButton quit = new JButton("Quit");
    
    //The constructor sets up the panel
    public MenuPanel () {
        
        //vertical space between the buttons
        double vspace = 50;
        
        //2 dimensional array that 
        //holds this layout manager's
        //column and row sizes
        double size[][] = {{200, TableLayout.FILL, 200}, 
            {vspace, TableLayout.FILL, vspace, TableLayout.FILL, vspace,
                TableLayout.FILL, vspace, TableLayout.FILL, vspace}};
        //TableLayout.FILL is a special variable
        //that lets this manager know to set it
        //to fill the remaining space
            
        //instantiate the layout
        TableLayout layout = new TableLayout(size);
        
        setLayout(layout);
        
        //add start button to column 1 row 3
        add(start, "1, 3");
        
        start.addActionListener(this);
        
        //add quit button to column 1 row 5
        add(quit, "1, 5");
        
        quit.addActionListener(this);
    }
    
    public void actionPerformed (ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Start Game")) {
            Global.getMainFrame().switchToGame();
        }else if (command.equals("Quit")) System.exit(0);
    }
}