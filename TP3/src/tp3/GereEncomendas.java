package tp3;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Classe que faz a gestão de encomendas
 * Insere, lista, pesquisa e verifica encomendas na/da base de dados.
 * 
 *
 */
public class GereEncomendas {

	Connection conn;
    Statement st;
    ResultSet rs;
    String ip, porto, nomebd, login, pass;
    
    JavaProperties file = new JavaProperties();
    
    /**
     * Construtor da classe Gereencomendas que recebe como parametros 
	 * os dados para poder acessar a Base de Dados
     * @param aIP
     * @param aPorto
     * @param aNomeBD
     * @param aLogin
     * @param aPass
     */
    GereEncomendas(String aIP, String aPorto, String aNomeBD, String aLogin, String aPass){
    	
    	if(file.leFicheiroTexto("ip") == null ){

        	file.escreveFicheiroTexto(aIP, aPorto, aNomeBD, aLogin, aPass);
    	}
    }
    
    GereEncomendas(){
    	
    }
    
    /**
     * Este método serve para inserir um encomenda na base de dados
     * Recebe como Parametros de entrada: 
     * aEncomenda - objeto do tipo encomendas que contem todos os dados de um encomenda.
     */
    public void insereEncomenda(Encomenda aEncomenda, int aPID, int aUID){

    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();

    		
    		st.execute("INSERT INTO encomendas(U_ID, P_ID, E_IDENTIFICADOR, E_CUSTOTOTAL, E_DATACRIACAO, E_DATAACEITACAO, E_DATAENTREGA, E_ESTADO)"
    				+ "VALUES ('"+ aUID +"', '"+ aPID +"', '"+ aEncomenda.getID() +"', '"+ aEncomenda.getCusto() +"', '" + aEncomenda.getDataCriacao()+"', 'sysdate()', 'sysdate()', '"+ aEncomenda.getEstado()+ "');");
    		
    		st.close();

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
    }
    
