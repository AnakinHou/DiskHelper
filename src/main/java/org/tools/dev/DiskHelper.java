package org.tools.dev;

import java.util.ArrayList;
import java.util.List;

import org.tools.dev.common.MacHWDiskStore;
import org.tools.dev.common.MacUsbDevice;
import org.tools.dev.common.WindowsHWDiskStore;
import org.tools.dev.hd.HWDiskStore;
import org.tools.dev.hd.HWPartition;
import org.tools.dev.usb.UsbDevice;
import org.tools.dev.util.FormatUtil;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

public class DiskHelper {

	private static class SingletonHoler {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static DiskHelper instance = new DiskHelper();
	}

	private DiskHelper() {
	}

	public static DiskHelper getInstance() {
		return SingletonHoler.instance;
	}

	/**
	 * 从Usb设备中 查找 移动硬盘
	 * 
	 * @param usbList
	 * @param hdModel
	 * @return
	 */
	public UsbDevice getDiskFromUSB(List<UsbDevice> usbList, String hdModel) {
		for (int i = 0; i < usbList.size(); i++) {
//			System.out
//					.println("======= usbList.get(i).getName():" + usbList.get(i).getName() + "   hdModel " + hdModel);
			if (usbList.get(i).getName().equals(hdModel)) {
				return usbList.get(i);
			}
			//
			List<UsbDevice> connectedUsbList = usbList.get(i).getConnectedDevices();
			for (int j = 0; j < connectedUsbList.size(); j++) {
//				System.out.println("================= connectedUsbList.get(j).getName():"
//						+ connectedUsbList.get(j).getName() + "   hdModel " + hdModel);
				if (connectedUsbList.get(j).getName().equals(hdModel)) {
					return connectedUsbList.get(j);
				}

				List<UsbDevice> connected2UsbList = connectedUsbList.get(j).getConnectedDevices();
				for (int k = 0; k < connected2UsbList.size(); k++) {
//					System.out.println("=================********* connected2UsbList.get(k).getName():"
//							+ connected2UsbList.get(k).getName() + "   hdModel " + hdModel);
					if (connected2UsbList.get(k).getName().equals(hdModel)) {
						return connected2UsbList.get(k);
					}
				}
			}
		}
		return null;
	}

	public List<HD> getDisks() {
		String os = System.getProperty("os.name");
		System.out.println(os);
		if (os.toLowerCase().startsWith("win")) {
			return getWinDisks();
		} else if (os.toLowerCase().startsWith("mac")) {
			return getMacDisks();
		} else {
			System.out.println("Sorry, This program just work on Windows or MacOS");
			return null;
		}
	}

	private List<HD> getWinDisks() {
		List<HWDiskStore> winDiskList = WindowsHWDiskStore.getDisks();
		List<HD> hdList = new ArrayList<HD>(winDiskList.size());
		for (int i = 0; i < winDiskList.size(); i++) {
			HWDiskStore disk = winDiskList.get(i);
//			System.out.println(" " + disk.toString());
//
			List<HWPartition> partitions = disk.getPartitions();
			List<HD.Partition> pts = new ArrayList<HD.Partition>(partitions.size());
			for (HWPartition p : partitions) {
//				System.out.println(" |-- " + p.toString());
//				HWPartition p = partitions.get(j);
				HD.Partition pt = new HD.Partition(p.getIdentification(), p.getName(), p.getType(), p.getUuid(),
						p.getSize(), p.getMajor(), p.getMinor(), p.getMountPoint());
				pts.add(pt);
			}

//		windows下不太好确定 是不是usb硬盘   需要到 getUsbDevice里面查找， 没必要
			HD hd = new HD(disk.getName(), disk.getModel(), disk.getSize(), 2, "", "", "", disk.getSerial(), "", pts);
			hdList.add(hd);
		}
		return hdList;
	}

	private List<HD> getMacDisks() {
		// 获取所有的硬盘信息
		List<HWDiskStore> macDiskList = MacHWDiskStore.getDisks();
		List<HD> hdList = new ArrayList<HD>(macDiskList.size());

		// 获取USB设备信息
		List<UsbDevice> usbList = MacUsbDevice.getUsbDevices(true);
		for (int i = 0; i < macDiskList.size(); i++) {
			HWDiskStore disk = macDiskList.get(i);
//				System.out.println(" " + disk.toString());
			List<HWPartition> partitions = disk.getPartitions();
			List<HD.Partition> pts = new ArrayList<HD.Partition>(partitions.size());
			for (int j = 0; j < partitions.size(); j++) {
				HWPartition p = partitions.get(j);
				HD.Partition pt = new HD.Partition(p.getIdentification(), p.getName(), p.getType(), p.getUuid(),
						p.getSize(), p.getMajor(), p.getMinor(), p.getMountPoint());
				pts.add(pt);
			}

			String name = disk.getName();
			String model = disk.getModel();
			long size = disk.getSize();
			int usbHD = 0;
			String vendor = "";
			String vendorId = "";
			String productId = "";
			String serialNumber = "";
			String uniqueDeviceId = "";

			// 多数情况下getDisks()，USB移动硬盘 无法获取到 序列号，
			// 但是getUsbDevices()，USB设备中 有移动硬盘的序列号
			// 所以必须结合 两个结果 重新组装一个 HD 对象
			// 如果 usb设备中没有硬盘信息，那么说明该硬盘是本地硬盘
			// 如果 USB设备中有，那么说明该硬盘是移动硬盘
//			System.out.print("===== disk.getModel(): " + disk.getModel());
//			System.out.println("===== disk.getSerial(): " + disk.getSerial());
			// 序列号为空 那么去usb设备中 去找找
			if (disk.getSerial() == null || "".equals(disk.getSerial())) {
				UsbDevice usbDisk = getDiskFromUSB(usbList, disk.getModel());
//				System.out.println("*********  getDiskFromUSB ");
				if (usbDisk != null) {
					usbHD = 1;
					vendor = usbDisk.getVendor();
					vendorId = usbDisk.getVendorId();
					productId = usbDisk.getProductId();
					serialNumber = usbDisk.getSerialNumber();
					uniqueDeviceId = usbDisk.getUniqueDeviceId();
				}
			}
			HD hd = new HD(name, model, size, usbHD, vendor, vendorId, productId, serialNumber, uniqueDeviceId, pts);
			hdList.add(hd);

		}
		return hdList;
	}

