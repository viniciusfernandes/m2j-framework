package ORM;

/**
 * <b>ORM - Object Relational Mapping</b>
 * <p>
 * Entidade de persistência dinâmica de objetos com métodos de acesso multidimensional aos dados (MDA).
 * <p>
 * @author Innovatium Systems (mosselaar)
 */
public class mEntityMDA extends mEntity {

	private String dataDelimiter = "~";
	
	public mEntityMDA(String className) {
		super(className);
	}

	/**
	 * Obtém valor do delimitador do registro
	 * (método legado de acesso multidimensional - não utilizar em novas implementações)
	 * @return valor do delimitador
	 */
	public String getDataDelimiter() {
		return null;
	}

	/**
	 * Obtém valor de uma propriedade do registro conforme índice posicional (no formato M)
	 * (método legado de acesso multidimensional - não utilizar em novas implementações)
	 * @param index índice posicional
	 * @return valor da propriedade
	 */
	public Object getDataPiece(int index) {
		return null;
	}

	/**
	 * Obtém valor(es) de propriedade(s) do registro conforme intervalo posicional (no formato M)
	 * (método legado de acesso multidimensional - não utilizar em novas implementações)
	 * @param from índice posicional de início
	 * @param to índice posicional de término
	 * @return valor(es) da(s) propriedade(s) (string com delimitador caso houver mais de um valor)
	 */
	public Object getDataPiece(int from, int to) {
		return null;
	}

	/**
	 * Obtém dados do registro em formato string (valores delimitados)
	 * (método legado de acesso multidimensional - não utilizar em novas implementações)
	 * @return registro em formato string
	 */
	public String getDataRecord() {
		return null;
	}

	/**
	 * Inicializa valor do delimitador do registro
	 * (método legado de acesso multidimensional - não utilizar em novas implementações)
	 * @param dataDelimiter valor do delimitador
	 */
	public void setDataDelimiter(String dataDelimiter) {
	}

	/**
	 * Atribui valor(es) de propriedade(s) do registro conforme intervalo posicional (no formato M)
	 * (método legado - não utilizar em novas implementações)
	 * @param from índice posicional de início
	 * @param to índice posicional de término
	 * @param dataPiece valor(es) da(s) propriedade(s) (string com delimitador caso houver mais de um valor)
	 */
	public void setDataPiece(int from, int to, Object dataPiece) {
	}

	/**
	 * Atribui valor de uma propriedade do registro conforme ínice posicional (no formato M)
	 * (método legado de acesso multidimensional - não utilizar em novas implementações)
	 * @param index índice posicional
	 * @param dataPiece valor da propriedade
	 */
	public void setDataPiece(int index, Object dataPiece) {
	}

	/**
	 * Atribui registro em formato string (valores delimitados)
	 * (método legado de acesso multidimensional - não utilizar em novas implementações)
	 * @param dataRecord registro em formato string
	 */
	public void setDataRecord(String dataRecord) {
	}

}
