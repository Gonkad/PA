package tp3;
import java.sql.Date;

public class Encomenda{
	/**
	 * 
	 */
	private String id, estado;
	private float custo;
	private Date dataCriacao, dataAceitacao, dataEntrega;     
	
	public Encomenda(String aID, float aCusto, String aEstado) {
		id = aID;	
		custo = aCusto;
		dataCriacao = dataHoje();
		estado = aEstado;
		
	}

	public Encomenda() {	
	}

	public String toString() {		
		return "Encomenda:\n"
				+ "Custo total = " +custo+ "\n"
				+ "Data de Criacao = " +dataCriacao+"\n"
				+ "Data de Aceitacao = " +dataAceitacao+ "\n"
		        + "Data de Entrega = " +dataEntrega+ "\n"
				+ "Estado = " +estado+ "\n\n";
	}

	public String getID() {
		return id;
	}



	public String getEstado() {
		return estado;
	}
	
	public float getCusto() {
		return custo;
	}
	
    private Date dataHoje(){
		
		return new Date(System.currentTimeMillis());
	}
    
	
	public Date getDataCriacao() {
		return dataCriacao;
	}
}
