/**
 * プリセットとして提供するセルパターンをまとめたクラス
 * @author Atsuya Sato
 */
import java.util.Random;
public class CellPattern{
	enum Pattern { GLIDER, SPACESHIP, GALAXY }
 static  Random rnd=new Random();
  static int r = rnd.nextInt(3)+1;
	//グライダーパターン
	private final static int[][] glider_ptn = {
			{r,1,r},
			{0,0,r},
			{r,r,r}
			};  
	//宇宙船パターン
	private final static int[][] spaceship_ptn = {
            {1,1,3,2,1,2,3,2,1,2,4,4,1,4},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,4},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {4,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,4},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,4},
            {1,2,2,1,4,3,1,2,3,4,2,3,3,2}
			};		
	//銀河パターン
	private final static int[][] galaxy_ptn = {

 
            {1,2,3,4,1,2,3,4,1,2,3,2,1,2},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {4,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,4},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {4,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,2},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,4},
            {2,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {4,0,0,0,0,0,0,0,0,0,0,0,0,3},
            {3,0,0,0,0,0,0,0,0,0,0,0,0,4},
            {1,2,3,1,4,2,1,2,1,4,2,3,1,2}
			};
	
	/**
	 * パターンの生成
	 * @param cells 盤面の二重配列
	 * @param p パターンの指定
	 */
	public static void patternGenerator(LifeCell[][] cells,CellPattern.Pattern p){
		int pattern[][] = {{}};
		switch(p){
			case GLIDER:
				pattern = glider_ptn;
				break;
			case SPACESHIP:
				pattern = spaceship_ptn;
				break;
			case GALAXY:
				pattern = galaxy_ptn;
				break;
		
		}	
		int x_max = cells[0].length;
		int y_max = cells.length;				
		//盤面の大きさより、パターンの生成領域超えていた場合
		if(Const.sponeLocation.width + pattern[0].length > x_max){ return; }
		if(Const.sponeLocation.height + pattern.length > y_max){ return; }
		//パターンに合わせて、強制的に盤面塗り替え
		for(int y = 0; y < pattern.length; y++){
			for(int x = 0; x < pattern[0].length; x++){
				LifeCell cell = cells[Const.sponeLocation.height + y][Const.sponeLocation.width + x];
				if(pattern[y][x] == 1){
					cell.forceSpawn();
				}
			}					
		}			
	}
}