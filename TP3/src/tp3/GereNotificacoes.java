package tp3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe que gere todas as notificações
 * De gestores, clientes e funcionários.
 * 
 *
 */
public class GereNotificacoes {

	GereUtilizadores user = new GereUtilizadores();
	Connection conn;
	Statement st;
	ResultSet rs;
	String ip, porto, nomebd, login, pass;

	JavaProperties file = new JavaProperties();
	
	GereNotificacoes(){
		
	}
	
	/**
	 * Devolve as notificacoes do Gestor
	 * @return
	 */
	public String GestorNotificacoes(){
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();

    		rs = st.executeQuery("SELECT * "
    							+"FROM encomendas "
    							+"WHERE e_estado = 'iniciada'");
			String output = "";
    		while (rs.next()) {
    			output += "\nEncomeda com o identificador: " + rs.getString("e_identificador") + " está por autorizar.\n";
    		}
    		rs = st.executeQuery("SELECT * "
								+"FROM encomendas "
								+"WHERE e_estado = 'confirmada'");
    		
    		while (rs.next()) {
    			output += "\nEncomenda com o identificador: " + rs.getString("e_identificador") + " está finalizada.\n";
    		}
    		if(output.equals("")){
				output = "Não tem nenhuma notificação";
			} 
    		return output;

    	} catch (SQLException e) {
    		System.out.println("!! SQL Exception !!\n"+e);
    		e.printStackTrace();
    	} catch (ClassNotFoundException e) {
    		System.out.println("!! Class Not Found. Unable to load Database Drive !!\n"+e);
    	} catch (IllegalAccessException e) {
    		System.out.println("!! Illegal Access !!\n"+e);
    	} catch (InstantiationException e) {
    		System.out.println("!! Class Not Instanciaded !!\n"+e);
    	} finally {
    		if (st != null) {
    			try {
    				st.close();
    			} catch (Exception e) {
    				System.out.println("!! Exception returning statement !!\n"+e);
    			}
    		}
    		if (conn != null) {
    			try {
    				conn.close();
    			} catch (Exception e) {
    				System.out.println("!! Exception closing DB connection !!\n"+e);
    			}
    		}
    	} 
		
		return "";
	}

	/**
	 * Devolve as notificacoes do Cliente
	 * @return
	 */

	
	/**
	 * Devolve as notificacoes do Tecnico
	 * @return
	 */
	public String FuncionarioNotificacoes(){
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();

			String output = "";
			rs = st.executeQuery("SELECT * "
					+"FROM encomendas "
					+"WHERE e_estado = 'aguardar entrega'");

			while (rs.next()) {
				output += "\nEntrega com o identificador: " + rs.getString("e_estado") + " aceite pelo gestor.\n";
			}
    		
    		if(output.equals("")){
				output = "Não tem nenhuma notificação";
			} 
    		return output;

    	} catch (SQLException e) {
    		System.out.println("!! SQL Exception !!\n"+e);
    		e.printStackTrace();
    	} catch (ClassNotFoundException e) {
    		System.out.println("!! Class Not Found. Unable to load Database Drive !!\n"+e);
    	} catch (IllegalAccessException e) {
    		System.out.println("!! Illegal Access !!\n"+e);
    	} catch (InstantiationException e) {
    		System.out.println("!! Class Not Instanciaded !!\n"+e);
    	} finally {
    		if (st != null) {
    			try {
    				st.close();
    			} catch (Exception e) {
    				System.out.println("!! Exception returning statement !!\n"+e);
    			}
    		}
    		if (conn != null) {
    			try {
    				conn.close();
    			} catch (Exception e) {
    				System.out.println("!! Exception closing DB connection !!\n"+e);
    			}
    		}
    	} 
		
		return "";
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

}

