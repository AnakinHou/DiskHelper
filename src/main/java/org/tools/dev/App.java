package org.tools.dev;

import java.util.List;

import org.tools.dev.common.MacHWDiskStore;
import org.tools.dev.common.MacUsbDevice;
import org.tools.dev.common.WindowsHWDiskStore;
import org.tools.dev.common.WindowsUsbDevice;
import org.tools.dev.hd.HWDiskStore;
import org.tools.dev.hd.HWPartition;
import org.tools.dev.usb.UsbDevice;

/**
 * ---------------Mac----------Win <br>
 * -硬盘序列号 -----NO ----------Yes 文本 <br>
 * -硬盘分区名称 ---Yes ---------Yes 无法获取逻辑分区的所有名称 <br>
 * -硬盘分区序列号                                      YES
 * -USB硬盘序列号 --Yes(16进制）  NO <br>
 * -USB硬盘名称 ----Yes --------Yes <br>
 */
public class App {
	public static void main(String[] args) {
		String os = System.getProperty("os.name");
		System.out.println(os);
//        Windows 8.1
//        Mac OS X

		List<HWDiskStore> diskList = null;
		if (os.toLowerCase().startsWith("win")) {
			diskList = WindowsHWDiskStore.getDisks();
		} else if (os.toLowerCase().startsWith("mac")) {
			diskList = MacHWDiskStore.getDisks();
		} else {
			return;
		}

		for (int i = 0; i < diskList.size(); i++) {
			HWDiskStore disk = diskList.get(i);
			System.out.println(" " + disk.toString());

			List<HWPartition> partitions = disk.getPartitions();
			for (HWPartition part : partitions) {
				System.out.println(" |-- " + part.toString());
			}
		}

//		System.out.println("USB Devices:");
		List<UsbDevice> usbList = null;
		if (os.toLowerCase().startsWith("win")) {
			usbList = WindowsUsbDevice.getUsbDevices(true);
		} else {
			usbList = MacUsbDevice.getUsbDevices(true);
		}
		for (UsbDevice usbDevice : usbList) {
			System.out.println(usbDevice.toString());
		}

	}
}
