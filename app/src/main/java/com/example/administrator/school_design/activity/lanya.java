package com.example.administrator.school_design.activity;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by Administrator on 2018/4/2.
 */

public class lanya {

        /**
         * 当前 Android 设备是否支持 Bluetooth
         * @return true：支持 Bluetooth false：不支持 Bluetooth
         **/

        public static boolean isBluetoothSupported()
        {

            return BluetoothAdapter.getDefaultAdapter() != null ? true : false;

        }

        /**
         * 当前 Android 设备的 bluetooth 是否已经开
         * @return true：Bluetooth 已经开启 false：Bluetooth 未开启
         */

        public static boolean isBluetoothEnabled()
        {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                    .getDefaultAdapter();

            if (bluetoothAdapter != null)
            {
                return bluetoothAdapter.isEnabled();
            }
            return false;
        }

        /**
         * 强制开启当前 Android 设备的 Bluetooth
         * @return true：强制打开 Bluetooth　成功　false：强制打开 Bluetooth 失败
         */

        public static boolean turnOnBluetooth()
        {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                    .getDefaultAdapter();
            if (bluetoothAdapter != null)
            {
                return bluetoothAdapter.enable();

            }

            return false;
        }
}
