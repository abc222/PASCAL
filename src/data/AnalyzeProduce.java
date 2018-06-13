package data;


import java.io.Serializable;

/**
 * 
 * @function 分析过程Bean
 *
 */
public class AnalyzeProduce implements Serializable{
	private static final long serialVersionUID = 10L;
	private Integer index;
	private String analyzeStackStr;//栈元素
	private String str;//剩余字符串
	private String useExpStr;//所用表达式

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getAnalyzeStackStr() {
		return analyzeStackStr;
	}

	public void setAnalyzeStackStr(String analyzeStackStr) {
		this.analyzeStackStr = analyzeStackStr;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getUseExpStr() {
		return useExpStr;
	}

	public void setUseExpStr(String useExpStr) {
		this.useExpStr = useExpStr;
	}

}


