package service.core;

import java.io.Serializable;
/**
 * Data Class that contains client information
 * 
 * @author Rem
 *
 */
public class ClientInfo implements Serializable{
	public static final char MALE				= 'M';
	public static final char FEMALE				= 'F';
	
	public ClientInfo(String name, char sex, int age, double height, double weight, boolean smoker, boolean medicalIssues) {
		this.name = name;
		this.gender = sex;
		this.age = age;
		this.height = height;
		this.weight = weight;
		this.smoker = smoker;
		this.medicalIssues = medicalIssues;
	}
	
	public ClientInfo() {}

	public String toString() {return "Name: " + name + " age: " + age + " sex: " + gender + " height: " + height + " weight: " + weight + " smoker: " + smoker + " medical issues: " + medicalIssues;}

	/**
	 * Public fields are used as modern best practice argues that use of set/get
	 * methods is unnecessary as (1) set/get makes the field mutable anyway, and
	 * (2) set/get introduces additional method calls, which reduces performance.
	 */
	public String name;
	public char gender;
	public int age;
	public double height;
	public double weight;
	public boolean smoker;
	public boolean medicalIssues;
}
