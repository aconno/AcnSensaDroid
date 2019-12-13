class Slot {
    constructor() {
        this.frameType = FrameType.DEFAULT
        this.frame = {}
        this.name = "Name"
        this.advertising = false
        this.packetCount = 0
        this.supportedtxPower = []
        this.txPower = 0
        this.readOnly = false
        this.addInterval = 0
    }
}

var FrameType = {
    UID: 0,
    URL: "URL",
    TLM: 2,
    IBEACON: "iBeacon",
    DEVICE_INFO: 4,
    DEFAULT: "DEFAULT",
    EMPTY: "EMPTY",
    CUSTOM: "Custom (MSD)"
};

const KEY_ADVERTISING_CONTENT_IBEACON_UUID =
    "KEY_ADVERTISING_CONTENT_IBEACON_UUID"
const KEY_ADVERTISING_CONTENT_IBEACON_MAJOR =
    "KEY_ADVERTISING_CONTENT_IBEACON_MAJOR"
const KEY_ADVERTISING_CONTENT_IBEACON_MINOR =
    "KEY_ADVERTISING_CONTENT_IBEACON_MINOR"
const KEY_ADVERTISING_CONTENT_UID_NAMESPACE_ID =
    "KEY_ADVERTISING_CONTENT_UID_NAMESPACE_ID"
const KEY_ADVERTISING_CONTENT_UID_INSTANCE_ID =
    "KEY_ADVERTISING_CONTENT_UID_INSTANCE_ID"
const KEY_ADVERTISING_CONTENT_URL_URL =
    "KEY_ADVERTISING_CONTENT_URL_URL"
const KEY_ADVERTISING_CONTENT_DEFAULT_DATA =
        "KEY_ADVERTISING_CONTENT_DEFAULT_DATA"
const KEY_ADVERTISING_CONTENT_CUSTOM_CUSTOM =
    "KEY_ADVERTISING_CONTENT_CUSTOM_CUSTOM"
const KEY_ADVERTISING_CONTENT_CUSTOM_IS_HEX_MODE_ON =
    "KEY_ADVERTISING_CONTENT_CUSTOM_IS_HEX_MODE_ON"