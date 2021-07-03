package tp3;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Classe de objeto que faz a gestão do conteudo de um fichiro java properties.
 * 
 * @author Goncalo-PC 
 */
public class JavaProperties {

	private Properties propertie;

	public JavaProperties(){

		propertie = new Properties();
	}

	/**
	 * Este método recebe como argumento de entrada a property da qual deve ir ao ficheiro java properties e devolver o que corresponde a essa string.
	 * @param aString
	 * @return
	 */
	public String leFicheiroTexto(String aString){

		try {
			propertie.load(new FileInputStream("acessoBD.txt"));
		}catch(FileNotFoundException fnfe){

			return "";

		}catch (IOException e) {
			e.printStackTrace();
		}

		return propertie.getProperty(aString);

	}

	/**
	 * Este método recebe como parametros de entrada, os que estão abaixo descritos, e depois guarda-os no ficheiro java properties, para depois poderem ser utilizados.
	 * @param aIP - ip de acesso a base de dados
	 * @param aPorto - porto de acesso a base de dados
	 * @param aNomeBD - nome da base de dados
	 * @param aLogin - login de acesso à base de dados
	 * @param aPass - password para o login
	 * @param aNum - Numero de licencas da base de dados (parametro introduzido pelo gestor)
	 */
	public void escreveFicheiroTexto(String aIP, String aPorto, String aNomeBD, String aLogin, String aPass){

		if(aIP != null){
			propertie.setProperty("ip", aIP);
		}
		if(aPorto != null){
			propertie.setProperty("porto", aPorto);
		}
		if(aNomeBD != null){
			propertie.setProperty("nome", aNomeBD);
		}
		if(aLogin != null){
			propertie.setProperty("login", aLogin);
		}
		if(aPass != null){
			propertie.setProperty("pass", aPass);
		}

		try {
			propertie.store(new FileOutputStream("acessoBD.txt"),"");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
