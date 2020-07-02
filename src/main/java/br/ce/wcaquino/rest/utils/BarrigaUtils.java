package br.ce.wcaquino.rest.utils;

import io.restassured.RestAssured;

public class BarrigaUtils {
	
	
	public static Integer getIdContapeloNome(String nomeConta) {
		return RestAssured.get("/contas?nome="+nomeConta).then().extract().path("id[0]");
	}

}
