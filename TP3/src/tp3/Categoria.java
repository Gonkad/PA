package tp3;
public class Categoria {
      /**
       * 
       */
	private String designacao, classificacao;
	  
	  
	public Categoria(String aDesignacao, String aClassificacao) {
		      
			designacao = aDesignacao;
			classificacao = aClassificacao;
			
		}
		
		public String getDesignacao() {
		return designacao;
	}

	public String getClassificacao() {
		return classificacao;
	}

		public Categoria() {	
		}

		public String toString() {		
			return "Categoria:\n"
					+ "Designacao  = " +designacao+ "\n"
					+ "Classificacao = " +classificacao+ "\n\n";
		}
		
	}


