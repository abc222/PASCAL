package compiler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

import compiler.Word;

/**
 * 词法分析器
 * 
 * @author KB
 * 
 */
public class LexAnalyse {

	public ArrayList<Word> wordList = new ArrayList<Word>();// 单词表
	public ArrayList<Error> errorList = new ArrayList<Error>();// 错误信息列表
	public int errorCount = 0;// 统计错误个数
	public boolean noteFlag = false;// 多行注释标志
	public boolean lexErrorFlag = false;// 词法分析出错标志
	
	public LexAnalyse() {}

	public LexAnalyse(String str) {
		lexAnalyse(str);
	}
	/**
	 * 数字字符判断
	 * @param ch
	 * @return
	 */
	private static boolean isDigit(char ch) {
		boolean flag = false;
		if ('0' <= ch && ch <= '9')
			flag = true;
		return flag;
	}

	/**
	 * 判断单词是否为常量 
	 * @param string
	 * @return
	 */
	private static boolean isInteger(String word) {
		int i;
		boolean flag = false;
		for (i = 0; i < word.length(); i++) {
			if (Character.isDigit(word.charAt(i))) {
				continue;
			} else {
				break;
			}
		}
		if (i == word.length()) {
			flag = true;
		}
		return flag;
	}


	/**
	 * 判断字符是否为字母
	 * @param ch
	 * @return
	 */
	private static boolean isLetter(char ch) {
		boolean flag = false;
		if (('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z'))
			flag = true;
		return flag;
	}

	/**
	 * 判断单词是否为合法标识符
	 * @param word
	 * @return
	 */
	private static boolean isID(String word) {
		boolean flag = false;
		int i = 0;
		if (Word.isKey(word) != -1)
			return flag;
		char temp = word.charAt(i);
		if (isLetter(temp)) {
			for (i = 1; i < word.length(); i++) {
				temp = word.charAt(i);
				if (isLetter(temp) || isDigit(temp))
					continue;
				else
					break;
			}
			if (i >= word.length())
				flag = true;
		} else
			return flag;

		return flag;
	}
	
	/**
	 * 判断词法分析是否通过
	 */
	public boolean isFail() {
		return lexErrorFlag;
	}

