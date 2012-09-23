package org.study.java.jboss.javaee5.mi.jndi;

public class Test {

	private Integer id;
	private String value;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Test [id=" + id + ", value=" + value + "]";
	}
	
}
