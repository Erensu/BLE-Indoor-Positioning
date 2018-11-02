package com.nexenio.bleindoorpositioning.ble.beacon.distance.benchmark;

import com.nexenio.bleindoorpositioning.ble.advertising.IBeaconAdvertisingPacket;
import com.nexenio.bleindoorpositioning.ble.beacon.IBeacon;
import com.nexenio.bleindoorpositioning.ble.beacon.distance.BeaconDistanceCalculator;
import com.nexenio.bleindoorpositioning.testutil.benchmark.BeaconInfo;
import com.nexenio.bleindoorpositioning.testutil.benchmark.RssiMeasurements;

import java.util.ArrayList;
import java.util.List;

public class BeaconDistanceCalculatorBenchmark {

    private BeaconDistanceCalculator beaconDistanceCalculator;

    private RssiMeasurements rssiMeasurements;

    private BeaconInfo beaconInfo;

    public BeaconDistanceCalculatorBenchmark(BeaconDistanceCalculator beaconDistanceCalculator, RssiMeasurements rssiMeasurements, BeaconInfo beaconInfo) {
        this.beaconDistanceCalculator = beaconDistanceCalculator;
        this.rssiMeasurements = rssiMeasurements;
        this.beaconInfo = beaconInfo;
    }

    protected float calculateDistance() {
        IBeacon<IBeaconAdvertisingPacket> beacon = createBeacon();
        return beaconDistanceCalculator.calculateDistanceTo(beacon);
    }

    protected IBeacon<IBeaconAdvertisingPacket> createBeacon() {
        IBeacon<IBeaconAdvertisingPacket> beacon = new IBeacon<>();
        List<IBeaconAdvertisingPacket> advertisingPackets = createAdvertisingPackets();
        if (advertisingPackets.isEmpty()) {
            throw new IllegalArgumentException("No advertising packets were created");
        }
        beacon.applyPropertiesFromAdvertisingPacket(advertisingPackets.get(0));
        for (IBeaconAdvertisingPacket advertisingPacket : advertisingPackets) {
            // TODO: Maximum package age is defined in beacon with 60s
            beacon.addAdvertisingPacket(advertisingPacket);
        }
        return beacon;
    }

    protected List<IBeaconAdvertisingPacket> createAdvertisingPackets() {
        return createAdvertisingPacketsForRssis(rssiMeasurements.getRssis(), beaconInfo.getManufacturerData(), beaconInfo.getTransmissionPower());
    }

    protected static List<IBeaconAdvertisingPacket> createAdvertisingPacketsForRssis(int[] rssis, byte[] manufacturerData, int beaconTransmissionPower) {
        List<IBeaconAdvertisingPacket> advertisingPackets = new ArrayList<>();
        for (int rssi : rssis) {
            IBeaconAdvertisingPacket advertisingPacket = new IBeaconAdvertisingPacket(manufacturerData);
            advertisingPacket.setMeasuredPowerByte((byte) beaconTransmissionPower);
            advertisingPacket.setRssi(rssi);
        }
        return advertisingPackets;
    }

}
