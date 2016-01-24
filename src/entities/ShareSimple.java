package entities;

public class ShareSimple {
	private String id;
	private String share;
	
	public ShareSimple() {}
	public ShareSimple(String id, String share) {
		setId(id);
		setShare(share);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShare() {
		return share;
	}
	public void setShare(String share) {
		this.share = share;
	}
}
