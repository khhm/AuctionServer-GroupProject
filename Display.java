import java.awt.*;
import javax.swing.Timer; //for timer

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.IOException;
import java.util.*; 
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Display 
    extends JPanel implements ActionListener { 
    JLabel [] symbolLabels = new JLabel[9];
    JLabel [] nameLabels = new JLabel[9];
    JLabel [] priceLabels = new JLabel[9];
    JTextArea temp;
    JTextArea textArea;
    JScrollPane pane;
    VisualServer server; 
    StocksDB allowedSymbols;
    String row;

    public Display(VisualServer server,StocksDB allowedSymbols) {
        
        setBackground(Color.BLACK);
        setLayout(new GridLayout(11,4));
      //describe information of given stocks  
        symbolLabels[0] = new JLabel("FB");
        symbolLabels[1] = new JLabel("VRTU");
        symbolLabels[2] = new JLabel("MSFT");
        symbolLabels[3] = new JLabel("GOOGL");
        symbolLabels[4] = new JLabel("YHOO");
        symbolLabels[5] = new JLabel("XLNX");
        symbolLabels[6] = new JLabel("TSLA");
        symbolLabels[7] = new JLabel("TXN");
        symbolLabels[8] = new JLabel("");

        nameLabels[0] = new JLabel("Facebook");
        nameLabels[1] = new JLabel("Virtusa Corporation - common stock");
        nameLabels[2] = new JLabel("Microsoft Corporation - Common Stock");
        nameLabels[3] = new JLabel("Google Inc. - Class A Common Stock");
        nameLabels[4] = new JLabel("Yahoo! Inc. - Common Stock");
        nameLabels[5] = new JLabel("Xilinx");
        nameLabels[6] = new JLabel("Tesla Motors");
        nameLabels[7] = new JLabel("Texas Instruments Incorporated - Common Stock");
        nameLabels[8] = new JLabel("");

        priceLabels[0] = new JLabel("");
        priceLabels[1] = new JLabel("");
        priceLabels[2] = new JLabel("");
        priceLabels[3] = new JLabel("");
        priceLabels[4] = new JLabel("");
        priceLabels[5] = new JLabel("");
        priceLabels[6] = new JLabel("");
        priceLabels[7] = new JLabel("");
        priceLabels[8] = new JLabel("");

        
	//set color,font and allignements of labels in jPanel
	for (int i = 0; i < 9; i++) {
            priceLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            priceLabels[i].setFont(new Font("Vandana", Font.BOLD, 15));
            priceLabels[i].setForeground(Color.YELLOW);
            nameLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            nameLabels[i].setFont(new Font("Vandana", Font.BOLD, 15));
            nameLabels[i].setForeground(Color.DARK_GRAY);
            symbolLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
            symbolLabels[i].setFont(new Font("Vandana", Font.BOLD, 15));
            symbolLabels[i].setForeground(Color.BLUE);
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0) add(nameLabels[i]);
                if (j == 1) add(symbolLabels[i]);
                if (j == 2) add(priceLabels[i]);
            }
        }
        //define text area to display bidding summery
        temp = new JTextArea();
        temp.setVisible(false);
        add(temp);
        textArea = new JTextArea();
        textArea.setRows(3);
        textArea.setEditable(false);
        textArea.setFont(new Font("Vandana", Font.BOLD, 15));
        textArea.setForeground(Color.BLACK);
	pane = new JScrollPane(textArea);
        add(pane);
        //set timer to next bid
        Timer timer = new Timer(500, this); 
	timer.start(); 
        //display initial prices of stocks 
        for (int i = 0; i < 8; i++) {
            priceLabels[i].setText(allowedSymbols.symbolList.get(symbolLabels[i].getText()));
        }
        
	this.server = server; 
    }
    
    public void actionPerformed(ActionEvent e) { 
	//take the user bid details and display appropiate way


	String newline = server.getMSG(); 
	if(newline!=null) { 
            String[] n=newline.split(",");
            row=n[1]+" : "+n[2];
            
            switch(n[0]){
              case "FB":
                   priceLabels[0].setText(row);
                   break;
              case "VRTU":
                   priceLabels[1].setText(row);
                   break;
              case "MSFT":
                   priceLabels[2].setText(row);
                   break;
              case "GOOGL":
                   priceLabels[3].setText(row);
                   break;
              case "YHOO":
                   priceLabels[4].setText(row);
                   break;
              case "XLNX":
                   priceLabels[5].setText(row);
                   break;
              case "TSLA":
                   priceLabels[6].setText(row);
                   break;
              case "TXN":
                   priceLabels[7].setText(row);
                   break;
            }
            textArea.append(n[1]+" : "+n[0]+" : "+n[2]+ "\n"); 

	    //Make sure the new text is visible, even if there
	    //was a selection in the text area.
	    textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }

    public static void main(String [] args) throws IOException { 
	//Create and set up the window.
        JFrame frame = new JFrame("Auction");
        frame.setSize(600,600); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	StocksDB allowedSymbols = new StocksDB("stocks.csv");
	VisualServer server = new VisualServer(MainServer.BASE_PORT,
					       allowedSymbols); 
        //Add contents to the window.
        frame.add(new Display(server,allowedSymbols));

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	server.server_loop(); 
    }
    
}
	
