package mSystem;

import mLibrary.mContext;

public class $System {
	private CSP csp;
	private Encryption encryption;
	private License license;
	private OBJ obj;
	private Process process;
	private SQL sql;
	public Status Status;
	private Task task;
	private Util util;
	private Version version;

	public $System(mContext m$) {
		Status = new Status(m$);
		license = new License(m$);
		encryption = new Encryption(m$);
		process = new Process(m$);
		version = new Version(m$);
		csp = new CSP(m$);
		obj = new OBJ();
		sql = new SQL();
		util = new Util();
		task = new Task();
		
	}

	public CSP getCSP() {
		return csp;
	}

	public Encryption getEncryption() {
		return encryption;
	}

	public License getLicense() {
		return license;
	}

	public OBJ getOBJ() {
		return obj;
	}

	public Process getProcess() {
		return process;
	}

	public SQL getSQL() {
		return sql;
	}

	public Status getStatus() {
		return Status;
	}

	public Task getTask() {
		return task;
	}

	public Util getUtil() {
		return util;
	}

	public Version getVersion() {
		return version;
	}

	public void setEncryption(Encryption encryption) {
		this.encryption = encryption;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public void setStatus(Status status) {
		this.Status = status;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

}
