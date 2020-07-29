package org.tools.dev;

import java.util.List;

import org.tools.dev.util.FormatUtil;

public class HD {

	private final String name;
	private final String model;
	private final long size;
	// 1 是usb  0 不是   2 unkown
	private final int usbHD;  
	private final String vendor;
	private final String vendorId;
	private final String productId;
	private final String serialNumber;
	private final String uniqueDeviceId;

	private List<Partition> partitionList;

	public HD(String name, String model, long size, int usbHD, String vendor, String vendorId, String productId,
			String serialNumber, String uniqueDeviceId, List<Partition> partitionList) {
		this.name = name;
		this.model = model;
		this.size = size;
		this.usbHD = usbHD;
		this.vendor = vendor;
		this.vendorId = vendorId;
		this.productId = productId;
		this.serialNumber = serialNumber;
		this.uniqueDeviceId = uniqueDeviceId;
		this.partitionList = partitionList;
	}

	public String getName() {
		return name;
	}

	public String getModel() {
		return model;
	}

	public long getSize() {
		return size;
	}

	public String getVendor() {
		return vendor;
	}

	public String getVendorId() {
		return vendorId;
	}

	public String getProductId() {
		return productId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getUniqueDeviceId() {
		return uniqueDeviceId;
	}

	public int isUsbHD() {
		return usbHD;
	}

	public List<Partition> getPartitionList() {
		return partitionList;
	}

	public void setPartitionList(List<Partition> partitionList) {
		this.partitionList = partitionList;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(this.getName());
		sb.append(":  ");
		sb.append(" model: ").append(getModel());
		sb.append(",  SerialNumber: ").append(getSerialNumber());
		sb.append(",  size: ").append(FormatUtil.formatBytesDecimal(getSize()));
		sb.append(",  usbHD: ").append(isUsbHD());
		sb.append(",  vendor: ").append(getVendor());
		sb.append(",  vendorId: ").append(getVendorId());
		sb.append(",  productId: ").append(getProductId());
		sb.append(",  UniqueDeviceId: ").append(getUniqueDeviceId());

		List<Partition> partitions = getPartitionList();
		for (Partition part : partitions) {
			sb.append("\n |--- ").append(part.getName());
			sb.append(", (id:").append(part.getIdentification()).append(") ");
			sb.append(", (type:").append(part.getType()).append(") ");
			sb.append(", (uuid:").append(part.getUuid()).append(") ");
			sb.append(", (Maj:Min=").append(part.getMajor()).append(":").append(part.getMinor()).append(")");
			sb.append(", (size: ").append(FormatUtil.formatBytesDecimal(part.getSize()));
			sb.append(", (mountPoint: ").append(part.getMountPoint());
		}
		return sb.toString();

	}

	/**
	 * 分区
	 * 
	 * @author
	 *
	 */
	public static class Partition {

		private final String identification;
		private final String name;
		private final String type;
		private final String uuid;
		private final long size;
		private final int major;
		private final int minor;
		private final String mountPoint;

		public Partition(String identification, String name, String type, String uuid, long size, int major, int minor,
				String mountPoint) {
			this.identification = identification;
			this.name = name;
			this.type = type;
			this.uuid = uuid;
			this.size = size;
			this.major = major;
			this.minor = minor;
			this.mountPoint = mountPoint;
		}

		/**
		 * <p>
		 * Getter for the field <code>identification</code>.
		 * </p>
		 *
		 * @return Returns the identification.
		 */
		public String getIdentification() {
			return this.identification;
		}

		/**
		 * <p>
		 * Getter for the field <code>name</code>.
		 * </p>
		 *
		 * @return Returns the name.
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * <p>
		 * Getter for the field <code>type</code>.
		 * </p>
		 *
		 * @return Returns the type.
		 */
		public String getType() {
			return this.type;
		}

		/**
		 * <p>
		 * Getter for the field <code>uuid</code>.
		 * </p>
		 *
		 * @return Returns the uuid.
		 */
		public String getUuid() {
			return this.uuid;
		}

		/**
		 * <p>
		 * Getter for the field <code>size</code>.
		 * </p>
		 *
		 * @return Returns the size in bytes.
		 */
		public long getSize() {
			return this.size;
		}

		/**
		 * <p>
		 * Getter for the field <code>major</code>.
		 * </p>
		 *
		 * @return Returns the major device ID.
		 */
		public int getMajor() {
			return this.major;
		}

		/**
		 * <p>
		 * Getter for the field <code>minor</code>.
		 * </p>
		 *
		 * @return Returns the minor device ID.
		 */
		public int getMinor() {
			return this.minor;
		}

		/**
		 * <p>
		 * Getter for the field <code>mountPoint</code>.
		 * </p>
		 *
		 * @return Returns the mount point.
		 */
		public String getMountPoint() {
			return this.mountPoint;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(getIdentification()).append(": ");
			sb.append(getName()).append(" ");
			sb.append("(").append(getType()).append(") ");
			sb.append("Maj:Min=").append(getMajor()).append(":").append(getMinor()).append(", ");
			sb.append("size: ").append(FormatUtil.formatBytesDecimal(getSize()));
			sb.append(getMountPoint().isEmpty() ? "" : " @ " + getMountPoint());
			return sb.toString();
		}
	}

}
