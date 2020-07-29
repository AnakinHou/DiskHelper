package org.tools.dev.demo;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.ptr.IntByReference;

public class JNADemo {

    private static final int BUFSIZE = 255;

    public static void main(String[] args) {
        String logicalDrive = "K:\\";
        char[] volumeChr = new char[BUFSIZE];

        Kernel32.INSTANCE.GetVolumeNameForVolumeMountPoint(logicalDrive, volumeChr, BUFSIZE);
        String aa = new String(volumeChr);
        System.out.println(aa);
        
//        String ptName = "WD_Series";
//        char[] ptChr = new char[BUFSIZE];
//        Kernel32.INSTANCE.GetVolumePathName(ptName, ptChr, BUFSIZE);
//        System.out.println("==== "+new String(ptChr));
//        Parameters:lpRootPathName A string that contains the root directory of thevolume to be described. 
//          If this parameter is null, the root ofthe current directory is used. A trailing backslash is required. 
//                 For example,you specify "\\MyServer\MyShare\", or "C:\"."
//              lpVolumeNameBuffer If not null then receives the name ofthe specified volume. 
//                                The buffer size is specified by the nVolumeNameSizeparameter.
//              nVolumeNameSize The length of the volume name buffer - max. size is WinDef.MAX_PATH + 1 - 
//                                ignored if no volume name buffer provided
//              lpVolumeSerialNumber Receives the volume serial number - can be null if the serial number is not required
//              lpMaximumComponentLength Receives the maximum length of a file namecomponent that the underlying file system supports - 
//                                can be nullif this data is not required
//              lpFileSystemFlags Receives flags associated with the file system- can be null if this data is not required
//              lpFileSystemNameBuffer If not null then receives the nameof the file system. The buffer size is 
//                                specified by the nFileSystemNameSizeparameter.
//              nFileSystemNameSize The length of the file system name buffer -max. size is WinDef.MAX_PATH + 1 - 
//                                ignored if no file system namebuffer providedReturns:true if succeeds. 
//                                If fails then call GetLastError()to get extended error information
        
        char[] lpVolumeNameBuffer = new char[BUFSIZE];
        IntByReference lpVolumeSerialNumber = new IntByReference();
        IntByReference lpMaximumComponentLength = new IntByReference();
        IntByReference lpFileSystemFlags = new IntByReference();
        char[] lpFileSystemNameBuffer = new char[BUFSIZE];
        Kernel32.INSTANCE.GetVolumeInformation("K:\\", lpVolumeNameBuffer, 260, lpVolumeSerialNumber, lpMaximumComponentLength, lpFileSystemFlags, lpFileSystemNameBuffer, BUFSIZE);
        
        
        System.out.println("lpVolumeNameBuffer："+new String(lpVolumeNameBuffer));
        System.out.println("lpVolumeSerialNumber："+lpVolumeSerialNumber.getValue());
        System.out.println("lpMaximumComponentLength:"+lpMaximumComponentLength.getValue());
        System.out.println("lpFileSystemFlags:"+lpFileSystemFlags.getValue());
        System.out.println("lpFileSystemNameBuffer:"+new String(lpFileSystemNameBuffer));
    
    
    }

}
