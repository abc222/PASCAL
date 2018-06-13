package compiler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

import util.TextUtil;

/**
 * LL1文法
 * @author WangYuchao
 *
 */
public class LL1 implements Serializable{
	
	private static final long serialVersionUID = 1L;  
	  
    public LL1() {  
        super();  
        gsArray = new ArrayList<String>();  
        nvSet = new TreeSet<String>();  
        ntSet = new TreeSet<String>();  
        firstMap = new HashMap<String, TreeSet<String>>();  
        followMap = new HashMap<String, TreeSet<String>>();  
        selectMap = new TreeMap<String, HashMap<String, TreeSet<String>>>();  
    }
    
    private String[][] analyzeTable;  
    
    /** 
     * Select集合 
     */  
    private TreeMap<String, HashMap<String, TreeSet<String>>> selectMap;  
    /** 
     * LL（1）文法产生集合 
     */  
    private ArrayList<String> gsArray;  
    /** 
     * 表达式集合 
     */  
    private HashMap<String, ArrayList<String>> expressionMap;  
    /** 
     * 开始符 
     */  
    private String s;  
    /** 
     * Vn非终结符集合 
     */  
    private TreeSet<String> nvSet;  
    /** 
     * Vt终结符集合 
     */  
    private TreeSet<String> ntSet;  
    /** 
     * First集合 
     */  
    private HashMap<String, TreeSet<String>> firstMap;  
    /** 
     * Follow集合 
     */  
    private HashMap<String, TreeSet<String>> followMap;  
  
    public String[][] getAnalyzeTable() {  
        return analyzeTable;  
    }  
  
    public void setAnalyzeTable(String[][] analyzeTable) {  
        this.analyzeTable = analyzeTable;  
    }  
  
    public TreeMap<String, HashMap<String, TreeSet<String>>> getSelectMap() {  
        return selectMap;  
    }  
  
    public void setSelectMap(TreeMap<String, HashMap<String, TreeSet<String>>> selectMap) {  
        this.selectMap = selectMap;  
    }  
  
    public HashMap<String, TreeSet<String>> getFirstMap() {  
        return firstMap;  
    }  
  
    public void setFirstMap(HashMap<String, TreeSet<String>> firstMap) {  
        this.firstMap = firstMap;  
    }  
  
    public HashMap<String, TreeSet<String>> getFollowMap() {  
        return followMap;  
    }  
  
    public void setFollowMap(HashMap<String, TreeSet<String>> followMap) {  
        this.followMap = followMap;  
    }  
  
    public HashMap<String, ArrayList<String>> getExpressionMap() {  
        return expressionMap;  
    }  
  
    public void setExpressionMap(HashMap<String, ArrayList<String>> expressionMap) {  
        this.expressionMap = expressionMap;  
    }  
  
    public ArrayList<String> getGsArray() {  
        return gsArray;  
    }  
  
    public void setGsArray(ArrayList<String> gsArray) {  
        this.gsArray = gsArray;  
    }  
  
    public String getS() {  
        return s;  
    }  
  
    public void setS(String s) {  
        this.s = s;  
    }  
  
    public TreeSet<String> getNvSet() {  
        return nvSet;  
    }  
  
    public void setNvSet(TreeSet<String> nvSet) {  
        this.nvSet = nvSet;  
    }  
  
    public TreeSet<String> getNtSet() {  
        return ntSet;  
    }  
  
    public void setNtSet(TreeSet<String> ntSet) {  
        this.ntSet = ntSet;  
    }  
    
    /** 
     * 获取非终结符集与终结符集 
     *  
     * @param gsArray 
     * @param nvSet 
     * @param ntSet 
     */  
    public void getNvNt() {  
        for (String gsItem : gsArray) {  
            String[] nvNtItem = gsItem.split("->");  
            String itemStr = nvNtItem[0];   
            // nv在左边  
            nvSet.add(itemStr);
        }  
        for (String gsItem : gsArray) {  
            String[] nvNtItem = gsItem.split("->");  
            // nt在右边  
            String[] ntItemStr = nvNtItem[1].split(" ");
            // 遍历每一个字  
            for (int i = 0; i < ntItemStr.length; i++) { 
                if (!nvSet.contains(ntItemStr[i])) {  
                    ntSet.add(ntItemStr[i]);  
                }    
            }  
        }  
    }  
    
