package ORM;

import util.mFunctionUtil;
import mLibrary.mContext;
import mLibrary.mFunction;

public class mNameMapping {

	public static void initialize(mContext m$) {
		mNameMapping nameMap = new mNameMapping(m$,null);
		if (nameMap.RDBMS == "ORACLE") {
			String ReservedWords = "Number,State,Start,Type,Rank,DateTime,Session,Class,Lock,View,Comment,Index,Repeat,Each,Operator,Reverse,Parameters,Share,Access,Minus,Row,Bin,Successful,Repeat,Operation,Share,Modify,Rowid,Search";
			String[] ReservedWordsArray = ReservedWords.split("\\,",-1);
			for (int i=0;(i<ReservedWordsArray.length);i++) {
				nameMap.setMapName(ReservedWordsArray[i],ReservedWordsArray[i]+"_");
			}
		}
	}

	private Boolean caseSensitive = false;
	
	private mContext m$;
	
	private String mapName;
	
	private Integer maxLength = 30;
	
	private String name;
	
	private String RDBMS = "ORACLE";
	
	private Boolean subObject;

	public mNameMapping (mContext m$, String name) {
		this(m$,name,false);
	}
	
	public mNameMapping (mContext m$, String name, Boolean subObject) {
		this.m$ = m$;
		this.name = name;
		this.subObject = subObject;
		if (name == null) {
			this.setMapName(null);
			return;
		}
		this.setMapName(getMapName(name));
		if ((this.getMapName() != null) && (this.getMapName().length() <= this.maxLength)) {
			return;
		}
		if (this.subObject) {
			this.setMapName(name);
			if ((this.getMapName() != null) && (this.getMapName().length() <= this.maxLength)) {
				return;
			}
		}
		if (name.length() <= this.maxLength) {
			this.setMapName(name);
		}
		else {
			this.setMapName(null);
			String mapName = name.replace('.','_');
			for (int i=mFunctionUtil.integerConverter(mFunction.$length(mapName,"_"));(i>=1);i--) {
				int x = 0;
				String mapNamePiece = String.valueOf(mFunction.$piece(mapName,"_",i));
				while (true) {
					if (mapNamePiece.length() <= 5) {
						break;
					}
					if (mapNamePiece.length() <= (3+mapName.length()-this.maxLength)) {
						break;
					}
					if (mapName.length()-this.maxLength > 0) {
						mapNamePiece = mapNamePiece.substring(0,mapNamePiece.length()-(mapName.length()-this.maxLength)-((x>0)?Integer.toString(x).length():0));
					}
					else {
						mapNamePiece = mapNamePiece.substring(0,mapNamePiece.length()-((x>0)?Integer.toString(x).length():0));
					}
					if (mapNamePiece.length() < 3) {
						break;
					}
					mapName = ((i>1)?mFunctionUtil.toString(mFunction.$piece(mapName,"_",1,i-1))+"_":"")+mapNamePiece+((x>0)?Integer.toString(x):"")+((i<mFunctionUtil.integerConverter(mFunction.$length(mapName,"_")))?"_"+mFunctionUtil.toString(mFunction.$piece(mapName,"_",i+1,999)):"");
					if (mapName.length() <= this.maxLength) {
						if (getName(mapName) == null) {
							this.setMapName(mapName);
							break;
						}
					}
					if (++x > 999) {
						break;
					}
				}
				if (this.getMapName() != null) {
					break;
				}
			}
		}
		if (this.getMapName() != null) {
			setMapName(name,this.getMapName());
		}
	}
	
	public String getMapName() {
		return mapName;
	}
	
	public void setMapName(String mapName) {
		if (mapName == null) {
			this.mapName = null;
		}
		else {
			this.mapName = mapName.replace('.','_');
		}
	}
	
	
	private String getMapName(String name) {
		Object mapObject;
		if (!this.caseSensitive) {
			mapObject = this.m$.var("^WWWNAMEMAPPING",this.RDBMS,1,name.toUpperCase()).getValue();
		}
		else {
			mapObject = this.m$.var("^WWWNAMEMAPPING",this.RDBMS,1,name).getValue();
		}
		if (mapObject == null) {
			return null;
		}
		return mFunctionUtil.toString(mapObject).replace('.','_');
	}

	private String getName(String mapName) {
		Object nameObject;
		if (!this.caseSensitive) {
			nameObject = this.m$.var("^WWWNAMEMAPPING",this.RDBMS,2,mapName.toUpperCase()).getValue();
		}
		else {
			nameObject = this.m$.var("^WWWNAMEMAPPING",this.RDBMS,2,mapName).getValue();
		}
		if (nameObject == null) {
			return null;
		}
		return mFunctionUtil.toString(nameObject);
	}

	private void setMapName(String name, String mapName) {
		mapName = mapName.replace('.','_');
		if (!this.caseSensitive) {
			this.m$.var("^WWWNAMEMAPPING",this.RDBMS,1,name.toUpperCase()).set(mapName);
		}
		else {
			this.m$.var("^WWWNAMEMAPPING",this.RDBMS,1,name).set(mapName);
		}
		if (!this.caseSensitive) {
			this.m$.var("^WWWNAMEMAPPING",this.RDBMS,2,mapName.toUpperCase()).set(name);
		}
		else {
			this.m$.var("^WWWNAMEMAPPING",this.RDBMS,2,mapName).set(name);
		}
	}

}
