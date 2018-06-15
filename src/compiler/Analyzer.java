package compiler;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;

import data.AnalyzeProduce;
import util.TextUtil;

/**
 * 
 * @function 句子分析器
 *
 */
public class Analyzer {

	public Analyzer() throws Exception {
		super();
		analyzeStatck = new Stack<String>();
		// 结束符进栈
		analyzeStatck.push("#");
		graAnalyzer();
	}

	private ArrayList<AnalyzeProduce> analyzeProduces;
	
	/**
	 * 成功标志
	 * 0 为语法分析失败
	 * 1 为语法分成功
	 * 2 为词法分析失败
	 */
	public int flag = 0;

	/**
	 * LL（1）文法
	 */
	private LL1 ll1;

	public LL1 getLl1() {
		return ll1;
	}

	public void setLl1(LL1 ll1) {
		this.ll1 = ll1;
	}

	/**
	 * 开始符
	 */
	private String startChar;

	/**
	 * 分析栈
	 */
	private Stack<String> analyzeStatck;
	/**
	 * 剩余输入串
	 */
	private String str;
	/**
	 * 推导所用产生或匹配
	 */
	private String useExp;

	public ArrayList<AnalyzeProduce> getAnalyzeProduces() {
		return analyzeProduces;
	}

	public void setAnalyzeProduces(ArrayList<AnalyzeProduce> analyzeProduces) {
		this.analyzeProduces = analyzeProduces;
	}

	public String getStartChar() {
		return startChar;
	}

	public void setStartChar(String startChar) {
		this.startChar = startChar;
	}

	public Stack<String> getAnalyzeStatck() {
		return analyzeStatck;
	}

	public void setAnalyzeStatck(Stack<String> analyzeStatck) {
		this.analyzeStatck = analyzeStatck;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getUseExp() {
		return useExp;
	}

	public void setUseExp(String useExp) {
		this.useExp = useExp;
	}
	
	/**
	 * 分析
	 */
	public void analyze() {
		analyzeProduces = new ArrayList<AnalyzeProduce>();
		// 开始符进栈
		analyzeStatck.push(startChar);
		System.out.println("开始符:" + startChar);
		int index = 0;
		// 开始分析
		String nowUseExpStr = null;
		while (!analyzeStatck.peek().equals("#") && !str.equals("#") && flag != 2) {
			index++;
			String[] ss = str.split(" ");
			if (!analyzeStatck.peek().equals(ss[0])) {
				//System.out.println("bu匹配："+analyzeStatck.peek()+" "+ss[0]);
				// 到分析表中找到这个产生式
				nowUseExpStr = TextUtil.findUseExp(ll1.getSelectMap(), analyzeStatck.peek(), ss[0]);
				System.out.println(index + "\t\t\t" + analyzeStatck.toString() + "\t\t\t" + str + "\t\t\t"
						+ analyzeStatck.peek() + "->" + nowUseExpStr);
				AnalyzeProduce produce = new AnalyzeProduce();
				produce.setIndex(index);
				produce.setAnalyzeStackStr(analyzeStatck.toString());
				produce.setStr(str.toString());
				if (nowUseExpStr == null) {
					produce.setUseExpStr("无法匹配!");
					break;
				} else {
					produce.setUseExpStr(analyzeStatck.peek() + "->" + nowUseExpStr);
				}
				analyzeProduces.add(produce);
				// 将之前的分析栈中的栈顶出栈
				analyzeStatck.pop();
				// 将要用到的表达式入栈,反序入栈
				if (null != nowUseExpStr && !nowUseExpStr.equals("ε")) {
					String[] sss = nowUseExpStr.split(" ");
					for (int j = sss.length - 1; j >= 0; j--) {
						String currentChar = sss[j];
						analyzeStatck.push(currentChar);
					}
				}
				continue;
			}
			// 如果可以匹配,分析栈出栈，串去掉一位
			if (analyzeStatck.peek().equals(ss[0])) {
				//System.out.println("匹配："+analyzeStatck.peek()+" "+str[0]);
				System.out.println(index + "\t\t\t" + analyzeStatck.toString() + "\t\t\t" + str + "\t\t\t" + "“"
						+ ss[0] + "”匹配");
				AnalyzeProduce produce = new AnalyzeProduce();
				produce.setIndex(index);
				produce.setAnalyzeStackStr(analyzeStatck.toString());
				produce.setStr(str.toString());
				produce.setUseExpStr("“" + ss[0] + "”匹配");
				analyzeProduces.add(produce);
				analyzeStatck.pop();
				int i = 1;
				while(!String.valueOf(str.charAt(i)).equals(" ")){
					i++;
				}				
				str = str.substring(i+1);
			}
		}
		if(flag == 2)
			System.out.println("词法分析失败！");
		else if(analyzeStatck.peek().equals("#") && str.equals("#")){
			System.out.println(++index + "\t\t\t" + analyzeStatck.toString() + "\t\t\t" + str + "\t\t\t" 
					+ "“#”匹配");
			System.out.println("语法分析成功！");
			flag = 1;
		}else{
			System.out.println("语法分析失败！");
		}

	}
	
	/**
	 * 将词法分析产生的二元式文件作为语法分析的输入
	 * @param filePath
	 * @throws Exception
	 */
	public void gramAnalyse() throws Exception {
		FileInputStream fis = new FileInputStream("D:\\gl\\workspace\\PASCAL\\result\\wordListForNext.txt");
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(bis, "utf-8");
		BufferedReader inbr = new BufferedReader(isr);
		String st0 = "";
		st0 = inbr.readLine();
		if(String.valueOf(st0.charAt(0)).equals("!"))
			flag = 2;
		String[] st1 = st0.split(" ");
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i < st1.length;i ++){
			String st2 = st1[i].replace("(","");
			String[] st3 = st2.split(",");
			sb.append(st3[0]+" ");	
		}
		sb.append("#");
		str = sb.toString();
		System.out.println(str);
		inbr.close();
	}
	
