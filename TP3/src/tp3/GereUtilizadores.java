package tp3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class GereUtilizadores {

	Connection conn;
    Statement st, st1, st2;
    ResultSet rs, rs1;
    String ip, porto, nomebd, login, pass;
    
    JavaProperties file = new JavaProperties();
    
    /**
     * Construtor da classe GereUtilizadores que recebe como parametros 
	 * os dados para poder acessar a Base de Dados
     * @param aIP
     * @param aPorto
     * @param aNomeBD
     * @param aLogin
     * @param aPass
     */
    GereUtilizadores(String aIP, String aPorto, String aNomeBD, String aLogin, String aPass){
    	
    	if(file.leFicheiroTexto("ip") == null ){

        	file.escreveFicheiroTexto(aIP, aPorto, aNomeBD, aLogin, aPass);
    	}
    }
    
    GereUtilizadores(){
    	
    }

    public void atualizaUti(String aLogin, String aTipo, String aVar, Clientes aCli, Funcionarios aFunci, Utilizadores aUti){
    	
    	getJavaProperties();
    	
    	String tabela = "";
    	String coluna = "";
    	   	
    	if(aTipo.substring(0,1).equals("u")){
    		tabela = "utilizadores";
    	} else if(aTipo.substring(0,1).equals("c")){
    		tabela = "clientes";
    	} else {
    		tabela = "funcionarios";    		
    	}
    	if(aCli == null && aFunci == null && aUti == null){
    		coluna = aTipo;
    		coluna = coluna + " = " + aVar;
    	} else {
    		if(aTipo == "ugeral"){
    			coluna = "u_nome = '" + aUti.getNome() + "', u_login = '" + aUti.getLogin() + "', u_email = '" + aUti.getEmail() + "' , u_password = '" + aUti.getPass() + "' ";
    		} else if(aTipo == "cli"){
    			coluna = "c_numcontribuinte = " + aCli.getNumContribuinte() + ", c_contactotelefonico = " + aCli.getContactoTelefonico() + ", c_morada = '" + aCli.getMorada() +"'";
    		} else if(aTipo == "func"){
    			coluna = "f_numcontribuinte = " + aFunci.getNumContribuinte() + ", f_telefone = " + aFunci.getContactoTelefonico() + ", f_morada = '" + aFunci.getMorada() +"', f_especializacao = '" + aFunci.getEspecializacao() + "'";
    		}
    	}
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		st.executeUpdate("UPDATE "+tabela+" "
    						+ "set "+coluna+" "
    						+ "where u_id = "+getUID(aLogin)+";");
    		
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
     * Este método devolve um inteiro com o id de um utilizador.
     * Isto é feito com a utilização de um login que vem como parametro de entrada.
     * @param aLogin
     * @return
     */
    public int getUID(String aLogin){
    	getJavaProperties();
    	int u_id = 0;
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		rs = st.executeQuery("SELECT * FROM utilizadores WHERE U_LOGIN = '"+ aLogin +"';");
    		
			if(rs.next()){
				u_id = rs.getInt("U_ID");
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
    	return u_id;
    }
    
    /**
     * Este método lista todos os utilizadores.
     * E caso o gestor queira uma lista ordenada, o parametro de entrada aOrd, vem com o campo de ordenação e o sentido(ascendente, descendente).
     * @param aOrd
     * @return
     */
    public String listaUtilizadores(String aOrd){
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		if(!aOrd.equals("")){
    			aOrd = "ORDER BY " + aOrd;
    		}
    		
    		rs = st.executeQuery("SELECT * " 
    							+ "FROM utilizadores " + aOrd + ";");

    		int i = 0;
			String estado ="";
			String output = "";
    		while (rs.next()) {
    			i++;
    			if(rs.getInt("U_ESTADO")==0){
    				estado = "Por Aprovado";
    			} else if(rs.getInt("U_ESTADO")==1){
    				estado = "Aprovado";
    			} else {
    				estado = "Reprovado";
    			}
    			output += "\n     Utilizador      \nNome: " + rs.getString("U_Nome")+"\nLogin: "  +rs.getString("U_Login")+ "\nEmail: " +rs.getString("U_EMAIL")+ "\nEstado: " +estado+ "\nTipo: " +rs.getString("U_TIPO")+"\n";
    			if(i == 10){
					i=0;
    				output += "limite10";
    			}
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
     * Este método lista todos os clientes
     * E caso o gestor queira uma lista ordenada, o parametro de entrada aOrd, vem com o campo de ordenação e o sentido(ascendente, descendente).
     * @param aOrd
     * @return
     */
    public String listaClientes(String aOrd){
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		if(!aOrd.equals("")){
    			aOrd = "ORDER BY " + aOrd;
    		}
    		
    		rs = st.executeQuery("SELECT U_NOME, U_LOGIN, U_ESTADO, U_EMAIL, C_NUMCONTRIBUINTE, C_CONTACTOTELEFONICO, C_MORADA, C_SETORATIVIDADE, C_ESCALAO "
    					+"FROM utilizadores as u, clientes as c "
    					+"WHERE u.U_ID = c.U_ID " + aOrd + ";");

    		int i = 0;
			String estado ="";
			String output = "";
    		while (rs.next()) {
    			i++;
    			if(rs.getInt("U_ESTADO")==0){
    				estado = "Por Aprovar";
    			} else {
    				estado = "Aprovado";
    			}
    			output += "\n     Clientes     \nNome: " + rs.getString("U_Nome")+"\nLogin: "  +rs.getString("U_Login")+ "\nEmail: " +rs.getString("U_EMAIL")+ "\nEstado: " 
    					+ estado + "\nNúmero Contribuinte: " +rs.getString("C_NUMCONTRIBUINTE")+ "\nNúmero Telefone: " +rs.getString("C_CONTACTOTELEFONICO")+ "\nMorada: " +rs.getString("C_MORADA")
    					+ "\nSetor de Atividade: " +rs.getString("C_SETORATIVIDADE")+ "\nEscalao: " +rs.getString("C_ESCALAO")+"\n";
    			if(i == 10){
					i=0;
    				output += "limite10";
    			}
    		} 	
    		
    		if(output.equals("")){
				output = "\n Não tem nenhum cliente registado no sistema!\n";
			}

    		output += "Limite de 10";
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
     * Este método lista todos os funcionarios
     * @return
     */
    
    public String listaFuncionarios(){
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		rs = st.executeQuery("SELECT * FROM funcionarios WHERE F_ESPECIALIZACAO like 'false';");

    		int i = 0;
			String estado ="";
			String output = "";
    		while (rs.next()) {
    			i++;
    			if(rs.getInt("U_ESTADO")==0){
    				estado = "Por Aprovar";
    			} else {
    				estado = "Aprovado";
    			}
    			output += "\n     Funcionarios     \nNome: " + rs.getString("U_Nome")+"\nLogin: "  +rs.getString("U_Login")+ "\nEmail: " +rs.getString("U_EMAIL")+ "\nEstado: " 
    					+ estado + "\nNúmero Contribuinte: " +rs.getString("F_NUMCONTRIBUINTE")+ "\nNúmero Telefone: " +rs.getString("F_CONTACTOTELEFONICO")+ "\nMorada: " +rs.getString("F_MORADA");
    			if(i == 10){
					i=0;
    				output += "limite10";
    			}
    		} 	
    		
    		if(output.equals("")){
				output = "\n Não tem nenhum cliente registado no sistema!\n";
			}

    		output += "Limite de 10";
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
     * Este método pesquisa por um ou mais utilizadores
     * Dependendo dos argumentos de entrada.
     * aTipo - tipo de pesquisa.
     * aVar - argumento para o tipo de pesquisa
     * @param aTipo
     * @param aArg
     * @param aOrd
     * @return
     */
    public String pesquisaUtilizador(String aTipo, String aVar){
    	
    	String uti = "";
    	
    	if(aTipo == "login"){
    		uti = "U_LOGIN = '" + aVar + "'";
    	}
    	if(aTipo == "nome"){
    		uti = "U_NOME = '%"+aVar+"%'";
    	}
    	if(aTipo == "tipo"){
    		uti = "U_TIPO = '"+aVar+"'";
    	}
    	System.out.println(uti);
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
System.out.println(uti);
    		rs = st.executeQuery("SELECT * FROM utilizadores WHERE "+uti+";");

			String estado ="";
			String output = "";
			int i = 0;
    		while (rs.next()) {
    			i++;
    			if(rs.getInt("U_ESTADO")==0){
    				estado = "Por Aprovar";
    			} else {
    				estado = "Aprovado";
    			}
    			output += "\n    Utilizadores    \nNome: " + rs.getString("U_NOME")+"\nLogin: "  +rs.getString("U_LOGIN")+ "\nEmail: " +rs.getString("U_EMAIL")+ "\nEstado: " +estado+ "\nTipo: " +rs.getString("U_TIPO")+"\n";
    			if(i == 10){
					i=0;
    				output += "Limite de 10";
    			}
    		} 	
    		if(output.equals("")){
				output = "\n Não tem nenhum utilizador registado no sistema!\n";
					
			}

    		output += "Limite de 10";
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
     * Este método é utilizado pelo gestor, que pode aprovar ou reprovar, um ou varios utilizadores
     * aTipo - serve para saber se é um ou mais utilizadores para aprovar.
     * aVar - caso seja só um recebe o login do utilizador que é para aprovar ou reprovar.
     * @param aTipo
     * @param aVar
     */
    public void aprovaPedido(String aTipo, String aVar){
    
    	String uti = "";
    	int estado = 1;
    	
    	if(aTipo == "login"){
    		uti = "and U_LOGIN = '"+aVar+"'";
    	}
    	if(aTipo == "todos"){
    		uti = "";
    	}
    	if(aTipo == "rlogin"){
    		estado = -1;
    		uti = "and U_LOGIN = '"+aVar+"'";
    	}
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();

    		st.executeUpdate("UPDATE utilizadores set U_ESTADO = " + estado + " WHERE u_estado = 0 " + uti +";");
    		st1 = conn.createStatement();
    		st1.executeUpdate("UPDATE funcionarios set U_ESTADO = " + estado + " WHERE u_estado = 0 " + uti +";");
    		st2 = conn.createStatement();
    		st2.executeUpdate("UPDATE clientes set U_ESTADO = " + estado + " WHERE u_estado = 0 " + uti +";");
    		
    		
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
    		if (st1 != null) {
    			try {
    				st1.close();
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
     * Este método serve para introduzir um utilizador na base de dados.
     * Caso seja venha um dos utilizadores os outros são colocados como null
     * @param aFuncionario
     * @param aCliente
     * @param aGestor
     */
    public void insereUtilizador(Funcionarios aFuncionario, Clientes aCliente, Utilizadores aGestor){
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		if (aCliente == null && aGestor == null) {

        		st.execute("INSERT INTO utilizadores(U_NOME, U_LOGIN, U_PASSWORD, U_ESTADO, U_EMAIL, U_TIPO)"
        				+ "VALUES ('"+aFuncionario.nome+"', '"+aFuncionario.login+"', '"+aFuncionario.password+"', 0, '"+aFuncionario.email+"', 'Funcionario');");
        		
        		int u_id = 0;
        		rs = st.executeQuery("SELECT U_ID FROM utilizadores WHERE U_LOGIN = '"+aFuncionario.login+"';");
        		if (rs == null) {
				     System.out.println("No Record !!");
        		} else {
				  while (rs.next()) {
					  u_id = rs.getInt("U_ID");
				  }
    			}
        		
        		st.execute("INSERT INTO funcionarios(U_ID, U_NOME, U_LOGIN, U_PASSWORD, U_ESTADO, U_EMAIL, U_TIPO, F_NUMCONTRIBUINTE, F_CONTACTOTELEFONICO, F_MORADA, F_ESPECIALIZACAO)"
        				+ "VALUES ('"+ u_id +"','"+aFuncionario.nome+"', '"+aFuncionario.login+"', '"+aFuncionario.password+"', 0, '"+aFuncionario.email+"', 'Funcionario', '"+ aFuncionario.getNumContribuinte() +"','"+ aFuncionario.getContactoTelefonico() +"','"+ aFuncionario.getMorada() +"','"+ aFuncionario.getEspecializacao() +"');");
        		
			} if (aFuncionario == null && aGestor == null) {
			
				st.execute("INSERT INTO utilizadores(U_NOME, U_LOGIN, U_PASSWORD, U_ESTADO, U_EMAIL, U_TIPO)"
        				+ "VALUES ('"+aCliente.nome+"', '"+aCliente.login+"', '"+aCliente.password+"', 0, '"+aCliente.email+"', 'Cliente')");		
				
				int u_id = 0;
        		rs = st.executeQuery("SELECT U_ID FROM utilizadores WHERE U_LOGIN = '"+aCliente.login+"';");
        		if (rs == null) {
				     System.out.println("No Record!");
        		} else {
				  while (rs.next()) {
					  u_id = rs.getInt("U_ID");
				  }
    			}
        		
        		st.execute("INSERT INTO clientes(U_ID, U_NOME, U_LOGIN, U_PASSWORD, U_ESTADO, U_EMAIL, U_TIPO, C_NUMCONTRIBUINTE, C_CONTACTOTELEFONICO, C_MORADA)"
        				+ "VALUES ('"+ u_id +"','"+aCliente.nome+"', '"+aCliente.login+"', '"+aCliente.password+"', 0, '"+aCliente.email+"', 'Cliente', '"+ aCliente.getNumContribuinte() +"','"+ aCliente.getContactoTelefonico() +"','"+ aCliente.getMorada() +"');");
				
			} if (aCliente == null && aFuncionario == null) {
		
				int bool = 0;
				if(verificaGestor()){
					bool = 0;
				}
				else{
					bool = 1;
				}

				st.execute("INSERT INTO utilizadores(U_NOME, U_LOGIN, U_PASSWORD, U_ESTADO, U_EMAIL, U_TIPO)"
        				+ "VALUES ('"+aGestor.nome+"', '"+aGestor.login+"', '"+aGestor.password+"', "+ bool +", '"+aGestor.email+"', 'Gestor')");
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
    		/*if (ps != null) {
    			try {
    				ps.close();
    			} catch (Exception e) {
    				System.out.println("!! Exception returning statement !!\n"+e);
    			}
    		}*/
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
     * Este método devolve true ou false, caso exista algum gestor, ou caso não exista nenhum.
     * @return
     */
    public boolean verificaGestor(){
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		rs = st.executeQuery("SELECT * FROM utilizadores WHERE U_TIPO = 'gestor';");
    		
    		if (rs == null) {
			  return false;
    		} else {
			  if (rs.next()) {
				  return true;
			  }
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
    	} 
    	return false;
    }
    
    /**
     * Este método é utilizado para verificar o Estado de um utilizador
     * @param aLogin
     * @return
     */
    public int verificaEstado(String aLogin){
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();

    		rs = st.executeQuery("SELECT U_ESTADO FROM utilizadores WHERE U_LOGIN = '"+ aLogin +"';");

    		if (rs.next()) {
    			return rs.getInt("U_ESTADO");
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
    	} 
    	return 0;
    }
    
    /**
     * Este método verifica se o login existe na base de dados
     * @param aLogin
     * @return
     */
    public boolean verificaLogin(String aLogin){
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		rs = st.executeQuery("SELECT * FROM utilizadores WHERE U_LOGIN = '"+ aLogin +"';");
    		
    		if (rs == null) {
			  return false;
    		} else {
			  if (rs.next()) {
				  return true;
			  }
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
    	return false;
    }

    /**
     * Este método verifica se o email existe na base de dados
     * @param aEmail
     * @return
     */
    public boolean verificaEmail(String aEmail){
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		rs = st.executeQuery("SELECT * FROM utilizadores WHERE U_EMAIL = '"+ aEmail +"';");
    		
    		if (rs == null) {
			  return false;
    		} else {
			  if (rs.next()) {
				  return true;
			  }
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
    	return false;
    }
    
   
    /**
     * Este método faz a autenticação do inicio de sessão.
     * @param aLogin
     * @param aPass
     * @return
     */
    public boolean verificaAutenticacao(String aLogin, String aPass){

    	if(verificaLogin(aLogin)){

    		getJavaProperties();
    		try {
    			Class.forName("com.mysql.jdbc.Driver").newInstance();
    			conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    			st = conn.createStatement();

    			rs = st.executeQuery("SELECT * FROM utilizadores WHERE U_LOGIN = '"+ aLogin +"'AND U_PASSWORD = '"+ aPass +"';");
    			if (rs == null) {
    				return false;
    			} else {
    				if (rs.next()) {
    					return true;
    				}
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
    		}
    	}
    	return false;
    }
    
    /**
     * Este método é utilizado para obter o tipo do utilizador e devolve o seu tipo (cliente, gestor, funcionário)
     * @param aLogin
     * @return
     */
    public String getTipoUti(String aLogin){
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		rs = st.executeQuery("SELECT * FROM utilizadores WHERE U_LOGIN = '"+ aLogin +"';");
    	
    		while (rs.next()) {
    			return rs.getString("U_TIPO");
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
    	}
    	
    	return "";
    }
    
    /**
     * Este método é utilizado para obter o tipo de especialização de um funcionário
     * @param aLogin
     * @return
     */
    
    public String getEspecFunci(String aLogin){
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		rs = st.executeQuery("SELECT * FROM funcionarios WHERE U_LOGIN = '"+ aLogin +"';");
    	
    		while (rs.next()) {
    			return rs.getString("F_ESPECIALIZACAO");
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
    	}
    	
    	return "";
    }
    
    /**
     * Esta função serve para verrificar o contribuinte do utilizador
     * Assim pode ser apresentado as seguintes mensagens: bem vindo nome, adeus nome.
     * @param aLogin
     * @return
     */
    
     public boolean verificaContribuinte(int aCont, String aTipo){
    	
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);

    		st = conn.createStatement();
    		
			rs = st.executeQuery("SELECT * FROM clientes WHERE c_numcontribuinte = '"+ aCont +"';");
    		if (rs == null) {
			  return false;
    		} else {
			  while (rs.next()) {
				  return true;
			  }
			}
    		
    		rs = st.executeQuery("SELECT * FROM funcionarios WHERE f_numcontribuinte = '"+ aCont +"';");
    		if (rs == null) {
  			  return false;
      		} else {
  			  while (rs.next()) {
  				  return true;
  			  }
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
    	} 
    	return false;
    }

     /**
      * Esta função devolve o nome de um determinado login.
      * @param aLogin
      * @return
      */
    public String devolveNome(String aLogin){
    	getJavaProperties();
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
    		conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+porto+"/"+nomebd, login, pass);
    		
    		st = conn.createStatement();
    		
    		rs = st.executeQuery("SELECT * FROM utilizadores WHERE U_LOGIN = '"+ aLogin +"';");
    	
    		while (rs.next()) {
    			String nome = rs.getString("U_NOME");
    			return nome;
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

