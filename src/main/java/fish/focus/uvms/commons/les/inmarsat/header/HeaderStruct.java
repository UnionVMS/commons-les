package fish.focus.uvms.commons.les.inmarsat.header;

import java.util.LinkedHashMap;

/**
 *
 * Header structure for the typse
 <div>
 <table>
 <thead><tr><th>Field</th><th>DNID</th><th>DNID_MSG</th><th>MSG</th><th>PDN</th><th>NDN</th></tr></thead><tbody>
 <tr><td>START_OF_HEADER</td><td>1</td><td>1</td><td>1</td><td>1</td><td>1</td></tr>
 <tr><td>LEAD_TEXT</td><td>3</td><td>3</td><td>3</td><td>3</td><td>3</td></tr>
 <tr><td>HEADER_TYPE</td><td>1</td><td>1</td><td>1</td><td>1</td><td>1</td></tr>
 <tr><td>HEADER_LENGTH</td><td>1</td><td>1</td><td>1</td><td>1</td><td>1</td></tr>
 <tr><td>MSG_REF_NO</td><td>4</td><td>4</td><td>4</td><td>4</td><td>4</td></tr>
 <tr><td>PRESENTATION</td><td>1</td><td>1</td><td>1</td><td>0</td><td>0</td></tr>
 <tr><td>FAILURE_REASON</td><td>0</td><td>0</td><td>0</td><td>0</td><td>1</td></tr>
 <tr><td>DELIVERY_ATTEMPTS</td><td>0</td><td>0</td><td>0</td><td>1</td><td>1</td></tr>
 <tr><td>LES_ID</td><td>1</td><td>1</td><td>1</td><td>0</td><td>0</td></tr>
 <tr><td>DATA_LENGTH</td><td>2</td><td>2</td><td>2</td><td>0</td><td>0</td></tr>
 <tr><td>STORED_TIME</td><td>4</td><td>4</td><td>4</td><td>4</td><td>4</td></tr>
 <tr><td>DNID</td><td>2</td><td>2</td><td>0</td><td>0</td><td>0</td></tr>
 <tr><td>MEMBER_NO</td><td>1</td><td>0</td><td>0</td><td>0</td><td>0</td></tr>
 <tr><td>MES_MOB_NO</td><td>0</td><td>4</td><td>4</td><td>4</td><td>4</td></tr>
 <tr><td>END_OF_HEADER</td><td>1</td><td>1</td><td>1</td><td>1</td><td>1</td></tr>
 <tr><td>Summa</td><td>22</td><td>25</td><td>23</td><td>20</td><td>21</td></tr>
 </tbody></table>
 </div>

 */
public class HeaderStruct {
	public static final int POS_START_OF_HEADER_POS = 0;
	public static final int POS_LEAD_TEXT_0 = 1;
	public static final int POS_LEAD_TEXT_1 = 2;
	public static final int POS_LEAD_TEXT_2 = 3;
	public static final int POS_TYPE = 4;
	public static final int POS_HEADER_LENGTH = 5;
	public static final int POS_REF_NO_START = 6;
	public static final int POS_REF_NO_END = 9;


	private final boolean presentation;
	private final boolean failure;
	private final boolean delivery;
	private final boolean satIdAndLesId;
	private final boolean dataLength;
	private final boolean dnid;
	private final boolean memberNo;
	private final boolean mesMobNo;
	private final int length;
	private final LinkedHashMap<HeaderByte, Integer> lhs = new LinkedHashMap<>();

	public HeaderStruct(Part1 part1, Part2 part2) {
		this.presentation = part1.isPresentation();
		this.failure = part1.isFailure();
		this.delivery = part1.isDelivery();
		this.satIdAndLesId = part1.isSatIdAndLesId();
		this.dataLength = part1.isDataLength();
		this.dnid = part2.isDnid();
		this.memberNo = part2.isMemberNo();
		this.mesMobNo = part2.isMesMobNo();

		int index = 0;
		lhs.put(HeaderByte.START_OF_HEADER, index);
		index += HeaderByte.START_OF_HEADER.getNoOfBytes();

		lhs.put(HeaderByte.LEAD_TEXT, index);
		index += HeaderByte.LEAD_TEXT.getNoOfBytes();

		lhs.put(HeaderByte.TYPE, index);
		index += HeaderByte.TYPE.getNoOfBytes();

		lhs.put(HeaderByte.HEADER_LENGTH, index);
		index += HeaderByte.HEADER_LENGTH.getNoOfBytes();

		lhs.put(HeaderByte.MSG_REF_NO, index);
		index += HeaderByte.MSG_REF_NO.getNoOfBytes();

		if (part1.isPresentation()) {
			lhs.put(HeaderByte.PRESENTATION, index);
			index += HeaderByte.PRESENTATION.getNoOfBytes();
		}

		if (part1.isFailure()) {
			lhs.put(HeaderByte.FAILURE_RESON, index);
			index += HeaderByte.FAILURE_RESON.getNoOfBytes();
		}

		if (part1.isDelivery()) {
			lhs.put(HeaderByte.DELIVERY_ATTEMPTS, index);
			index += HeaderByte.DELIVERY_ATTEMPTS.getNoOfBytes();
		}

		if (part1.isSatIdAndLesId()) {
			lhs.put(HeaderByte.SATID_AND_LESID, index);
			index += HeaderByte.SATID_AND_LESID.getNoOfBytes();
		}

		if (part1.isDataLength()) {
			lhs.put(HeaderByte.DATA_LENGTH, index);
			index += HeaderByte.DATA_LENGTH.getNoOfBytes();
		}

		lhs.put(HeaderByte.STORED_TIME, index);
		index += HeaderByte.STORED_TIME.getNoOfBytes();

		if (part2.isDnid()) {
			lhs.put(HeaderByte.DNID, index);
			index += HeaderByte.DNID.getNoOfBytes();
		}

		if (part2.isMemberNo()) {
			lhs.put(HeaderByte.MEMBER_NO, index);
			index += HeaderByte.MEMBER_NO.getNoOfBytes();
		}

		if (part2.isMesMobNo()) {
			lhs.put(HeaderByte.MES_MOB_NO, index);
			index += HeaderByte.MES_MOB_NO.getNoOfBytes();
		}
		lhs.put(HeaderByte.END_OF_HEADER, index);
		index += HeaderByte.END_OF_HEADER.getNoOfBytes();

		length = index;

	}

