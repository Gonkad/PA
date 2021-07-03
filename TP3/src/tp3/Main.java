package tp3;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
	private static Scanner teclado = new Scanner(System.in);
	private static String login1 = "";
	private static String utinome = "";
	private static Clientes cliente;
	private static Funcionarios funcionario;
	private static Utilizadores gestor;
	private static String dataInicio;
	private static String dataFim;
    private static JavaProperties file = new JavaProperties();
    private static GereBaseDados GereBD = new GereBaseDados();
    private static GereLog gerelog = new GereLog();
    private static GereUtilizadores uti = new GereUtilizadores();
    
	public static void main(String[] args) {        
		
		dataInicio = dataHoje(); 		
		verDadosBD();
		entrada(uti);
		System.out.println("Putas e vinho verde");
	}
	
public static void verDadosBD(){
	
	if(GereBD.carregaProperties()){

		boolean bool = leBoolean("Deseja fazer uma nova conexão á base de dados? (s/n): ");
		
		if(bool){
			if(insereDadosBD()){
				if(GereBD.verTabelasDadosBD()){
					System.out.println("Conexão á Base de Dados com sucesso!\n\n");
				} else {
					System.out.println("Conexão á Base de Dados com sucesso!\nMas a base de dados não tem todas as tabelas necessárias para correr o programa\n\n");
				}
			} else {
				System.out.println("\nComo não deseja introduzir novos dados, o programa vai arrancar a com a base de dados anterior!!\n\n");
			}
		}
		
	} else {		
		
		System.out.println("\nNão houve nenhuma ligação a uma base de dados, insira os dados para criar uma nova!!\n\n");
		
		if(insereDadosBD()){

			System.out.println("Conexão á base de dados com sucesso!\n\n");
			 
		} else {
			
			System.out.println("Como desejou não introduzir nada. Adeus!!!\n\n");
			System.exit(0);
		}
		
	}

}

public static boolean insereDadosBD(){
	
	String tab [] = new String [5];
	
	do{
		tab[0] = leString("\n\nIntroduza o ip da Base de Dados: ");
		tab[1] = leString("Introduza o porto da Base de Dados: ");
		tab[2] = leString("Introduza o nome da Base de Dados: ");
		tab[3] = leString("Introduza o login para acessar a Base de Dados: ");
		tab[4] = leString("Introduza a palavra passe da Base de Dados: ");
		
		if(GereBD.carregaDados(tab[0], tab[1], tab[2], tab[3], tab[4])){

			file.escreveFicheiroTexto(tab[0], tab[1], tab[2], tab[3], tab[4]);
			return true;

		} else {
			System.out.println("\nDados incorretos!\n");
			boolean bool = leBoolean("Deseja tentar novamente? (s/n): ");
			
			if(!bool){
				return false;
			}
			
		}
	} while(true);
	
}

public static void entrada(GereUtilizadores aUti){
	do{
		if(aUti.verificaGestor()){
			utinome = "";

			
			System.out.println("\n|----------Menu Principal----------|\n"
					 + "1 - Iniciar Sessão\n"
					 + "2 - Registar Utilizador\n"
					 + "3 - Sair");

			switch (LerOpcao(3)) {
			case 1:{
				if(iniciarSessao()){
                    System.out.println("\n-----Bem vindo " + utinome + "-----\n");
					menuTipo();
					break;
				} break;
			}
			case 2: {
				registroUtilizador(); 
				break;
			}
			case 3:{
				teclado.close();
				dataFim = dataHoje();
				tempo();
				System.out.println("\n|----------Informação----------|\n"
						 + "Adeus!! :D");
				System.exit(0);
			}
			}	

		} else {
			System.out.println("\n|----------Informação----------|\n"
					 + "Não há nenhum gestor registado, introduz um primeiro\n");
			registroGestor(aUti);
		}
	}while(true);
} 

public static boolean iniciarSessao(){
	
	GereUtilizadores guti = new GereUtilizadores();
	utinome = "";
	String login = leString("\nIntroduza o login: ");
	String pass = leString("Introduza a pass: ");
	
	do {
		if(guti.verificaAutenticacao(login, pass)){
			if(guti.verificaEstado(login) == 1){
				login1 = login;
				utinome = guti.devolveNome(login);
				return true;
			} else if (guti.verificaEstado(login) == 0){
				System.out.println("O utilizador que colocou ainda não foi aceite pelo Gestor.\n");
				return false;
			} else {
				System.out.println("O utilizador que colocou não foi aprovado pelo Gestor.\n");
				return false;
			}
		} else {
			System.out.println("Os dados que colocou não se encontram corretos.\n");

			boolean bool = leBoolean("Deseja tentar outra vez? (s/n): ");
			if(!bool){
				return false;
			}
			login = leString("\n\nIntroduza o login: ");
			pass = leString("Introduza a pass: ");
		}
	} while (true);
	
}

public static void menuTipo(){
	
	String string = uti.getTipoUti(login1);
	
	if(string.equals("Cliente")){
		menuCliente();
			
	} else if(string.equals("Funcionario")){
		menuFuncionario();
		
	} else if(string.equals("Gestor")){
		menuGestor();
	}
}

