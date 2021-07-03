package tp3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe que gere a informação que está na tabela log da base de dados.
 * Insere, pesquisa e lista logs.
 * 
 *
 */
public class GereLog {

	GereUtilizadores user = new GereUtilizadores();
	Connection conn;
	Statement st;
	ResultSet rs;
	String ip, porto, nomebd, login, pass;

	JavaPropertiesFile file = new JavaPropertiesFile();
	
	/**
     * Construtor da classe GereLog que recebe como parametros 
	 * os dados para poder acessar a Base de Dados
	 * @param aIP
	 * @param aPorto
	 * @param aNomeBD
	 * @param aLogin
	 * @param aPass
	 */
	GereLog(String aIP, String aPorto, String aNomeBD, String aLogin, String aPass){
	    	
	    	if(file.leFicheiroTexto("ip") == null ){

	        	file.escreveFicheiroTexto(aIP, aPorto, aNomeBD, aLogin, aPass);
	    	}
	    }
	
	GereLog(){
		
		
	}
	
	/**
	 * Este método insere um log na base de dados
	 * Log este composto por data, hora, utilizador e ação
	 * @param aAccao - parametro que contém a ação feita pelo utilizador
	 * @param aLogin - login do utilizador que realizou algum tipo de ação.
	 */
	public void insereLog(String aAccao, String aLogin){
		
		getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		st.execute("INSERT INTO log (U_ID, L_DATA, L_HORA, L_ACCAO)"
    				+ " VALUES (" + user.getUID(aLogin) + ", sysdate(), sysdate(), '" + aAccao + "')");

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
    	} // end of finally
	}
	
	/**
	 * Este Método lista os logs registados na base de dados
	 * logs estes que podem ser todos, ou os de um determinado utilizador.
	 * @param aArg - parametro que caso seja para listar os logs de um determinado utilizador vem com o seu login, caso contrário não vem com nada.
	 * @param aOrd - parametro de ordenação da listagem.
	 * @return - é devolvida uma string com todos os logs pretendidos.
	 */
	public String listaLog(String aArg, String aOrd){

    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		if(!aOrd.equals("")){
    			aOrd = " ORDER BY " + aOrd;
    		}
    		if(!aArg.equals("")){
    			
    			aArg = " AND u_login = '" + aArg + "'";
    		}
    		st = conn.createStatement();

    		rs = st.executeQuery("select l_data, l_hora, u_login, l_accao "
    				+"FROM log as l, utilizadores as u "
    				+"WHERE l.u_id = u.u_id" + aArg + aOrd);
    		String output = "";
    		int i = 0;
    		if(rs == null ){
    			output="Sem Equipamentos registados";
    		} else {
    			while (rs.next()) {
    				i++;
    				output += "\n<" + rs.getString(1) + "> " + "<" + rs.getString(2) + "> " + "<" + rs.getString(3) + "> " + "<" + rs.getString(4) + ">\n";
    				if(i == 10){
						i=0;
    					output += "limite10";
    				}
    			} 
    		}
    		if(output.equals("")){

        		output = "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n"
    					 + "! Sem Logs para o utilizador inserido !"
    				   + "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n";
    		}

    		output += "limite10";
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