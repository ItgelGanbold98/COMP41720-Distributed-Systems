package service.core;

import java.io.Serializable;
import java.util.Objects;

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

	@Override
	public String toString() {
		return "ClientInfo{" +
				"name= " + name + '\'' +
				", gender= " + gender +
				", age= " + age +
				", height= " + height +
				", weight= " + weight +
				", smoker= " + smoker +
				", medicalIssues=" + medicalIssues +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ClientInfo that = (ClientInfo) o;
		return age == that.age &&
				gender == that.gender &&
				Double.compare(that.height, height) == 0 &&
				Double.compare(that.weight, weight) == 0 &&
				smoker == that.smoker &&
				medicalIssues == that.medicalIssues &&
				Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, gender, age, height, weight, smoker, medicalIssues);
	}


}