public static void menuCliente(){
	
	GereEncomendas enc = new GereEncomendas();
	boolean bool = true;
	do{
		if(enc.exists()){
			
			System.out.println("\n|----------Menu Cliente----------|\n"
					 + "1 - Fazer pedido de Encomenda\n"
					 + "2 - Listar Encomendas\n"
					 + "3 - Pesquisar Encomendas\n"
					 + "4 - Notificações\n"
					 + "5 - Listar Produtos\n"
					 + "6 - Pesquisar Produtos\n"
					 + "7 - Sair");
			

			switch(LerOpcao(7)){
			case 1:{
				pedeEncomenda();
				break;
			}
			case 2: {
				menuListarEncomendas();
				break;
			}
			case 3: {
				menuPesquisarEncomendas();
				break;
			}
			case 4:{
				notificacoesCliente();
				break;
			}
			case 5:{
				listarProdutos();
				break;
			}
			case 6:{
				pesquisaProdutos();
				break;
			}
			case 7: {
				bool = false;
			}
			}

		} else {
			
			System.out.println("Nenhuma encomenda ainda registada!");	
			System.out.println("\n|----------Menu Cliente----------|\n"
					 + "1 - Fazer pedido de Encomenda\n"
					 + "2 - Listar Encomendas\n"
					 + "3 - Listar Produtos\n"
					 + "4 - Notificacoes\n"
					 + "5 - Sair");
			
			switch(LerOpcao(5)){
			case 1:{
				pedeEncomenda();
				break;
			}
			case 2: {
				menuListarEncomendas();
				break;
			}
			case 3: {
				listarProdutos();
				break;
			}
			case 4: {
				notificacoesCliente();
				break;
			}
			case 5: {
				bool = false;
				break;
			}
			}
		}
	} while (bool);

}

public static void menuFuncionario(){
	
	String string = uti.getEspecFunci(login1);	
	GereEncomendas enc = new GereEncomendas();
	boolean bool = true;
	do{
		if(enc.exists()){
			if(string.equals("true")){//funcionario armazenista

			System.out.println("\n|----------Menu Funcionario Armazenista----------|\n"
						 + "1 - Notificacoes\n"
						 + "2 - Listar Encomendas\n"
						 + "3 - Pesquisar Encomendas\n"
						 + "4 - Inserir Produtos\n"
						 + "5 - Listar Produtos\n"
						 + "6 - Pesquisar Produtos\n"
						 + "7 - Dar encomenda como preparada\n"
						 + "8 - Atribuir encomenda\n"
						 + "9 - Sair");

			switch(LerOpcao(9)){
			case 1:{
				notificacoesFuncionarioArmazenista();//por fazer
				break;
			}
			case 2: {
				menuListarEncomendas();
				break;
			}
			case 3: {
				menuPesquisarEncomendas();
				break;
			}
			case 4:{
				insereProduto();
				break;
			}
			case 5: {
				listarProdutos();	
				break;		
			}
			case 6:{
				pesquisaProdutos();
				break;
			}
			case 7:{
				encomendaPreparada();
				break;
			}
			case 8:{
				atribuiEncomenda();
				break;
			}
			case 9: {
				bool = false;
			}
			}
			}else if(string.equals("false")){
				
			//funcionario estafeta
				System.out.println("\n|----------Menu Funcionario Estafeta----------|\n"
						 + "1 - Notificacoes\n"
						 + "2 - Listar Encomendas\n"
						 + "3 - Pedidos de Entrega\n"
						 + "4 - Pesquisar Encomendas\n"
						 + "5 - Entregar Encomenda\n"
						 + "6 - Sair");
				
				switch(LerOpcao(6)){
				case 1:{
					notificacoesFuncionarioEstafeta();//por fazer
					break;
				}
				case 2: {
					menuListarEncomendas();
					break;
				}
				case 3: {
					pedidosEntrega();
					break;
				}
				case 4: {
					menuPesquisarEncomendas();
					break;
				}
				case 5: {
					entregaEncomenda();
					break;
				}
				case 6: {
					gerelog.insereLog("Terminou Sessão", login1);
					bool = false;
				}
				}
				
			}
		} else {
		System.out.println("Nenhuma encomenda foi ainda aprovada!");
		System.out.println("\n|----------Menu Funcionario----------|\n"
				 + "1 - Notificacoes\n"
				 + "2 - Inserir Produtos\n"
				 + "3 - Listar Produtos\n"
				 + "4 - Sair");

		switch(LerOpcao(4)){
		case 1: {
			break;
		}
		case 2:{
			insereProduto();
			break;
		}
		case 3:{
			listarProdutos();	
			break;	
		}
		case 4: {
			bool = false;
			break;	
		}
		}
		}
	} while (bool);

}

public static void menuGestor(){
	
	boolean bool = true;
	do{

	System.out.println("\n|----------Menu Gestor----------|\n"
			 + "1 - Utilizadores\n"
			 + "2 - Encomendas\n"
			 + "3 - Notificações\n"
			 + "4 - Log\n"
			 + "5 - Informação\n"
			 + "6 - Sair");
	
	switch(LerOpcao(6)){
	case 1:
		menuGestorUtilizadores();
		break;
	
	case 2: 
		menuGestorEncomenda();
		break;
	
	case 3: 
		menuGestorNotificacoes();
		break;
	
	case 4:
		menuGestorLog();
		break;
		
	case 5:
		tempo();
		break;
  
	case 6: 
		bool = false;
	
	}
	} while (bool);
}

