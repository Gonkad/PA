package tp3;
import java.sql.*;

public class GereBaseDados {

	
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;
	String ip, porto, nomebd, login, pass;

	JavaProperties file = new JavaProperties();

	GereBaseDados(){
	
	}
	

	public boolean carregaDados(String aIP, String aPorto, String aNomeBD, String aLogin, String aPass){

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://"+aIP+":"+aPorto+"/"+aNomeBD, aLogin, aPass);

		} catch (SQLException e) {
			return false;
		} catch (ClassNotFoundException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		} catch (InstantiationException e) {
			return false;
		} finally {
			
			if (conn != null) {
				try {
					conn.close();
					file.escreveFicheiroTexto(aIP, aPorto, aNomeBD, aLogin, aPass);
					return true;
				} catch (Exception e) {
					System.out.println("!! Exception closing DB connection !!\n"+e);
				}
			}
		} 
		return false;
	}
	
	/**
	 * Caso consiga estabelecer ligação a uma base de dados, esta função verifica se a base de dados é a pretendida
	 * se contém todas as tabelas necessárias para que a aplicação corra sem erros.
	 * @return
	 */
	public boolean verTabelasDadosBD(){
		getJavaProperties();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
			
			st = conn.createStatement();
			
			rs = st.executeQuery("Select * from CATEGORIA,CLIENTES,ENCOMENDAS,FUNCIONARIO,PRODUTOS,UTILIZADORES");
			
		} catch (SQLException e) {
			return false;
		} catch (ClassNotFoundException e) {
			return false;
		} catch (IllegalAccessException e) {
			return false;
		} catch (InstantiationException e) {
			return false;
		} finally {
			
			if (conn != null) {
				try {
					conn.close();
					return true;
				} catch (Exception e) {
					System.out.println("!! Exception closing DB connection !!\n"+e);
				}
			}
			if (st != null) {
				try {
					st.close();
					return true;
				} catch (Exception e) {
					System.out.println("!! Exception closing DB statement !!\n"+e);
				}
			}
		} 
		return false;
	}
	
	/**
	 * Este método apenas carrega os dados de acesso a base de dados, para variaveis globais.
	 */
	private void getJavaProperties(){

		ip = file.leFicheiroTexto("ip");
		porto = file.leFicheiroTexto("porto");
		nomebd = file.leFicheiroTexto("nome");
		login = file.leFicheiroTexto("login");
		pass = file.leFicheiroTexto("pass");
	}
	
	/**
	 * Verifica se o ficheiro java properties contém os dados necessários para estabelecer uma ligação à base de dados.
	 * @return - se tiver devolve true, caso contrário devolve false.
	 */
	public boolean carregaProperties(){
		
		if(file.leFicheiroTexto("ip") == null || file.leFicheiroTexto("ip").equals("")){
			return false;
		}
		if(file.leFicheiroTexto("porto") == null || file.leFicheiroTexto("porto").equals("")){
			return false;
		}
		if(file.leFicheiroTexto("login") == null || file.leFicheiroTexto("login").equals("")){
			return false;
		}
		if(file.leFicheiroTexto("nome") == null || file.leFicheiroTexto("nome").equals("")){
			return false;
		}
		if(file.leFicheiroTexto("pass") == null || file.leFicheiroTexto("pass").equals("")){
			return false;
		}
		
		if(carregaDados(file.leFicheiroTexto("ip"), file.leFicheiroTexto("porto"), file.leFicheiroTexto("nome"), file.leFicheiroTexto("login"), file.leFicheiroTexto("pass"))){

			return true;
		} else {
			return false;
		}
	}
	
}