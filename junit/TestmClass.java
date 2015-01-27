import mLibrary.m$op;
import mLibrary.mClass;
import mLibrary.mVar;

public class TestmClass extends mClass {
	public Object concat(Object... _p) {

		mVar arg1 = m$.newVarParamRef(_p, 1, "arg1");
		mVar arg2 = m$.newVarParamRef(_p, 2, "arg2");

		return m$op.Concat(arg1.get(), arg2.get());
	}

	// concatByref(arg1,&arg2)
	public Object concatByRef(Object... _p) {
		mVar arg1 = m$.newVarParamRef(_p, 1, "arg1");
		mVar arg2 = m$.newVarParamRef(_p, 2, "arg2");

		arg2.set(m$op.Concat(arg1.get(), arg2.get()));

		return arg2.get();
	}

	public void doMethod(Object... _p) {
		mVar arg1 = m$.newVarParamRef(_p, 1, "arg1");

		m$.var("x").set(arg1.getSuppressedNullValue());

		arg1.set("modified");

		return;
	}

	public void doMethod2() {
		mVar arg1 = m$.var("arg1");
		m$.newVar(arg1);

		// m$.var("x").set(arg1.getSuppressedNullValue());
		arg1.set("modified");
		m$.restoreVarBlock(1);

		return;
	}

	// concat(arg1,arg2)
	public void main() {

		mVar a = m$.var("a");
		a.set("modified");
	}

	public Object returnArguments(Object... _p) {
		mVar arg1 = m$.newVarParamRef(_p, 1, "arg1", "default");
		System.out.println(m$.dumpLocal());
		mVar arg2 = m$.newVarParamRef(_p, 2, "arg2", "");

		Object buff = arg1.get();
		arg1.set(arg2.get());
		arg2.set(buff);

		return "hello world2!";
	}

	// swapArguments(arg1="default",arg2)
	public Object swapArguments(Object... _p) {
		mVar arg1 = m$.newVarParamRef(_p, 1, "arg1", "default");
		mVar arg2 = m$.newVarParamRef(_p, 2, "arg2", "");

		Object buff = arg1.get();
		arg1.set(arg2.get());
		arg2.set(buff);

		return 1;
	}

}
