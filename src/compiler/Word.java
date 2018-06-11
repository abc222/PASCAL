package compiler;

import java.util.ArrayList;

import compiler.LexAnalyse;
import data.BoundarySign;
import data.Key;
import data.Operator;
import view.Mainform;

/**
 * 单词类
 * 
 * 1、单词序号 2、单词的值 3、单词类型 4、单词所在行 5、单词是否合法
 */
public class Word {
	public final static String KEY = "关键字";
	public final static String OPERATOR = "运算符";
	public final static String BOUNDARYSIGN = "分隔符";
	public final static String IDENTIFIER = "标识符";
	public final static String INT_CONST = "整形常量";
	public final static String UNIDEF = "未知类型";
	public static ArrayList<Key> key = new ArrayList<Key>();// 关键字集合
	public static ArrayList<Operator> operator = new ArrayList<Operator>();// 运算符集合
	public static ArrayList<BoundarySign> boundarySign = new ArrayList<BoundarySign>();// 分隔符集合
	public static ArrayList<String> idTable = new ArrayList<String>();// 标识符表
	
	static {
		Word.key.add(new Key("PROGRAM",1));
		Word.key.add(new Key("BEGIN",2));
		Word.key.add(new Key("END",3));
		Word.key.add(new Key("VAR",4));
		Word.key.add(new Key("INTEGER",5));
		Word.key.add(new Key("WHILE",6));
		Word.key.add(new Key("DO",7));
		Word.key.add(new Key("IF",8));
		Word.key.add(new Key("THEN",9));
		Word.key.add(new Key("ELSE",10));
		//Word.operator.add(new Operator(':=',11));
		Word.operator.add(new Operator('+',12));
		Word.operator.add(new Operator('-',13));
		Word.operator.add(new Operator('*',14));
		Word.operator.add(new Operator('/',15));
		Word.operator.add(new Operator('(',16));
		Word.operator.add(new Operator(')',17));
		//Word.operator.add(new Operator('<',18));
		//Word.operator.add(new Operator("<=",19));
		//Word.operator.add(new Operator('>',20));
		//Word.operator.add(new Operator(">=",21));
		//Word.operator.add(new Operator("<>",22));
		Word.operator.add(new Operator('=',23));
		Word.boundarySign.add(new BoundarySign('.',24));
		//Word.boundarySign.add(new BoundarySign(':',25));
		Word.boundarySign.add(new BoundarySign(';',26));
		Word.boundarySign.add(new BoundarySign(',',27));
	}
	public int id;// 单词码
	public String value;// 单词的值
	public String type;// 单词类型
	//public String attribute;//单词的属性
	public int line;// 单词所在行
	public boolean flag = true;//单词是否合法

	public Word() {

	}

	public Word(int id, String value, String type) {
		this.id = id;
		this.value = value;
		this.type = type;
	}


	/**
	 * 判断是不是关键字
	 * @param word
	 * @return
	 */
	public static int isKey(String word) {
		for(int i = 0;i < Word.key.size();i ++)
			if(Word.key.get(i).value.equals(word))
				return i;
		return -1;
	}
	
	/**
	 * 判断是不是运算符
	 * @param word
	 * @return
	 */
	public static int isOperator(char word) {
		for(int i = 0;i < Word.operator.size();i ++)
			if(Word.operator.get(i).value == word)
				return i;
		return -1;
	}
	
	/**
	 * 判断是不是分隔符
	 * @param word
	 * @return
	 */
	public static int isBoundarySign(char word) {
		for(int i = 0;i < Word.boundarySign.size();i ++)
			if(Word.boundarySign.get(i).value == word)
				return i;
		return -1;
	}
	
	/**
	 * 向标识符表添加标识符
	 * @param word
	 * @return
	 */
	public static void addToTable(String word){
		idTable.add(word);
	}
	
	/**
	 * 查表，是否有标识符，返回下标
	 * @param word
	 * @return
	 */
	public static int findIdentifierByName(String word){
		return idTable.indexOf(word) + 1;
	}
	
}
