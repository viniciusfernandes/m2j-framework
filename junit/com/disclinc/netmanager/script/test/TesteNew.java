package com.disclinc.netmanager.script.test;

//*****************************************************************************
//** TASC - ALPHALINC - MAC TesteNew
//** Innovatium Systems - Code Converter - v1.24
//** 2014-05-19 17:53:41
//*****************************************************************************

import mLibrary.*;

//<< ;#########################################################
//<< ;#     New all                                           #
//<< ;#########################################################
public class TesteNew extends mClass {

	// << labelNewAll
	public void labelNewAll() {
		// << New (zzz)
		mVar zzz = m$.var("zzz");
		m$.newVarExcept(zzz);
		// << Write
		// !!,"A partir daqui as variáveis estarão INVISIVEIS   -  Exceto as publicas (%) e a zzz  ",!,"===================================================",!
		m$.Cmd.Write("\n", "\n", "A partir daqui as variáveis estarão INVISIVEIS   -  Exceto as publicas (%) e a zzz  ",
				"\n", "===================================================", "\n");
		// << Set locVarAfterNew = $Get(locVar)
		mVar locVarAfterNew = m$.var("locVarAfterNew");
		locVarAfterNew.set(m$.Fnc.$get(m$.var("locVar")));
		// << Merge vetorLocalAfterNew = vetorLocal
		mVar vetorLocalAfterNew = m$.var("vetorLocalAfterNew");
		mVar vetorLocal = m$.var("vetorLocal");
		m$.Cmd.Merge(vetorLocalAfterNew, vetorLocal);
		// << Write !,"locVar (AfterNew) : >",locVarAfterNew,"<"
		m$.Cmd.Write("\n", "locVar (AfterNew) : >", locVarAfterNew.get(), "<");
		// << Write
		// !,"vetorLocal (AfterNew) : >",$get(vetorLocalAfterNew("xpto",34,"A")),"<"
		m$.Cmd.Write("\n", "vetorLocal (AfterNew) : >", m$.Fnc.$get(vetorLocalAfterNew.s$("xpto", 34, "A")), "<");
		// << Write !,"%varPublica(1,56) (AfterNew) : >",$G(%varPublica(1,56)),"<"
		m$.Cmd.Write("\n", "%varPublica(1,56) (AfterNew) : >", m$.Fnc.$get(m$.var("%varPublica", 1, 56)), "<");
		// << Write !,"zzz : >",zzz,"<"
		m$.Cmd.Write("\n", "zzz : >", zzz.get(), "<");
	}