	public void graAnalyzer() throws Exception{
		//LL（1）文法产生集合
		ArrayList<String> gsArray = new ArrayList<String>();
		LL1 gs = new LL1();
		initLL1(gsArray);
		gs.setGsArray(gsArray);
		gs.getNvNt();
		gs.initExpressionMaps();
		gs.getFirst();
		// 设置开始符
		gs.setS("S");
		gs.getFollow();
		//输出查看
		TreeSet s = gs.getNtSet();
		TreeSet ss = gs.getNvSet();
		System.out.print("非终止：");
		for (Iterator iter = ss.iterator(); iter.hasNext();) {
			System.out.print(iter.next() + " ");
		}
		System.out.println();
		System.out.print("终止：");
		for (Iterator iter = s.iterator(); iter.hasNext();) {
			System.out.print(iter.next() + " ");
		}
		System.out.println();
		// 遍历FITST集合
		System.out.println("FIRST集合：");
		gs.getFirst();
		HashMap m2 = gs.getFirstMap();
		Iterator iter2 = m2.entrySet().iterator();
		String key2;
		while (iter2.hasNext()) {
			Map.Entry entry = (Map.Entry) iter2.next();
			// 获取key
			key2 = (String) entry.getKey();
			System.out.print(key2 + " :");
			// 获取value
			for (Iterator ite = ((TreeSet) entry.getValue()).iterator(); ite.hasNext();) {
				System.out.print(ite.next() + " ");
			}
			System.out.println();
		}

		// 遍历FELLOW集合
		System.out.println("FELLOW集合：");
		gs.getFollow();
		HashMap m3 = gs.getFollowMap();
		Iterator iter3 = m3.entrySet().iterator();
		String key3;
		while (iter3.hasNext()) {
			Map.Entry entry = (Map.Entry) iter3.next();
			// 获取key
			key3 = (String) entry.getKey();
			System.out.print(key3 + " :");
			// 获取value
			for (Iterator ite = ((TreeSet) entry.getValue()).iterator(); ite.hasNext();) {
				System.out.print(ite.next() + " ");
			}
			System.out.println();
		}
		gs.getSelect();
		// 创建一个分析器
		// analyzer.setStartChar("E");
		setStartChar("S");
		setLl1(gs);
		System.out.println("分析表：");
		gs.genAnalyzeTable();
		System.out.println("输入串：");
		gramAnalyse();
		setStr(str);
		System.out.println("分析过程：");
		analyze();
	}
	
	
	public static void main(String[] args) throws Exception {
		// // LL（1）文法产生集合
		ArrayList<String> gsArray = new ArrayList<String>();
		LL1 gs = new LL1();
		initLL1(gsArray);
		gs.setGsArray(gsArray);
		gs.getNvNt();
		gs.initExpressionMaps();
		gs.getFirst();
		// 设置开始符
	 	gs.setS("E");
		gs.getFollow();
		//输出查看
		TreeSet s = gs.getNtSet();
	    TreeSet ss = gs.getNvSet();  
	    System.out.print("非终止：");
	    for(Iterator iter = ss.iterator(); iter.hasNext(); ) { 
	    	System.out.print(iter.next()+" ");
	    } 
	    System.out.println();
	    System.out.print("终止：");
	    for(Iterator iter = s.iterator(); iter.hasNext(); ) { 
	        System.out.print(iter.next()+" ");
	    }
	    System.out.println();
	  //遍历FITST集合
	    System.out.println("FIRST集合：");
	    gs.getFirst();
	    HashMap m2 = gs.getFirstMap();
	    Iterator iter2 = m2.entrySet().iterator();
	    String key2;
	    while(iter2.hasNext()) {
	        Map.Entry entry = (Map.Entry)iter2.next();
	        // 获取key
	        key2 = (String)entry.getKey();
	        System.out.print(key2+" :");
	        // 获取value    
	        for(Iterator ite = ((TreeSet)entry.getValue()).iterator(); ite.hasNext(); ) { 
		    	System.out.print(ite.next()+" ");
		    } 
	        System.out.println();
	    }
	    
	  //遍历FELLOW集合
	    System.out.println("FELLOW集合：");
	    gs.getFollow();
	    HashMap m3 = gs.getFollowMap();
	    Iterator iter3 = m3.entrySet().iterator();
	    String key3;
	    while(iter3.hasNext()) {
	        Map.Entry entry = (Map.Entry)iter3.next();
	        // 获取key
	        key3 = (String)entry.getKey();
	        System.out.print(key3+" :");
	        // 获取value    
	        for(Iterator ite = ((TreeSet)entry.getValue()).iterator(); ite.hasNext(); ) { 
		    	System.out.print(ite.next()+" ");
		    } 
	        System.out.println();
	    }
		
		gs.getSelect();
		// 创建一个分析器
		Analyzer analyzer = new Analyzer();
		//analyzer.setStartChar("E");
		analyzer.setStartChar("S");
		analyzer.setLl1(gs);
		//analyzer.setStr("i + i * i #");
		//analyzer.setStr("1 28 26 4 28 27 28 25 5 26 2 28 11 29 26 8 28 "
		//		+ "20 29 9 28 11 29 10 28 11 29 26 3 24 #");
		analyzer.setStr("2 28 11 29 26 8 28 20 29 6 28 18 29 7 28 11 29"
				+ " 10 28 11 29 3 24 #");
		//analyzer.setStr(new String[]{"2","28","11","29","26","8","28","20","29","6","28","18",
		//		"29","7","28","11","29","10","28","11","29","3","24","#"});
		gs.genAnalyzeTable();
		analyzer.analyze();
		System.out.println("");
	    analyzer.gramAnalyse();
	}

