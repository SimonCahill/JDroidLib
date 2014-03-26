 /* Copyright (C) 2014 beatsleigher.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */

package JDroidLib.enums;

/**
 *
 * @author beatsleigher
 */
public enum DeviceState {
    DEVICE, OFFLINE, RECOVERY, FASTBOOT, UNKNOWN, BOOTLOADER ,HOST, SIDELOAD, NOPERM, UNAUTHORIZED;

    //this is taken from "transport.c" from adb source ("https://github.com/android/platform_system_core/blob/master/adb/transport.c")

    /*switch(t->connection_state){
    case CS_OFFLINE: return "offline";
    case CS_BOOTLOADER: return "bootloader";
    case CS_DEVICE: return "device";
    case CS_HOST: return "host";
    case CS_RECOVERY: return "recovery";
    case CS_SIDELOAD: return "sideload";
    case CS_NOPERM: return "no permissions";
    case CS_UNAUTHORIZED: return "unauthorized";
    default: return "unknown";
    }*/

    /**
     * Convert device state from string to one of the enum constants
     * @param state state of the device as string
     * @return state of the device represented as on of the constants from {@link JDroidLib.enums.DeviceState}
     **/
    public static DeviceState getState(String state)
    {
        switch (state)
        {
            case "device":
                return DEVICE;
            case "offline":
                return OFFLINE;
            case "recovery":
                return RECOVERY;
            case "fastboot":
                return FASTBOOT;
            case "bootloader":
                return BOOTLOADER;
            case "host":
                return HOST;
            case "sideload":
                return SIDELOAD;
            case "no permissions":
                return NOPERM;
            case "unauthorized":
                return UNAUTHORIZED;
            default:
                return UNKNOWN;
        }
    }

    /**
     * Get human readable representation of the device state
     * @param state DeviceState constant to be converted to human readable string
     * @return */
    public static String getState(DeviceState state)
    {
        switch (state)
        {
            case DEVICE:
                return "device";
            case OFFLINE:
                return "offline";
            case RECOVERY:
                return "recovery";
            case FASTBOOT:
                return "fastboot";
            case BOOTLOADER:
                return "bootloader";
            case HOST:
                return "host";
            case SIDELOAD:
                return "sideload";
            case NOPERM:
                return "no permissions";
            case UNAUTHORIZED:
                return "unauthorized";
            default:
                return "unknown";
        }
    }
}