	/**
	 * 分析源代码
	 */
	public void analyse(String str, int line) {
		int beginIndex;
		int endIndex;
		int index = 0;
		int length = str.length();
		Word word = null;
		Error error;
		char temp;
		while (index < length) {
			temp = str.charAt(index);
			if(temp == '{')
				noteFlag = true;
			if (!noteFlag) {
				if (isLetter(temp)) {// 字母开头，关键字或者标识符
					beginIndex = index;
					index++;
					while ((index < length)
							&& (Word.isOperator(str.substring(index, index + 1).charAt(0)) == -1)
							&& (Word.isBoundarySign(str.substring(index,index + 1).charAt(0)) == -1)
							&& (str.charAt(index) != ':')
							&& (str.charAt(index) != '<')
							&& (str.charAt(index) != '>')
							&& (str.charAt(index) != ' ')
							&& (str.charAt(index) != '\t') 
							&& (str.charAt(index) != '\r')
							&& (str.charAt(index) != '\n')) {
						index++;
					}
					endIndex = index;
					word = new Word();
					word.value = str.substring(beginIndex, endIndex);
					if (Word.isKey(word.value) != -1) {//是关键字
						word.id = Word.key.get(Word.isKey(word.value)).id;
						word.type = Word.KEY;
					} else if (isID(word.value)) {//是标识符
						word.id = 25;
						word.type = Word.IDENTIFIER;
						if(Word.findIdentifierByName(word.value) == 0)
							Word.addToTable(word.value);
						word.value = Word.findIdentifierByName(word.value) + "";
						
					} else {
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "非法标识符", line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;
				} else if (isDigit(temp)) {// 数字开头，判断是不是常数
					beginIndex = index;
					index++;
					while ((index < length)
							&& (Word.isOperator(str.substring(index, index + 1).charAt(0)) == -1)
							&& (Word.isBoundarySign(str.substring(index,index + 1).charAt(0)) == -1)
							&& (str.charAt(index) != ':')
							&& (str.charAt(index) != '<')
							&& (str.charAt(index) != '>')
							&& (str.charAt(index) != ' ')
							&& (str.charAt(index) != '\t')
							&& (str.charAt(index) != '\r')
							&& (str.charAt(index) != '\n')) {
						index++;
					}
					endIndex = index;
					word = new Word();
					word.id = 26;
					word.value = str.substring(beginIndex, endIndex);
					if (isInteger(word.value)) {
						word.type = Word.INT_CONST;
					} else {
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "非法数", line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
					index--;
				}else if (temp == ':') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') {
						endIndex = index + 1;
						word = new Word();
						word.id = 10;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					} else {
						word = new Word();
						word.id = 22;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				}else if (temp == '<') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') {
						endIndex = index + 1;
						word = new Word();
						word.id = 16;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					} else if (index < length && str.charAt(index) == '>'){
						word = new Word();
						word.id = 19;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}else {
						word = new Word();
						word.id = 15;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				}else if (temp == '>') {
					beginIndex = index;
					index++;
					if (index < length && str.charAt(index) == '=') {
						endIndex = index + 1;
						word = new Word();
						word.id = 18;
						word.value = str.substring(beginIndex, endIndex);
						word.type = Word.OPERATOR;
					} else {
						word = new Word();
						word.id = 17;
						word.value = str.substring(index - 1, index);
						word.type = Word.OPERATOR;
						index--;
					}
				} else if (Word.isOperator(temp) != -1) {//判断是不是只有一个符号的运算符
					beginIndex = index;
					index++;
					endIndex = index;
					word = new Word();
					word.value = str.substring(beginIndex, endIndex);
					word.id = Word.operator.get(Word.isOperator(word.value.charAt(0))).id;
					word.type = Word.OPERATOR;
					index--;	
				}else if (Word.isBoundarySign(temp) != -1) {//判断是不是只有一个符号的分隔符
					beginIndex = index;
					index++;
					endIndex = index;
					word = new Word();
					word.value = str.substring(beginIndex, endIndex);
					word.id = Word.boundarySign.get(Word.isBoundarySign(word.value.charAt(0))).id;
					word.type = Word.BOUNDARYSIGN;
					index--;	
				} 
				else {// 不是关键字、标识符、数字常量、运算符、分隔符

					switch (temp) {
					case ' ':
					case '\t':
					case '\r':
					case '\n':
						word = null;
						break;// 过滤空白字符
					default:
						word = new Word();
						word.id = 0;
						word.line = line;
						word.value = String.valueOf(temp);
						word.type = Word.UNIDEF;
						word.flag = false;
						errorCount++;
						error = new Error(errorCount, "非法字符", line, word);
						errorList.add(error);
						lexErrorFlag = true;
					}
				}
			} else {
				int i = str.indexOf('}');
				if (i != -1) {
					noteFlag = false;
					index = i + 2;
					continue;
				} else
					break;
			}
			
			if (word == null) {
				index++;
				continue;
			}

			wordList.add(word);
			index++;
		}
	}

	public ArrayList<Word> lexAnalyse(String str) {
		String buffer[];
		if(str==null){
			return wordList;
		}
		buffer = str.split("\n");
		int line = 1;
		for (int i = 0; i < buffer.length; i++) {
			analyse(buffer[i].trim(), line);
			line++;
		}
		return wordList;
	}

	public ArrayList<Word> lexAnalyse1(String filePath) throws IOException {//在本文件中测试用的方法
		FileInputStream fis = new FileInputStream(filePath);
		BufferedInputStream bis = new BufferedInputStream(fis);
		InputStreamReader isr = new InputStreamReader(bis, "utf-8");
		BufferedReader inbr = new BufferedReader(isr);
		String str = "";
		int line = 1;
		while ((str = inbr.readLine()) != null) {
			// System.out.println(str);
			analyse(str.trim(), line);
			line++;
		}
		inbr.close();
		return wordList;
	}
	