public static void menuGestorLog(){
	
	System.out.println("\n|----------Menu Gestor----------|\n"
			 + "1 - Registos todos do log\n"
			 + "2 - Pesquisar log de um utilizador\n"
			 + "3 - Sair");
	
	String string = "";
	String login = "";
	
	switch(LerOpcao(3)){
	case 1:{	
		boolean bool = leBoolean("Deseja visualizar uma lista do log ordenada? (s/n): ");
			if(bool){
				boolean bool1 = leBoolean("Deseja ver uma lista ascendente(s) ou descendente(n)? (s/n): ");
				if(bool1){
				string = "u_login asc";
				}else{
				string = "u_login desc";	
				}
		} else {
			break;
		}
	}
	case 2:{
		login = leString("Introduza o login do utilizador que deseja ver os logs: ");
		boolean bool = leBoolean("Deseja visualizar uma lista do log ordenada? (s/n): ");
		if(bool){
			boolean bool1 = leBoolean("Deseja ver uma lista ascendente(s) ou descendente(n)? (s/n): ");
			if(bool1){
			string = "u_login asc";
			}else{
			string = "u_login desc";	
			}
	} else {
		break;
	} 
	}
	case 3:{
		break;
	}
}
	String lista = gerelog.listaLog(login, string);
    System.out.println(lista);
}

private static void pedeEncomenda(){
	
   System.out.println("Nova Encomenda");
   GereProdutos prod = new GereProdutos();
   GereEncomendas enc = new GereEncomendas();
   
   int produtosarray[];
   produtosarray = new int[30];
   int i = 0;
   String Id_E = "";
   boolean boole = false;
   if(prod.exists()){
	do{	
	    String identificador = leString("Qual a designacao do produto que deseja adicionar á encomenda?\n");
		if((prod.getPID(identificador) == 0)){
			System.out.print("Não existe nenhum produto com essa designacao.\n");
		}else{
			int id_prod = prod.getPID(identificador);
			boolean bool = leBoolean("Deseja adicionar este produto á encomenda?(s/n)");
			if(bool){
				produtosarray[i] = id_prod;
				i++;
				int quantidade =  leInt("Que quantidade do produto selecionado deseja adicionar á encomenda?");
				if(prod.verStock(quantidade, id_prod)){
				boolean bool2 =  leBoolean("Deseja adicionar mais algum produto á encomenda?(s/n).");
				if(bool2){
				
				}else{
			    for(int x = 0; x <= produtosarray.length; x++){
			    if(x == 0){
			    int custototal = prod.getCusto(produtosarray[x]);
			    custototal = custototal * quantidade;
			    Id_E = enc.geraIDEnc();
			    Encomenda encomenda = new Encomenda(Id_E, custototal, "iniciada");
			    int uid = uti.getUID(login1);
			    enc.insereEncomenda(encomenda, id_prod, uid);
			    }else{
				int id2 = produtosarray[x];
				enc.adicionaProdutoEncomenda(Id_E, id2);
			    }
			    }
			    System.out.println("Produtos adicionados á encomenda.");
			    boole = true;
				}
				}else{
				System.out.println("Não há stock suficiente para adicionar o mesmo á encomenda.");
				}
			}
		}
	}while(boole = true);
	}else{
		System.out.println("Não é possivel fazer um pedido de encomenda pois ainda não produtos registados no sistema.");
	}
   }

public static void menuGestorNotificacoes(){//por mudar
	
	System.out.println("\n|----------Notificacoes Gestor----------|\n");
	System.out.println(new GereNotificacoes().GestorNotificacoes());
	gerelog.insereLog("Verificou as suas notificações", login1);
}

public static void menuGestorUtilizadores(){
	

	System.out.println("\n|----------Menu Gestor----------|\n"
			 + "1 - Criar Utilizador\n"
			 + "2 - Listar\n"
			 + "3 - Pesquisar\n"
			 + "4 - Aprovar\n"
			 + "5 - Sair");
	
	switch(LerOpcao(5)){
	case 1: 
		registroUtilizador();
		break;

	case 2: 
		listaUtilizadores();
		gerelog.insereLog("Listou Utilizadores", login1);
		break;
	
	case 3:
		pesquisaUtilizadores();
		break;
		
	case 4: 
		aprovarPedidos();		
		break;	
	
	case 5: 
		break;
	
	}
}

private static void encomendaPreparada(){
	
	GereEncomendas enc = new GereEncomendas();
	String string = "";
	boolean bool = leBoolean("Deseja dar como preparada uma encomenda?\n");
	if(bool){
		string = "preparada";
		String identificador = leString("Qual o identificador da encomenda que deseja dar como preparada?\n");
		enc.processaArmazenista(identificador, string, "");
		gerelog.insereLog("Deu como encomenda preparada", login1);
	}
}

private static void atribuiEncomenda(){
	
	GereEncomendas enc = new GereEncomendas();
	String string = "";
	
	String lista = uti.listaFuncionarios();
    System.out.println(lista);
	
	boolean bool = leBoolean("Deseja atribuir um funcionario estafeta a uma encomenda?(s/n)\n");
	if(bool){
		String login = leString("Qual o login do funcionario que deseja atribuir a encomenda?");
		string = "aguarda entrega";
		String identificador = leString("Qual o identificador da encomenda que deseja dar como preparada?\n");
		enc.processaArmazenista(identificador, string, login);
		gerelog.insereLog("Atribuiu encomenda", login1);
	}
}

