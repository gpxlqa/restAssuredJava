package br.ce.wcaquino.rest.tests;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.sun.scenario.effect.Filterable;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.utils.DateUtils;
import io.restassured.RestAssured;
import io.restassured.specification.FilterableRequestSpecification;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTest extends BaseTest {
	
	
	//static para n�o zerar a vari�vel durante as execu��es do JUnit
	private static String CONTA_NAME = "conta " + System.nanoTime();
	
	private static Integer CONTA_ID;
	private static Integer MOV_ID;
	
	
	@BeforeClass
	public static void login() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "test@t");
		login.put("senha", "abc123");
		
		//fazer login p extrair o token da requisi�o
	 String	token =	given()
			.body(login)
		.when()
			.post("/signin")
		.then()
			.statusCode(200)
			.extract().path("token");
		
		RestAssured.requestSpecification.header("Authorization", "JWT " + token);
	}
	
	
	
	@Test
	public void t02_deveIncluirContaComSucesso() {
		
		CONTA_ID =	given()
				.body("{\"nome\": \""+CONTA_NAME+"\"}")
			.when()
				.post("/contas")
			.then()
				.statusCode(201)
	     	.extract().path("id")
			;
	}

	@Test
	public void t03_deveAlterarContaComSucesso() {
			given()
				.body("{\"nome\": \""+CONTA_NAME+"\"}")
				.pathParam("id", CONTA_ID)
			.when()
				.put("/contas/{id}")
			.then()
				.log().all()
				.statusCode(200)
				.body("nome", Matchers.is(CONTA_NAME))
           
				;
		
	}
	
	@Test
	public void t04_naoDeveInserirContaMesmoNome() {
		
		given()
			.body("{\"nome\": \""+CONTA_NAME+"\"}")
		.when()
			.post("/contas")
	    .then()
			.statusCode(400)
			.body("error", Matchers.is("J� existe uma conta com esse nome!"))
			
	       ;
	}
	
	@Test
	public void t05_deveInserirMovimentacaoComSucesso() {
			Movimentacao mov = getMovimentacaoValida();
			
	MOV_ID	= 	given()
		       .body(mov)
		    .when()
		    	.post("/transacoes")
		    .then()
		    	.statusCode(201)
		    	.extract().path("id")
		
       ;
	}
	
	@Test
	public void t06_deveValidarCamposObrigatoriosMovimentacao() {
	
			given()
		       .body("{}")
		    .when()
		    	.post("/transacoes")
		    .then()
		    	.statusCode(400)
		    	.body("$", Matchers.hasSize(8)) // 8 mensagens
		    	.body("msg", Matchers.hasItems(
		    			 "Data da Movimenta��o � obrigat�rio",
		    			 "Data do pagamento � obrigat�rio",
		    			 "Descri��o � obrigat�rio",
		    			 "Interessado � obrigat�rio",
		    			 "Valor � obrigat�rio",
		    			 "Valor deve ser um n�mero",
		    			 "Conta � obrigat�rio",
		    			 "Situa��o � obrigat�rio"
		    			))
		
       ;
	}
	
	@Test
	public void t07_naoDeveInserirMovimentacaoComDataFuturo() {
		
		Movimentacao mov = getMovimentacaoValida();
		mov.setData_transacao(DateUtils.getDataDiferencaDias(2));
		
			given()
		       .body(mov)
		    .when()
		    	.post("/transacoes")
		    .then()
		    	.statusCode(400)
		    	.body("$", Matchers.hasSize(1))
		    	.body("msg", Matchers.hasItem("Data da Movimenta��o deve ser menor ou igual � data atual"))
		
       ;
	}
	
	@Test
	public void t08_naoDeveRemoverContaMovimentacao() {
			given()
				.pathParam("id", CONTA_ID)
			.when()
	    		.delete("/contas/{id}")
	    	.then()
	    		.statusCode(500)
	    		.body("constraint", Matchers.is("transacoes_conta_id_foreign"))
	
	    		;
	}
	
	@Test
	public void t09_deveCalcularSaldoDasContas() {
			given()
			.when()
	    		.get("/saldo")
	    	.then()
	    		.log().all()
	    		.statusCode(200)
	    		.body("find{it.conta_id =="+CONTA_ID+" }.saldo", Matchers.is("100.00"))
	
	    		;
		
		
	}

	@Test
	public void t10_deveRemoverMovimentacao() {
			given()
				.pathParam("id", MOV_ID)
			.when()
	    		.delete("/transacoes/{id}")
	    	.then()
	    		.log().all()
	    		.statusCode(204)
	
	    		;
		
		
	}

	@Test
	public void t11_naoDeveAcessarAPISemToken() {
	
		FilterableRequestSpecification req = (FilterableRequestSpecification) RestAssured.requestSpecification;
		req.removeHeader("Authorization"); // retira o token de autoriza��o
		
		
	    given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401)
		;
	}
	
	private Movimentacao getMovimentacaoValida() {
			
			Movimentacao mov = new Movimentacao();
			mov.setConta_id(CONTA_ID);
		//	mov.setUsuario_id(usuario_id);
			mov.setDescricao("descri��o da movimenta��o");
			mov.setEnvolvido("Envolvido na movimentacao");
			mov.setTipo("REC");
			mov.setData_transacao(DateUtils.getDataDiferencaDias(-1));
			mov.setData_pagamento(DateUtils.getDataDiferencaDias(5));
			mov.setValor(100f);
			mov.setStatus(true);
			
			return mov;
		}

}


	
