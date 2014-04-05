package ar.com.dcsys.data.document;

import java.util.Date;

public class DocumentBean implements Document {

	private String id;
	private Date created;
	private String name;
	private String description;
	private String mimeType;
	private String encoding;
	private byte[] content;
	
	
	/**
	 * Copia el objeto en una nueva instancia, pero sin el contenido.
	 * @return
	 */
	public Document cloneWithoutContent() {
		DocumentBean d = new DocumentBean();
		d.setId(getId() != null ? new String(getId()) : null);
		d.setCreated(getCreated() != null ? new Date(getCreated().getTime()) : null);
		d.setName(getName() != null ? new String(getName()) : getName());
		d.setDescription(getDescription() != null ? new String(getDescription()) : null);
		d.setMimeType(getMimeType() != null ? new String(getMimeType()) : null);
		d.setEncoding(getEncoding() != null ? new String(getEncoding()) : null);
		return d;
	}
	
	public DocumentBean() {
		created = new Date();
	}
	
	public DocumentBean(String name, byte[] content) {
		created = new Date();
		this.name = name;
		this.content = content;
	}

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public Date getCreated() {
		return created;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public String getEncoding() {
		return encoding;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

}