	public boolean isPresentation() {
		return presentation;
	}

	public boolean isFailure() {
		return failure;
	}

	public boolean isDelivery() {
		return delivery;
	}

	public boolean isSatIdAndLesId() {
		return satIdAndLesId;
	}

	public boolean isDataLength() {
		return dataLength;
	}

	public boolean isDnid() {
		return dnid;
	}

	public boolean isMemberNo() {
		return memberNo;
	}

	public boolean isMesMobNo() {
		return mesMobNo;
	}

	@SuppressWarnings("SameReturnValue")
	public int getPostionStartOfHeader() {
		return POS_START_OF_HEADER_POS;
	}

	@SuppressWarnings("SameReturnValue")
	public int getPostionLeadText() {
		return POS_LEAD_TEXT_0;
	}

	@SuppressWarnings("SameReturnValue")
	public int getPostionType() {
		return POS_TYPE;
	}

	@SuppressWarnings("SameReturnValue")
	public Integer getPostionHeaderLength() {
		return POS_HEADER_LENGTH;
	}

	@SuppressWarnings("SameReturnValue")
	public Integer getPostionMsgRefNo() {
		return POS_REF_NO_START;
	}

	public Integer getPostionPresentation() {
		return lhs.get(HeaderByte.PRESENTATION);
	}

	public Integer getPostionFailureReason() {
		return lhs.get(HeaderByte.FAILURE_RESON);
	}

	public Integer getPostionDeliveryAttempts() {
		return lhs.get(HeaderByte.DELIVERY_ATTEMPTS);
	}

	public Integer getPostionSatIdAndLesId() {
		return lhs.get(HeaderByte.SATID_AND_LESID);
	}

	public Integer getPostionDataLength() {
		return lhs.get(HeaderByte.DATA_LENGTH);
	}

	public Integer getPostionStoredTime() {
		return lhs.get(HeaderByte.STORED_TIME);
	}

	public Integer getPostionDnid() {
		return lhs.get(HeaderByte.DNID);
	}

	public Integer getPostionMemberNo() {
		return lhs.get(HeaderByte.MEMBER_NO);
	}

	public Integer getPostionMesMobNo() {
		return lhs.get(HeaderByte.MES_MOB_NO);
	}

	public Integer getPostionEndOfHeader() {
		return lhs.get(HeaderByte.END_OF_HEADER);
	}

	public int getLength() {
		return length;
	}

	static class Part1 {
		private final boolean presentation;
		private final boolean failure;
		private final boolean delivery;
		private final boolean satIdAndLesId;
		private final boolean dataLength;

		Part1(boolean presentation, boolean failure, boolean delivery, boolean satIdAndLesId, boolean dataLength) {
			this.presentation = presentation;
			this.failure = failure;
			this.delivery = delivery;
			this.satIdAndLesId = satIdAndLesId;
			this.dataLength = dataLength;
		}

		public boolean isPresentation() {
			return presentation;
		}

		public boolean isFailure() {
			return failure;
		}

		public boolean isDelivery() {
			return delivery;
		}

		public boolean isSatIdAndLesId() {
			return satIdAndLesId;
		}

		public boolean isDataLength() {
			return dataLength;
		}
	}


	static class Part2 {
		private final boolean dnid;
		private final boolean memberNo;
		private final boolean mesMobNo;

		Part2(boolean dnid, boolean memberNo, boolean mesMobNo) {
			this.dnid = dnid;
			this.memberNo = memberNo;
			this.mesMobNo = mesMobNo;
		}

		public boolean isDnid() {
			return dnid;
		}

		public boolean isMemberNo() {
			return memberNo;
		}

		public boolean isMesMobNo() {
			return mesMobNo;
		}
	}
}
