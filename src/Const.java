import java.awt.Dimension;
import java.util.stream.IntStream;

/**
 * 定数値を扱うクラス
 * @author Atsuya Sato
 */
public final class Const {
	private Const(){
		
	}
	//メインフレームのサイズ
    public final static Dimension FRAME_SIZE = new Dimension(504,504);
	
	//セルの大きさ(正方形一辺の長さ)
	//セルサイズはフレームサイズの縦幅及び横幅の約数となっている必要がある
	public final static int CELL_SIZE = 36;
	
	//無限ループ内でのスリープ時間 ( ex. 値を小さくするほど更新速度が上がる)
	//レベル1での更新間隔（基準値）。レベルが上がるごとに短くなる。
	public final static long SLEEP_TIME_MS = 1500;

	//難易度進行：更新間隔の下限と、レベルごとに短縮する量
	public final static long MIN_SLEEP_TIME_MS = 300;
	public final static long SLEEP_STEP_MS = 100;

	//スコアがこの値だけ増えるごとにレベルが1上がる
	public final static int SCORE_PER_LEVEL = 50;

	//同時消しコンボの倍率上限（1世代でK個消すと min(K, この値) 倍）
	public final static int MAX_COMBO_MULT = 5;

	//生存セルがこの数を超えるとゲームオーバー
	public final static int GAMEOVER_LIMIT = 120;

	//ハイスコアの保存先ファイル名（ユーザのホーム直下）
	public final static String HIGHSCORE_FILE = ".cellautomaton_highscore";


	//セルが生存するために必要な周辺セル数の範囲
	public final static Range<Integer> LIVING_RANGE = new Range<Integer>(2,3);
	public final static int BIRTH_CNT = 3;
	
	//ボタンによるパターン生成の生成位置
	public final static Dimension sponeLocation = new Dimension(0,0);
		
}