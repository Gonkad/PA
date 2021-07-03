package tp3;
import java.sql.Date;

public class Funcionarios extends Utilizadores{
	
protected int numContribuinte;
protected int contactoTelefonico;
protected String morada;
protected boolean especializacao;
protected Date dataInicioAtividade;

	Funcionarios (String aNome, String aLogin, String aPass, String aMail, String aTipo, int aNumContribuinte, int aContactoTelefonico, String aMorada, boolean aEspecializacao){
		super(aNome,aLogin,aPass,aMail, aTipo);
		numContribuinte = aNumContribuinte;
		contactoTelefonico = aContactoTelefonico;
		morada = aMorada;
        especializacao = aEspecializacao;
        dataInicioAtividade = dataHoje();
	}

	Funcionarios(Utilizadores aUti, int aNumContribuinte, int aContactoTelefonico, String aMorada, boolean aEspecializacao) {
		super(aUti.nome, aUti.login, aUti.password, aUti.email, aUti.tipo);
		numContribuinte = aNumContribuinte;
		contactoTelefonico = aContactoTelefonico;
		morada = aMorada;
        especializacao = aEspecializacao;
        dataInicioAtividade = dataHoje();
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

	public boolean getEspecializacao() {
		return especializacao;
	}

	public Date getDataInicioAtividade() {
		return dataInicioAtividade;
	}
    private Date dataHoje(){
		
		return new Date(System.currentTimeMillis());
	}
}