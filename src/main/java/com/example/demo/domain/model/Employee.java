package com.example.demo.domain.model;

import lombok.Data;

@Data
public class Employee {

	/** ID */
	private Integer id;
	
	/** 名前 */
	private String name;
	
	/** 年齢 */
	private Integer age;
	
	/** 性別(数値) */
	private Integer gender;
	
	/** 性別(文字列) */
	private String genderString;
	
	/** 性別の数値を文字列に変換 */
	public void convertGenderIntToString() {
		
		// 数値を文字列に変換
		if (gender == 1) {
			genderString = "男性";
		} else if (gender == 2) {
			genderString = "女性";
		} else {
			String errorMessage = "Gender is invalid: " + gender;
			throw new IllegalStateException(errorMessage);
		}
	}
}
