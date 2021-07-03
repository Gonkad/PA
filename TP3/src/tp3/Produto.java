package tp3;
import java.sql.Date;

public class Produto{
	/**
	 *  
	 */
	private String designacao, fabricante;
	private int peso, SKU, lote, preco, stock;    
	private Date dataProducao, dataValidade;
	
	
	public Produto(String aDesignacao, String aFabricante, int aPeso, int aPreco, int aSKU, int aLote, int aStock) {
		designacao = aDesignacao;
		fabricante = aFabricante;
		peso = aPeso;
		preco = aPreco;
		SKU = aSKU;
		lote = aLote;
		dataProducao = dataHoje();
        dataValidade = dataHoje();
        stock = aStock;
	}
	
	
	public String getDesignacao() {
		return designacao;
	}

	public String getFabricante() {
		return fabricante;
	}

	public int getPeso() {
		return peso;
	}

	public int getSKU() {
		return SKU;
	}

	public int getLote() {
		return lote;
	}

	public int getPreco() {
		return preco;
	}

	public int getStock() {
		return stock;
	}

	public Date getDataProducao() {
		return dataProducao;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public Produto() {	
	}

    private Date dataHoje(){
		
		return new Date(System.currentTimeMillis());
	}
    
	public String toString() {		
		return "Produto:\n"
				+ "Designacao = " +designacao+ "\n"
				+ "Fabricante = " +fabricante+ "\n"
				+ "Codigo SKU = " +SKU+"\n"
				+ "Lote= " +lote+ "\n"
				+ "Data de Producao = " +dataProducao+ "\n"
				+ "Data de Validade = " +dataValidade+ "\n"
		        + "Quantidade em Stock = " +stock+ "\n\n";
	}


	public String getCategoria() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

