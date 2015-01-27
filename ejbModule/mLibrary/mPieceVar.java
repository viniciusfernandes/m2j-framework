package mLibrary;

public class mPieceVar extends mVar {
	private String delimiter;
	private Integer position;
	private Integer to;

	public mPieceVar(mVar var, Object delimiter, Object position) {
		super(var.getSubs(), var.getmData());
		vMPieceVar(var, delimiter, position,null);
	}
	
	public mPieceVar(mVar var, Object delimiter, Object position, Object to) {
		super(var.getSubs(), var.getmData());
		vMPieceVar(var, delimiter, position,to);
	}
	
	public void set(Object value) {
		if (value instanceof mVar) {
			value = ((mVar) value).get();
		}
		super.set(mFunction.$setpiece(this.getValue(), delimiter, position, value, to));
	}

	public void vMPieceVar(mVar var, Object delimiter, Object position,Object to) {
		this.delimiter = String.valueOf(delimiter);
		if (position instanceof Integer) {
			this.position = (Integer) position;
		} else if (position instanceof Double) {
			this.position = ((Double) position).intValue();
		} else {
			if (position instanceof String) {
				this.position = Double.valueOf(position.toString()).intValue();
				try {
				} catch (NumberFormatException e) {
				}
			} else {
				this.position = null;

			}
		}
		if (this.position == null || this.position < 0) {
			throw new IllegalArgumentException(
					"The position paramenter must greater than zero");
		}

		if (to != null) {
			if (to instanceof Integer) {
				this.to = (Integer) to;
			} else if (to instanceof Double) {
				this.to = ((Double) to).intValue();
			} else {
				if (to instanceof String) {
					this.to = Double.valueOf(position.toString()).intValue();

				} else {

					this.to = null;
				}

			}
		} else {
			this.to = null;

		}

	}
}
