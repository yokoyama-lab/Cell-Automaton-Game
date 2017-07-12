import java.awt.*;
import java.awt.event.*;

public class Botan extends Frame{
    int a;
    public static void main(String[] args) {
        Botan f = new Botan();
        f.setSize(100, 100);
        Button myb;
        myb=new Button("ボタンA");
        f.add(myb, BorderLayout.NORTH);
                        
        f.setVisible(true);
        f.addWindowListener(new Ad());
              
    }
    public boolean action(Event e,Object o){
        if(o.equals("ボタンA")){
         //ボタンAが押されたときの処理
            a++;
         }
         repaint();
         return true;
    }

    public void paint(Graphics g)
    {
        g.drawString(a+"回",50,70); 
    }
}
class Ad extends WindowAdapter
{
    public void windowClosing(WindowEvent e){
       System.exit(0);
    }
}