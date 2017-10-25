package fish.focus.uvms.commons.les.inmarsat.header;

import java.util.Date;

public class HeaderData {
	private final HeaderType type;
	private final int refno;
	private final HeaderDataPresentation dataPresentation;
	private final int failureReason;
	private final int deliveryAttempts;
	private final int satIdAndLesId;
	private final Date storedTime;
	private final int dataLength;
	private final int dnid;
	private final int memNo;
	private final int mesNo;

	public HeaderData(HeaderType type, int refno, HeaderDataPresentation dataPresentation, int failureReason,
			int deliveryAttempts, int satIdAndLesId, Date storedTime, int dataLength, int dnid, int memNo, int mesNo) {
		this.type = type;
		this.refno = refno;

		this.dataPresentation = dataPresentation;
		this.failureReason = failureReason;
		this.deliveryAttempts = deliveryAttempts;
		this.satIdAndLesId = satIdAndLesId;
		this.dataLength = dataLength;
		this.storedTime = storedTime;

		this.dnid = dnid;
		this.memNo = memNo;
		this.mesNo = mesNo;

	}

	public HeaderType getType() {
		return type;
	}

	public int getRefno() {
		return refno;
	}

	public HeaderDataPresentation getDataPresentation() {
		return dataPresentation;
	}

	public int getFailureReason() {
		return failureReason;
	}

	public int getDeliveryAttempts() {
		return deliveryAttempts;
	}

	public int getSatIdAndLesId() {
		return satIdAndLesId;
	}

	public Date getStoredTime() {
		return storedTime;
	}

	public int getDataLength() {
		return dataLength;
	}

	public int getDnid() {
		return dnid;
	}

	public int getMemNo() {
		return memNo;
	}

	public int getMesNo() {
		return mesNo;
	}
}
