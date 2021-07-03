package tp3;
public class Clientes extends Utilizadores{
	
protected int numContribuinte;
protected int contactoTelefonico;
protected String morada;

	Clientes (String aNome, String aLogin, String aPass, String aMail, String aTipo, int aNumContribuinte, int aContactoTelefonico, String aMorada){
		super(aNome,aLogin,aPass,aMail,aTipo);
		numContribuinte = aNumContribuinte;
		contactoTelefonico = aContactoTelefonico;
		morada = aMorada;
	}

	Clientes (Utilizadores aUti, int aNumContribuinte, int aContactoTelefonico, String aMorada) {
		super(aUti.nome, aUti.login, aUti.password, aUti.email, aUti.tipo);
		numContribuinte = aNumContribuinte;
		contactoTelefonico = aContactoTelefonico;
		morada = aMorada;
	}
	
	public int getNumContribuinte() {
		return numContribuinte;
	}

	public int getContactoTelefonico() {
		return contactoTelefonico;
	}

	public String getMorada() {
		return morada;
	}
}