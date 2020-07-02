package br.ce.wcaquino.rest.tests.refact;

import static io.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.tests.Movimentacao;
import br.ce.wcaquino.rest.utils.BarrigaUtils;
import br.ce.wcaquino.rest.utils.DateUtils;
import io.restassured.RestAssured;

public class ContasTeste extends BaseTest {
	
	
	@Test
	public void deveIncluirContaComSucesso() {
			
			given()
				.body("{\"nome\": \"Conta Inserida\"}")
			.when()
				.post("/contas")
			.then()
				.statusCode(201)
			;
	}

	@Test
	public void deveAlterarContaComSucesso() {
			Integer CONTA_ID = BarrigaUtils.getIdContapeloNome("Conta para alterar");
		
			given()
				.body("{\"nome\": \"Conta alterada\"}")
				.pathParam("id", CONTA_ID)
			.when()
				.put("/contas/{id}")
			.then()
				.log().all()
				.statusCode(200)
				.body("nome", Matchers.is("Conta alterada"))
           
				;
		
	}
	
	
	@Test
	public void naoDeveInserirContaMesmoNome() {
		
		given()
			.body("{\"nome\": \"Conta mesmo nome\"}")
		.when()
			.post("/contas")
	    .then()
			.statusCode(400)
			.body("error", Matchers.is("J� existe uma conta com esse nome!"))
			
	       ;
	}
	
	@Test
	public void deveInserirMovimentacaoComSucesso() {
			MovimentacaoTeste mov = getMovimentacaoValida();
			
			given()
		       .body(mov)
		    .when()
		    	.post("/transacoes")
		    .then()
		    	.statusCode(201)
		
       ;
	}
	
	
	@Test
	public void deveValidarCamposObrigatoriosMovimentacao() {
	
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
	public void naoDeveInserirMovimentacaoComDataFuturo() {
		
		MovimentacaoTeste mov = getMovimentacaoValida();
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
	public void _naoDeveRemoverContaMovimentacao() {
			
		Integer CONTA_ID = BarrigaUtils.getIdContapeloNome("Conta com movimentacao");
		
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
	public void deveRemoverMovimentacao() {
		
		Integer MOV_ID = getIdMovimentacaoDescricao("Movimentacao para exclusao");
		
		given()
				.pathParam("id", MOV_ID)
			.when()
	    		.delete("/transacoes/{id}")
	    	.then()
	    		.log().all()
	    		.statusCode(204)
	
	    		;
		
		
	}

	
	
	
	

	
	public Integer getIdMovimentacaoDescricao(String desc) {
		return RestAssured.get("/transacoes?descricao="+desc).then().extract().path("id[0]");

	}
	
	private MovimentacaoTeste getMovimentacaoValida() {
		
		MovimentacaoTeste mov = new MovimentacaoTeste();
		mov.setConta_id(BarrigaUtils.getIdContapeloNome("Conta para movimentacoes"));
	//	mov.setUsuario_id(usuario_id);
		mov.setDescricao("descri��o da movimenta��o");
		mov.setEnvolvido("Envolvido na movimentacao");
		mov.setTipo("REC");
		mov.setData_transacao(DateUtils.getDataDiferencaDias(-1));
		mov.setData_pagamento(DateUtils.getDataDiferencaDias(5));
		mov.setValor(100f);
		mov.setStatus(true);
		
		return (MovimentacaoTeste) mov;
	}


}
