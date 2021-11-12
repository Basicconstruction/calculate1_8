package test;

import javax.swing.*;
import java.awt.event.*;

class MyMenu {
    JFrame frame = new JFrame("右键菜单");
    JMenuItem item1 = new JMenuItem("子菜单1", new ImageIcon("close.gif"));
    JMenuItem item2 = new JMenuItem("子菜单2");
    JMenuItem item3 = new JMenuItem("子菜单3");
    JPopupMenu menu = new JPopupMenu();
    JMenu m = new JMenu();
    JPanel panel = new JPanel();

    public MyMenu() {
        menu.add(new JMenuItem("选择"));
        menu.add(new JMenuItem("退出"));
        m.add(item1);
        m.add(item2);
        menu.add(m);
        menu.add(item3);
        panel.addMouseListener(
                new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        if (e.isPopupTrigger()) {
                            menu.show(panel, e.getX(), e.getY());
                        }
                    }
                });

        panel.add(menu);
        frame.add(panel);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}

public class Test {
    public static void main(String args[]) {
        new MyMenu();
    }
}
class Test2{
    public static void main(String[] args){
        System.out.println(Integer.parseInt("-100"));
    }
}
