/**
 * MIT License
 *
 * Copyright (c) 2010 - 2020 The OSHI Project Contributors: https://github.com/oshi/oshi/graphs/contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.tools.dev.usb.win;

import org.tools.dev.hd.win.WmiQueryHandler;

import com.sun.jna.platform.win32.COM.WbemcliUtil.WmiQuery; //NOSONAR squid:S1191
import com.sun.jna.platform.win32.COM.WbemcliUtil.WmiResult;

//import oshi.annotation.concurrent.ThreadSafe;
//import oshi.util.platform.windows.WmiQueryHandler;

/**
 * Utility to query WMI class {@code Win32_USBController}
 */
//@ThreadSafe
public final class Win32USBController {

    private static final String WIN32_USB_CONTROLLER = "Win32_USBController";

    /**
     * USB Controller properties
     */
    public enum USBControllerProperty {
        PNPDEVICEID;
    }

    private Win32USBController() {
    }

    /**
     * Queries the USB Controller device IDs
     *
     * @return Information regarding each disk drive.
     */
    public static WmiResult<USBControllerProperty> queryUSBControllers() {
        WmiQuery<USBControllerProperty> usbControllerQuery = new WmiQuery<>(WIN32_USB_CONTROLLER,
                USBControllerProperty.class);
        return WmiQueryHandler.createInstance().queryWMI(usbControllerQuery);
    }
}