    /**
     * Este método serve para listar todas as encomendas.
     * Caso o utilizador pretenda listá-los por ordem, o parametro de entrada aOrdena vem com os dados para ordenação
     * O parametro aLogin - serve para verificar se o utilizador que pediu a listagem é um cliente ou funcionário, caso seja, apenas lista as suas encomendas.
     * @param aOrdena
     * @param aLogin
     * @return
     */
    public String listaEncomendas(String aOrdena, String aLogin){
   
    	GereUtilizadores uti = new GereUtilizadores();
    	String string = "";
    	boolean bool = true;
    		
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		if(!aOrdena.equals("")){
    			aOrdena = "ORDER BY " + aOrdena;
    		}
    		
    		st = conn.createStatement();

    		rs = st.executeQuery("SELECT * FROM encomendas " + string + aOrdena + ";");

    		String output = "";
    		int i = 0;
    		if(rs == null ){
    			output="Sem encomendas registadas";
    		} else {
    			i++;
    			while (rs.next()) {
    				output += "\n        Encomenda        \nIdentificador da encomenda: " + rs.getString("e_identificador") + "\nCusto Total: " + rs.getString("e_custototal")+"\nData de criacao: "  +rs.getString("e_datacriacao")+ "\nData de Entrega: " + rs.getString("e_dataentrega") + "\nEstado: " +rs.getString("e_estado")+"\n";
    				if(i == 10){
						i=0;
    					output += "Limite de 10!";
    				}
    			} 
    		}
			if(output.equals("")){
				if(bool){
					output = "Não tem encomendas registadas no sistema!";
				} else {
					output = "Não tem encomendas registadas em seu nome!";
				}
			}
    		output += "Limite de 10!";
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
     * Este método devolve um inteiro com o id de um utilizador.
     * Isto é feito com a utilização de um login que vem como parametro de entrada.
     * @param aLogin
     * @return
     */
    public int getUID(String aLogin){
    	
    	int u_id = 0;
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		rs = st.executeQuery("SELECT * FROM utilizadores WHERE U_LOGIN = '"+ aLogin +"';");
    		
			rs.next();
			u_id = rs.getInt("U_ID");
			    		
    	
    		st.close();

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
    	return u_id;
    }
    
    
    /**
     * Este método devolve um inteiro com o id de um funcionario.
     * Isto é feito com a utilização de um login que vem como parametro de entrada.
     * @param aLogin
     * @return
     */
    public int getFID(String aLogin){
    	
    	int f_id = 0;
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		rs = st.executeQuery("SELECT * FROM funcionarios WHERE U_LOGIN = '"+ aLogin +"';");
    		
			rs.next();
			f_id = rs.getInt("F_ID");
			    		
    	
    		st.close();

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
    	return f_id;
    }
    
    /**
     * Este método é utilizado para devolver encomendas com um determinado estado. 
     * @param aEstado
     * @return - Devolve true caso exista, e false caso não exista.
     */
    
    public boolean getEstado(String aEstado){
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		st = conn.createStatement();
    			
			rs = st.executeQuery("SELECT * FROM encomendas WHERE E_ESTADO = '"+ aEstado +"';");
    		if (rs == null) {
			  return false;
    		} else {
			  if (rs.next()) {
				  return true;
			  }
			}    		
    	
    		st.close();

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
    	return false;
    }

    /**
     * Este método adiciona um produto a uma encomenda.
     * @param aIdEnc - Identificador único da encomenda
     * @param aIdProd - Id do produto
     * @return
     */

    
    public void adicionaProdutoEncomenda(String aIdEnc, int aIdProd){

    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();

    		st.executeUpdate("UPDATE encomendas set P_ID = '"+ aIdProd +"' WHERE e_identificador = '"+aIdEnc+"';");
    		
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
	}
    
    /**
     * Este método verifica se o identificador unico da encomenda existe na base de dados ou não.
     * Devolve True caso exista, devolve false caso não exista.
     * @param aID
     * @return
     */
    public boolean verIdEncomenda(String aID){
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		st = conn.createStatement();
    			
			rs = st.executeQuery("SELECT * FROM encomendas WHERE E_IDENTIFICADOR = '"+ aID +"';");
    		if (rs == null) {
			  return false;
    		} else {
			  if (rs.next()) {
				  return true;
			  }
			}    		
    	
    		st.close();

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
    	return false;
    }
    
    /**
     * Este método gera o identificador único da encomenda.
     * Devolve o identifcador único.
     * @param aID
     * @return
     */
    
    public String geraIDEnc(){

		getJavaProperties();
		Date data = dataHoje();
		DateFormat dataform = new SimpleDateFormat("yyyyMMddhhmmss");
		String str = dataform.format(data);
		int idEnc = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();

			conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
			st = conn.createStatement();
			
			rs = st.executeQuery("SELECT max(E_ID) FROM encomendas;");
			if(rs.next()){

				idEnc = rs.getInt("max(E_ID)") + 1;
				
			}
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
		str = idEnc+str;
		return str; 
	}
    
    
    /**
     * Este método devolve uma string com uma pesquisa de uma ou várias encomendas
     * Dependendo dos parametros de entrada:
     * @param aTipo - tipo de pesquisa
     * @param aVar - argumento de pesquisa
     * @param aLogin - login do utilizador que está a pesquisar.
     * @return
     */
    
    public String pesquisaEncomenda(String aTipo, String aVar, String aLogin){
    	GereUtilizadores uti = new GereUtilizadores();
    	String str = "";
    	
    	if(aTipo == "data"){
    		str = "e_data like "+aVar;
    	}
    	
    	if(aTipo == "identificador"){
    		str = "e_id = "+aVar;
    	}
    	if(aTipo == "estado"){
    		str = "e_estado = '"+aVar+"'";
    	}  	
    	if(uti.getTipoUti(aLogin).equals("Gestor")){
    		str += " and u_id = " + getUID(aVar);
    	}
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();

    		rs = st.executeQuery("SELECT * " 
    							+ "FROM encomendas "
    							+ "WHERE "+str+";");
    		String output = "";
    		String dataAceitacao = "";
    		int i = 0;
    		while (rs.next()) {
    			i++;
    			if(rs.getString("e_dataaceitacao") == null || rs.getString("e_dataaceitacao").equals("")){
    				dataAceitacao = "Encomenda ainda não aceite";
    			} else {
    				dataAceitacao = rs.getString("e_dataaceitacao");
    			}
    			output += "\n        Encomenda        \nIdentificador: " + rs.getString("e_id")+"\"\nCusto total: " 
    					+rs.getString("e_custototal")+ "\nEstado: " + rs.getString("e_estado")
    					+"\nData de Aceitação: "+dataAceitacao+"\n";
    		if(i == 10){
				i=0;
    			output += "Limite de 10!";
    		}
    		}
    		if(output.equals("")){
    			output = "Nenhum encomenda encontrado com as especificações pedidas!";
    		}

			output += "Limite de 10!";
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
     * Este método verifica se existe alguma encomenda ou não
     * Caso exista devolve true, caso contrário devolve false.
     * @return
     */
    public boolean exists(){
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		st = conn.createStatement();
    			
			rs = st.executeQuery("SELECT count(*) FROM encomendas;");
    		if (rs == null) {
			  return false;
    		} else {
			  while (rs.next()) {
				  int count = rs.getInt("count(*)");
				  if(count == 0){
					  return false;
				  } else {
					  return true;
				  }
			  }
			}    		
    	
    		st.close();

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
    	
    	return false;
    }
   
    /**
     * Este método permite ao gestor aceitar uma ou várias encomendas e mudar o seu estado
     * Dependendo dos parametros de entrada:
     * @param aTipo - tipo de pesquisa
     * @param aIdEncomenda - argumento de pesquisa
     * @param aEstado - estado que se deve mudar aquando a aceitacao da encomenda.
     * @return
     */
    
    public void aceitaEncomendaGestor(String aTipo, String aIdEncomenda, String aEstado){
		
		String string = "";
    	
    	if(!aTipo.equals("todas")){
    		string = " AND e_identificador = '"+aIdEncomenda+"'";
    	} 
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();

    		st.executeUpdate("UPDATE encomendas set E_ESTADO = '"+ aEstado +"' WHERE e_estado = 'iniciada'" + string + ";");
    		
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
	}
    
    /**
     * Este método permite ao estafeta processar o estado de uma encomenda
     * Dependendo dos parametros de entrada:
     * @param aIdEncomenda - tipo de pesquisa
     * @param aEstado - estado de uma encomenda
     * @return
     */
    
    public void processaEstafeta(String aIdEncomenda, String aEstado){
    
    	String string = " AND e_identificador = '"+aIdEncomenda+"'";
    	String string1 = ""; 
    	if(aEstado.equals("em transporte")){
        string1 = "'aguarda entrega'"; 
    	}else{
    	string1 = "'em transporte'";	
    	}
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();

    		st.executeUpdate("UPDATE encomendas set E_ESTADO = '"+ aEstado +"' WHERE e_estado like " + string1 + "" + string + ";");
    		
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
	}
    
    /**
     * Este método permite ao armazenista processar o estado de uma encomenda
     * Dependendo dos parametros de entrada:
     * @param aIdEncomenda - tipo de pesquisa
     * @param aEstado - estado de uma encomenda
     * @param aEstado - estado de uma encomenda
     * @return
     */
    
    public void processaArmazenista(String aIdEncomenda, String aEstado, String aLogin){
        
    	String string = " AND e_identificador = '"+aIdEncomenda+"'";
    	String string1 = "";
    	String string2 = "";
    	
        string1 = "'aceite'";
        if(!aLogin.equals("")){
            string1 = "'preparada'"; 
            string2 = " AND U_ID = "+ getUID(aLogin) +"";
        	}
        
        
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();

    		st.executeUpdate("UPDATE encomendas set E_ESTADO = '"+ aEstado +"' WHERE e_estado = " + string1 + "" + string + ";");
    		
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
	}
    
    
	private Date dataHoje(){

		return new Date(System.currentTimeMillis());
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
