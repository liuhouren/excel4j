package com.liuhr.excel4j.test;

import com.liuhr.excel4j.annotations.Excel;

public class Father {

	@Excel(codes = { "11" ,"22"}, columnIndex = 6,columnName="编码")
	private String code;

	@Excel(codes = { "11" ,"22"}, columnIndex = 7,columnName="数量")
	private int count;

	@Excel(codes = { "11" ,"22"}, columnIndex = 8,columnName="已删除")
	private boolean deleted;

	@Excel(codes = { "11" ,"22"}, columnIndex = 9,readMethodName="getFatherCode",writeMethodName="setFatherCode",writeMethodParameterType=String.class)
	private Father father;

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Father getFather() {
		return father;
	}

	public void setFather(Father father) {
		this.father = father;
	}

	public String getFatherCode(){
		return this.father==null?"":this.father.code;
	}

	public void setFatherCode(String fatherCode){
		this.father=new Father();
		this.father.setCode(fatherCode);
	}

}
