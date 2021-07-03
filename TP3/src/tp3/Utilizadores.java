package tp3;
public class Utilizadores{
	/**
	 * 
	 */
	protected String nome;
	protected String login;
	protected String password;
	protected boolean estado;
	protected String email;
	protected String tipo;
	
	Utilizadores (String aNome, String aLogin, String aPass, String aMail, String aTipo){
		nome = aNome;
		login = aLogin;
		password = aPass;
		email = aMail;
		tipo = aTipo;
		estado = false;
	}
	Utilizadores (){
		
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getLogin() {
		return login;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	public String getPass() {
		return password;
	}
	
	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public String getEmail() {
		return email;
	}

	public String toString() {
		return "Login= " +login+"\n"
				+ "Tipo= " +tipo+ "\n";
	}
}
