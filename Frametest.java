import java.awt.*;
import java.awt.event.*;

public class Frametest{
   public static void main(String[] args) {

      //フレーム表示
      Frame f = new Frame();
      f.setSize(200, 100);
      f.setVisible(true);
      f.addWindowListener(new Ada());
   }
}
class Ada extends WindowAdapter
{
   public void windowClosing(WindowEvent e){
      //×を押した時の処理
      System.exit(0);
   }
}