private static void pedidosEntrega(){
	
	GereEncomendas enc = new GereEncomendas();
	String string = "";
	
	System.out.println("\n        Encomendas       \n");
	
	String var = enc.listaEncomendas(string, login1);
    System.out.println(var);
	
	boolean bool = leBoolean("Deseja aceitar algum pedido de entrega?\n");
	if(bool){
		string = "em transporte";
		String identificador = leString("Qual o identificador da encomenda que deseja aceitar?\n");
		enc.processaEstafeta(identificador, string);
		gerelog.insereLog("Aceitou pedido", login1);
	}
}

private static void entregaEncomenda(){
	
	GereEncomendas enc = new GereEncomendas();
	String string = "";
	
	System.out.println("\n        Encomendas       \n");
	
	String var = enc.listaEncomendas(string, login1);
    System.out.println(var);
	boolean bool = leBoolean("Deseja mudar alguma encomenda como entregue?(s/n)\n");
	if(bool){
		string = "entregue";
		String identificador = leString("Qual o identificador da encomenda que deseja dar como entregue?\n");
		enc.processaEstafeta(identificador, string);
		gerelog.insereLog("Deu como entregue", login1);
	}
	
}

public static void aprovarPedidos(){
	

	String string1 = "", tipo = "";

	System.out.println("\n|----------Menu Aprovar----------|\n"
			 + "1 - Aprovar todos os utilizadores\n"
			 + "2 - Aprovar por login\n"
			 + "3 - Reprovar por login\n"
			 + "4 - Sair");
	
	switch(LerOpcao(4)){
	case 1: {
		tipo = "todos"; 
		gerelog.insereLog("Aprovou todos os utilizadores", login1);
		break;
	}
	case 2:{
		tipo = "login"; 
		string1 = leString("Introduza o login do qual deseja aprovar: ");
		do {
			if(uti.verificaLogin(string1)){
				break;					
			} else {
				string1 = leString("\nEste login não está registado!\nIntroduza um login que esteja registado: ");
			}
		} while (true);
		gerelog.insereLog("Aprovou o login: " + string1, login1);
		break;
	}
	case 3: {
		tipo = "rlogin"; 
		string1 = leString("Introduza o login que deseja reprovar: ");
		do {
			if(uti.verificaLogin(string1)){
				break;					
			} else {
				string1 = leString("\nEste login não está registado\nIntroduza um login que esteja registado: ");
			}
		} while (true);
		gerelog.insereLog("Reprovou o login: " + string1, login1);
		break;
	}
	case 4: {
		break;
	}
	}
	
	uti.aprovaPedido(tipo, string1);
	
	if(!tipo.equals("rlogin")){
		System.out.println("Utilizador(es) aprovado(s) com sucesso.");
	} else {
		System.out.println("Utilizador reprovado com sucesso.");
	}
	}


public static void listaUtilizadores(){
	
	boolean bool = leBoolean("Deseja ver uma lista ordenada dos utilizadores? (s/n): ");
	String string = "";
	if(bool){
		boolean bool1 = leBoolean("Deseja ver uma lista ascendente(s) ou descendente(n)? (s/n): ");
		if(bool1){
		string = "u_nome asc";
		}else{
		string = "u_nome desc";	
		}
	}
	
	String lista = uti.listaUtilizadores(string);
    System.out.println(lista);
}

public static boolean pesquisaProdutos(){
	
	GereProdutos prod = new GereProdutos();
	String string = uti.getTipoUti(login1);
	String tipo = ""; 
	String string1 = "";
	
	if(string.equals("Funcionario")){
	
	System.out.println("\n|----------Menu Pesquisa----------|\n"
			 + "1 - Por stock\n"
			 + "2 - Por designacao\n"
			 + "3 - Por categoria\n"
			 + "4 - Sair");

	switch(LerOpcao(4)){
	
	case 1: 
	{
		tipo = "stock"; 
		string1 = leString("Introduza o stock abaixo de certa quantidade para pesquisar: ");
		boolean bool = leBoolean("Deseja ver uma lista ordenada dos produtos? (s/n): ");		
		
		if(bool){
			boolean bool1 = leBoolean("Deseja ver uma lista ascendente(s) ou descendente(n)? (s/n): ");
			if(bool1){
				string += string1 + "asc";
				}else{
				string += string1 + "desc";	
				}
		}
		break;
	}
	case 2: 
	{
		tipo = "designacao"; 
		string1 = leString("Introduza a designacao do produto que deseja pesquisar: ");	
		boolean bool = leBoolean("Deseja ver uma lista ordenada dos utilizadores? (s/n): ");		
		
		if(bool){
			boolean bool1 = leBoolean("Deseja ver uma lista ascendente(s) ou descendente(n)? (s/n): ");
			if(bool1){
				string += string1 + "asc";
				}else{
				string += string1 + "desc";	
				}
		}else{
		break;	
		}
	}
	case 3: 
	{
		tipo = "categoria"; 
		string1 = leString("Introduza a categoria do produto que deseja pesquisar: ");
		boolean bool = leBoolean("Deseja ver uma lista ordenada dos utilizadores? (s/n): ");		
		
		if(bool){
			boolean bool1 = leBoolean("Deseja ver uma lista ascendente(s) ou descendente(n)? (s/n): ");
			if(bool1){
				string += string1 + "asc";
				}else{
				string += string1 + "desc";	
				}
		}else{
		break;	
		}
	}
	
	case 4: {
		return false;
	}
	}
	}else{
		System.out.println("\n|----------Menu Pesquisa----------|\n"
				 + "1 - Por designacao\n"
				 + "2 - Por categoria\n"
				 + "3 - Sair");
		switch(LerOpcao(3)){
		case 1: 
		{
			tipo = "designacao"; 
			string1 = leString("Introduza a designacao do produto que deseja pesquisar: ");	
			boolean bool = leBoolean("Deseja ver uma lista ordenada dos utilizadores? (s/n): ");		
			
			if(bool){
				boolean bool1 = leBoolean("Deseja ver uma lista ascendente(s) ou descendente(n)? (s/n): ");
				if(bool1){
					string += string1 + "asc";
					}else{
					string += string1 + "desc";	
					}
			}else{
			break;	
			}
		}
		case 2: 
		{
			tipo = "categoria"; 
			string1 = leString("Introduza a categoria do produto que deseja pesquisar: ");
			boolean bool = leBoolean("Deseja ver uma lista ordenada dos utilizadores? (s/n): ");		
			
			if(bool){
				boolean bool1 = leBoolean("Deseja ver uma lista ascendente(s) ou descendente(n)? (s/n): ");
				if(bool1){
					string += string1 + "asc";
					}else{
					string += string1 + "desc";	
					}
			}else{
			break;	
			}
		}
		
		case 3: {
			return false;
		}
		}
	}
		String var = prod.pesquisaProduto(tipo, string, login1);
        System.out.println(var);
        return true;
}