	/**
	 * 初始化LL(1)文法
	 * 
	 * @param gsArray
	 */
	private static void initLL1(ArrayList<String> gsArray) {
		//测试一
//		gsArray.add("D->* F D");
//		gsArray.add("D->ε");
//		gsArray.add("T->F D");
//		gsArray.add("E->T C");
//		gsArray.add("F->( E )");
//		gsArray.add("F->i");
//		gsArray.add("C->+ T C");
//		gsArray.add("C->ε");
		//测试二
//		gsArray.add("E->T E'");
//		gsArray.add("E'->A T E'");
//		gsArray.add("E'->ε");
//		gsArray.add("T->F T'");  
//		gsArray.add("T'->M F T'");
//		gsArray.add("T'->ε");
//		gsArray.add("F->( E )");
//		gsArray.add("F->i");  
//		gsArray.add("A->+");  
//		gsArray.add("A->-");
//		gsArray.add("M->*");
//		gsArray.add("M->/");
		
		//全部产生式，但是太多有错误
//		gsArray.add("S->1 28 26 A");
//		gsArray.add("A->B 2 F 3 24");
//		gsArray.add("B->4 C 26");
//		gsArray.add("C->E 25 D");
//		gsArray.add("C->E 25 D 26 C");
//		gsArray.add("D->5");
//		//gsArray.add("E->P");
//		gsArray.add("E->P 27 E");
//		gsArray.add("F->ε");
//		gsArray.add("F->G 26 F");
//		gsArray.add("G->H");
//		gsArray.add("G->I");
//		gsArray.add("G->J");
//		gsArray.add("G->K");
//		gsArray.add("H->P 11 L");
//		gsArray.add("I->8 O 9 G 10 H");
//		gsArray.add("J->6 O 7 H");
//		gsArray.add("K->2 F 3");  
//		gsArray.add("L->M L'");//消除左递归
//		gsArray.add("L'->ε");
//		gsArray.add("L'->12 M L'");
//		gsArray.add("L'->13 M L'");
//		gsArray.add("M->N M'");//消除左递归
//		gsArray.add("M'->ε");
//		gsArray.add("M'->14 N M'");
//		gsArray.add("M'->15 N M'");
//		gsArray.add("N->P");
//		gsArray.add("N->29");
//		gsArray.add("N->16 L 17");
//		gsArray.add("O->L V L");  
//		gsArray.add("P->28"); 
//		gsArray.add("V->18");
//		gsArray.add("V->19");
//		gsArray.add("V->20");
//		gsArray.add("V->21");
//		gsArray.add("V->22");
//		gsArray.add("V->23");
		
		//最终使用
		gsArray.add("S->2 P 3 24");
		//gsArray.add("P->A");
		gsArray.add("P->A 26 B");
		//gsArray.add("P->C");
		gsArray.add("A->28 11 E");//赋值语句
		gsArray.add("B->8 D 9 C 10 A");//if语句
		gsArray.add("C->6 D 7 A");//while语句
		gsArray.add("D->E H E");//关系表达式
		gsArray.add("E->F");
//		gsArray.add("E->F E'");//算术表达式  消除左递归
//		gsArray.add("E'->12 F E'");
//		gsArray.add("E'->13 F E'");
//		gsArray.add("E'->ε");
		gsArray.add("F->G");
//		gsArray.add("F->G F'");//项  消除左递归
//		gsArray.add("F'->14 G F'");
//		gsArray.add("F'->15 G F'");
//		gsArray.add("F'->ε");
		gsArray.add("G->28");//因式
		gsArray.add("G->29");
		gsArray.add("G->16 E 17");
		gsArray.add("H->18");//关系符
		gsArray.add("H->19");
		gsArray.add("H->20");
		gsArray.add("H->21");
		gsArray.add("H->22");
		gsArray.add("H->23");
	}

}


