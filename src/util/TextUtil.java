package util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @function 字符工具类
 */
public class TextUtil {

	/**
	 * 是否包含这种的字符串<Br>
	 * (2)Ab,=First(b)-ε,直接添加终结符
	 * 
	 * @param str
	 * @param A
	 * @return
	 */
	public static boolean containsAb(TreeSet<String> ntSet, String itemCharStr, String A) {
		String[] str = itemCharStr.split(" ");
		int aIndex = -1;
		for(int i = 0;i < str.length;i ++)
			if(str[i].equals(A)){
				aIndex = i;
			}
		if(aIndex != -1){
			String findStr;
			try {
				findStr = str[aIndex+1];
			} catch (Exception e) {
				return false;
			}
			if (ntSet.contains(findStr)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 是否包含这种的字符串<Br>
	 * (2)AB,=First(B)-ε
	 * 
	 * @param str
	 * @param A
	 * @return
	 */
	public static boolean containsAB(TreeSet<String> nvSet, String itemCharStr, String A) {
		String[] str = itemCharStr.split(" ");
		int aIndex = -1;
		for(int i = 0;i < str.length;i ++)
			if(str[i].equals(A)){
				aIndex = i;
			}
		if(aIndex != -1){
			String findStr;
			try {
				findStr = str[aIndex+1];
			} catch (Exception e) {
				return false;
			}
			if (nvSet.contains(findStr)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 获取A后的字符
	 * 
	 * @param itemCharStr
	 * @param A
	 * @return
	 */
	public static String getAlastChar(String itemCharStr, String A) {
		String[] str = itemCharStr.split(" ");
		int aIndex = -1;
		for(int i = 0;i < str.length;i ++)
			if(str[i].equals(A)){
				aIndex = i;
			}
		if(aIndex != -1){
			String findStr = "";
			try {
				findStr = str[aIndex+1];
			} catch (Exception e) {
				return null;
			}
			return findStr;
		}
		return null;
	}
	
	/**
	 * 形如aBb,b=空
	 * 
	 * @param nvSet
	 * @param itemCharStr
	 * @param A
	 * @param expressionMap
	 * @return
	 */
	public static boolean containsbAbIsNull(TreeSet<String> nvSet, String itemCharStr, String A,
			HashMap<String, ArrayList<String>> expressionMap) {
		if (containsAB(nvSet, itemCharStr, A)) {
			String alastChar = getAlastChar(itemCharStr, A);
			//System.out.println("----------------+++++++++++++++++++--" + expressionMap.toString());
			ArrayList<String> arrayList = expressionMap.get(alastChar);
			if (arrayList.contains("ε")) {
				//System.out.println(alastChar + "  contains('ε')" + A);
				return true;
			}
		}
		return false;

	}
	
	/**
	 * (3)B->aA,=Follow(B)
	 * 
	 * @param nvSet
	 * @param itemCharStr
	 * @param a
	 * @param expressionMap
	 * @return
	 */
	public static boolean containsbA(TreeSet<String> nvSet, String itemCharStr, String a,
			HashMap<String, ArrayList<String>> expressionMap) {
		String[] str = itemCharStr.split(" ");
		String lastStr = str[str.length-1];
		if (lastStr.equals(a)) {
			return true;
		}
		return false;

	}

	/**
	 * 是否为ε开始的
	 * 
	 * @param selectExp
	 * @return
	 */
	public static boolean isEmptyStart(String selectExp) {
		if (selectExp.equals("ε")) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是终结符开始的
	 * 
	 * @param ntSet
	 * @param selectExp
	 * @return
	 */
	public static boolean isNtStart(TreeSet<String> ntSet, String selectExp) {
		String[] str = selectExp.split(" ");
		if (ntSet.contains(str[0])) {
			return true;
		}
		return false;
	}

	/**
	 * 是否是非终结符开始的
	 * 
	 * @param nvSet
	 * @param selectExp
	 * @return
	 */
	public static boolean isNvStart(TreeSet<String> nvSet, String selectExp) {
		String[] str = selectExp.split(" ");
		if (nvSet.contains(str[0])) {
			return true;
		}
		return false;
	}

	/**
	 * 查找产生式
	 * 
	 * @param selectMap
	 * @param peek 当前Nv
	 * @param charAt 当前字符
	 * @return
	 */
	public static String findUseExp(TreeMap<String, HashMap<String, TreeSet<String>>> selectMap, String peek,
			String charAt) {
		try {
			HashMap<String, TreeSet<String>> hashMap = selectMap.get(peek);
			Set<String> keySet = hashMap.keySet();
			for (String useExp : keySet) {
				TreeSet<String> treeSet = hashMap.get(useExp);
				if (treeSet.contains(charAt)) {
					return useExp;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	/*
	 * 测试用
	 */
	public static void main(String[] args) {
//		TreeSet<String> set = new TreeSet<String>();
//		String str = "a b c ε";
//		set.add("ε");
//		set.add(str);
//		
//		int aIndex = str.indexOf("a");
//		String findStr;
//		findStr = str.substring(aIndex + 1, aIndex + 2);
//		
//		set.remove("ε");
//		Iterator iter = set.iterator();
//		while(iter.hasNext())
//			System.out.println(iter.next());
//		//System.out.println(str.contains("b"));
		
		String s1 = "a b";
		String s2 = "a";
		int i = 1;
		while(!String.valueOf(s1.charAt(i)).equals(" ")){
			i++;
		}			
		System.out.println(i);
		System.out.println(s1.substring(i+1));
	}
}