public static boolean pesquisaUtilizadores(){
	
	String string = "";
	String tipo = ""; 
	String string1 = "";
	
	System.out.println("\n|----------Menu Pesquisa----------|\n"
			 + "1 - Por nome\n"
			 + "2 - Por login\n"
			 + "3 - Por tipo\n"
			 + "4 - Sair");

	switch(LerOpcao(4)){
	
	case 1: 
	{
		tipo = "nome"; 
		string1 = leString("Introduza o nome do utilizador do qual deseja pesquisar: ");
		boolean bool = leBoolean("Deseja ver uma lista ordenada dos utilizadores? (s/n): ");		
		
		if(bool){
			boolean bool1 = leBoolean("Deseja ver uma lista ascendente(s) ou descendente(n)? (s/n): ");
			if(bool1){
				string = "u_nome asc";
				}else{
				string = "u_nome desc";	
				}
		}
		break;
	}
	case 2: 
	{
		
		tipo = "login"; 
		string1 = leString("Introduza o login do utilizador que deseja pesquisar: ");
		
		do {
			if(uti.verificaLogin(string1)){
				break;				
			} else {
				string1 = leString("\nLogin não registado\nIntroduza um que esteja registado: ");
			}
		}while (true);
		
		boolean bool = leBoolean("Deseja ver uma lista ordenada dos utilizadores? (s/n): ");		
		
		if(bool){
			boolean bool1 = leBoolean("Deseja ver uma lista ascendente(s) ou descendente(n)? (s/n): ");
			if(bool1){
				string = "u_login asc";
				}else{
				string = "u_login desc";	
				}
		}else{
		break;	
		}
	}
	case 3: {
		
		tipo = "tipo"; 

		System.out.println("\nPara pesquisar por tipo digite entre (1-3)\n Gestor - 1 \n Funcionario - 2 \n Cliente - 3\n");
		
		int opcao = LerOpcao(3);
		if(opcao == 1){ 
			string1 = "gestor";	
		}else if(opcao == 2){
			string1 = "funcionario";
		}
		else string1 = "cliente";
		break;
	}
	
	case 4: 
		return false;
	}
	
		String var = uti.pesquisaUtilizador(string, string1);
        System.out.println(var);
		return true;
}

public static void menuGestorEncomenda(){
	
	GereEncomendas enc = new GereEncomendas();

	System.out.println("\n|----------Menu Gestor Encomendas----------|\n"
			 + "1 - Listar Encomendas\n"
			 + "2 - Pesquisar Encomendas\n"
			 + "3 - Aceitar Encomendas\n"
			 + "4 - Sair");

	switch(LerOpcao(4)){
	case 1: 
		menuListarEncomendas();
		break;
	
	case 2: 
		menuPesquisarEncomendas();
		break;
	
	case 3: 
		menuAceitaEncomendas();
		break;
	}
}

public static void menuAceitaEncomendas(){
	
	GereEncomendas enc = new GereEncomendas();
	String id = "";
		
		System.out.println("\n|----------Menu Gestor----------|\n"
				 + "1 - Aceitar todas as Encomendas\n"
				 + "2 - Aceitar uma Encomenda\n"
				 + "3 - Sair");
	
	switch(LerOpcao(3)){
	case 1: {
		enc.aceitaEncomendaGestor("todas", id, "aceite");
		System.out.println("\n\nEncomendas aprovadas com sucesso!!");
		gerelog.insereLog("Aprovou todas as encomendas", login1);
		break;
	}
	case 2: {
		String idencomenda = leString("Introduza o identificador da encomenda: ");
		do{
			if(enc.verIdEncomenda(idencomenda)){
				break;
			} else {
				System.out.println("O id encomenda "+ idencomenda +" não existe no nosso sistema tente novamente");
				idencomenda = leString("Introduza o identificador da encomenda: ");
			}
			
		}while(true);
		
		gerelog.insereLog("Aprovou uma encomenda", login1);
		enc.aceitaEncomendaGestor("gest", idencomenda, "aceite");
		System.out.println("\n\nEncomenda aceite com sucesso!");
		break;
	}
	case 3:{
		break;
	}
	}
}

