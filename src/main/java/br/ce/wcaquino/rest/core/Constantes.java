package br.ce.wcaquino.rest.core;

import io.restassured.http.ContentType;

//Constantes do projeto
public interface Constantes {

	String APP_BASE_URL = "http://barrigarest.wcaquino.me";
	Integer APP_PORT = 80; //não é necessário, pois é padrão. Para https é 440
	String APP_BASE_PATH = "";
	
	ContentType APP_ContentType = ContentType.JSON;
	
	//tempo máximo da requisição
	Long MAX_TIMEOUT = 5000L; //5 segundos
}
