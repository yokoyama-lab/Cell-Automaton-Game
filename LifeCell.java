import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

/**
 * ライフゲームで使用するCell
 * @author Atsuya Sato
 */
class LifeCell extends JButton{
	//周囲のセル
	private ArrayList<LifeCell> surroundings;
	//現在の生存フラグ
	private int isLiving = 0;
	//世代更新後の生存フラグ
	private int willLiving = 0;
	/**
	 * Constructors
	 */
	public LifeCell(){
		super("");
		surroundings = new ArrayList<LifeCell>();
		setLayout();
	}

	public LifeCell(Rectangle rect) {
		super("");
		this.setBounds(rect);
		surroundings = new ArrayList<LifeCell>();
		setLayout();
	}
	/**
	 * レイアウトの設定
	 */
	private void setLayout(){
		this.setLayout(null);
		this.setBackground(Color.white);
        // these next two lines do the magic..
        this.setContentAreaFilled(true);
        this.setOpaque(true);
        this.setBorderPainted(false);
		LifeCell button = this;
		
		this.addActionListener(
				new ActionListener(){
						public void actionPerformed(ActionEvent event){
							if(button.isLiving==1){
								button.forceKill();
							}else{
								button.forceSpawn();
							}
						}
					}
				);
	}
	/**
	 * 周囲のセルを追加
	 * @param cell:LifeCell
	 */
	public void addSurroundings(LifeCell cell){
		surroundings.add(cell);
	}
	/**
	 * 世代交代を行う
	 */
	public void generationalChange(){
		isLiving = willLiving;
		willLiving = 0;
		if(isLiving == 1){
			setBackground(Color.yellow);
                }
                      else  if(isLiving == 2){
                            setBackground(Color.red);
                        }

                      else  if(isLiving == 3){
                            setBackground(Color.green);
                        }
                        
                      else  if(isLiving == 4){
                            setBackground(Color.blue);
                        }


else{
                            setBackground(Color.white);			
                        }
                }
	/**
	 * 周りのセルを調べて、世代交代の準備
	 */
	public void checkSurroundings(){
		int cnt = 0;
int cnt1 = 0;
int cnt2 = 0;
int cnt3 = 0;
int cnt4 = 0;
int cntall =0;
		for(LifeCell cell : surroundings){
			if(cell.isLiving == 1) cnt1++;
                        if(cell.isLiving == 2) cnt2++;
                        if(cell.isLiving == 3) cnt3++;
                        if(cell.isLiving == 4) cnt4++;
                        cnt= cnt1 + cnt2 + cnt3 + cnt4;
                        cntall= cnt1 + (2*cnt2) + (3*cnt3) + (4*cnt4);
        
		}
		//誕生
		if(isLiving == 0){
			//生きているセルが3つ
			if(cnt == Const.BIRTH_CNT){
                            
                            this.willLiving = (cntall % 4)+1;
                        }
                        
			return;
                       
		}
 else this.willLiving = isLiving; 
        }
	
	/**
	 * ボタンを押して生成する場合
	 */
	public void forceSpawn(){
                isLiving = 1;
		this.setBackground(Color.yellow);
          
	}
	/**
	 * ボタンを押してセルを消す場合
	 */
	public void forceKill(){
		isLiving = 2;
		this.setBackground(Color.red);
	}
	/**
	 * 盤面初期化用
	 */
	public static void forceKillAll(ArrayList<LifeCell> cells){
		for(LifeCell cell : cells){
			cell.isLiving = 0;
			cell.willLiving = 0;
			cell.setBackground(Color.white);			
		}
	}
}