public static void menuListarEncomendas(){
	
	GereEncomendas enc = new GereEncomendas();
	
	
	boolean bool = leBoolean("Pretende ver uma lista ordenada das encomendas? (s/n): ");
	String string = "";
	
	if(bool){
		boolean bool1 = leBoolean("Pretende por ordem ascendente(s) ou descendente(n)? (s/n): ");
		String ordenacao = "";
		if (bool1){
			ordenacao = "asc";
		} else {
			ordenacao = "desc";
		}
		boolean bool2 = leBoolean("Pretende listar ordenando por data de criação(s) ou pelo identificador(n)? (s/n): ");
		if (bool2){
			string = "e_datacriacao " + ordenacao;
		} else {
			string = "e_id " + ordenacao;
		}
	}
	
	System.out.println("\n        Encomendas       \n");
	
	String var = enc.listaEncomendas(string, login1);
    System.out.println(var);
	gerelog.insereLog("Listou encomendas", login1);
}

public static void menuPesquisarEncomendas(){

	GereEncomendas enc = new GereEncomendas();
	String string = uti.getTipoUti(login1);
	String tipo = "";
	String string1 = "";
	
	if(string.equals("Funcionario") || string.equals("Cliente")){
		
	System.out.println("\n|----------Menu Pesquisa Encomendas----------|\n"
			 + "1 - Por Data de Criação\n"
			 + "2 - Por Identificador\n"
			 + "3 - Por Estado\n"
			 + "4 - Sair");
	
	switch(LerOpcao(4)){
	case 1: 
	{
		tipo = "data";
		string1 = leString("Introduza a data pela qual deseja pesquisar a sua encomenda: ");
		gerelog.insereLog("Pesquisou encomenda por data", login1);
		break;
	}
	case 2: 
	{
		tipo = "identificador";
		string1 = leString("Introduza o identificador pelo qual deseja pesquisar a sua encomenda: ");
		gerelog.insereLog("Pesquisou encomenda pelo identificador", login1);
		break;
	}
	case 3:
	{
		tipo = "estado";
		string1 = leString("Introduza o estado pelo qual deseja pesquisar a encomenda:");
		gerelog.insereLog("Pesquisou encomedas por estado", login1);
		break;
	}

	case 4: {
		break;
	}
	}
	}else{
		System.out.println("\n|----------Menu Pesquisa Encomendas----------|\n"
				 + "1 - Por Data de Criação\n"
				 + "2 - Por Identificador\n"
				 + "3 - Por Estado\n"
				 + "4 - Por Cliente\n"
				 + "5 - Por Periodo Temporal\n"
				 + "5 - Sair");
		
		switch(LerOpcao(5)){
		case 1: 
		{
			tipo = "data";
			string1 = leString("Introduza a data pela qual deseja pesquisar a sua encomenda: ");
			gerelog.insereLog("Pesquisou encomenda por data", login1);
			break;
		}
		case 2: 
		{
			tipo = "identificador";
			string1 = leString("Introduza o identificador pelo qual deseja pesquisar a sua encomenda: ");
			gerelog.insereLog("Pesquisou encomenda pelo identificador", login1);
			break;
		}
		case 3:
		{
			tipo = "estado";
			string1 = leString("Introduza o estado pelo qual deseja pesquisar a encomenda:");
			gerelog.insereLog("Pesquisou encomedas por estado", login1);
			break;
		}
		case 4:
		{
			tipo = "cliente";
			string1 = leString("Introduza o login do cliente pelo qual deseja pesquisar a encomenda:");
			gerelog.insereLog("Pesquisou encomedas por cliente", login1);
			break;
		}

		case 5: {
			{
			tipo = "periodo";
			string1 = leString("Introduza o estado pelo qual deseja pesquisar a encomenda:");
			gerelog.insereLog("Pesquisou encomedas por estado", login1);
			break;
			}
		}
		case 6: {
			break;
		}
		}
	}

		String var = enc.pesquisaEncomenda(tipo, string1, login1);
        System.out.println(var);
	}
	
public static void notificacoesCliente(){
	
	System.out.println("\n|----------Notificacoes Cliente----------|\n");
	gerelog.insereLog("Verificou as suas notificações", login1);
}

public static void notificacoesFuncionarioArmazenista(){
	
	System.out.println("\n|----------Notificacoes Funcionario Armazenista----------|\n");
	gerelog.insereLog("Verificou as suas notificações", login1);
}

public static void notificacoesFuncionarioEstafeta(){
	
	System.out.println("\n|----------Notificacoes Funcionario Estafeta----------|\n");
	gerelog.insereLog("Verificou as suas notificações", login1);
}


public static void listarProdutos(){
	
	GereProdutos prod = new GereProdutos();
	boolean bool = leBoolean("Pretende ver uma lista ordenada dos produtos? (s/n): ");
	String string = "";
	
	if(bool){
		boolean bool1 = leBoolean("Pretende por ordem ascendente(s) ou descendente(n)? (s/n): ");
		String ordenacao = "";
		if (bool1){
			ordenacao = "asc";
		} else {
			ordenacao="desc";
		}
		boolean bool2 = leBoolean("Pretende listar ordenando por designacao(s) ou por categoria(n)? (s/n): ");
		if (bool2){
			string = "p_designacao " + ordenacao;
		} else {
			string = "p_categoria " + ordenacao;
		}
	}
	
	System.out.println("\n          Produtos          \n\n");
	
	String var = prod.listaProdutos(string, login1);
    System.out.println(var);
	
	gerelog.insereLog("Listou todas os produtos", login1);
}
	 	
