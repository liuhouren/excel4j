package com.liuhr.excel4j.test;

import java.io.Serializable;
import java.util.Date;

import com.liuhr.excel4j.annotations.Excel;
import com.liuhr.excel4j.enums.CellType;

/**
 * @author nc-wl001
 *
 */

public class EnvObject extends Father implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7163577873426720697L;

	/**
	 * ����
	 */
	@Excel(codes = { "11" }, columnIndex = 0)
	private String id;

	/**
	 * ���󴴽�ʱ��
	 */

	@Excel(codes = { "11" }, columnIndex = 1,cellType=CellType.NUMERIC)
	private Date createTime = new Date();

	/**
	 * ��Ա���ƻ��ƺ�
	 */
	@Excel(codes = { "11" }, columnIndex = 2)
	private String name;

	/**
	 * GPRS�ն˺�
	 */
	@Excel(codes = { "11" }, columnIndex = 3,comments={"ֻ����������","�������ʮ��λ����"})
	private String gprsTerminalNum;

	/**
	 * �ֻ���
	 */
	@Excel(codes = { "11" }, columnIndex = 4,comments={"��д��ȷ���ֻ���"})
	private String mobile;

	/**
	 * ״̬
	 */
	@Excel(codes = { "11" }, columnIndex = 5)
	private String state;

	public EnvObject() {
		super();
	}

	public EnvObject(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGprsTerminalNum() {
		return gprsTerminalNum;
	}

	public void setGprsTerminalNum(String gprsTerminalNum) {
		this.gprsTerminalNum = gprsTerminalNum;
	}


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "EnvObject [id=" + id + ", createTime=" + createTime + ", name="
				+ name + ", gprsTerminalNum=" + gprsTerminalNum + ", mobile="
				+ mobile + ", state=" + state + "]";
	}

}
