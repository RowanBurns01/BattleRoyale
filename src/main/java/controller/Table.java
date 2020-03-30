package controller;

import model.entities.Player;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Table extends JFrame {

    private Object[][] data;
    private BufferedImage image;

    public void makeTable(){
        String[] columns = {"Rank","District", "Tribute", "Level", "Kills","Health","Attack","Defence","Speed", "Status"};
        JTable table = new JTable(data, columns);
        JScrollPane scroll = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(2).setMinWidth(115);
        JPanel p = new JPanel(new BorderLayout());
        p.add(scroll, BorderLayout.CENTER);
        JFrame f = new JFrame("Never shown");
        f.setContentPane(scroll);
        f.pack();
        JTableHeader h = table.getTableHeader();
        Dimension dH = h.getSize();
        Dimension dT = table.getSize();
        int x = (int)dH.getWidth();
        int y = (int)dH.getHeight() + (int)dT.getHeight();
        scroll.setDoubleBuffered(false);
        BufferedImage bi = new BufferedImage(x,y,BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        h.paint(g);
        g.translate(0,h.getHeight());
        table.paint(g);
        g.dispose();
        image = bi;
    }

    public void setTable(int rowsDisplayed){
        data = new Object[rowsDisplayed][10];
    }

    public BufferedImage getImage(){
        return image;
    }

    public void addContestant(Player p, int i){
        data[i][0] = Integer.toString(i+1);
        data[i][1] = Integer.toString(p.getDistrict());
        data[i][2] = p.getFullName();
        data[i][3] = Integer.toString(p.getLevel());
        data[i][4] = Integer.toString(p.getKillCount());
        data[i][5] = Integer.toString(p.getTotalHealth());
        data[i][6] = Integer.toString(p.getAttack());
        data[i][7] = Integer.toString(p.getDefence());
        data[i][8] = Integer.toString(p.getDexterity());
        data[i][9] = p.getAlive() ? "Alive" : "Fallen";
    }

}