public static void registroUtilizador(){	
	
	GereUtilizadores uti = new GereUtilizadores();
	
	System.out.println("\n|----------Registar Utilizador----------|\n"
						 + "1 - Gestor\n"
						 + "2 - Cliente\n"
						 + "3 - Funcionario\n"
						 + "4 - Sair");
		
	
		switch (LerOpcao(4)) {
		case 1: 
		registroGestor(uti);
		System.out.println("Utilizador inserido com sucesso!");
		break;

		case 2: 
		registroCliente(uti);
		System.out.println("Utilizador inserido com sucesso!");
		break;
		
		case 3: 
		registroFuncionario(uti);
		System.out.println("Utilizador inserido com sucesso!");
		break;
		
		case 4: 
		break;
		}
	
}

private static void registroFuncionario(GereUtilizadores aUti){
	
	System.out.println("Novo Funcionario");
	String nome = leString("Introduza o nome: ");
	String email = leString("Introduza o email: ");
	do {
		if(aUti.verificaEmail(email)){
			
			System.out.println("Este email já está registado.\n");
			email = leString("Introduza o email: ");
		
		}else{
			if(verificaEmail(email)){
				break;
			}else{
				System.out.println("O email tem de seguir o formato padrão(xxxxx@yyyyyy.zzz).\n");
				email = leString("Introduza o email: ");
			}
		}
	} while (true);
	
	String login = leString("Introduza o login: ");
	do {
		if(aUti.verificaLogin(login)){
			System.out.println("Já existe este login.\n");
			login = leString("Introduza o login: ");
		}else{
			break;
		}
	} while (true);
	
	String pass = leString("Introduza a pass: ");
	int numContr = leInt("Introduza o num. Contribuinte: ");
	do {
		if(String.valueOf(numContr).length() == 9){
			break;
		}else{
			System.out.println("Introduza um valor possivel para um numero de contribuinte(9 digitos)\n");
			numContr = leInt("Introduza o num. Contribuinte: ");
		}
	} while (true);
	do {
		if(aUti.verificaContribuinte(numContr, "Funcionario")){
			
			System.out.println("Num. Contribuinte já existe\n");
			numContr = leInt("Introduza o num. Contribuinte: ");
		}else{
			break;
		}
	} while (true);
	int numTele = leInt("Introduza o num. de Telefone: ");
	do {
		if(String.valueOf(numTele).length() == 9){
			break;
		}else{
			System.out.println("Introduza um valor possivel para um numero de telefone(9 digitos)\n");
			numTele = leInt("Introduza o num. de Telefone: ");
		}
	} while (true);
	
	String morada = leString("Introduza a morada: ");
	boolean especial = leBoolean("Introduza a especialização do funcionário(s/y - Armazenista n - Estafeta): ");
	funcionario = new Funcionarios(nome, login, pass, email, "Funcionario", numContr, numTele, morada, especial);
	aUti.insereUtilizador(funcionario, null, null);
	
}

private static void registroCliente(GereUtilizadores aUti){
	
	System.out.println("Novo Cliente");
	String nome = leString("Introduza o nome: ");
	String email = leString("Introduza o email: ");
	do {
		if(aUti.verificaEmail(email)){
			
			System.out.println("Este email já está registado.\n");
			email = leString("Introduza o email: ");
		
		} else {
			if(verificaEmail(email)){
				break;
			}else{
				System.out.println("O email tem de seguir o formato padrão(xxxxx@yyyyyy.zzz).\n");
				email = leString("Introduza o email: ");
			}
		}
	} while (true);
	String login = leString("Introduza o login: ");
	do {
		if(aUti.verificaLogin(login)){
			System.out.println("Já existe este login.\n");
			login = leString("Introduza o login: ");
		}else{
			break;
		}
	} while (true);
	
	String pass = leString("Introduza a pass: ");
	int numContr = leInt("Introduza o num. Contribuinte: ");
	do {
		if(String.valueOf(numContr).length() == 9){
			break;
		}else{
			System.out.println("Introduza um valor possivel para um numero de contribuinte(9 digitos)\n");
			numContr = leInt("Introduza o num. Contribuinte: ");
		}
	} while (true);
	do {
		if(aUti.verificaContribuinte(numContr, "Cliente")){
			
			System.out.println("Num. Contribuinte já existe\n");
			numContr = leInt("Introduza o num. Contribuinte: ");
		}else{
			break;
		}
	} while (true);
	int contactoTelefonico = leInt("Introduza o num. de Telefone: ");
	do {
		if(String.valueOf(contactoTelefonico).length() == 9){
			break;
		}else{
			System.out.println("Introduza um valor possivel para um numero de telefone(9 digitos)\n");
			contactoTelefonico = leInt("Introduza o num. de Telefone: ");
		}
	} while (true);
	String morada = leString("Introduza a morada: ");
	cliente = new Clientes(nome, login, pass, email, "Cliente", numContr, contactoTelefonico, morada);
	aUti.insereUtilizador(null, cliente, null);
}