	/**
	 * 输出结果，好看的格式
	 * @return
	 * @throws IOException
	 */
	public String outputWordList() throws IOException {

		File file = new File("./result/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// 如果这个文件不存在就创建它
		}
		String path = file.getAbsolutePath();
		FileOutputStream fos = new FileOutputStream(path + "/wordList.txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
		PrintWriter pw1 = new PrintWriter(osw1);
		pw1.println("单词码\t单词值\t单词类型 \t单词是否合法");
		Word word;
		for (int i = 0; i < wordList.size(); i++) {
			word = wordList.get(i);
			pw1.println(word.id+"\t"+word.value +"\t"+word.type+"\t\t"+word.flag);
		}
		if (lexErrorFlag) {
			Error error;
			pw1.println("错误信息如下：");

			pw1.println("错误序号\t错误信息\t错误所在行 \t错误单词");
			for (int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				pw1.println(error.id+"\t\t"+error.info +"\t"+error.line+"\t\t"+error.word.value);
			}
		} else {
			pw1.println("词法分析通过！");
		}
		pw1.close();
		return path + "/wordList.txt";
	}
	
	/**
	 * 输出到文件，老师要求的格式
	 * @return
	 * @throws IOException
	 */
	public void outputWordListForNext() throws IOException {

		File file = new File("./result/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// 如果这个文件不存在就创建它
		}
		String path = file.getAbsolutePath();
		FileOutputStream fos = new FileOutputStream(path + "/wordListForNext.txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
		PrintWriter pw1 = new PrintWriter(osw1);
		Word word;
		for (int i = 0; i < wordList.size(); i++) {
			word = wordList.get(i);
			if(word.type.equals(Word.IDENTIFIER) || word.type.equals(Word.INT_CONST))
				pw1.print("("+word.id+","+word.value +")  ");
			else
				pw1.print("("+word.id+",0)  ");
		}
		if (lexErrorFlag) {
			Error error;
			pw1.println("错误信息如下：");

			pw1.println("错误序号\t错误信息\t错误所在行 \t错误单词");
			for (int i = 0; i < errorList.size(); i++) {
				error = errorList.get(i);
				pw1.println(error.id+"\t\t"+error.info +"\t"+error.line+"\t\t"+error.word.value);
			}
		} else {
			pw1.println("词法分析通过！");
		}
		pw1.close();
	}
	
	/**
	 * 输出标识符表
	 * @return
	 * @throws IOException
	 */
	public String outputIdentifierTable() throws IOException {

		File file = new File("./result/");
		if (!file.exists()) {
			file.mkdirs();
			file.createNewFile();// 如果这个文件不存在就创建它
		}
		String path = file.getAbsolutePath();
		FileOutputStream fos = new FileOutputStream(path + "/identifiertable.txt");
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		OutputStreamWriter osw1 = new OutputStreamWriter(bos, "utf-8");
		PrintWriter pw1 = new PrintWriter(osw1);
		pw1.println("标识符\t地址");
		for (int i = 0; i < Word.idTable.size(); i++) {
			pw1.println(Word.idTable.get(i)+"\t"+ (i+1));
		}
		pw1.close();
		return path + "/identifiertable.txt";
	}

	
	public static void main(String[] args) throws IOException {//本文件测试用
		LexAnalyse lex = new LexAnalyse();
		lex.lexAnalyse1("C:\\Users\\WangYuchao\\Documents\\Tencent Files\\1569433461\\FileRecv\\passed1.c");
		lex.outputWordList();
	}

	public ArrayList<Word> getWordList() {
		// TODO Auto-generated method stub
		return wordList;
	}
}
