package mSystem;

//*****************************************************************************
//** TASC - ALPHALINC - CLASS Util
//** Innovatium Systems - Code Converter - v1.40
//** 2014-09-30 13:08:47
//*****************************************************************************

import mLibrary.*;

//<< Include %sySite
//<< 
//<< /// The <class>%SYSTEM.Util</class> class provides an interface for managing
//<< /// Cach&eacute; utility functions.<br>
//<< /// <p>You can use it via the special <b>$system</b> object:<br>
//<< /// <p>
//<< /// <p>You can call help to get a list of all entrypoints:<br>
//<< /// <p>
//<< /// Do $system.Util.Help()
//<< Class %SYSTEM.Util Extends Help [ Abstract, ProcedureBlock ]
public class Util extends mClass {

  //<< }
  //<< 
  //<< /// Returns the location of the binaries directory, where executables, DLL's, shared libraries, scripts, etc are kept.
  //<< ClassMethod BinaryDirectory() As %String
  public Object BinaryDirectory() {
    m$.newVar();
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// Check if this job could be blocked by the specific switch represented by 'SwitchNumber'.<br>
  //<< /// Return 1 if the job could pass this check (not blocked), return 0 if this job will be blocked.<br>
  //<< /// For switch 10 and 11, if the job could not pass this test it also returns the PID of the job who set this switch in the '0,PID' format.
  //<< ClassMethod CheckSwitch(SwitchNumber As %Integer) As %Integer
  public Object CheckSwitch(Object ... _p) {
    mVar SwitchNumber = m$.newVarParamRef(_p,1,"SwitchNumber");
    m$.newVarExcept(SwitchNumber);
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// Creates and returns a globally unique identifier.
  //<< /// <br>
  //<< /// A GUID is a 16 byte (128 bit) globally unique identifier.
  //<< ClassMethod CreateGUID() As %String
  public Object CreateGUID() {
    m$.newVar();
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// Returns the value of an environment variable.
  //<< ClassMethod GetEnviron(VariableName As %String) As %String
  public Object GetEnviron(Object ... _p) {
    mVar VariableName = m$.newVarParamRef(_p,1,"VariableName");
    m$.newVarExcept(VariableName);
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// Returns the priority of the current process or another process.
  //<< /// <br>
  //<< ClassMethod GetPrio(pid As %Integer = "") As %Integer
  public Object GetPrio(Object ... _p) {
    mVar pid = m$.newVarParamRef(_p,1,"pid","");
    m$.newVarExcept(pid);
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// When the SwitchNumber is not specified, the $SYSTEM.Util.GetSwitch() gets the value of all system switches.<br>
  //<< /// This returns the value of all switches.<br><br>
  //<< /// When the SwitchNumber is specified (0 to 31), the $SYSTEM.Util.GetSwitch(SwitchNumber) gets the value of a specific switch represented by SwitchNumber.<br>
  //<< /// This returns 1 if this switch is set, returns 0 if it is cleared.<br>
  //<< /// For switch 10 and 11, it also returns the PID of the job who set this switch in the '1,PID' format.<br><br>
  //<< /// The system switches is represented by bit in this value, and it started from switch 0 ended with switch 31.<br>
  //<< /// For example:<br>
  //<< /// Switch 0 -> 1<br>
  //<< /// Switch 1 -> 2<br>
  //<< /// Switch 2 -> 4<br>
  //<< /// ...<br>
  //<< /// Switch 31 -> 0x80000000<br><br>
  //<< /// Switch 0 to 7 are not used by system and are resvered for user to use.<br>
  //<< /// Switch 8 (0x100)   : Inhibits responding to network request for DCP, DDP and DTM. It won't block ECP traffic.<br>
  //<< /// Switch 9 (0x200)   : Inhibits new network signons, this only apply for DCP.<br>
  //<< /// Switch 10(0x400)   : Inhibits all global and lock access except the job setting this switch.<br>
  //<< /// Switch 11(0x800)   : Inhibits all global and lock access except the job setting this switch. This overrides switch 10 and is reserved for use by the system (should only be set by the system).<br>
  //<< /// Switch 12(0x1000)  : Inhibits new users to signon the system.<br>
  //<< /// Switch 13(0x2000)  : Inhibits sets, kills, and zsaves.<br>
  //<< /// Switch 14(0x4000)  : Inhibits access to globals and routines.<br>
  //<< /// Switch 15(0x8000)  : Allow network references from peers, even if switch 10,13, or 14 would normally prevent the access.<br>
  //<< /// Switch 16(0x10000) : Used in ^SHUTDOWN only.<br>
  //<< /// Switch 17(0x20000) : Used internally by system to skip waiting in journal synch.<br>
  //<< /// Switch 18(0x40000) : Used internally by system to disable pausing processes in gblkrd() if the queue for a block gets too long.<br>
  //<< /// Switch 19(0x80000) : Inhibits TSTART.<br>
  //<< /// Switch 20(0x100000): Disable dead job cleaning.<br>
  //<< ClassMethod GetSwitch(SwitchNumber As %Integer) As %Integer
  public Object GetSwitch(Object ... _p) {
    mVar SwitchNumber = m$.newVarParamRef(_p,1,"SwitchNumber");
    m$.newVarExcept(SwitchNumber);
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// Returns a list of the number of global buffers for each buffer size.
  //<< /// <br>
  //<< ClassMethod GlobalBuffers() As %List
  public Object GlobalBuffers() {
    m$.newVar();
    return null;
  //<< {
  }

  //<< {
  //<< 
  //<< /*
  //<< ---------------------Documentation------------------------------------
  //<< SML738 07/14/06, Simon Li,Add class document for SetSwitch/GetSwitch/CheckSwitch methods.
  //<< PWC923 11/03/04 Peter Cudhea, Created %Library.Function, %Library.Utility,
  //<< and %Sys.System classes.
  //<< CFL1114 07/30/04, Carlos Lopes, enhance Replace method
  //<< GK333  07/29/04, Garen Kotikian, add Cache instance GUID.
  //<< SML471 07/09/04, Simon Li, Move CleanDeadJob metheod to here.
  //<< CFL1098 07/06/04, Carlos Lopes, add Replace method
  //<< LFT1235 6/04/04, Laura Tillem, add TempDirectory
  //<< SAP177  5/11/04, Steve Pettibone, modify setbatch to SetBatch, change logic
  //<< SAP176  5/11/04, Steve Pettibone, document methods and return codes
  //<< SAP168  5/10/04, Steve Pettibone, add severity to WriteToConsoleLog
  //<< HYY883 03/04/04, Huayong Yang, add APIs for setting/clearing switches
  //<< LRS714 08/20/03, Lee Smith, implement NullDevice method
  //<< STC464 08/05/03, Steve Clay, Add WriteToConsoleLog
  //<< LRS671 03/25/03, Lee Smith, implement GetEnviron method
  //<< LRS626 08/30/02, Lee Smith, initial version with InstallDirectory,
  //<< ManagerDirectory, and BinaryDirectory methods
  //<< -------------------End Documentation----------------------------------
  //<< */
  //<< /// Returns the location of the installation directory, i.e. that which is displayed by ccontrol on Unix and VMS, where the .cpf configuration file is kept.
  //<< ClassMethod InstallDirectory() As %String
  public Object InstallDirectory() {
    m$.newVar();
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// Sets the job priority increment/decrement to the specified number.
  //<< /// <br>
  //<< /// The priority of a jobbed process = priority of parent + job priority
  //<< /// <br>
  //<< /// The new jobprio value is returned
  //<< ClassMethod JobPrio(jprio As %Integer) As %Integer
  public Object JobPrio(Object ... _p) {
    mVar jprio = m$.newVarParamRef(_p,1,"jprio");
    m$.newVarExcept(jprio);
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// Returns the location of the manager directory, where the CACHESYS database and runtime files are kept.
  //<< ClassMethod ManagerDirectory() As %String
  public Object ManagerDirectory() {
    m$.newVar();
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// Returns the number of CPUs on the system.
  //<< /// <br>
  //<< ClassMethod NumberOfCPUs() As %Integer
  public Object NumberOfCPUs() {
    m$.newVar();
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// Sets the calling process or one identified by pid to be a batch process.
  //<< /// <br>
  //<< /// A batch process yields its turn to non-batch processes.<br>
  //<< /// For example, if multiple processes are waiting for access to global buffers,
  //<< /// non-batch processes have priority over batch processes.
  //<< /// <br>
  //<< /// flag - 1 - set to batch mode, 0 - set to non-batch mode
  //<< /// <br>
  //<< /// pid - pid to set to batch mode. Null argument sets calling process.
  //<< /// <br>
  //<< /// Returns the old value of the batch flag, or -1 if error
  //<< ClassMethod SetBatch(flag As %Integer, pid As %Integer = "") As %Integer
  public Object SetBatch(Object ... _p) {
    mVar flag = m$.newVarParamRef(_p,1,"flag");
    mVar pid = m$.newVarParamRef(_p,2,"pid","");
    m$.newVarExcept(flag,pid);
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// Adjusts the priority of the current process or another process up or down by delta amount.
  //<< /// <br>
  //<< /// The new priority is returned
  //<< ClassMethod SetPrio(delta As %Integer, pid As %Integer = "") As %String
  public Object SetPrio(Object ... _p) {
    mVar delta = m$.newVarParamRef(_p,1,"delta");
    mVar pid = m$.newVarParamRef(_p,2,"pid","");
    m$.newVarExcept(delta,pid);
    return null;
  //<< {
  }

  //<< }
  //<< 
  //<< /// When one parameter is specified, the $SYSTEM.Util.SetSwitch(SwitchValue) sets the value for all system switches with 'SwitchValue'.<br>
  //<< /// This returns the value of all original switches.<br>
  //<< /// This is not recommended to be used by users.<br><br>
  //<< /// When two parameters are specified, the $SYSTEM.Util.SetSwitch(SwitchNumber,SwitchValue) sets the value for a specific switch 'SwitchNumber' with 'SwitchValue'.<br>
  //<< /// This returns the original switch value.<br>
  //<< ClassMethod SetSwitch(SwitchNumber As %Integer, SwitchValue As %Integer) As %Integer
  public Object SetSwitch(Object ... _p) {
    mVar SwitchNumber = m$.newVarParamRef(_p,1,"SwitchNumber");
    mVar SwitchValue = m$.newVarParamRef(_p,2,"SwitchValue");
    m$.newVarExcept(SwitchNumber,SwitchValue);
    return null;
  //<< {
  }

//<< }
//<< 
//<< }
}