    /** 
     * 初始化表达式集合 
     */  
    public void initExpressionMaps() {  
        expressionMap = new HashMap<String, ArrayList<String>>();  
        for (String gsItem : gsArray) {  
            String[] nvNtItem = gsItem.split("->");  
            String itemStr = nvNtItem[0];  
            String itemRightStr = nvNtItem[1];    
            if (!expressionMap.containsKey(itemStr)) {  
                ArrayList<String> expArr = new ArrayList<String>();  
                expArr.add(itemRightStr);  
                expressionMap.put(itemStr, expArr);  
            } else {  
                ArrayList<String> expArr = expressionMap.get(itemStr);  
                expArr.add(itemRightStr);  
                expressionMap.put(itemStr, expArr);  
            }  
        }  
    }  
    
    /** 
     * 获取First集 
     */  
    public void getFirst() {  
        // 遍历所有Nv,求出它们的First集合  
        Iterator<String> iterator = nvSet.iterator();  
        while (iterator.hasNext()) {  
            String item = iterator.next();//遍历所有终止符  
            ArrayList<String> arrayList = expressionMap.get(item);//该终止符的表达式集合  
            for (String itemStr : arrayList) {//遍历每个表达式  
                boolean shouldBreak = false;  
                // Y1Y2Y3...Yk  
                for (int i = 0; i < itemStr.length(); i++) {
                	String[] str = itemStr.split(" ");
                	String itemChar = str[i];//表达式每个字符  
                    TreeSet<String> itemSet = firstMap.get(item);//正在遍历的终止符的First集合  
                    if (null == itemSet) {  
                        itemSet = new TreeSet<String>();  
                    }  
                    shouldBreak = calcFirst(itemSet, item, itemChar);  
                    if (shouldBreak) {  
                        break;  
                    }  
                }  
            }  
        }  
    }
    
    /** 
     * 计算First函数 
     *  
     * @param itemSet 正在计算的终止符的First集合 
     * @param item 正在计算的终止符
     * @param itemChar 正在计算的终止符对应的表达式第一个字符
     * @return
     */  
    private boolean calcFirst(TreeSet<String> itemSet, String item, String itemChar) { 
    	// get ago
    	// TreeSet<String> itemSet = new TreeSet<String>();  
        // 将它的每一位和Nt判断下  
        // 是终结符或空串,就停止，并将它加到FirstMap中    
        if (itemChar == "ε" || ntSet.contains(itemChar)) {  
            itemSet.add(itemChar);  
            firstMap.put(item, itemSet);  
            return true;  
        } else if (nvSet.contains(itemChar)) {//非终结符  
            ArrayList<String> arrayList = expressionMap.get(itemChar);//该字符的表达式集合  
            for (int i = 0; i < arrayList.size(); i++) {//遍历表达式  
                String string = arrayList.get(i);//每个表达式
                String[] str = string.split(" ");
            	String ss = str[0];//表达式第一个字符 
                calcFirst(itemSet, item, ss);//递归求解  
            }
            itemSet.remove("ε");  
        }  
        return true;  
    }  
	
    /** 
     * 获取Follow集合 
     */  
    public void getFollow() {  
        for (String tempKey : nvSet) {//为每个非终止符添加fellow集合  
            TreeSet<String> tempSet = new TreeSet<String>();  
            followMap.put(tempKey, tempSet);  
        }  
        // 遍历所有Nv,求出它们的First集合  
        Iterator<String> iterator = nvSet.descendingIterator();//返回在此 set 元素上按降序进行迭代的迭代器。  
        while (iterator.hasNext()) {  
            String charItem = iterator.next();//遍历每个非终止符   
            //System.out.println("charItem:" + charItem); 
            Set<String> keySet = expressionMap.keySet();//非终止符集合  
            for (String keyCharItem : keySet) {  
                ArrayList<String> charItemArray = expressionMap.get(keyCharItem);//每个终止符的表达式集合  
                for (String itemCharStr : charItemArray) {//遍历表达式  
                    //System.out.println(keyCharItem + "->" + itemCharStr);  
                    TreeSet<String> itemSet = followMap.get(charItem);//正在遍历的非终止符的fellow集合  
                    calcFollow(charItem, charItem, keyCharItem, itemCharStr, itemSet);  
                }  
            }  
        }  
    }  
  
