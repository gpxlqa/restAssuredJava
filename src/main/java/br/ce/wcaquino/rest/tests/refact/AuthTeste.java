package br.ce.wcaquino.rest.tests.refact;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.tests.Movimentacao;
import br.ce.wcaquino.rest.utils.DateUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

public class AuthTeste extends BaseTest {
	
	@Test
	public void naoDeveAcessarAPISemToken() {
	
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization"); // retira o token de autorização
		
		
	    given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401)
		;
	}
	


}