package $Library;

import mLibrary.mClass;

public class GlobalCharacterStream extends mClass  {
	StringBuffer data;
	public Object $New(Object... args) {
		data = new StringBuffer();
		return this;
	}
	public Object OutputToDevice(Object... args){
		m$.Cmd.Write(data.toString());
		return true;
	}
	public Object Write(Object... args){
		
		return true;
	}	
	public Object WriteLine(Object string){
		data.append(string+"\n");
		return true;
	}	
}
