package com.cuiyf.minitools.exception;

@SuppressWarnings("serial")
public class OperateException extends RuntimeException {

	private String msg;
	private String code;

	public OperateException() {
		super();
	}

	public OperateException(String msg, String code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public OperateException(String msg) {
		super(msg);
		this.msg = msg;
		this.code = "200001";
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
