import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class Botan1 extends Frame  {
    String a = "選択されてません";
    static Label la;
    static Botan1 f = new Botan1();
        
    static JRadioButton myr;
    static JRadioButton myr2; 

    public static void main(String[] args) {
     
        f.setSize(200, 100);
        f.la = new Label("選択：なし");
             
        myr = new JRadioButton ("ラジオA");
        myr2 = new JRadioButton ("ラジオB");
                         
        //ラベル表示 
        f.add(la,BorderLayout.NORTH);
        //ラジオ表示
        f.add(myr,BorderLayout.WEST);
        f.add(myr2,BorderLayout.CENTER);     
     
       //ラジオグループ作成
        ButtonGroup gr = new ButtonGroup();
        gr.add(myr);
        gr.add(myr2);
        //フレーム表示
        f.setVisible(true);
        //リスナー設定
        f.addWindowListener(new Ad());
        f.myr.addActionListener(new Ad());
        f.myr2.addActionListener(new Ad());              
    }

     public void paint(Graphics g)
    {     
    }
}
class Ad extends WindowAdapter implements ActionListener
{
    public void windowClosing(WindowEvent e){
       System.exit(0);
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== Botan1.f.myr) {
            //Aが選択されたときの処理
            Botan1.f.la.setText("Aが選択");
        }
        if(e.getSource()== Botan1.f.myr2) {
           //Bが選択されたときの処理
            Botan1.f.la.setText("Bが選択");
        }               
    }



}