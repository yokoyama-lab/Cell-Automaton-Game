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
        private LifeCell surroundings2[][] = {
            {null,null,null,null,null},
            {null,null,null,null,null},
            {null,null,null,null,null},
            {null,null,null,null,null},
            {null,null,null,null,null}
        };
        private ArrayList<LifeCell> surroundings3;
	/**
	 * Constructors
	 */
	public LifeCell(){
		super("");
		surroundings = new ArrayList<LifeCell>();
                surroundings2 = new LifeCell[5][5];
                /*for (int i = 0; i < 5; i++) {
                    for (int s = 0; s < 5; s++){
                        surroundings2[i][s] = 0;
                    }
                }*/
                surroundings3 = new ArrayList<LifeCell>();
		setLayout();
	}

	public LifeCell(Rectangle rect) {
		super("");
		this.setBounds(rect);
		surroundings = new ArrayList<LifeCell>();
                //surroundings2 = new ArrayList<LifeCell>();
                surroundings2 = new LifeCell[5][5];
                /*for (int i = 0; i < 5; i++) {
                    for (int s = 0; s < 5; s++){
                        surroundings2[i][s] = 0;
                    }
                    }*/
                surroundings3 = new ArrayList<LifeCell>();
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
        //(8近傍)
	public void addSurroundings(LifeCell cell){
		surroundings.add(cell);
	}
        //(12近傍)
        public void addSurroundings2(LifeCell cell, int y, int x, int flag){
            if(flag == 1){    
                surroundings2[y][x] = cell;
            }else{
                surroundings2[y][x] = cell;
                surroundings2[y][x].isLiving = 0;
            }
	}
        //(回転用近傍)
        public void addSurroundings3(LifeCell cell){
		surroundings3.add(cell);
	}
        //privateのisLivingの取得
        /*
        public int getisLiving(){
            return isLiving;
        }
        */
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
                int cnt = 0, cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0, cnt5 = 0;
                int cntall = 0;
                
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
                       
		} else {
                    cnt = 0;
                    if(isLiving == surroundings2[1][2].isLiving) {
                        cnt++;
                    }
                    if(isLiving == surroundings2[2][3].isLiving) {
                        cnt++;
                    }
                    if(isLiving == surroundings2[2][1].isLiving) {
                        cnt++;
                    }
                    if(isLiving == surroundings2[3][2].isLiving) {
                        cnt++;
                    }

                    if(cnt >= 2) {
                        this.willLiving = 0;
                    }else if(cnt == 1){
                        if(isLiving == surroundings2[2-1][2].isLiving && 
                           ((surroundings2[2-1][2-1].isLiving == isLiving || surroundings2[2-2][2].isLiving == isLiving) || surroundings2[2-1][2+1].isLiving == isLiving))
                            this.willLiving = 0; 
                        else if(isLiving == surroundings2[2][2-1].isLiving && 
                           ((surroundings2[2-1][2-1].isLiving == isLiving || surroundings2[2][2-2].isLiving == isLiving) || surroundings2[2+1][2-1].isLiving == isLiving))
                            this.willLiving = 0; 
                        else if(isLiving == surroundings2[2][2+1].isLiving &&
                           ((surroundings2[2-1][2+1].isLiving == isLiving || surroundings2[2][2+2].isLiving == isLiving) || surroundings2[2+1][2+1].isLiving == isLiving))
                            this.willLiving = 0;
                        else if(isLiving == surroundings2[2+1][2].isLiving &&
                           ((surroundings2[2+1][2-1].isLiving == isLiving || surroundings2[2+1][2+1].isLiving == isLiving) || surroundings2[2+2][2].isLiving == isLiving) )
                            this.willLiving = 0;
                        else this.willLiving = isLiving;
                    }else{
                        //生存
                        this.willLiving = isLiving; 
                    }
                    return;
                }
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