    /** 
     * 计算Follow集 
     * 
     * @param putCharItem 正在查询item           
     * @param charItem 待找item            
     * @param keyCharItem 节点名     
     * @param itemCharStr 符号集            
     * @param itemSet 结果集合            
     */  
    private void calcFollow(String putCharItem, String charItem, String keyCharItem, String itemCharStr,  
            TreeSet<String> itemSet) {   
        // （1）A是S（开始符)，加入#  
        if (charItem.equals(s)) {  
            itemSet.add("#");  
            //System.out.println("---------------find S:" + charItem + "   ={#}+Follow(E)");  
            followMap.put(putCharItem, itemSet);  
        }  
        // (2)Ab,=First(b)-ε,直接添加终结符  
        if (TextUtil.containsAb(ntSet, itemCharStr, charItem)) {  
            String alastChar = TextUtil.getAlastChar(itemCharStr, charItem);  
            //System.out.println("---------------find Ab:" + itemCharStr + "    " + charItem + "   =" + alastChar);  
            itemSet.add(alastChar);  
            followMap.put(putCharItem, itemSet);  
            // return;  
        }  
        // (2).2AB,=First(B)-ε，添加first集合  
        if (TextUtil.containsAB(nvSet, itemCharStr, charItem)) {  
            String alastChar = TextUtil.getAlastChar(itemCharStr, charItem);  
            //System.out.println(  
            //        "---------------find AB:" + itemCharStr + "    " + charItem + "   =First(" + alastChar + ")");  
            TreeSet<String> treeSet = firstMap.get(alastChar);  
            itemSet.addAll(treeSet);  
            if (treeSet.contains("ε")) {  
                itemSet.add("#");  
            }  
            itemSet.remove("ε");  
            followMap.put(putCharItem, itemSet); 
            
			///////////////////////
			if (TextUtil.containsbAbIsNull(nvSet, itemCharStr, charItem, expressionMap)) {
				String tempChar = TextUtil.getAlastChar(itemCharStr, charItem);
				//System.out.println("tempChar:" + tempChar + "  key" + keyCharItem);
				if (!keyCharItem.equals(charItem)) {
					//System.out.println("---------------find tempChar bA: " + "tempChar:" + tempChar + keyCharItem
					//		+ "   " + itemCharStr + "    " + charItem + "   =Follow(" + keyCharItem + ")");
					Set<String> keySet = expressionMap.keySet();
					for (String keyCharItems : keySet) {
						ArrayList<String> charItemArray = expressionMap.get(keyCharItems);
						for (String itemCharStrs : charItemArray) {
							calcFollow(putCharItem, keyCharItem, keyCharItems, itemCharStrs, itemSet);
						}
					}
				}
			}
            
        }  
        // (3)B->aA,=Follow(B),添加followB  
        if (TextUtil.containsbA(nvSet, itemCharStr, charItem, expressionMap)) {  
            if (!keyCharItem.equals(charItem)) {  
                //System.out.println("---------------find bA: " + keyCharItem + "   " + itemCharStr + "    " + charItem  
                //        + "   =Follow(" + keyCharItem + ")");  
                Set<String> keySet = expressionMap.keySet();  
                for (String keyCharItems : keySet) {  
                    ArrayList<String> charItemArray = expressionMap.get(keyCharItems);  
                    for (String itemCharStrs : charItemArray) {  
                        calcFollow(putCharItem, keyCharItem, keyCharItems, itemCharStrs, itemSet);  
                    }  
                }  
            }  
        }  
    }  
    
    /** 
     * 获取Select集合 
     * 填充LL1表，查找产生式用
     */  
    public void getSelect() {  
        // 遍历每一个表达式  
        // HashMap<String, HashMap<String, TreeSet<String>>>  
        Set<String> keySet = expressionMap.keySet();  
        for (String selectKey : keySet) {  
            ArrayList<String> arrayList = expressionMap.get(selectKey);   
            HashMap<String, TreeSet<String>> selectItemMap = new HashMap<String, TreeSet<String>>();  
            for (String selectExp : arrayList) {// 每一个表达式   
                /** 
                 * 存放select结果的集合 
                 */  
                TreeSet<String> selectSet = new TreeSet<String>();  
                // set里存放的数据分3种情况,由selectExp决定  
                // 1.A->ε,=follow(A)  
                if (TextUtil.isEmptyStart(selectExp)) {  
                    selectSet = followMap.get(selectKey);  
                    selectSet.remove("ε");  
                    selectItemMap.put(selectExp, selectSet);  
                }  
                // 2.Nt开始,=Nt  
                // 终结符开始  
                if (TextUtil.isNtStart(ntSet, selectExp)) { 
                	String[] str = selectExp.split(" ");
                    selectSet.add(str[0]);  
                    selectSet.remove("ε");  
                    selectItemMap.put(selectExp, selectSet);  
                }  
                // 3.Nv开始，=first(Nv)  
                if (TextUtil.isNvStart(nvSet, selectExp)) {  
                    selectSet = firstMap.get(selectKey);  
                    selectSet.remove("ε");  
                    selectItemMap.put(selectExp, selectSet);  
                }  
                selectMap.put(selectKey, selectItemMap);  
            }  
        }  
    }  
    
