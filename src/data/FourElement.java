package data;
/**
 * 四元式类型
 * @author WangYuchao
 *
 */
public class FourElement {
	
	public String fuhao;                                    
	public String op1;
	public String op2;
	public ResultNode p1;
	
	
	
	public FourElement() {
		super();
	}



	public FourElement(String fuhao, String op1, String op2, ResultNode p1) {
		super();
		this.fuhao = fuhao;
		this.op1 = op1;
		this.op2 = op2;
		this.p1 = p1;
	}
	
}


