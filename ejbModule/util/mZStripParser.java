package util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class mZStripParser {
	public static String zstrip(String target,String action,String remchars,String keepchars)
	{	
		mZStripParser zstripParser = new mZStripParser(target,action,remchars,keepchars);
		zstripParser.convertPattern();
		return zstripParser.performStrip();
	}
	private String _action;
	private String _codeAction;
	private String _javaPattern;

	private String _keepChars;
	
	private String _remChars;	
	
	private String _target;
		
	private Map <Character,String> patternTranslateTable;
	
	
	public mZStripParser(String target,String action,String remchars,String keepchars)
	{
		_target = target;
		_action = action; 
		_remChars = remchars;
		_keepChars = keepchars;
		
		_codeAction = "";
		
		_javaPattern = "";
		
		patternTranslateTable = new HashMap<Character, String>();
		patternTranslateTable.put('A',"a-zA-Z");
		patternTranslateTable.put('C',"\\p{Cntrl}");
		patternTranslateTable.put('E',"\\p{ASCII}");
		patternTranslateTable.put('L',"\\p{Lower}");
		patternTranslateTable.put('N',"\\d");
		patternTranslateTable.put('P'," \\p{Punct}");
		patternTranslateTable.put('U',"\\p{Upper}");
		patternTranslateTable.put('W'," \\t");
		//TODO tratar metacaracteres uteis ao idioma Russo (R,B,M) e Japones (ZFWCHARZ , ZHWKATAZ) 	
		/// minÃºsculas
		patternTranslateTable.put('a',"a-zA-Z");
		patternTranslateTable.put('c',"\\p{Cntrl}");
		patternTranslateTable.put('e',"\\p{ASCII}");
		patternTranslateTable.put('l',"\\p{Lower}");
		patternTranslateTable.put('n',"\\d");
		patternTranslateTable.put('p'," \\p{Punct}");
		patternTranslateTable.put('u',"\\p{Upper}");
		patternTranslateTable.put('w'," \\t");
		//
	}
	
	private void convertPattern() {

		//Code Actions
		/*
		 *  <	leading characters that match the masks.
			>	trailing characters that match the masks.
			*	all characters that match the masks.
			<>	leading and trailing characters that match the mask specification.
			=	repeating characters. When encountering a series of repeated characters, this code strips the duplicate characters leaving a single instance. This code only strips duplicate adjacent characters. Thus stripping â€œaâ€� from â€œaaaaaabcâ€� yields â€œabcâ€�, but stripping â€œaâ€� from â€œabacaâ€� returns the string â€œabacaâ€� unchanged.
		 * */

		String codeActionChars = "<>*=";
		int position=0;
		
		while (position<_action.length())
		{
			if (!codeActionChars.contains( _action.substring(position, position+1))) //
			{
				break;
			}
			position++;
		}
		if (position==0){
			throw new IllegalArgumentException("Code Action not found. action=".concat(_action));
		}
		_codeAction = _action.substring(0, position);
		if (position >= _action.length()) return;
		if ( _codeAction.contains("*") && (_codeAction.contains("<") || _codeAction.contains(">") ) ){
			throw new IllegalArgumentException("Code Action not found. action=".concat(_action));
		}
		//Mask Codes - CachÃ© style
		boolean negativeLookBehindStarted = false;
		String maskCodes = "",negativeLookBehind = "";
		while (position < _action.length()) {
			if ((_action.charAt(position)!='\'') && !patternTranslateTable.containsKey(_action.charAt(position))){	//maskCode invÃ¡lido
				throw new IllegalArgumentException("Invalid Code Action! - ".concat(_action));
			}
			negativeLookBehindStarted = (!negativeLookBehindStarted) ? (_action.charAt(position)=='\'') : negativeLookBehindStarted ;
			if (negativeLookBehindStarted){ 
				if (_action.charAt(position)!='\''){	//Uma vez iniciadas as negaÃ§Ãµes de codes, todos os codes subsequentes deveriam ser precedidos de ' (aspa simples - not).
					throw new IllegalArgumentException("Invalid Code Action! - ".concat(_action));
				}
				position++; // avanÃ§a negaÃ§Ã£o '	
				negativeLookBehind = negativeLookBehind.concat( _action.substring(position, position+1));
				position++; //prox.
				continue;
			}
			maskCodes = maskCodes.concat( _action.substring(position, position+1));
			position++; //prox.
		}
		//maskCodes
		if (!maskCodes.isEmpty()){
			_javaPattern = toJavaPattern(maskCodes);
		}
		if (!negativeLookBehind.isEmpty()){
			_javaPattern = _javaPattern.concat("(?<!".concat(toJavaPattern(negativeLookBehind)).concat(")"));
		}
	}
	
	private void normalizeAdditionalChars() {
		// Elimina ocorrÃªncias de caracteres que se conflitariam nos argumentos _remChars e _keepChars. Priorizando o _keepChars
		String tmpRemChars="";
		
		// remChars
		for (int i=0; i<_remChars.length();i++){
			if (_keepChars.contains(_remChars.substring(i, i+1))){
				continue;
			}
			tmpRemChars = tmpRemChars.concat(_remChars.substring(i, i+1));
		}
		_remChars = tmpRemChars;
	}
		
	private String performStrip() {
		normalizeAdditionalChars();
		//(?<![abc]) - Negative look behind
		if (!_keepChars.isEmpty()){
			_javaPattern = _javaPattern.concat("(?<![").concat(Pattern.quote(_keepChars)).concat("])");
		}
		//
		if (!_remChars.isEmpty()){
			if (_javaPattern.isEmpty()){ //remChars adicionais foram informados, no entanto somente Action Codes foram especificados (nenhum MaskCode presente).
				_javaPattern = "([".concat(Pattern.quote(_remChars)).concat("])");
			}else{
				_javaPattern = "(".concat(_javaPattern).concat("|[").concat(Pattern.quote(_remChars)).concat("])");
			}
		}
		//------------------------------------------------------------
		
		String pattern = _javaPattern;
		
		if (_codeAction.contains("<")){
			pattern = "^(".concat(_javaPattern).concat(")*");
		}
		if (_codeAction.contains(">")){
			if(_codeAction.contains("<")){ //Strip em caracteres a direita e esquerda
				pattern = "(".concat(pattern).concat("|(").concat(_javaPattern).concat(")*$)");
			}else{
				pattern = "(".concat(_javaPattern).concat(")*$");
			}	
		}
		//
		if (_codeAction.contains("=")){
			if (_codeAction.contains("<") || _codeAction.contains(">")){ //Primeiro realiza remoÃ§Ã£o dos limites inicio (^) e fim ($)
				_target = _target.replaceAll(pattern, "");
			}
			pattern = "(".concat(_javaPattern).concat(")\\1+");
			_target = _target.replaceAll(pattern, "$1");
		}else{
			_target = _target.replaceAll(pattern, "");
		}
		return _target;
	}


	private String toJavaPattern(String cachePattern) {
		String javaPattern = "";
		for (int i=0 ; i < cachePattern.length() ; i++)
		{ 
			if (patternTranslateTable.containsKey(cachePattern.charAt(i))) // Ã© um code pattern (metacaracter)
			{
					javaPattern = javaPattern.concat(patternTranslateTable.get(cachePattern.charAt(i)));
			}
		}
		return "[".concat(javaPattern).concat("]");
	}
}
