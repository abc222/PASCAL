package compiler;

import java.util.Stack;


public class LL1test {
	
	//加入同步符号的LL(1)分析表
	private  String [][] analysisTable = new String[][]{
			{"TZ","","","TZ","synch","synch"},
			{"","+TZ","","","ε","ε"},
			{"FY","synch","","FY","synch","synch"},
			{"","ε","*FY","","ε","ε"},
			{"i","synch","synch","(E)","synch","synch"}
	};
	
	//存储终结符 
	private String [] VT = new String[]{"i","+","*","(",")","#"};
	
	//存储非终结符
	private String [] VN = new String[]{"E","Z","T","Y","F"};
	
	//输入串
	private StringBuilder strToken = new StringBuilder("i*i+i");
	
	//分析栈
	private Stack<String> stack = new Stack<String>();
	
	//a保存从输入串中读取的一个输入符号，当前符号
	private String a = null;
	
	//X中保存stack栈顶符号
	private String X = null;
	
	//flag标志预测分析是否成功
	private boolean flag = true;
	
	//记录输入串中当前字符的位置
	private int cur = 0;
	
	//记录步数
	private int count = 0;

	//初始化
	protected void init(){
		strToken.append("#");
		stack.push("#");
		System.out.printf("%s %15s %53s %13s\n","步骤 ","符号栈 ","输入串 ","所用产生式 ");
		stack.push("E");
		curCharacter();
		System.out.printf("%d %11s %20s \n",count,stack.toString(),strToken.substring(cur, strToken.length()));
	}
	
	//读取当前栈顶符号
	protected String stackPeek(){
		X = stack.peek();
		return X;
	}
	
	//返回输入串中当前位置的字母
	private String curCharacter(){
			a = String.valueOf(strToken.charAt(cur));
		return a;
	}
	
	//判断X是否是终结符
	protected boolean XisVT(){
		for(int i = 0 ; i < (VT.length - 1); i++){
			if(VT[i].equals(X)){
				return true;
			}
		}
		return false;
	}
	
	//查找X在非终结符中分析表中的横坐标
	protected String VNTI(){
		int Ni = 0 , Tj = 0;
		for(int i = 0 ; i < VN.length ; i++){
			if(VN[i].equals(X)){
				Ni = i;
			}
		}
		for(int j = 0 ; j < VT.length ; j++){
			if(VT[j].equals(a)){
				Tj = j;
			}
		}
		return analysisTable[Ni][Tj];
	}
	
	//判断M[A,a]={X->X1X2...Xk}
	//把X1X2...Xk推进栈
	//X1X2...Xk=ε，不推什么进栈
	protected boolean productionType(){
		if(VNTI() != ""){
			return true;
		}
		return false;
	}
	
	//推进stack栈
	protected void pushStack(){
		stack.pop();
		String M = VNTI();
		String ch;
		for(int i = (M.length() -1) ; i >= 0 ; i--){
			ch = String.valueOf(M.charAt(i));
			stack.push(ch);
		}
		System.out.printf("%-6d %-20s %6s %-1s->%-12s\n",(++count),
				stack.toString(),strToken.substring(cur, strToken.length()),X,M);
	}
	
	//总控程序
	protected void totalControlProgram(){
		while(flag == true){
			stackPeek();
			if(XisVT() == true){
				if(X.equals(a)){
					cur++;
					a = curCharacter();
					stack.pop();
					System.out.printf("%-6d %-20s %6s \n",(++count),stack.toString(),
							strToken.substring(cur, strToken.length()));
				}else{
					ERROR();
				}
			}else if(X.equals("#")){
				if(X.equals(a)){
					flag = false;
				}else{
					ERROR();
				}
			}else if(productionType() == true){
				if(VNTI().equals("synch")){
					ERROR();
				}else if(VNTI().equals("ε")){
					stack.pop();
					System.out.printf("%-6d %-20s %6s %-1s->%-12s\n",(++count),stack.toString(),
							strToken.substring(cur, strToken.length()),X,VNTI());
				}else{
					pushStack();
				}
			}else{
				ERROR();
			}
		}
	}
	
	//出现错误
	protected void ERROR(){
		System.out.println("输入串出现错误，无法进行分析");
		System.exit(0);
	}
	
	//打印存储分析表
	protected void printf(){
		if(flag == false){
			System.out.println("========分析成功");
		}else {
			System.out.println("========分析失败");
		}
		
	}
	
	public static void main(String[] args) {
		LL1test LL1test = new LL1test();
		LL1test.init();
		LL1test.totalControlProgram();
		LL1test.printf();
		
	}
	
}


