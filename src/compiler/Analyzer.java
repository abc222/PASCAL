package compiler;


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

	public Analyzer() {
		super();
		analyzeStatck = new Stack<String>();
		// 结束符进栈
		analyzeStatck.push("#");
	}

	private ArrayList<AnalyzeProduce> analyzeProduces;

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
		// while (analyzeStatck.peek() != '#' && ss[0] != '#') {
		while (!analyzeStatck.peek().equals("#")) {
			index++;
			String[] ss = str.split(" ");
			if (!analyzeStatck.peek().equals(ss[0])) {
				//System.out.println("bu匹配："+analyzeStatck.peek()+" "+ss[0]);
				// 到分析表中找到这个产生式
				String nowUseExpStr = TextUtil.findUseExp(ll1.getSelectMap(), analyzeStatck.peek(), ss[0]);
				System.out.println(index + "\t\t\t" + analyzeStatck.toString() + "\t\t\t" + str + "\t\t\t"
						+ analyzeStatck.peek() + "->" + nowUseExpStr);
				AnalyzeProduce produce = new AnalyzeProduce();
				produce.setIndex(index);
				produce.setAnalyzeStackStr(analyzeStatck.toString());
				produce.setStr(str);
				if (null == nowUseExpStr) {
					produce.setUseExpStr("无法匹配!");
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
				//System.out.println("匹配："+analyzeStatck.peek()+" "+ss[0]);
				System.out.println(index + "\t\t\t" + analyzeStatck.toString() + "\t\t\t" + str + "\t\t\t" + "“"
						+ ss[0] + "”匹配");
				AnalyzeProduce produce = new AnalyzeProduce();
				produce.setIndex(index);
				produce.setAnalyzeStackStr(analyzeStatck.toString());
				produce.setStr(str);
				produce.setUseExpStr("“" + ss[0] + "”匹配");
				analyzeProduces.add(produce);
				analyzeStatck.pop();
				int i = 1;
				while(!String.valueOf(str.charAt(i)).equals(" ")){
					i++;
				}				
				str = str.substring(i+1);
				continue;
			}
		}
		
		if(str.equals("#")){
			System.out.println(++index + "\t\t\t" + analyzeStatck.toString() + "\t\t\t" + str + "\t\t\t" 
					+ "“#”匹配");
			System.out.println("分析成功！");
		}
		else
			System.out.println("分析失败！");

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
		analyzer.setStartChar("E");
		//analyzer.setStartChar("S");
		analyzer.setLl1(gs);
		analyzer.setStr("i + i * i #");
		//analyzer.setStr("1 28 26 4 28 27 28 25 5 26 2 28 11 29 26 8 28 "
		//		+ "20 29 9 28 11 29 10 28 11 29 26 3 24 #");
		//analyzer.setStr("2 6 28 20 29 7 28 11 29 3 #");
		gs.genAnalyzeTable();
		analyzer.analyze();
		System.out.println("");
		
		
		    
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
		gsArray.add("E->T E'");
		gsArray.add("E'->A T E'");
		gsArray.add("E'->ε");
		gsArray.add("T->F T'");  
		gsArray.add("T'->M F T'");
		gsArray.add("T'->ε");
		gsArray.add("F->( E )");
		gsArray.add("F->i");  
		gsArray.add("A->+");  
		gsArray.add("A->-");
		gsArray.add("M->*");
		gsArray.add("M->/");
		
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
//		gsArray.add("S->2 M 3");
//		gsArray.add("M->B");
//		gsArray.add("M->C");
//		gsArray.add("A->28 11 E");
//		gsArray.add("B->8 D 9 A 10 A");
//		gsArray.add("C->6 D 7 A");
//		gsArray.add("D->E H E");
//		gsArray.add("E->F E'");
//		gsArray.add("E'->12 F E'");
//		gsArray.add("E'->13 F E'");
//		gsArray.add("E'->ε");
//		gsArray.add("F->G F'");
//		gsArray.add("F'->14 G F'");
//		gsArray.add("F'->15 G F'");
//		gsArray.add("F'->ε");
//		gsArray.add("G->28");
//		gsArray.add("G->29");
//		gsArray.add("G->16 E 17");
//		gsArray.add("H->18");
//		gsArray.add("H->19");
//		gsArray.add("H->20");
//		gsArray.add("H->21");
//		gsArray.add("H->22");
//		gsArray.add("H->23");
	}

}