	public void main() {
		// << Set locVar = "1.0"
		mVar locVar = m$.var("locVar");
		locVar.set("1.0");
		// << Set vetorLocal("xpto",34,"A")="xpto~34~A"
		mVar vetorLocal = m$.var("vetorLocal");
		vetorLocal.s$("xpto", 34, "A").set("xpto~34~A");
		// << Set vetorLocal("xpto",34,"B")="xpto~34~B"
		vetorLocal.s$("xpto", 34, "B").set("xpto~34~B");
		// << Set %varPublica(1,56)="eis o meu valor!"
		m$.var("%varPublica", 1, 56).set("eis o meu valor!");
		// << Set zzz = "valor Original"
		mVar zzz = m$.var("zzz");
		zzz.set("valor Original");
		// << ;
		// <<
		// << Write
		// "Inicialmente as variáveis estarão VISIVEIS     ",!,"===================================================",!
		m$.Cmd.Write("Inicialmente as variáveis estarão VISIVEIS     ", "\n",
				"===================================================", "\n");
		// << Write !,"locVar (Before New) : >",locVar,"<"
		m$.Cmd.Write("\n", "locVar (Before New) : >", locVar.get(), "<");
		// << Write
		// !,"vetorLocal(""xpto"",34,""A"") (Before New) : >",vetorLocal("xpto",34,"A"),"<"
		m$.Cmd.Write("\n", "vetorLocal(\"xpto\",34,\"A\") (Before New) : >", vetorLocal.s$("xpto", 34, "A").get(), "<");
		// << Write !,"%varPublica(1,56) : >",%varPublica(1,56),"<"
		m$.Cmd.Write("\n", "%varPublica(1,56) : >", m$.var("%varPublica", 1, 56).get(), "<");
		// << Write !,"zzz : >",zzz,"<"
		m$.Cmd.Write("\n", "zzz : >", zzz.get(), "<");
		// << Do labelNewAll
		m$.Cmd.Do("labelNewAll");
		// << ;Set retorno = $$sucessivosNews(.locVar)
		// <<
		// << Write
		// !!,"A partir daqui as variáveis voltam a estar visíveis",!,"===================================================",!
		m$.Cmd.Write("\n", "\n", "A partir daqui as variáveis voltam a estar visíveis", "\n",
				"===================================================", "\n");
		// << Write !,"locVar : >",locVar,"<"
		m$.Cmd.Write("\n", "locVar : >", locVar.get(), "<");
		// << Write
		// !,"vetorLocal(""xpto"",34,""A"")  : >",vetorLocal("xpto",34,"A"),"<"
		m$.Cmd.Write("\n", "vetorLocal(\"xpto\",34,\"A\")  : >", vetorLocal.s$("xpto", 34, "A").get(), "<");
		// << Write !,"%varPublica(1,56) : >",%varPublica(1,56),"<"
		m$.Cmd.Write("\n", "%varPublica(1,56) : >", m$.var("%varPublica", 1, 56).get(), "<");
		// << Write !,"ByRefVar - indefinida neste ponto",$Get(ByRefVar)
		m$.Cmd.Write("\n", "ByRefVar - indefinida neste ponto", m$.Fnc.$get(m$.var("ByRefVar")));
		// << Write !,"zzz : >",zzz,"<"
		m$.Cmd.Write("\n", "zzz : >", zzz.get(), "<");
		// <<
		// << Quit
		return;
	}

	// << sucessivosNews(ByRefVar)
	public Object sucessivosNews(Object... _p) {
		mVar ByRefVar = m$.newVarRef("ByRefVar", (((_p != null) && (_p.length >= 1)) ? _p[0] : null));
		// << /*
		// << new ByRefVar
		// << Set ByRefVar = locVar
		// << */
		// << Write !!,"Inicio do método sucessivosNews()"
		m$.Cmd.Write("\n", "\n", "Inicio do método sucessivosNews()");
		// << Write !,"  Valor de ByRefVar: ",ByRefVar
		m$.Cmd.Write("\n", "  Valor de ByRefVar: ", ByRefVar.get());
		// << Write !,"  Valor de locVar  : ",locVar
		m$.Cmd.Write("\n", "  Valor de locVar  : ", m$.var("locVar").get());
		// << ;
		// << Set ByRefVar =
		// ByRefVar_" ->  Valor atribuído (append) dentro do label sucessivosNews"
		ByRefVar.set(mOperation.Concat(ByRefVar.get(), " ->  Valor atribuído (append) dentro do label sucessivosNews"));
		// << New locVar,ByRefVar
		mVar locVar = m$.var("locVar");
		ByRefVar = m$.var("ByRefVar");
		m$.newVar(locVar, ByRefVar);
		// << Set locVar = "2.0"
		locVar.set("2.0");
		// << Write !,"A partir daqui -> New locVar,ByRefVar"
		m$.Cmd.Write("\n", "A partir daqui -> New locVar,ByRefVar");
		// << Write !,"  Valor de ByRefVar: ",$Get(ByRefVar)
		m$.Cmd.Write("\n", "  Valor de ByRefVar: ", m$.Fnc.$get(ByRefVar));
		// << Write !,"  Valor de locVar  : ",locVar
		m$.Cmd.Write("\n", "  Valor de locVar  : ", locVar.get());
		// << New locVar
		m$.newVar(locVar);
		// << Set locVar = "3.0"
		locVar.set("3.0");
		// << Write !,"A partir daqui -> New locVar"
		m$.Cmd.Write("\n", "A partir daqui -> New locVar");
		// << Write !,"  Valor de locVar  : ",locVar
		m$.Cmd.Write("\n", "  Valor de locVar  : ", locVar.get());
		// <<
		// << Quit "retorno do método sucessivosNews()"
		return "retorno do método sucessivosNews()";
	}

}
