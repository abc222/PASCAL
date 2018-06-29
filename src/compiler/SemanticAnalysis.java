package compiler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import data.FourElement;
import data.ResultNode;

public class SemanticAnalysis {
	Vector semanticAnaString = new Vector();//语义分析串
	Vector boundarySign = new Vector();//界符
	Vector operator = new Vector();//操作数
	static int count = 0;
	int if_oversign = 0;
	int else_beginsign = 0;
	int while_beginsign = 10;
	int while_endsign = 0;
	int jump[] = new int[50];
	int sign = 0;
	static FourElement fourElement[] = new FourElement[40];

	public static void main(String[] args) throws Exception {
		SemanticAnalysis sem = new SemanticAnalysis();
		for (int k = 0; k < 40; k++) {
			sem.fourElement[k] = new FourElement();
			fourElement[k].p1 = new ResultNode();
		}
		String l = "";
		String d = "";
		String yuan = "";
		sem.operator.add("+");
		sem.operator.add("-");
		sem.operator.add("*");
		sem.operator.add("/");
		sem.operator.add(">");
		sem.operator.add(">=");
		sem.operator.add("<");
		sem.operator.add("<=");
		sem.operator.add("=");
		sem.boundarySign.add("(");
		sem.boundarySign.add(")");
		sem.boundarySign.add("{");
		sem.boundarySign.add("}");
		sem.boundarySign.add(";");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("请输入源代码:");
		while (true) {
			String s = br.readLine();
			boolean sign = false;
			String ls = "";
			if (s.equals("$"))
				break;
			int i = 0;
			for (i = 0; i < s.length(); i++) {
				ls = s.substring(i, i + 1);
				l = l + ls;
				//输入连续的字符
				while ((ls.compareToIgnoreCase("a") >= 0) && (ls.compareToIgnoreCase("z") <= 0)) {
					sign = true;
					i++;
					if (i < s.length()) {
						ls = s.substring(i, i + 1);
					}
					if (i == s.length() || ls.equals("}") || ls.equals("<") || ls.equals("<=") || ls.equals("{") 
							|| ls.equals(">")|| ls.equals(">=")|| ls.equals(",") || ls.equals(";") || ls.equals("=") 
							|| ls.equals(" ") || ls.equals("*") || ls.equals("/") || ls.equals("+") || ls.equals("-")) {
						break;
					} else {
						l = l + ls;
					}
				}
				//输入数字
				if ((ls.compareTo("0") >= 0) && (ls.compareTo("9") <= 0)) {//输入数字
					sign = true;
					i++;
					ls = s.substring(i, i + 1);
					while (true) {
						l = l + ls;
						i++;
						ls = s.substring(i, i + 1);
						if (i == s.length() || ls.equals("}") || ls.equals("<") || ls.equals("<=") || ls.equals("{") 
								|| ls.equals(">")|| ls.equals(">=")|| ls.equals(",") || ls.equals(";") || ls.equals("=") 
								|| ls.equals(" ") || ls.equals("*") || ls.equals("/") || ls.equals("+") || ls.equals("-")) {
							break;
						}
					}
				}
				if (Word.KEY.contains(l)) {//加入关键字
					sem.semanticAnaString.add(l);
					l = "";
					sign = false;
				} else if (!Word.KEY.contains(l) && sign == true) {//加入其他字符
					sem.semanticAnaString.add(l);
					l = "";
					sign = false;
				}
				if (sem.operator.contains(ls) || sem.boundarySign.contains(ls)) {//加入操作符和界符
					sem.semanticAnaString.add(ls);
					l = "";
				}
				if (ls.equals(" ")) {
					l = "";
				}
			}
		}
		sem.semanticAnaString.add("$");
		System.out.println(sem.semanticAnaString);
		sem.fourElement();
		System.out.println("四元式结果如下：");
		System.out.println();
		int i = 0;
		for (i = 0; i < count; i++) {
			if ((fourElement[i].fuhao).equals("j")) {
				System.out.println(i + "\t(" + fourElement[i].fuhao + "," + fourElement[i].op1 + "," + fourElement[i].op2
						+ "," + fourElement[i].p1.Address_Num + ")");
			} else if ((fourElement[i].fuhao).equals(">"))
				System.out.println(i + "\t(" + fourElement[i].fuhao + "," + fourElement[i].op1 + "," + fourElement[i].op2
						+ "," + fourElement[i].p1.Address_Num + ")");
			else if ((fourElement[i].fuhao).equals("<"))
				System.out.println(i + "\t(" + fourElement[i].fuhao + "," + fourElement[i].op1 + "," + fourElement[i].op2
						+ "," + fourElement[i].p1.Address_Num + ")");
			else
				System.out.println(i + "\t(" + fourElement[i].fuhao + "," + fourElement[i].op1 + "," + fourElement[i].op2
						+ "," + fourElement[i].p1.result + ")");
		}
	}

