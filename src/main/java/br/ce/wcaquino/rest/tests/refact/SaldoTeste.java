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

public class SaldoTeste extends BaseTest {
	
	@Test
	public void deveCalcularSaldoDasContas() {
		
		Integer CONTA_ID = getIdContapeloNome("Conta para saldo");
		
		given()
			.when()
	    		.get("/saldo")
	    	.then()
	    		.log().all()
	    		.statusCode(200)
	    		.body("find{it.conta_id =="+CONTA_ID+" }.saldo", Matchers.is("534.00"))
	
	    		;
		
		
	}

	
	
	
	public Integer getIdContapeloNome(String nomeConta) {
		return RestAssured.get("/contas?nome="+nomeConta).then().extract().path("id[0]");
	}
	
	public Integer getIdMovimentacaoDescricao(String desc) {
		return RestAssured.get("/transacoes?descricao="+desc).then().extract().path("id[0]");

	}
	
	
}