private static void registroGestor(GereUtilizadores aUti){

	System.out.println("Novo Gestor");
	String nome = leString("Introduza o nome: ");
	String email = leString("Introduza o email: ");
	do {
		if(aUti.verificaEmail(email)){
			
			System.out.println("Este email já está registado.\n");
			email = leString("Introduza o email: ");
		
		} else {
			if(verificaEmail(email)){
				break;
			}else{
				System.out.println("O email tem de seguir o formato padrão(xxxxx@yyyyyy.zzz).\n");
				email = leString("Introduza o email: ");
			}
		}
	} while (true);
	
	String login = leString("Introduza o login: ");
	do {
		if(aUti.verificaLogin(login)){
			System.out.println("Já existe este login.\n");
			login = leString("Introduza o login: ");
		}else{
			break;
		}
	} while (true);
	
	String pass = leString("Introduza a pass: ");
	gestor = new Utilizadores(nome, login, pass, email, "Gestor");
	aUti.insereUtilizador(null, null, gestor);

}

private static boolean verificaEmail(String aEmail){
	if(aEmail.indexOf('@') > 0 && aEmail.indexOf('@') < aEmail.indexOf('.')-1 && aEmail.indexOf('.') < aEmail.length()-1){
		return true;
	}
	return false;
}

private static String leString(String aMsg){
	String str = "";
	do{
		System.out.print(aMsg);
		str = teclado.nextLine();
		if(str.equals("")){
			System.out.println("Não é permitido a inserção de valores nulos. ");
		} else {
			break;
		}
	} while (true);
	
	return str;
}

private static void insereProduto(){
	
	GereProdutos prod = new GereProdutos();
	
	System.out.println("\nNovo Produto\n");
	
	String designacao = leString("Introduza a designacao: ");
	String fabricante = leString("Introduza o fabricante: ");
	int peso = leInt("Introduza o peso: ");
	int preco = leInt("Introduza o preco: ");
	int codSKU = 0;
	do {
		int codSKU1 = prod.geraSKU();
		if(prod.verSKU(codSKU1)){
			codSKU = codSKU1;
		}else{
		break;
		}
	} while (true);
	int lote = leInt("Introduza o lote do produto: ");
	int stock = leInt("Introduza o stock do produto: ");
	Produto prod1 = new Produto(designacao, fabricante, peso, preco, codSKU, lote, stock);	
	System.out.println("\nProduto adicionado com sucesso!\n");
	prod.insereProduto(prod1, login1);
	gerelog.insereLog("Introduziu um produto", login1);
	
}

public static int LerOpcao(int aMenu){
	
	int opcao = leInt("\n\nOpção: ");

	do {
		if (opcao > 0 && opcao <= aMenu) {
			break;
		} else {
			opcao = leInt("Deverá apenas inserir números de 1 a " + aMenu + " por favor. \nOpção: ");
		}

	} while (true);
	
	return opcao;
}


private static int leInt(String aMsg){
	
	do{
		try {
			
			return Integer.parseInt(leString(aMsg));
			
		} catch (NumberFormatException nfe) {
			
			System.out.println("Introduza apenas valores inteiros.");
			//nfe.printStackTrace();
		}
		
	} while (true);
}

private static char leChar(String aMsg){
	
	System.out.print(aMsg);
	return teclado.next().charAt(0);
}

private static boolean leBoolean(String aMsg){
	
	do{
		switch (leChar(aMsg)) {
		case 'y':
			return true;
		case 'Y':
			return true;
		case 'S':
			return true;
		case 's':
			return true;
		case 'n':
			return false;
		case 'N':
			return false;
		default:
			System.out.println("\nIntroduza valores (y/n ou s/n)\n");
		}
	} while (true);
}

private static float leFloat(String aMsg){

	do{
		try {

			return Float.parseFloat(leString(aMsg));

		} catch (NumberFormatException nfe) {

			System.out.println("Introduza apenas números.");
			//nfe.printStackTrace();
		}

	} while (true);
}

private static long diferenca(){
	DateFormat dataform = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSSS");
	try {
		Date data1 = dataform.parse(dataInicio.substring(2));
		long mili1 = data1.getTime();
		Date data2 = dataform.parse(dataFim.substring(2));
		long mili2 = data2.getTime();
		return mili2-mili1;
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 0;
}

private static String calculaMiliis(){
	
	int seconds = (int) (diferenca() / 1000) % 60 ;
	int minutes = (int) ((diferenca() / (1000*60)) % 60);
	int hours   = (int) ((diferenca() / (1000*60*60)) % 24);
	return seconds + " segundos; " + minutes + " Minutos; " + hours + " Horas";
}

private static void tempo(){
	
	String [] array = new String [8];
	array [1] = "Segunda-Feira";
	array [2] = "Terça-Feira";
	array [3] = "Quarta-Feira";
	array [4] = "Quinta-Feira";
	array [5] = "Sexta-Feira";
	array [6] = "Sábado";
	array [7] = "Domingo";
	
	System.out.println("\n\nInício do processo: " + array[Integer.parseInt(dataInicio.substring(0,1))] + "; " + dataInicio.substring(2, dataFim.length()-5));
	System.out.println("Fim do processo:    " + array[Integer.parseInt(dataFim.substring(0,1))] + "; " + dataFim.substring(2, dataFim.length()-5));
	System.out.println("Tempo de execução:  " + diferenca() + " Milisegundos (" + calculaMiliis() + ")\n\n");
}

private static String dataHoje(){
	
	DateFormat dataform = new SimpleDateFormat("u yyyy/MM/dd HH:mm:ss:SSSS");
	return dataform.format(new Date(System.currentTimeMillis()));
}

}