	public void fourElement() {
		String a = "";
		int i = 0;
		while (i < semanticAnaString.size()) {
			String str = "";
			String str2 = (String) semanticAnaString.elementAt(i);
			if (i < (semanticAnaString.size() - 1)) {
				str = (String) semanticAnaString.elementAt(i + 1);
			}
			if (str2.equals("IF")) {
				if (((String) semanticAnaString.elementAt(i + 2)).equals(">") || ((String) semanticAnaString.elementAt(i + 2)).equals(">=")|| 
						((String) semanticAnaString.elementAt(i + 2)).equals("<") || ((String) semanticAnaString.elementAt(i + 2)).equals("<=")
						|| ((String) semanticAnaString.elementAt(i + 2)).equals("=") || ((String) semanticAnaString.elementAt(i + 2)).equals("<>")) {
					fourElement[count].fuhao = (String) semanticAnaString.elementAt(i + 2);
					fourElement[count].op1 = (String) semanticAnaString.elementAt(i + 1);
					fourElement[count].op2 = (String) semanticAnaString.elementAt(i + 3);
					jump[sign] = count + 2;
					sign++;
					fourElement[count].p1.Address_Num = count + 2;
					count++;

					fourElement[count].fuhao = "j";
					fourElement[count].op1 = "_";
					fourElement[count].op2 = "_";
					else_beginsign = count;
					count++;
					i++;
				}
			} else if (str2.equals("ELSE")) {
				fourElement[count].fuhao = "j";
				fourElement[count].op1 = "_";
				fourElement[count].op2 = "_";

				if_oversign = count;
				count++;
				jump[sign] = count;
				sign++;
				fourElement[else_beginsign].p1.Address_Num = count;
				i++;
			}

			else if (str2.equals("WHILE")) {
				if (((String) semanticAnaString.elementAt(i + 2)).equals(">") || ((String) semanticAnaString.elementAt(i + 2)).equals(">=")|| 
						((String) semanticAnaString.elementAt(i + 2)).equals("<") || ((String) semanticAnaString.elementAt(i + 2)).equals("<=")
						|| ((String) semanticAnaString.elementAt(i + 2)).equals("=") || ((String) semanticAnaString.elementAt(i + 2)).equals("<>")) {
					while_beginsign = count;
					fourElement[count].fuhao = (String) semanticAnaString.elementAt(i + 2);
					fourElement[count].op1 = (String) semanticAnaString.elementAt(i + 1);
					fourElement[count].op2 = (String) semanticAnaString.elementAt(i + 3);

					jump[sign] = count + 2;
					sign++;
					fourElement[count].p1.Address_Num = count + 2;
					count++;
					fourElement[count].fuhao = "j";
					fourElement[count].op1 = "_";
					fourElement[count].op2 = "_";
					while_endsign = count;
					count++;
					i++;
				}
			}

			else if (((String) semanticAnaString.elementAt(i)).equals("}") && str.equals("ELSE")) {
				i++;
			} else if (((String) semanticAnaString.elementAt(i)).equals("}") && (if_oversign != 0)) {
				jump[sign] = count;
				sign++;
				fourElement[if_oversign].p1.Address_Num = count;
				if_oversign = 0;
				i++;

			} else if (str.equals("=")) {
				if (((String) semanticAnaString.elementAt(i + 5)).equals(";")) {
					fourElement[count].fuhao = (String) semanticAnaString.elementAt(i + 3);
					fourElement[count].op1 = (String) semanticAnaString.elementAt(i + 2);
					fourElement[count].op2 = (String) semanticAnaString.elementAt(i + 4);
					count++;

					fourElement[count].fuhao = (String) semanticAnaString.elementAt(i + 1);
					fourElement[count].op1 = fourElement[count - 1].p1.result;
					fourElement[count].op2 = "_";
					fourElement[count].p1.result = (String) semanticAnaString.elementAt(i);
					count++;
					i++;
				} else if (((String) semanticAnaString.elementAt(i + 3)).equals(";")) {
					fourElement[count].fuhao = (String) semanticAnaString.elementAt(i + 1);

					fourElement[count].op1 = (String) semanticAnaString.elementAt(i + 2);
					fourElement[count].op2 = "_";
					fourElement[count].p1.result = (String) semanticAnaString.elementAt(i);
					count++;
					i++;
				} else if (((String) semanticAnaString.elementAt(i + 7)).equals(";")) {
					i++;
				}
			} else if (((String) semanticAnaString.elementAt(i)).equals("}") && (while_endsign != 0)) {
				fourElement[count].fuhao = "j";

				fourElement[count].op1 = "_";
				fourElement[count].op2 = "_";

				jump[sign] = while_beginsign;
				sign++;
				fourElement[count].p1.Address_Num = while_beginsign;
				count++;
				jump[sign] = count;
				sign++;
				fourElement[while_endsign].p1.Address_Num = count;
				while_endsign = 0;
				i++;
			} else
				i++;
		}
	}

	public boolean jump(int j) {
		int i;
		for (i = 0; i <= sign - 1; i++) {
			if (j == jump[i])
				return true;
		}
		return false;
	}
}