    /** 
     * 生成预测分析表 
     */  
    public void genAnalyzeTable() throws Exception {  
        Object[] ntArray = ntSet.toArray();  
        Object[] nvArray = nvSet.toArray();  
        // 预测分析表初始化  
        analyzeTable = new String[nvArray.length + 1][ntArray.length + 1];  
  
        // 输出一个占位符  
        System.out.print("Nv/Nt" + "\t\t");  
        analyzeTable[0][0] = "Nv/Nt";  
        // 初始化首行  
        for (int i = 0; i < ntArray.length; i++) {  
            if (ntArray[i].equals("ε")) {  
                ntArray[i] = "#";  
            }  
            System.out.print(ntArray[i] + "\t\t");  
            analyzeTable[0][i + 1] = ntArray[i] + "";  
        }  
  
        System.out.println("");  
        for (int i = 0; i < nvArray.length; i++) {  
            // 首列初始化  
            System.out.print(nvArray[i] + "\t\t");  
            analyzeTable[i + 1][0] = nvArray[i] + ""; 
            //填充分析表
            for (int j = 0; j < ntArray.length; j++) {  
                String findUseExp = TextUtil.findUseExp(selectMap, (String) nvArray[i], (String) ntArray[j]);  
                if (null == findUseExp) {  
                    System.out.print("\t\t");  
                    analyzeTable[i + 1][j + 1] = "";  
                } else {  
                    System.out.print(nvArray[i] + "->" + findUseExp + "\t\t");  
                    analyzeTable[i + 1][j + 1] = nvArray[i] + "->" + findUseExp;  
                }  
            }  
            System.out.println();  
        }  
    }  
    
	public static void main(String[] args) {
		LL1 ll1 = new LL1();
//		ll1.gsArray.add("D->* F D");  
//		ll1.gsArray.add("D->ε");  
//		ll1.gsArray.add("T->F D");  
//		ll1.gsArray.add("E->C T");  
//		ll1.gsArray.add("F->( E )");
//		ll1.gsArray.add("F->11 C");
//		ll1.gsArray.add("F->i");  
//		ll1.gsArray.add("C->+ T C");  
//		ll1.gsArray.add("C->ε");
		ll1.gsArray.add("E->T E'");
		ll1.gsArray.add("E'->A T E'");
		ll1.gsArray.add("E'->ε");
		ll1.gsArray.add("T->F T'");  
		ll1.gsArray.add("T'->M F T'");
		ll1.gsArray.add("T'->ε");
		ll1.gsArray.add("F->( E )");
		ll1.gsArray.add("F->i");  
		ll1.gsArray.add("A->+");  
		ll1.gsArray.add("A->-");
		ll1.gsArray.add("M->*");
		ll1.gsArray.add("M->/");
	    ll1.getNvNt();
	    ll1.initExpressionMaps();
	    
	    TreeSet s = ll1.getNtSet();
	    TreeSet ss = ll1.getNvSet();  
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
	    
	    /**
	     * 遍历HashMap的值

第一步：根据value()获取HashMap的“值”的集合。
第二步：通过Iterator迭代器遍历“第一步”得到的集合。

// 假设map是HashMap对象
// map中的key是String类型，value是Integer类型
Integer value = null;
Collection c = map.values();
Iterator iter= c.iterator();
while (iter.hasNext()) {
    value = (Integer)iter.next();
}
	     */
	    //遍历表达式集合
	    HashMap m = ll1.getExpressionMap();
	    Iterator iter = m.entrySet().iterator();
	    String key;
	    while(iter.hasNext()) {
	        Map.Entry entry = (Map.Entry)iter.next();
	        // 获取key
	        key = (String)entry.getKey();
	        System.out.print(key+" :");
	        // 获取value
	        ArrayList a = (ArrayList)entry.getValue();
	        Iterator ite = a.iterator();
	        while (ite.hasNext()) {
	            System.out.print((String)ite.next()+",");
	        }
	        System.out.println();
	    }
	    
	    //遍历FITST集合
	    System.out.println("FIRST集合：");
	    ll1.getFirst();
	    HashMap m2 = ll1.getFirstMap();
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
	    ll1.getFollow();
	    HashMap m3 = ll1.getFollowMap();
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
	    
	    ll1.getSelect();
	    try {
			ll1.genAnalyzeTable();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
}


