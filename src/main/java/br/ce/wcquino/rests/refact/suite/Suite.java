package br.ce.wcquino.rests.refact.suite;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.tests.refact.AuthTeste;
import br.ce.wcaquino.rest.tests.refact.ContasTeste;
import br.ce.wcaquino.rest.tests.refact.MovimentacaoTeste;
import br.ce.wcaquino.rest.tests.refact.SaldoTeste;
import io.restassured.RestAssured;

//suite = conjunte de testes
@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
	ContasTeste.class,
	//MovimentacaoTeste.class,
	SaldoTeste.class,
	AuthTeste.class
	}	
	)
public class Suite extends BaseTest {
	
	
	@BeforeClass
	public static void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "test@t");
		login.put("senha", "abc123");
		
		//fazer login p extrair o token da requisião
	 String	token =	given()
			.body(login)
		.when()
			.post("/signin")
		.then()
			.statusCode(200)
			.extract().path("token");
		
		RestAssured.requestSpecification.header("Authorization", "JWT " + token);
		
		//resetar o BD
		RestAssured.get("/reset")
					.then()
						.statusCode(200);
	}
	
}