	public static void main(String[] args) {
		List<HD> hdList = DiskHelper.getInstance().getDisks();
		for (int i = 0; i < hdList.size(); i++) {
			System.out.println(hdList.get(i).toString());
		}
//		 SystemInfo si = new SystemInfo();
//
//	        HardwareAbstractionLayer hal = si.getHardware();
//	        OperatingSystem os = si.getOperatingSystem();
		
		  WinNT.LARGE_INTEGER 	userFreeBytes = new WinNT.LARGE_INTEGER(0L);
		  WinNT.LARGE_INTEGER  totalBytes = new WinNT.LARGE_INTEGER(0L);
		  WinNT.LARGE_INTEGER  systemFreeBytes = new WinNT.LARGE_INTEGER(0L);
        
//		volume:\\?\Volume{336c71d1-d178-4d1f-a416-ce65be0ee4ea}\
//		        volume:\\?\Volume{585d226f-5475-4e9e-8113-cb9178a09b4b}\
//		        volume:\\?\Volume{2dae403d-21cd-49a9-aa3f-2b4623675984}\
//		        volume:\\?\Volume{a0d6b1d1-b8c6-4106-b3d3-6e83234cd4d0}\
//		        volume:\\?\Volume{03632d64-5fc3-490d-84a1-028fd8a3a13f}\
//		        volume:\\?\Volume{47356ce7-95ae-4f85-b037-92dfbb28fef1}\
//		        volume:\\?\Volume{a49730a2-0000-0000-0000-100000000000}\
		  
//		        volume:\\?\Volume{a49730a2-0000-0000-0000-20c035000000}\
//		        volume:\\?\Volume{a49730a2-0000-0000-0000-30c04e000000}\
//		        volume:\\?\Volume{a49730a2-0000-0000-0000-40c080000000}\
//		        volume:\\?\Volume{a49730a2-0000-0000-0000-50c0cb000000}\
		  
//		        volume:\\?\Volume{a49730a2-0000-0000-0000-10c021000000}\
//		        volume:\\?\Volume{4f5dd02d-3af3-4070-a882-d9fbb016286a}\
	        
//		String  volumeK = "\\\\?\\Volume{a49730a2-0000-0000-0000-20c035000000}\\";
//		String  volumeL = "\\\\?\\Volume{a49730a2-0000-0000-0000-30c04e000000}\\";
//		String  volumeM = "\\\\?\\Volume{a49730a2-0000-0000-0000-40c080000000}\\";
//		String  volumeN = "\\\\?\\Volume{a49730a2-0000-0000-0000-50c0cb000000}\\";
////		FormatUtil.formatBytes(userFreeBytes.getValue());
//		                Kernel32.INSTANCE.GetDiskFreeSpaceEx(volumeK, userFreeBytes, totalBytes, systemFreeBytes);
//		                System.out.println("K userFreeBytes:"+ FormatUtil.formatBytes(userFreeBytes.getValue())+"  ,  totalBytes:"+FormatUtil.formatBytes(totalBytes.getValue())+" ,  systemFreeBytes:"+FormatUtil.formatBytes(systemFreeBytes.getValue()) );
//		                Kernel32.INSTANCE.GetDiskFreeSpaceEx(volumeL, userFreeBytes, totalBytes, systemFreeBytes);
//		                System.out.println("L userFreeBytes:"+FormatUtil.formatBytes(userFreeBytes.getValue())+"  ,  totalBytes:"+FormatUtil.formatBytes(totalBytes.getValue())+" ,  systemFreeBytes:"+FormatUtil.formatBytes(systemFreeBytes.getValue()) );
//		                Kernel32.INSTANCE.GetDiskFreeSpaceEx(volumeM, userFreeBytes, totalBytes, systemFreeBytes);
//		                System.out.println("M userFreeBytes:"+FormatUtil.formatBytes(userFreeBytes.getValue())+"  ,  totalBytes:"+FormatUtil.formatBytes(totalBytes.getValue())+" ,  systemFreeBytes:"+FormatUtil.formatBytes(systemFreeBytes.getValue()) );
//		                Kernel32.INSTANCE.GetDiskFreeSpaceEx(volumeN, userFreeBytes, totalBytes, systemFreeBytes);
//		                System.out.println("N userFreeBytes:"+FormatUtil.formatBytes(userFreeBytes.getValue())+"  ,  totalBytes:"+FormatUtil.formatBytes(totalBytes.getValue())+" ,  systemFreeBytes:"+FormatUtil.formatBytes(systemFreeBytes.getValue()) );
	}

}
