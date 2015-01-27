package util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class mRegExConverter {
	public static String convertPattern(String cachePattern) 
	{	
		
		mRegExConverter mPattern = new mRegExConverter(cachePattern);
		String javaPattern = "";
		for (;;)
		{	
			if (mPattern._mumpsPattern.length()<=mPattern._position){
				break;
			}
			javaPattern = javaPattern.concat(mPattern.parsePatternAtom()) ;
		}	
		return javaPattern;
	}
	private static boolean isQuantifier(char charAt) {
		return Character.isDigit(charAt) || (charAt=='.')  ;
	}
	
	private static String toJavaQuantifier(String cacheQuantifier) {
		// Valor indefinido
		if (cacheQuantifier.equals("."))
		{
			return "*";
		}
		// Valores minimo e máximo
		if (cacheQuantifier.contains("."))
		{
			String [] partes = cacheQuantifier.split("\\.");
			String min = partes[0];
			if (min.length()==0)
			{
				min = "0";
			}
			String max = "";
			if (partes.length>1)
			{
				max = partes[1];
			}
			return "{".concat(min).concat(",").concat(max).concat("}");
		}
		// Valor absoluto
		return "{".concat(cacheQuantifier).concat("}");
	}
	
	
	private String _mumpsPattern;
	
	
	private int _position;
	
	private Map <Character,String> patternAtomTranslateTable;
	
	public mRegExConverter(String cachePattern )
	{
		_mumpsPattern = cachePattern; 
		_position = 0;
		
		patternAtomTranslateTable = new HashMap<Character, String>();
		patternAtomTranslateTable.put('A',"[a-zA-Z]");
		patternAtomTranslateTable.put('C',"\\p{Cntrl}");
		patternAtomTranslateTable.put('E',"\\p{ASCII}");
		patternAtomTranslateTable.put('L',"\\p{Lower}");
		patternAtomTranslateTable.put('N',"\\d");
		patternAtomTranslateTable.put('P',"[\\p{Punct} ]");
		patternAtomTranslateTable.put('U',"\\p{Upper}");
		//TODO tratar metacaracteres uteis ao idioma Russo (R,B,M) e Japones (ZFWCHARZ , ZHWKATAZ) 	
		
		/// minúsculas
		patternAtomTranslateTable.put('a',"[a-zA-Z]");
		patternAtomTranslateTable.put('c',"\\p{Cntrl}");
		patternAtomTranslateTable.put('e',"\\p{ASCII}");
		patternAtomTranslateTable.put('l',"\\p{Lower}");
		patternAtomTranslateTable.put('n',"\\d");
		patternAtomTranslateTable.put('p',"[\\p{Punct} ]");
		patternAtomTranslateTable.put('u',"\\p{Upper}");
	}
	
	// Devolve o code. Ex: 1AN -> 'AN', 2.4P ->'P' 
	private String getMPatternCode() {
		int posIni = _position;
		for (;;)
		{
			if (_mumpsPattern.length()<=_position){
				break;
			}
			if (
					isQuantifier(_mumpsPattern.charAt(_position)) ||
					(_mumpsPattern.charAt(_position)=='(') ||
					(_mumpsPattern.charAt(_position)==',') ||
					(_mumpsPattern.charAt(_position)==')')
				){
				break;
			}
			if (!patternAtomTranslateTable.containsKey(_mumpsPattern.charAt(_position))){
				throw new IllegalArgumentException("Code Pattern inválido encontrado: ".concat(_mumpsPattern.substring(_position, _position+1)).concat(". Na posição: ".concat(String.valueOf(_position))));
			}
			_position++;
		}
		return _mumpsPattern.substring(posIni,_position);
	}


	private String parsePatternAtom() {
		String patternAtom="";
		String patternQuantifier="";
		String patternCode="";
		
		//parse na Parte quantificadora
		while (isQuantifier(_mumpsPattern.charAt(_position)))
		{
			patternQuantifier = patternQuantifier.concat(String.valueOf(_mumpsPattern.charAt(_position)));
			_position++;
		} //fim da parte quantificadora do pattern (Ex. 1.2ANP -> '1.2')
		
		if (patternQuantifier.isEmpty()) {
			//Exeção: <SYNTAX> Parte quantificadora não informada;
			throw new IllegalArgumentException("Parte quantificadora do pattern esperada. Caracter encontrado: ".concat(String.valueOf(_mumpsPattern.charAt(_position))).concat(". Na posição: ".concat(String.valueOf(_position))));
		}
		
		//Expressão delimitada por parenteses.
		if (_mumpsPattern.charAt(_position)=='(')
		{
			_position++;
			// (  -> abre parenteses
			patternAtom = "(".concat(parsePatternAtom());
			// ,  -> virgulas
			for (;;)
			{
				if (_mumpsPattern.length()<=_position){
					break;
				}
				if (_mumpsPattern.charAt(_position)==',')
				{
					_position++;
					patternAtom = patternAtom.concat("|").concat(parsePatternAtom());
				}else if(isQuantifier(_mumpsPattern.charAt(_position))){
					patternAtom = patternAtom.concat(parsePatternAtom());
				}
				else
				{
					break;
				}
			}
			// )  -> fecha parenteses
			if (_mumpsPattern.length()<=_position){ //percorreu todo o mumpsPattern e não encontrou o Fecha parenteses )
				throw new IllegalArgumentException("Fecha parenteses não encontrado.");
			}
			if (_mumpsPattern.charAt(_position)==')'){
				patternAtom = patternAtom.concat(")");
			}
			else
			{
				throw new IllegalArgumentException("Fecha parenteses não encontrado.");
			}
			_position++;
			return patternAtom.concat(toJavaQuantifier(patternQuantifier));
		}
		//Literal
		if (_mumpsPattern.charAt(_position)=='"')
		{	
			_position++; //" pula aspa inicial
			int posIni = _position;
			while (_mumpsPattern.charAt(_position++)!='"');
			patternCode = _mumpsPattern.substring(posIni,_position-1);	
			patternCode = "(".concat(Pattern.quote(patternCode)).concat(")");
			patternAtom = patternCode ;
		}
		else
		{
			//Metacaracteres do Mumps.
			patternCode = getMPatternCode();
			patternAtom = toJavaPattern(patternCode);
		}
		return patternAtom.concat(toJavaQuantifier(patternQuantifier));
	}

	private String toJavaPattern(String cachePattern) {
		String javaPattern = "";
		for (int i=0 ; i < cachePattern.length() ; i++)
		{ 
			if (patternAtomTranslateTable.containsKey(cachePattern.charAt(i))) // é um code pattern (metacaracter)
			{
				if (javaPattern.equals(""))
				{
					javaPattern = patternAtomTranslateTable.get(cachePattern.charAt(i));
				}
				else
				{
					javaPattern = javaPattern.concat("|").concat(patternAtomTranslateTable.get(cachePattern.charAt(i)));
				}
			}
		}
		return "(".concat(javaPattern).concat(")");
	}
	
}
